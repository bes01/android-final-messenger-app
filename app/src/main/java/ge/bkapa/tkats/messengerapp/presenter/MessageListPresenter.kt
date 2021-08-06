package ge.bkapa.tkats.messengerapp.presenter

import ge.bkapa.tkats.messengerapp.service.MessageListService
import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation
import ge.bkapa.tkats.messengerapp.storage.model.Message
import ge.bkapa.tkats.messengerapp.view.MessageListActivity
import ge.bkapa.tkats.messengerapp.view.fragment.MessageListFragment

class MessageListPresenter(var fragment: MessageListFragment,activity: MessageListActivity){

    var messageService = MessageListService(activity)


    fun getAllMessagesForUser(){
        messageService.getMessagesForUser { list: MutableList<ListMessageRepresentation> ->
            fragment.onMessageListFetched(list)
        }
    }

}