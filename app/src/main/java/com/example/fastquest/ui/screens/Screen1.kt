// Login screen - First screen the user sees with email/password authentication
package com.example.fastquest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*
import com.example.fastquest.viewmodel.AuthViewModel

@Composable
fun Screen1(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToRegister: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val authState by viewModel.authState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    
    // Navigate when logged in
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            viewModel.clearAuthState()
            onLoginClick()
        }
    }
    val emailText = "E-mail"
    val passwordText = "Senha"
    val welcomeText = "SEJA BEM-VINDO"
    val loginPromptText = "Faça login na sua conta"
    val rememberMeText = "Lembre-se de mim"
    val forgotPasswordText = "Esqueceu sua senha?"
    val loginButtonText = "LOGIN"
    val registerLinkText = "Cadastre-se"

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    GradientBackground(
        gradientColors = listOf(LoginGradientStart, LoginGradientEnd)
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

            // Welcome text
            Text(
                text = welcomeText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Login prompt
            Text(
                text = loginPromptText,
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email field
            RoundedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = emailText,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            RoundedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = passwordText,
                isPassword = true,
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Remember me and forgot password row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = CheckboxChecked,
                            uncheckedColor = TextPrimary,
                            checkmarkColor = TextPrimary
                        )
                    )
                    Text(
                        text = rememberMeText,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                }

                TextButton(onClick = { /* Handle forgot password */ }) {
                    Text(
                        text = forgotPasswordText,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (authState is NetworkResult.Error) {
                Text(
                    text = (authState as NetworkResult.Error).message,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Login button
            PrimaryButton(
                text = loginButtonText,
                onClick = {
                    if (viewModel.isValidEmail(email) && viewModel.isValidPassword(password)) {
                        viewModel.login(email, password)
                    }
                },
                enabled = authState !is NetworkResult.Loading &&
                         email.isNotBlank() &&
                         password.isNotBlank()
            )
            
            // Loading indicator
            if (authState is NetworkResult.Loading) {
                Spacer(modifier = Modifier.height(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register link
            UnderlinedTextButton(
                text = registerLinkText,
                onClick = onNavigateToRegister
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Screen1Preview() {
    FastQuestTheme {
        Screen1()
    }
}