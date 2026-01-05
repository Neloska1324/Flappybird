package com.example.flappybird


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

data class MainMenuState(
    var score : String = "",
    var error : String? = null,
    var loading : Boolean = false
)

class MainMenuViewModel : ViewModel() {

    var uiState = mutableStateOf(MainMenuState())
        private set

    fun saveScore(score: Int) {

        val currentUser = Firebase.auth.currentUser?.uid ?: return
        val db = Firebase.firestore

        uiState.value = uiState.value.copy(loading = true, error = null)

        val data = hashMapOf(
            "score" to score
        )

        db.collection("games")
            .document(currentUser)
            .set(data)
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
    }

    fun loadScore() {

        val currentUser = Firebase.auth.currentUser?.uid!!
        val db = Firebase.firestore

        db.collection("games")
            .document(currentUser)
            .get()
            .addOnSuccessListener { snapshot ->
                    val score = snapshot.data?.get("score")
                uiState.value = uiState.value.copy(
                    score = score.toString(),
                    loading = false
                )
            }
            .addOnFailureListener {
                uiState.value = uiState.value.copy(
                    error = it.message,
                    loading = false
                )
            }
    }
}