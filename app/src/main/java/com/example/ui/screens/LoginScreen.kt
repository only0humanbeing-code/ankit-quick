package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.components.QuickKartLogoBadge
import com.example.ui.theme.QuickAmber
import com.example.ui.theme.QuickAmberContainer
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickGreenDark
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickRed

@Composable
fun LoginScreen(
    onVerifyOtp: (phone: String, otp: String, onError: (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        QuickGreenContainer.copy(alpha = 0.65f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .statusBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QuickKartLogoBadge()

            Spacer(Modifier.height(10.dp))

            Surface(
                color = QuickAmberContainer,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, QuickAmber.copy(alpha = 0.7f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = QuickGreenDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "India's 10-Minute Grocery App",
                        style = MaterialTheme.typography.labelMedium,
                        color = QuickGreenDark
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = if (otpSent) "Verify OTP" else "Log in or Sign up",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (otpSent) {
                            "Enter the 4-digit OTP sent to +91 $phone"
                        } else {
                            "Enter your Indian mobile number. No password needed."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuickMuted
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }.take(10)
                            phone = digits
                            errorMessage = null
                        },
                        label = { Text("Mobile Number") },
                        prefix = {
                            Text(
                                text = "+91 ",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Rounded.PhoneAndroid, contentDescription = null)
                        },
                        enabled = !otpSent,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_phone_input")
                    )

                    if (!otpSent) {
                        Spacer(Modifier.height(8.dp))
                        AssistChip(
                            onClick = {
                                phone = "9876543210"
                                errorMessage = null
                            },
                            label = { Text("Fill Demo Number: 9876543210") }
                        )
                    }

                    AnimatedVisibility(visible = otpSent) {
                        Column {
                            Spacer(Modifier.height(14.dp))
                            OutlinedTextField(
                                value = otp,
                                onValueChange = { input ->
                                    otp = input.filter { it.isDigit() }.take(4)
                                    errorMessage = null
                                },
                                label = { Text("4-Digit OTP") },
                                placeholder = { Text("Enter 1234") },
                                leadingIcon = {
                                    Icon(Icons.Rounded.LockOpen, contentDescription = null)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_otp_input")
                            )

                            Spacer(Modifier.height(8.dp))
                            Surface(
                                color = QuickGreenContainer,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Rounded.Verified,
                                            contentDescription = null,
                                            tint = QuickGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "Demo OTP: 1234",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = QuickGreen
                                        )
                                    }
                                    TextButton(
                                        onClick = { otp = "1234" },
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text("Auto-fill", color = QuickGreen)
                                    }
                                }
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = errorMessage!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = QuickRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (!otpSent) {
                                if (phone.length != 10) {
                                    errorMessage = "Please enter a 10-digit mobile number"
                                } else {
                                    otpSent = true
                                    errorMessage = null
                                }
                            } else {
                                onVerifyOtp(phone, otp) { err -> errorMessage = err }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag(if (otpSent) "verify_otp_button" else "send_otp_button")
                    ) {
                        Text(
                            text = if (otpSent) "Verify & Continue" else "Continue with OTP",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }

                    if (otpSent) {
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                otpSent = false
                                otp = ""
                                errorMessage = null
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Change Mobile Number")
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Text(
                text = "Fresh groceries, dairy & daily staples in minutes • Prices in ₹ (INR)",
                style = MaterialTheme.typography.bodySmall,
                color = QuickMuted
            )
        }
    }
}
