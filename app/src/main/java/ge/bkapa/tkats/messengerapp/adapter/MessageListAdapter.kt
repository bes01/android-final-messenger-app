package ge.bkapa.tkats.messengerapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation
import ge.bkapa.tkats.messengerapp.storage.model.Message
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MessageListAdapter (var list: MutableList<ListMessageRepresentation>): RecyclerView.Adapter<MessageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


class MessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    fun bind(message:ListMessageRepresentation){
        userName.text = message.userName
        messageText.text = message.message
        sendTime.text = message.formattedTime
    }


    private var userName: TextView = itemView.findViewById(R.id.userName)
    private var messageText: TextView = itemView.findViewById(R.id.message)
    private var sendTime: TextView = itemView.findViewById(R.id.sendTime)

}