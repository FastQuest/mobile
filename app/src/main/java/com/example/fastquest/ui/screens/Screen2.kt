// Register screen - User registration with name, email, and password
package com.example.fastquest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fastquest.data.local.TokenManager
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.repository.AuthRepository
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*
import com.example.fastquest.viewmodel.AuthViewModel
import com.example.fastquest.viewmodel.AuthViewModelFactory

@Composable
fun Screen2(
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val authRepository = remember { AuthRepository(ApiClient.authService, tokenManager) }
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository)
    )
    
    val authState by viewModel.authState.collectAsState()
    
    // Navigate on successful registration
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onRegisterSuccess()
        }
    }
    val nameText = "Nome"
    val emailText = "E-mail"
    val passwordText = "Senha"
    val registerTitleText = "CADASTRE-SE"
    val registerPromptText = "Venha fazer parte da nossa família!"
    val registerButtonText = "CADASTRAR"
    val loginLinkText = "Login"

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    // Validate inputs
    fun validateInputs(): Boolean {
        var isValid = true
        
        if (name.isBlank()) {
            nameError = "Nome é obrigatório"
            isValid = false
        } else {
            nameError = null
        }
        
        if (email.isBlank()) {
            emailError = "E-mail é obrigatório"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "E-mail inválido"
            isValid = false
        } else {
            emailError = null
        }
        
        if (password.isBlank()) {
            passwordError = "Senha é obrigatória"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Senha deve ter no mínimo 6 caracteres"
            isValid = false
        } else {
            passwordError = null
        }
        
        return isValid
    }

    GradientBackground(
        gradientColors = listOf(RegisterGradientStart, RegisterGradientEnd)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            // Logo
            FastQuestLogo()

            Spacer(modifier = Modifier.height(48.dp))

            // Register title
            Text(
                text = registerTitleText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Register prompt
            Text(
                text = registerPromptText,
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name field
            Column {
                RoundedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    placeholder = nameText,
                    keyboardType = KeyboardType.Text,
                    isError = nameError != null
                )
                nameError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            Column {
                RoundedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    placeholder = emailText,
                    keyboardType = KeyboardType.Email,
                    isError = emailError != null
                )
                emailError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            Column {
                RoundedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    placeholder = passwordText,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    isError = passwordError != null
                )
                passwordError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error message from API
            authState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register button with loading state
            PrimaryButton(
                text = if (authState.isLoading) "CADASTRANDO..." else registerButtonText,
                onClick = {
                    if (validateInputs()) {
                        viewModel.register(name, email, password)
                    }
                },
                enabled = !authState.isLoading
            )
            
            // Loading indicator
            if (authState.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login link
            UnderlinedTextButton(
                text = loginLinkText,
                onClick = onNavigateToLogin
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Screen2Preview() {
    FastQuestTheme {
        Screen2()
    }
}