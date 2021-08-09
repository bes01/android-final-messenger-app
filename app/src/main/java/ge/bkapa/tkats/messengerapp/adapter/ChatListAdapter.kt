package ge.bkapa.tkats.messengerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.storage.model.ChatMessageRepresentation
import ge.bkapa.tkats.messengerapp.view.ChatActivity

class ChatListAdapter(var messages : MutableList<ChatMessageRepresentation>,var activity: ChatActivity ):
    RecyclerView.Adapter<AbstractChatItemViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractChatItemViewHolder<*> {


        return when (viewType) {
            TYPE_SENDER -> {
                val view = LayoutInflater.from(activity)
                    .inflate(R.layout.chat_sender_item, parent, false)
                ChatSenderItemHolder(view)
            }
            TYPE_RECEIVER -> {
                val view = LayoutInflater.from(activity)
                    .inflate(R.layout.chat_receiver_item, parent, false)
                ChatReceiverItemHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun getItemCount(): Int {
        return  messages.size
    }

    override fun onBindViewHolder(holder: AbstractChatItemViewHolder<*>, position: Int) {
        val element = messages[position]

        when (holder) {
            is ChatSenderItemHolder -> {
                holder.bind(element)
            }
            is ChatReceiverItemHolder -> {
                holder.bind(element)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (activity.getActiveUser() == messages[position].sender){
            0
        }else{
            1
        }
    }

    companion object {
        private const val TYPE_SENDER = 0
        private const val TYPE_RECEIVER = 1
    }

}

abstract class AbstractChatItemViewHolder<T>(itemView:View) : RecyclerView.ViewHolder(itemView){
    abstract fun bind(item : T);
}

class ChatSenderItemHolder (itemView: View): AbstractChatItemViewHolder<ChatMessageRepresentation>(itemView){
    override fun bind(item: ChatMessageRepresentation) {
        text.text = item.message
        time.text = item.sendTime
    }

    var text : TextView = itemView.findViewById(R.id.sender_text)
    var time : TextView = itemView.findViewById(R.id.sender_time)

}

class ChatReceiverItemHolder (itemView: View): AbstractChatItemViewHolder<ChatMessageRepresentation>(itemView){


    override fun bind(item: ChatMessageRepresentation) {
        text.text = item.message
        time.text = item.sendTime
    }

    var text : TextView = itemView.findViewById(R.id.receiver_text)
    var time : TextView = itemView.findViewById(R.id.receiver_time)

}


