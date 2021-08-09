package ge.bkapa.tkats.messengerapp.view

import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation

interface ChatActivityStarter {

    fun startChatActivity(uid: ListMessageRepresentation);

}