package ge.bkapa.tkats.messengerapp.storage.model

import com.google.firebase.database.IgnoreExtraProperties
import ge.bkapa.tkats.messengerapp.util.MessengerExtensions

@IgnoreExtraProperties

data class Message(
    var sender:String ? = null,
    var message:String ? = null,
    var sendTime:Long ? = null
){

    fun toListMessageRepresentation() :ListMessageRepresentation{
        return ListMessageRepresentation(
            this.sender!!,
            this.message!!,
            MessengerExtensions.toMessageListTime(this.sendTime!!)
        )
    }

}