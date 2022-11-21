package dev.jord.todo.data.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dev.jord.todo.data.model.User
import dev.jord.todo.util.FireStoreCollection
import dev.jord.todo.util.SharedPrefConstants
import dev.jord.todo.util.UiState
import javax.inject.Inject

class AuthRepositoryImplementation @Inject constructor(
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
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { it ->
                if(it.isSuccessful) {
                user.id = it.result.user?.uid ?: ""
                updateUserInfo(user) { state ->
                    if (state is UiState.Success) {
                        storeSession(id = it.result.user?.uid ?: "") {
                            if(it == null) {
                                result.invoke(UiState.Failure("Registration successful, Failed to store session."))
                            } else {
                                result.invoke(UiState.Success("Registration Successful!"))
                            }
                        }
                    }
                    else if (state is UiState.Failure) {
                        result.invoke(UiState.Failure(state.error))
                    }
                }
            } else {
                try {
                    throw it.exception ?: java.lang.Exception("Invalid authentication")
                } catch (e: FirebaseAuthWeakPasswordException) {
                    result.invoke(UiState.Failure("Authentication failed, please use a stronger password"))
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    result.invoke(UiState.Failure("Authentication failed, email invalid"))
                } catch (e: FirebaseAuthUserCollisionException) {
                    result.invoke(UiState.Failure("Authentication failed, email already exists"))
                } catch (e: Exception) {
                    result.invoke(UiState.Failure(e.message))
                }
            }
        } .addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { signIn ->
            if (signIn.isSuccessful) {
                val user = auth.currentUser
                storeSession(id = user?.uid ?: "") {
                    if (it != null) {
                        result.invoke(UiState.Success("Login Success"))
                    } else {
                        result.invoke(UiState.Failure("Failed to store session locally"))
                    }
                }
            } else {
                result.invoke(UiState.Failure("Authentication failed"))
            }
        }.addOnFailureListener {
            result.invoke(UiState.Failure("Authentication failed"))
        }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                result.invoke(UiState.Success("Password reset email has been sent"))
            } else {
                result.invoke(UiState.Failure(task.exception?.message))
            }
        }.addOnFailureListener {
            result.invoke(UiState.Failure("Failed, email does not exist"))
        }
    }

    override fun logout(result: () -> Unit) {
        auth.signOut()
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION,null).apply()
        result.invoke()
    }

    override fun getSession(result: (User?) -> Unit) {
        val userSession = appPreferences.getString(SharedPrefConstants.USER_SESSION,null)
        if (userSession == null){
            result.invoke(null)
        }else{
            val user = gson.fromJson(userSession,User::class.java)
            result.invoke(user)
        }
    }

    override fun storeSession(id: String, result: (User?) -> Unit) {
        database.collection(FireStoreCollection.USER).document(id).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObject(User::class.java)
                    appPreferences.edit().putString(SharedPrefConstants.USER_SESSION,gson.toJson(user)).apply()
                    result.invoke(user)
                } else {
                    result.invoke(null)
                }
            }.addOnFailureListener {
                result.invoke(null)
            }
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        document.set(user)
            .addOnSuccessListener {
                result.invoke(UiState.Success("User has been update successfully"))
                }.addOnFailureListener {
                    result.invoke(UiState.Failure(it.localizedMessage))
                }
    }
}