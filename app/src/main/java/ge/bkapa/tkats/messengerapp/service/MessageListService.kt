package ge.bkapa.tkats.messengerapp.service

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.bkapa.tkats.messengerapp.storage.Interactor
import ge.bkapa.tkats.messengerapp.storage.MessageListInteractor
import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation
import ge.bkapa.tkats.messengerapp.storage.model.Message
import ge.bkapa.tkats.messengerapp.view.MessageListActivity

class MessageListService (val parentActivity:MessageListActivity){

    private var auth: FirebaseAuth = Firebase.auth
    private var interactor: MessageListInteractor = Interactor.instance

    fun getMessagesForUser(function: (MutableList<ListMessageRepresentation>) -> Unit) {
        interactor.getMessagesForUser(auth.currentUser!!.uid) { list: MutableList<Message> ->
            function(list.map { message -> message.toListMessageRepresentation() } as MutableList<ListMessageRepresentation>)
        }
    }

    fun getImage(userName: String, kFunction1: (result: Bitmap?) -> Unit) {
        interactor.getImage(userName,kFunction1)
    }

    fun searchMessage(text: String, function: (MutableList<ListMessageRepresentation>) -> Unit) {
        interactor.searchMessagesForUser(auth.currentUser!!.uid,text){ list :MutableList<Message>->
            function(list.map { message -> message.toListMessageRepresentation() } as MutableList<ListMessageRepresentation>)
        }
    }

}
