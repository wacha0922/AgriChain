package com.example.agrichain.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInScreen(
    selectedRole: String = "Farmer",
    onSignIn: (String, String) -> Unit,
    onGoogleSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignUp: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    successMessage: String? = null
) {
    var emailOrPhone by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    val colors = MaterialTheme.colorScheme

    val isEmailValid =
        android.util.Patterns.EMAIL_ADDRESS
            .matcher(emailOrPhone.trim())
            .matches()

    val isSignInEnabled =
        emailOrPhone.isNotBlank() &&
                isEmailValid &&
                password.length >= 8 &&
                !isLoading

    /*
     * Keeps transient authentication messages easy to observe
     * if they are passed in by the navigation/auth layer.
     */
    LaunchedEffect(errorMessage, successMessage) {
        // Intentionally empty.
        // Message handling remains controlled by the caller.
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // AgriChain logo
            // =================================================

            Surface(
                modifier = Modifier.size(86.dp),
                shape = RoundedCornerShape(28.dp),
                color = colors.surface,
                tonalElevation = 5.dp,
                shadowElevation = 7.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = colors.primary.copy(
                            alpha = 0.10f
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🌱",
                                fontSize = 32.sp
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Sign in to your AgriChain account",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(
                    alpha = 0.65f
                ),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // Selected role
            // =================================================

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = colors.primary.copy(
                    alpha = 0.09f
                )
            ) {
                Text(
                    text = "Signing in as $selectedRole",
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    color = colors.primary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =================================================
            // Error message
            // =================================================

            if (!errorMessage.isNullOrBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.error.copy(
                        alpha = 0.10f
                    )
                ) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 12.dp
                        ),
                        color = colors.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }

            // =================================================
            // Success message
            // =================================================

            if (!successMessage.isNullOrBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.primary.copy(
                        alpha = 0.10f
                    )
                ) {
                    Text(
                        text = successMessage,
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 12.dp
                        ),
                        color = colors.primary,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }

            // =================================================
            // Email
            // =================================================

            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = {
                    emailOrPhone = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Email address")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                singleLine = true,
                isError = emailOrPhone.isNotEmpty() &&
                        !isEmailValid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor =
                        colors.outline.copy(
                            alpha = 0.5f
                        ),
                    errorBorderColor = colors.error,
                    focusedLabelColor = colors.primary,
                    cursorColor = colors.primary
                )
            )

            if (
                emailOrPhone.isNotEmpty() &&
                !isEmailValid
            ) {
                Text(
                    text = "Enter a valid email address.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 4.dp,
                            top = 4.dp
                        ),
                    color = colors.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =================================================
            // Password
            // =================================================

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible =
                                !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (passwordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                            contentDescription =
                                if (passwordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                        )
                    }
                },
                singleLine = true,
                isError = password.isNotEmpty() &&
                        password.length < 8,
                visualTransformation =
                    if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor =
                        colors.outline.copy(
                            alpha = 0.5f
                        ),
                    errorBorderColor = colors.error,
                    focusedLabelColor = colors.primary,
                    cursorColor = colors.primary
                )
            )

            if (
                password.isNotEmpty() &&
                password.length < 8
            ) {
                Text(
                    text = "Password must contain at least 8 characters.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 4.dp,
                            top = 4.dp
                        ),
                    color = colors.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =================================================
            // Forgot password
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.End
            ) {
                Text(
                    text = "Forgot password?",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            enabled = !isLoading,
                            onClick = onForgotPassword
                        )
                        .background(
                            colors.primary.copy(
                                alpha = 0.05f
                            )
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 7.dp
                        ),
                    color = colors.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =================================================
            // Sign In
            // =================================================

            Button(
                onClick = {
                    onSignIn(
                        emailOrPhone.trim(),
                        password
                    )
                },
                enabled = isSignInEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor =
                        colors.primary.copy(
                            alpha = 0.35f
                        ),
                    disabledContentColor =
                        colors.onPrimary.copy(
                            alpha = 0.65f
                        )
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 2.dp,
                    disabledElevation = 0.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = colors.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Sign In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // =================================================
            // Divider
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = colors.outline.copy(
                        alpha = 0.25f
                    )
                )

                Text(
                    text = "  OR  ",
                    color = colors.onBackground.copy(
                        alpha = 0.50f
                    ),
                    style = MaterialTheme.typography.labelMedium
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = colors.outline.copy(
                        alpha = 0.25f
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // Google Sign-In
            // =================================================

            OutlinedAuthButton(
                text = "Continue with Google",
                icon = "G",
                onClick = onGoogleSignIn,
                enabled = !isLoading
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // Polygon wallet
            // =================================================

            OutlinedAuthButton(
                text = "Connect Polygon Wallet",
                icon = "⬡",
                onClick = {
                    // Polygon wallet integration will be added later.
                },
                enabled = !isLoading
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            // =================================================
            // Sign Up
            // =================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = colors.onBackground.copy(
                        alpha = 0.65f
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Sign Up",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .clickable(
                            enabled = !isLoading,
                            onClick = onSignUp
                        )
                        .padding(5.dp),
                    color = colors.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Powered by Polygon Blockchain",
                color = colors.primary.copy(
                    alpha = 0.75f
                ),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OutlinedAuthButton(
    text: String,
    icon: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.Center,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}