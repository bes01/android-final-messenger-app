package ge.bkapa.tkats.messengerapp.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.bkapa.tkats.messengerapp.storage.ChatInteractor
import ge.bkapa.tkats.messengerapp.storage.Interactor
import ge.bkapa.tkats.messengerapp.storage.model.ChatMessageRepresentation
import ge.bkapa.tkats.messengerapp.storage.model.Message
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

class ChatService {

    private var auth: FirebaseAuth = Firebase.auth
    private var interactor: ChatInteractor = Interactor.instance

    fun getChatForUser(username1: String,username2: String,function: (MutableList<ChatMessageRepresentation>) -> Unit) {
        interactor.getMessagesForBetweenUsers(username1,username2) { list: MutableList<Message> ->
            function(list.map { message -> message.toChatMessageRepresentation() } as MutableList<ChatMessageRepresentation>)
        }
    }

    fun setSenderImage(username: String, receiveResult: KFunction1<Any?, Unit>) {
        interactor.setSenderImage(username, receiveResult)
    }

    fun setSenderWhatIDo(username: String, kFunction1: KFunction1<String, Unit>) {
        interactor.setSenderWhatIDo(username,kFunction1)
    }

    fun sendMessage(
        text: String?,
        activeUser: String,
        chatUser: String,
        kFunction0: KFunction2<String, String, Unit>
    ) {

        interactor.sendMessage(text,activeUser,chatUser,kFunction0)
    }

}