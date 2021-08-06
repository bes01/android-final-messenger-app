package ge.bkapa.tkats.messengerapp.storage.model

data class ListMessageRepresentation(
    var userName:String,
    var message:String,
    var formattedTime :String
    //TODO [bkapa] image
)