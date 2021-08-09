package ge.bkapa.tkats.messengerapp.storage.model

import android.graphics.Bitmap
import com.google.firebase.database.IgnoreExtraProperties
import ge.bkapa.tkats.messengerapp.util.MessengerExtensions

@IgnoreExtraProperties

data class Message(
    var sender: String? = null,
    var message: String? = null,
    var sendTime: Long? = null,
    var participantOne: String,
    var participantTwo: String?,

    ){
    var imageData : Bitmap ? = null
    var nickNameToRender: String? = participantTwo

    fun toListMessageRepresentation() :ListMessageRepresentation{
        return ListMessageRepresentation(
            participantTwo!!,
            this.message!!,
            MessengerExtensions.toMessageListTime(this.sendTime!!),
            nickNameToRender!!,
            imageData
        )
    }

    fun toChatMessageRepresentation() : ChatMessageRepresentation{
        return  ChatMessageRepresentation(
            sender,
            message,
            MessengerExtensions.toMessageListTime(this.sendTime!!),
            participantTwo!!,
            this.message!!,
        )
    }

}