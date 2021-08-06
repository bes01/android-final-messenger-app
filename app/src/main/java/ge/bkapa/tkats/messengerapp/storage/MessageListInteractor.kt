package ge.bkapa.tkats.messengerapp.storage

import ge.bkapa.tkats.messengerapp.storage.model.Message

interface MessageListInteractor {

    fun getMessagesForUser(uid: String, function: (MutableList<Message>) -> Unit)

}