package ge.bkapa.tkats.messengerapp.storage.model

class ChatMessageRepresentation(
    var sender: String? = null,
    var message: String? = null,
    var sendTime: String? = null,
    var participantOne: String,
    var participantTwo: String?,

    ) {
}