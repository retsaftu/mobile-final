package app.shopping.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(
    onSignIn: (String, String) -> Unit,
    onNavigateToSignUp: () -> Unit,
    authState: AuthState
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
        Text(text = "Sign In", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onSignIn(username, password) },
            enabled = authState !is AuthState.Loading
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToSignUp) {
            Text("Don't have an account? Sign Up")
        }

        if (authState is AuthState.Error) {
            Text(text = authState.message, color = MaterialTheme.colorScheme.error)
        }
    }
}
