package ge.bkapa.tkats.messengerapp.storage

import android.graphics.Bitmap
import ge.bkapa.tkats.messengerapp.storage.model.Message

interface MessageListInteractor {

    fun getMessagesForUser(uid: String, function: (MutableList<Message>) -> Unit)

    fun getImage(userName: String, kFunction1: (result: Bitmap?) -> Unit)

}