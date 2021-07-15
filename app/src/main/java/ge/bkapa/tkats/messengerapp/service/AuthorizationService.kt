package ge.bkapa.tkats.messengerapp.service

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthorizationService(private val parentActivity: Activity) {

    private var auth: FirebaseAuth = Firebase.auth

    fun doLogin(nickname: String, password: String, openHomePage: (uid: String) -> Unit) {
        auth.signInWithEmailAndPassword(processNickname(nickname), processPassword(password))
            .addOnCompleteListener(parentActivity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    openHomePage(user!!.uid)
                } else {
                    Toast.makeText(
                        parentActivity,
                        "Authentication failed. Please, fill fields properly.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun doRegister(nickname: String, password: String, whatIDo: String, openHomePage: (uid: String) -> Unit) {
        if (nickname.isEmpty() || password.isEmpty() || password.length < 6 || whatIDo.isEmpty()) {
            Toast.makeText(
                parentActivity,
                "Registration failed. Please, fill fields properly.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        auth.createUserWithEmailAndPassword(processNickname(nickname), processPassword(password))
            .addOnCompleteListener(parentActivity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    //TODO: add to db
                    openHomePage(user!!.uid)
                } else {
                    Log.e("Registration Error", task.exception.toString())
                    Toast.makeText(
                        parentActivity,
                        "Registration failed. You need unique nickname and minimum 6 character length password!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun getCurrentUserId(): String {
        return auth.currentUser!!.uid
    }

    fun isAuthorized(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }

    private fun processNickname(nickname: String): String {
        return nickname.split(' ').joinToString(separator = "_", prefix = "dummy@", postfix = ".ge")
    }

    private fun processPassword(password: String): String {
        Log.i("Password", password.hashCode().toString())
        return password.hashCode().toString()
    }

}