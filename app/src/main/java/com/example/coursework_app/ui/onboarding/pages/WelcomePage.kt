package com.example.coursework_app.ui.onboarding.pages

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework_app.ui.components.AppBarSize
import com.example.coursework_app.ui.components.AppBarTitle

@Composable
fun WelcomeScreen(
    onNextClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppBarTitle(
            title = "Добро пожаловать!",
            size = AppBarSize.LARGE,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Давайте познакомимся",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = it.isBlank()
            },
            label = { Text("Ваше имя") },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError,
            supportingText = {
                if (nameError) {
                    Text("Имя не может быть пустым")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !Patterns.EMAIL_ADDRESS.matcher(it).matches() && it.isNotBlank()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError,
            supportingText = {
                if (emailError) {
                    Text("Введите корректный email")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onNextClicked(name, email) },
            enabled = name.isNotBlank() && email.isNotBlank() && !emailError,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Продолжить", fontSize = 16.sp)
        }
    }
}