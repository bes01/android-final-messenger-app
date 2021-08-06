package ge.bkapa.tkats.messengerapp.storage

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.bkapa.tkats.messengerapp.storage.model.Message
import ge.bkapa.tkats.messengerapp.storage.model.User
import java.util.function.Consumer

class Interactor: AuthInteractor,MessageListInteractor {

    private val database = Firebase.database

    /** interface implementations  **/

    override fun addUser(uid: String, username: String, whatIdo: String) {
        val usersRef = database.getReference("users")
        usersRef.child(uid).setValue(User(username, whatIdo))
    }

    override fun getMessagesForUser(uid: String, function: (MutableList<Message>) -> Unit) {
        getFullUser(uid) { user -> getUserMessages(user,function) }
    }



    /** private methods **/

    private fun getUserMessages(user: User, function: (MutableList<Message>) -> Unit){
        val result = mutableListOf<Message>()
        user.username?.let {
            database.getReference("messages").child(it).get().addOnSuccessListener { res->
                res.children.forEach(Consumer { t ->
                    result.addAll(t.children.map { dataSnapshot ->
                        val value = dataSnapshot.value as HashMap<*,*>
                        Message(
                            value["sender"] as String,
                            value["message"] as String,
                            value["sendTime"] as Long
                        )
                    });
                })
                function(result)
            }
        }
    }

    private fun getFullUser(uid: String, function: (User) -> Unit) {
        database.getReference("users").child(uid).get().addOnCompleteListener(
            OnCompleteListener { task ->
                run {
                    if (task.isCanceled) {
                        Log.e("user.not.found", "User Not Found")
                    } else {
                        val res = task.result?.value as HashMap<*, *>
                        function(User(res["username"] as String, res["whatIdo"] as String))
                    }
                }
        })
    }

}
