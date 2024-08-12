package com.example.dolegal.presentation.screens.login_signup

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.example.dolegal.R
import com.example.dolegal.presentation.viewmodel.HomeViewModel

@Composable
fun SignUpPage(
    context: Context,
    vModel: HomeViewModel,
    onLoginClick: () -> Unit,
    imInset: Int,
    isLoading: Boolean,
    isLoadingChange: (Boolean) -> Unit,
    composition: LottieComposition?,
    modifier: Modifier
) {


    var name by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }
    var password2 by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = if (imInset <= 0) Alignment.Center else Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.do_legal_logo_notext),
                    contentDescription = "mylogo",
                    modifier = modifier
                        .padding(14.dp)
                        .clip(CircleShape)
                        .size(if (imInset <= 0) 180.dp else 62.dp)

                )
            }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF00A5E3)),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = modifier.padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = { Text(text = "Enter Name",color = Color.White) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xCD8DD7BF),
                            unfocusedContainerColor = Color(0xCD8DD7BF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )

                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = modifier.padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        placeholder = { Text(text = "Enter Email", color = Color.White) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xCD8DD7BF),
                            unfocusedContainerColor = Color(0xCD8DD7BF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = modifier.padding(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text(text = "Enter Password", color = Color.White) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xCD8DD7BF),
                            unfocusedContainerColor = Color(0xCD8DD7BF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    TextField(
                        value = password2,
                        onValueChange = { password2 = it },
                        modifier = modifier.padding(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text(text = "Enter Password Again", color = Color.White) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xCD8DD7BF),
                            unfocusedContainerColor = Color(0xCD8DD7BF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ElevatedButton(
                            onClick = { onLoginClick() },
                            modifier = modifier.padding(12.dp)

                        ) {

                            Text(text = "Login")

                        }

                        if (isLoading) {
                            LottieAnimation(
                                modifier = modifier.size(150.dp),
                                composition = composition, restartOnPlay = true,
                                iterations = Integer.MAX_VALUE
                            )
                        }
                        else {
                            ElevatedButton(
                                onClick = {

                                    if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                                        if (password == password2) {
                                            isLoadingChange(true)
                                            vModel.signup(
                                                email.trim(),
                                                name.trim(),
                                                password.trim(),
                                                isLoadingChange
                                            )
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Password do not match",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please fill all the fields",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                },
                                modifier = modifier.padding(12.dp)

                            ) {

                                Text(text = "SignUp")

                            }
                        }
                    }

                }

            }

        }

    }

}