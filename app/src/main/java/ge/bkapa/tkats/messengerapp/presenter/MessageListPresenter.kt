package ge.bkapa.tkats.messengerapp.presenter

import android.graphics.Bitmap
import ge.bkapa.tkats.messengerapp.service.AuthorizationService
import ge.bkapa.tkats.messengerapp.service.MessageListService
import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.view.MessageListActivity
import ge.bkapa.tkats.messengerapp.view.fragment.MessageListFragment

class MessageListPresenter(var fragment: MessageListFragment,activity: MessageListActivity){

    var messageService = MessageListService(activity)

    var authService = AuthorizationService(activity)

    fun getAllMessagesForUser(){
        fragment.initLoader()
        messageService.getMessagesForUser { list: MutableList<ListMessageRepresentation> ->
            fragment.onMessageListFetched(list)
            fragment.closeLoader()
        }
    }

    fun getActiveUser(function: (u: User) -> Unit) {
        authService.getCurrentUser(function)
    }

    fun getImage(userName: String, kFunction1: (result: Bitmap?) -> Unit) {
        messageService.getImage(userName,kFunction1)
    }

}