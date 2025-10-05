package com.layout.my_duo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Jitsi once at app start
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.jit.si"))
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(onContinue = { navController.navigate("signup") { popUpTo("splash") { inclusive = true } } }) }
                    composable("signup") { SignupScreen(onAlreadyAccount = { navController.navigate("login") }, onSignedUp = { navController.navigate("login") }) }
                    composable("login") { LoginScreen(onLoggedIn = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } }) }
                    composable("dashboard") { DashboardScreen() }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "My Duo")
        Button(onClick = onContinue) {
            Text("Continue")
        }
    }
}

@Composable
fun SignupScreen(onAlreadyAccount: () -> Unit, onSignedUp: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Signup")
        androidx.compose.material3.OutlinedTextField(value = nameState.value, onValueChange = { nameState.value = it }, label = { Text("Name") })
        Spacer(Modifier.height(8.dp))
        androidx.compose.material3.OutlinedTextField(value = emailState.value, onValueChange = { emailState.value = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(8.dp))
        androidx.compose.material3.OutlinedTextField(value = passwordState.value, onValueChange = { passwordState.value = it }, label = { Text("Password") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val user = hashMapOf(
                "name" to nameState.value,
                "email" to emailState.value
            )
            firestore.collection("users").document().set(user).addOnSuccessListener {
                auth.createUserWithEmailAndPassword(emailState.value, passwordState.value).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSignedUp()
                    }
                }
            }
        }) { Text("Create Account") }
        Button(onClick = onAlreadyAccount) { Text("Already have account") }
    }
}

@Composable
fun LoginScreen(onLoggedIn: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login")
        androidx.compose.material3.OutlinedTextField(value = emailState.value, onValueChange = { emailState.value = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(8.dp))
        androidx.compose.material3.OutlinedTextField(value = passwordState.value, onValueChange = { passwordState.value = it }, label = { Text("Password") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            auth.signInWithEmailAndPassword(emailState.value, passwordState.value).addOnCompleteListener { task ->
                if (task.isSuccessful) onLoggedIn()
            }
        }) { Text("Login") }
    }
}

@Composable
fun DashboardScreen() {
    val roomState = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Join a room")
        androidx.compose.material3.OutlinedTextField(value = roomState.value, onValueChange = { roomState.value = it }, label = { Text("Room code") })
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(roomState.value)
                .setWelcomePageEnabled(false)
                .build()
            JitsiMeetActivity.launch(context, options)
        }) { Text("Join") }
    }
}

@Preview
@Composable
fun PreviewSplash() {
    SplashScreen(onContinue = {})
}
