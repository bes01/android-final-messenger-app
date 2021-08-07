package ge.bkapa.tkats.messengerapp.storage

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.bkapa.tkats.messengerapp.storage.model.Message
import ge.bkapa.tkats.messengerapp.storage.model.User
import java.util.function.Consumer

class Interactor() : AuthInteractor, MessageListInteractor, ProfileInteractor {

    private val database = Firebase.database
    private val storageReference = Firebase.storage.reference

    /** interface implementations  **/

    override fun addUser(uid: String, username: String, whatIdo: String) {
        val usersRef = database.getReference("users")
        usersRef.child(uid).setValue(User(username, username, whatIdo))
    }

    override fun getMessagesForUser(uid: String, function: (MutableList<Message>) -> Unit) {
        getFullUser(uid) { user -> getUserMessages(user, function) }
    }

    /**
     * Profile Page
     */
    override fun getUserByIdRequest(
        id: String,
        resultKey: String,
        resultReceiver: (String, Any?) -> Unit
    ) {
        database.getReference("users").child(id).get().addOnSuccessListener {
            val iterator = it.children.iterator();
            val nickname = iterator.next().value as String
            val userName = iterator.next().value as String
            val whatIdo = iterator.next().value as String
            resultReceiver(resultKey, User(userName, nickname, whatIdo))
        }
    }

    override fun updateUser(
        id: String,
        user: User,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    ) {
        var userRef = database.getReference("users").child(id)
        userRef.setValue(user).addOnSuccessListener {
            resultReceiver(resultKey, resultReceiver(resultKey, null))
        }
    }

    override fun uploadImage(
        pathName: String,
        imgUri: Uri,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    ) {
        val ref = storageReference.child("images/$pathName")

        ref.putFile(imgUri).addOnSuccessListener {
            resultReceiver(resultKey, null)
        }
    }

    override fun downloadImage(
        pathName: String,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    ) {
        val ref = storageReference.child("images/$pathName")
        ref.getBytes(1024 * 1024 * 10).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            resultReceiver(resultKey, bitmap)
        }.addOnFailureListener {
            resultReceiver(resultKey, null)
        }
    }

    /*************************************************/

    /** private methods **/

    private fun getUserMessages(user: User, function: (MutableList<Message>) -> Unit) {
        val result = mutableListOf<Message>()
        user.username?.let {
            database.getReference("messages").child(it).get().addOnSuccessListener { res ->
                res.children.forEach(Consumer { t ->
                    result.addAll(t.children.map { dataSnapshot ->
                        val value = dataSnapshot.value as HashMap<*, *>
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
                        function(
                            User(
                                res["username"] as String,
                                res["nickname"] as String,
                                res["whatIdo"] as String
                            )
                        )
                    }
                }
            })
    }

}
