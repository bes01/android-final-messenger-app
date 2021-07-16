package ge.bkapa.tkats.messengerapp.storage

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.bkapa.tkats.messengerapp.storage.model.User

class Interactor: AuthInteractor {

    private val database = Firebase.database

    override fun addUser(uid: String, username: String, whatIdo: String) {
        val usersRef = database.getReference("users")
        usersRef.child(uid).setValue(User(username, whatIdo))
    }

}