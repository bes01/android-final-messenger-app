package ge.bkapa.tkats.messengerapp.presenter

import android.graphics.Bitmap
import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.service.ChatService
import ge.bkapa.tkats.messengerapp.storage.model.ChatMessageRepresentation
import ge.bkapa.tkats.messengerapp.view.ChatActivity

class ChatPresenter(var chatActivity:ChatActivity){

    private var chatService = ChatService()

    fun getAllChatForUser(username1: String,username2: String){
        chatActivity.findViewById<RecyclerView>(R.id.chat_list).visibility = View.GONE
        chatActivity.findViewById<ScrollView>(R.id.no_chat_result).visibility = View.GONE
        chatActivity.findViewById<ProgressBar>(R.id.chat_loading).visibility = View.VISIBLE

        chatService.getChatForUser(username1, username2) { list: MutableList<ChatMessageRepresentation> ->
            chatActivity.onChatFetched(list)
            if (list.size==0){
                chatActivity.findViewById<ScrollView>(R.id.no_chat_result).visibility = View.VISIBLE
                chatActivity.findViewById<RecyclerView>(R.id.chat_list).visibility = View.GONE
            }else{
                chatActivity.findViewById<ScrollView>(R.id.no_chat_result).visibility = View.GONE
                chatActivity.findViewById<RecyclerView>(R.id.chat_list).visibility = View.VISIBLE
            }
            chatActivity.findViewById<ProgressBar>(R.id.chat_loading).visibility = View.GONE
        }
    }

    fun setSenderImage(username: String) {
        chatActivity.findViewById<ProgressBar>(R.id.chat_sender_loader).visibility = View.VISIBLE
        chatActivity.findViewById<ImageView>(R.id.sender_image).visibility = View.GONE
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
        chatActivity.findViewById<ProgressBar>(R.id.chat_sender_loader).visibility = View.GONE
        chatActivity.findViewById<ImageView>(R.id.sender_image).visibility = View.VISIBLE

        if (result != null) {
            chatActivity.findViewById<ImageView>(R.id.sender_image).setImageBitmap((result as Bitmap))
        } else {
            chatActivity.findViewById<ImageView>(R.id.sender_image).setImageResource(R.drawable.avatar_image_placeholder)
        }
    }

    private fun receiveWhatIDoResult(res:String) {
        chatActivity.findViewById<TextView>(R.id.sender_what_i_do).text = res

    }

    fun setEventListener(username1: String, username2: String) {
        chatService.setEventListener(username1,username2,this::getAllChatForUser)
    }

}