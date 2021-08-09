package ge.bkapa.tkats.messengerapp.storage

import ge.bkapa.tkats.messengerapp.storage.model.Message
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

interface ChatInteractor {

    fun getMessagesForBetweenUsers(
        username1 : String,
        username2 : String,
        function: (MutableList<Message>) -> Unit
    )

    fun setSenderImage(username: String, receiveResult: KFunction1<Any?, Unit>)

    fun setSenderWhatIDo(username: String, kFunction1: KFunction1<String, Unit>)

    fun sendMessage(
        text: String?,
        activeUser: String,
        chatUser: String,
        kFunction0: KFunction2<String, String, Unit>
    )

}