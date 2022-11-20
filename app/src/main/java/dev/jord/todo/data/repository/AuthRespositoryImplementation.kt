package dev.jord.todo.data.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dev.jord.todo.data.model.User
import dev.jord.todo.util.UiState

class AuthRespositoryImplementation(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore,
    val appPreferences: SharedPreferences,
    val gson: Gson
) : AuthRepository {

    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun logout(result: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getSession(result: (User?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun storeSession(id: String, result: (User?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }
}