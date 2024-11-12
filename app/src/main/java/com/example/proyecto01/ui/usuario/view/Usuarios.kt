package com.example.proyecto01.ui.usuario.view
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class Usuarios {

    data class User(
        val username: String = "",
        val password: String = "",
        val numbers: List<Int> = listOf()
    )
}