package ge.bkapa.tkats.messengerapp.presenter

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.service.ChatService
import ge.bkapa.tkats.messengerapp.storage.model.ChatMessageRepresentation
import ge.bkapa.tkats.messengerapp.view.ChatActivity

class ChatPresenter(var chatActivity:ChatActivity){

    private var chatService = ChatService()

    fun getAllChatForUser(username1: String,username2: String){
        chatService.getChatForUser(username1, username2) { list: MutableList<ChatMessageRepresentation> ->
            chatActivity.onChatFetched(list)
        }
    }

    fun setSenderImage(username: String) {
        chatService.setSenderImage(username,this::receiveResult)
    }



    fun setSenderWhatIDo(username: String) {
        chatService.setSenderWhatIDo(username,this::receiveWhatIDoResult)
    }


    fun sendMessage(text: String?, activeUser: String, chatUser: String) {
        chatService.sendMessage(text,activeUser,chatUser, this::getAllChatForUser)
    }


    /** private functions **/
    private fun receiveResult( result: Any?) {
        if (result != null) {
            chatActivity.findViewById<ImageView>(R.id.sender_image).setImageBitmap((result as Bitmap))
        } else {
            chatActivity.findViewById<ImageView>(R.id.sender_image).setImageResource(R.drawable.avatar_image_placeholder)
        }
    }

    private fun receiveWhatIDoResult(res:String) {
        chatActivity.findViewById<TextView>(R.id.sender_what_i_do).text = res

    }

}