// Register screen - User registration with name, email, and password
package com.example.fastquest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*

@Composable
fun Screen2(
    onNavigateToLogin: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
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
            RoundedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = nameText,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

            // Register button
            PrimaryButton(
                text = registerButtonText,
                onClick = onRegisterClick
            )

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

// Made with Bob
