package com.example.flappybird


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

const val TAG = "EnterScreenViewModel"


data class LoginState(
    var email : String = "",
    var password : String = "",
    var error : String? = null,
    var loading : Boolean = false
)

class EnterScreenViewModel : ViewModel() {

    var uiState = mutableStateOf(LoginState())
        private set

    fun onChangeEmail(email : String) {
        uiState.value = uiState.value.copy(
            email = email
        )
    }

    fun onChangePassword(password : String) {
        uiState.value = uiState.value.copy(
            password = password
        )
    }

    fun login(onLoginSuccess : () -> Unit) {
        uiState.value = uiState.value.copy(
            loading = true
        )

        if (uiState.value.email.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Email is required",
                loading = false
            )
        }

        if (uiState.value.password.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Password is required",
                loading = false
            )
        }

        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(
            uiState.value.email,
            uiState.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                    onLoginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    uiState.value = uiState.value.copy(
                        error = task.exception?.message,
                        loading = false
                    )
                }
            }
    }

    fun signup(onSignupSuccess : () -> Unit)
    {
        uiState.value = uiState.value.copy(
            loading = true
        )

        if (uiState.value.email.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Email is required",
                loading = false
            )
        }

        if (uiState.value.password.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Password is required",
                loading = false
            )
        }

        val auth = Firebase.auth
        val db = Firebase.firestore

        auth.createUserWithEmailAndPassword(
            uiState.value.email,
            uiState.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-up user's information
                    Log.d(TAG, "signUpWithEmail:success")
                    val user = auth.currentUser

                    val data = hashMapOf(
                        "score" to 0
                    )

                    db.collection("games").document(auth.currentUser?.uid!!).set(data)
                        .addOnSuccessListener {
                            uiState.value = uiState.value.copy(
                                loading = false
                            )
                        }
                        .addOnFailureListener {
                            uiState.value = uiState.value.copy(
                                error = it.message,
                                loading = false
                            )
                        }

                    //updateUI(user)
                    onSignupSuccess()
                } else {
                    // If sign up fails, display a message to the user.
                    Log.w(TAG, "signUpWithEmail:failure", task.exception)
                    uiState.value = uiState.value.copy(
                        error = task.exception?.message,
                        loading = false
                    )
                }
            }

    }
}