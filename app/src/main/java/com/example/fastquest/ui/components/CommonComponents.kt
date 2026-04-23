// Common reusable UI components for FastQuest app
package com.example.fastquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fastquest.ui.theme.*

@Composable
fun FastQuestLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FAST",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = TextPrimary,
            letterSpacing = 2.sp
        )
        Text(
            text = "&Quest",
            fontSize = 36.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            color = TextPrimary,
            letterSpacing = 1.sp,
            modifier = Modifier.offset(y = (-12).dp)
        )
    }
}

@Composable
fun RoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 16.sp
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = TextFieldBackground,
            unfocusedContainerColor = TextFieldBackground,
            disabledContainerColor = TextFieldBackground,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(300.dp)
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonBackground,
            contentColor = TextPrimary,
            disabledContainerColor = ButtonBackground.copy(alpha = 0.5f),
            disabledContentColor = TextPrimary.copy(alpha = 0.5f)
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun GradientBackground(
    gradientColors: List<Color>,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            )
    ) {
        content()
    }
}

@Composable
fun UnderlinedTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
            )
        )
    }
}

// Made with Bob
