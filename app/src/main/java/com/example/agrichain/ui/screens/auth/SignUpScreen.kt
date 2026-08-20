package com.example.agrichain.ui.screens.auth

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onCreateAccount: (
        String,
        String,
        String,
        String
    ) -> Unit,
    onSignIn: () -> Unit
) {
    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    var termsAccepted by remember {
        mutableStateOf(false)
    }

    val colors = MaterialTheme.colorScheme

    // ---------------------------------------------------------
    // Validation
    // ---------------------------------------------------------

    val isFullNameValid =
        fullName.trim().length >= 2

    val isEmailValid =
        android.util.Patterns.EMAIL_ADDRESS
            .matcher(email.trim())
            .matches()

    val isPhoneValid =
        phone.length == 10 &&
                phone.all { it.isDigit() } &&
                phone.firstOrNull() in listOf(
            '6',
            '7',
            '8',
            '9'
        )

    val hasMinLength =
        password.length >= 8

    val hasMaxLength =
        password.length <= 12

    val hasUppercase =
        password.any { it.isUpperCase() }

    val hasLowercase =
        password.any { it.isLowerCase() }

    val hasNumber =
        password.any { it.isDigit() }

    val hasSpecialCharacter =
        password.any {
            !it.isLetterOrDigit()
        }

    val isPasswordValid =
        hasMinLength &&
                hasMaxLength &&
                hasUppercase &&
                hasLowercase &&
                hasNumber &&
                hasSpecialCharacter

    val passwordsMatch =
        password.isNotEmpty() &&
                password == confirmPassword

    val isFormValid =
        isFullNameValid &&
                isEmailValid &&
                isPhoneValid &&
                isPasswordValid &&
                passwordsMatch &&
                termsAccepted

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // =================================================
            // Back button
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.onBackground
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =================================================
            // Logo
            // =================================================

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .background(
                        colors.primary.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌱",
                    fontSize = 42.sp
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Create Your Account",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Join AgriChain and grow with us",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(alpha = 0.65f)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =================================================
            // Full Name
            // =================================================

            AgriAuthTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                },
                label = "Full Name",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Full Name"
                    )
                },
                keyboardType = KeyboardType.Text,
                isError = fullName.isNotEmpty() &&
                        !isFullNameValid
            )

            if (fullName.isNotEmpty() && !isFullNameValid) {
                ValidationMessage(
                    text = "Please enter your full name."
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // Email
            // =================================================

            AgriAuthTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = "Email Address",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                keyboardType = KeyboardType.Email,
                isError = email.isNotEmpty() &&
                        !isEmailValid
            )

            if (email.isNotEmpty() && !isEmailValid) {
                ValidationMessage(
                    text = "Enter a valid email address."
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // Phone
            // =================================================

            AgriAuthTextField(
                value = phone,
                onValueChange = { value ->
                    phone = value
                        .filter { it.isDigit() }
                        .take(10)
                },
                label = "Phone Number",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone"
                    )
                },
                keyboardType = KeyboardType.Phone,
                isError = phone.isNotEmpty() &&
                        !isPhoneValid
            )

            Text(
                text = "Enter a valid 10-digit Indian mobile number.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 4.dp,
                        top = 4.dp
                    ),
                style = MaterialTheme.typography.labelSmall,
                color = colors.onBackground.copy(
                    alpha = 0.55f
                )
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // Password
            // =================================================

            AgriAuthTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = "Password",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = {
                    passwordVisible = !passwordVisible
                },
                keyboardType = KeyboardType.Password,
                isError = password.isNotEmpty() &&
                        !isPasswordValid
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =================================================
            // Password requirements
            // =================================================

            PasswordRequirement(
                text = "8–12 characters",
                satisfied = hasMinLength && hasMaxLength
            )

            PasswordRequirement(
                text = "At least 1 uppercase letter",
                satisfied = hasUppercase
            )

            PasswordRequirement(
                text = "At least 1 lowercase letter",
                satisfied = hasLowercase
            )

            PasswordRequirement(
                text = "At least 1 number",
                satisfied = hasNumber
            )

            PasswordRequirement(
                text = "At least 1 special character",
                satisfied = hasSpecialCharacter
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // Confirm Password
            // =================================================

            AgriAuthTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                label = "Confirm Password",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirm Password"
                    )
                },
                isPassword = true,
                passwordVisible = confirmPasswordVisible,
                onPasswordVisibilityChange = {
                    confirmPasswordVisible =
                        !confirmPasswordVisible
                },
                keyboardType = KeyboardType.Password,
                isError = confirmPassword.isNotEmpty() &&
                        !passwordsMatch
            )

            if (
                confirmPassword.isNotEmpty() &&
                !passwordsMatch
            ) {
                ValidationMessage(
                    text = "Passwords do not match."
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // Terms & Conditions
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = {
                        termsAccepted = it
                    }
                )

                Text(
                    text = "I agree to the Terms & Conditions and Privacy Policy",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onBackground.copy(
                        alpha = 0.75f
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // Create Account
            // =================================================

            Button(
                onClick = {
                    onCreateAccount(
                        fullName.trim(),
                        email.trim(),
                        phone,
                        password
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isFormValid,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.primary.copy(
                        alpha = 0.35f
                    ),
                    disabledContentColor = colors.onPrimary.copy(
                        alpha = 0.65f
                    )
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    text = "Create Account",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // =================================================
            // Existing account
            // =================================================

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = colors.onBackground.copy(
                        alpha = 0.65f
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Sign In",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onSignIn()
                        }
                        .padding(6.dp),
                    color = colors.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Your account will be secured by Firebase Authentication.",
                style = MaterialTheme.typography.labelSmall,
                color = colors.primary.copy(alpha = 0.70f)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
private fun AgriAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: (() -> Unit)? = null,
    isError: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        leadingIcon = leadingIcon,
        trailingIcon = if (isPassword) {
            {
                IconButton(
                    onClick = {
                        onPasswordVisibilityChange?.invoke()
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (passwordVisible) {
                            "Hide password"
                        } else {
                            "Show password"
                        }
                    )
                }
            }
        } else {
            null
        },
        singleLine = true,
        isError = isError,
        keyboardOptions =
            androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = keyboardType
            ),
        visualTransformation =
            if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.outline,
            errorBorderColor = colors.error,
            focusedLabelColor = colors.primary,
            cursorColor = colors.primary
        )
    )
}

@Composable
private fun PasswordRequirement(
    text: String,
    satisfied: Boolean
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 2.dp,
                horizontal = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (satisfied) {
                Icons.Default.CheckCircle
            } else {
                Icons.Default.Close
            },
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = if (satisfied) {
                colors.primary
            } else {
                colors.onBackground.copy(
                    alpha = 0.40f
                )
            }
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (satisfied) {
                colors.primary
            } else {
                colors.onBackground.copy(
                    alpha = 0.55f
                )
            }
        )
    }
}

@Composable
private fun ValidationMessage(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 4.dp,
                top = 4.dp
            ),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.error
    )
}