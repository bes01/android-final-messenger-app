package ge.bkapa.tkats.messengerapp.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.bkapa.tkats.messengerapp.storage.model.Message
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.storage.model.UserWithId
import java.util.function.Consumer
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

class Interactor : AuthInteractor,
    MessageListInteractor,
    ProfileInteractor,
    SearchInteractor,
    ChatInteractor,
    SearchAdapterInteractor {

    private val database = Firebase.database
    private val storageReference = Firebase.storage.reference

    /** interface implementations  **/

    override fun addUser(uid: String, username: String, whatIdo: String) {
        val usersRef = database.getReference("users")
        usersRef.child(uid).setValue(User(username, username, whatIdo))
    }

    override fun getUser(uid: String, function: (u: User) -> Unit) {
        getFullUser(uid, function)
    }

    override fun getMessagesForUser(uid: String, function: (MutableList<Message>) -> Unit) {
        getFullUser(uid) { user -> getUserMessages(user, function) }
    }

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
        ref.getBytes(MAX_IMG_SIZE).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            resultReceiver(resultKey, bitmap)
        }.addOnFailureListener {
            resultReceiver(resultKey, null)
        }
    }

    override fun downloadImage(pathName: String?, resultReceiver: (result: Bitmap?) -> Unit) {
        val ref = storageReference.child("images/$pathName")
        ref.getBytes(MAX_IMG_SIZE).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            resultReceiver(bitmap)
        }.addOnFailureListener {
            resultReceiver(null)
        }
    }

    override fun getNextChunkOfUsers(
        lastUserId: String?,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    ) {
        val usersRef = database.getReference("users")
        if (lastUserId != null) {
            usersRef.orderByKey().startAfter(lastUserId).limitToFirst(USER_CHUNK_SIZE).get()
                .addOnSuccessListener {
                    resultReceiver(resultKey, getChunkOfUsers(it))
                }
        } else {
            usersRef.orderByKey().limitToFirst(USER_CHUNK_SIZE).get().addOnSuccessListener {
                resultReceiver(resultKey, getChunkOfUsers(it))
            }
        }
    }

    override fun getUserByNickname(
        nickname: String,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    ) {
        val usersRef = database.getReference("users")
        usersRef.orderByChild("nickname").equalTo(nickname).get().addOnSuccessListener {
            resultReceiver(resultKey, getChunkOfUsers(it))
        }
    }

    override fun getMessagesForBetweenUsers(
        username1: String,
        username2: String,
        function: (MutableList<Message>) -> Unit
    ) {
        val result = mutableListOf<Message>()
        username1.let {
            database.getReference("messages").child(it).child(username2).get()
                .addOnSuccessListener { res ->
                    result.addAll(res.children.map { dataSnapshot ->
                        val value = dataSnapshot.value as HashMap<*, *>
                        Message(
                            value["sender"] as String,
                            value["message"] as String,
                            value["sendTime"] as Long,
                            username1,
                            username2
                        )
                    });
                    function(result)
                }
        }
    }

    override fun setSenderImage(username: String, receiveResult: KFunction1<Any?, Unit>) {
        val ref = storageReference.child("images/$username")
        ref.getBytes(MAX_IMG_SIZE).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            receiveResult(bitmap)
        }.addOnFailureListener {
            receiveResult(null)
        }
    }

    override fun setSenderWhatIDo(username: String, kFunction1: KFunction1<String, Unit>) {
        val ref = database.getReference("users")
        ref.orderByChild("username").equalTo(username).get().addOnSuccessListener {
            val value = (it.value as HashMap<*, *>).toList()[0].second as HashMap<*, *>
            kFunction1(value["whatIdo"] as String)
        }
    }

    override fun sendMessage(
        text: String?,
        activeUser: String,
        chatUser: String,
        kFunction0: KFunction2<String, String, Unit>
    ) {
        val dbReference = database.getReference("messages")

        val activeUserRef = dbReference.child(activeUser).ref
        val chatUserRef = dbReference.child(chatUser).ref

        dbReference.child(activeUser).child(chatUser).get().addOnSuccessListener {
            val children = mutableListOf<Message>()

            if (it.value != null) {
                it.children.forEach(Consumer { item ->
                    val value = item.value as HashMap<*, *>
                    children.add(
                        Message(
                            value["sender"] as String,
                            value["message"] as String,
                            value["sendTime"] as Long,
                            activeUser,
                            chatUser
                        )
                    )
                })
            }
            children.add(
                Message(
                    activeUser,
                    text,
                    System.currentTimeMillis(),
                    activeUser,
                    chatUser
                )
            )
            val data = HashMap<String, Message>()
            var i: Int = 0
            children.forEach(Consumer { message ->
                if (message != null) {
                    data[i.toString()] = message
                    i++
                }
            })
            activeUserRef.child(chatUser).updateChildren(data as Map<String, Any>)
            kFunction0(activeUser, chatUser)
        }

        dbReference.child(chatUser).child(activeUser).get().addOnSuccessListener {
            val children = mutableListOf<Message>()

            if (it.value != null) {
                it.children.forEach(Consumer { item ->
                    val value = item.value as HashMap<*, *>
                    children.add(
                        Message(
                            value["sender"] as String,
                            value["message"] as String,
                            value["sendTime"] as Long,
                            chatUser,
                            activeUser
                        )
                    )
                })
            }
            children.add(
                Message(
                    activeUser,
                    text,
                    System.currentTimeMillis(),
                    chatUser,
                    activeUser
                )
            )
            val data = HashMap<String, Message>()
            var i: Int = 0
            children.forEach(Consumer { message ->
                if (message != null) {
                    data[i.toString()] = message
                    i++
                }
            })
            chatUserRef.child(activeUser).updateChildren(data as Map<String, Any>)
        }
    }


    /*************************************************/

    /** private methods **/

    private fun getUserMessages(user: User, function: (MutableList<Message>) -> Unit) {
        val result = mutableListOf<Message>()
        user.username?.let {
            database.getReference("messages").child(it).get().addOnSuccessListener { res ->
                res.children.toList().forEach(Consumer { t ->
                    val curData: List<Message> = t.children.map { dataSnapshot ->
                        val value = dataSnapshot.value as HashMap<*, *>

                        Message(
                            value["sender"] as String,
                            value["message"] as String,
                            value["sendTime"] as Long,
                            user.username!!,
                            t.key
                        )

                    }

                    curData.sortedBy { curMes -> curMes.sendTime }

                    if (curData.isNotEmpty()) {
                        result.add(curData[curData.size - 1])
                    }

                    var i = 0

                    result.map { item ->
                        getFullUserByUsername(item.participantTwo!!) { user ->

                            item.nickNameToRender = user.nickname

                            val ref = storageReference.child("images/" + item.participantTwo)
                            ref.getBytes(MAX_IMG_SIZE).addOnSuccessListener { curIt ->
                                i++
                                val bitmap = BitmapFactory.decodeByteArray(curIt, 0, curIt.size)
                                item.imageData = bitmap
                                if (i == result.size) {
                                    function(result)
                                }
                            }.addOnFailureListener {
                                i++
                                item.imageData = null
                                if (i == result.size) {
                                    function(result)
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    private fun getFullUser(uid: String, function: (User) -> Unit) {
        database.getReference("users").child(uid).get().addOnCompleteListener { task ->
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
        }
    }

    private fun getFullUserByUsername(userName: String, function: (User) -> Unit) {
        val usersRef = database.getReference("users")
        usersRef.orderByChild("username").equalTo(userName).get().addOnSuccessListener { task ->
            run {
                val res = task.children.toList()[0].value as HashMap<*, *>
                function(
                    User(
                        res["username"] as String,
                        res["nickname"] as String,
                        res["whatIdo"] as String
                    )
                )
            }
        }

    }

    private fun getChunkOfUsers(it: DataSnapshot?): MutableList<UserWithId> {
        val users = mutableListOf<UserWithId>()
        it?.children?.forEach { u ->
            val userInfo = u.value as HashMap<String, String>
            users.add(
                UserWithId(
                    u.key as String,
                    User(userInfo["username"], userInfo["nickname"], userInfo["whatIdo"])
                )
            )
        }
        return users
    }

    companion object {
        const val USER_CHUNK_SIZE = 10
        const val MAX_IMG_SIZE: Long = 1024 * 1024 * 10

        private var interactorInstance: Interactor? = null

        val instance: Interactor
            get() {
                if (interactorInstance == null) interactorInstance = Interactor()
                return interactorInstance!!
            }
    }

}
