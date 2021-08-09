package ge.bkapa.tkats.messengerapp.storage.model

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

    fun toListMessageRepresentation() :ListMessageRepresentation{
        return ListMessageRepresentation(
            participantTwo!!,
            this.message!!,
            MessengerExtensions.toMessageListTime(this.sendTime!!)
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