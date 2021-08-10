package ge.bkapa.tkats.messengerapp.view

import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation

interface ChatActivityStarter : FragmentFetcher{

    fun startChatActivity(uid: ListMessageRepresentation);

}