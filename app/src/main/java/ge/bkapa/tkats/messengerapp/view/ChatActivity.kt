package ge.bkapa.tkats.messengerapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.adapter.ChatListAdapter
import ge.bkapa.tkats.messengerapp.presenter.ChatPresenter
import ge.bkapa.tkats.messengerapp.storage.model.ChatMessageRepresentation

class ChatActivity : AppCompatActivity() {

    private lateinit var chatPresenter : ChatPresenter

    private lateinit var chatListAdapter : ChatListAdapter

    private lateinit var activeUsername : String

    private lateinit var chatMessages : MutableList<ChatMessageRepresentation>

    private lateinit var chatList : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        init()
    }

    fun init(){
        chatPresenter =  ChatPresenter(this)
        chatMessages = mutableListOf()
        chatListAdapter = ChatListAdapter(chatMessages,this)

        chatList = findViewById(R.id.chat_list)
        chatList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        chatList.adapter = chatListAdapter

        intent.getStringExtra(MessageListActivity.USERNAME1).let {
            activeUsername = it!!
        }

        findViewById<TextView>(R.id.sender_username).text = intent.getStringExtra(
            MessageListActivity.NAME
        )!!


        chatPresenter.setSenderImage(intent.getStringExtra(MessageListActivity.USERNAME2)!!)
        chatPresenter.setSenderWhatIDo(intent.getStringExtra(MessageListActivity.USERNAME2)!!)


        chatPresenter.getAllChatForUser(
            intent.getStringExtra(MessageListActivity.USERNAME1)!!,
            intent.getStringExtra(MessageListActivity.USERNAME2)!!
        )

        findViewById<ImageView>(R.id.chat_back_icon).setOnClickListener(View.OnClickListener {
            finish()
        })

        findViewById<ImageView>(R.id.send_message).setOnClickListener(View.OnClickListener {
            chatPresenter.sendMessage(
                findViewById<EditText>(R.id.message_text).text.toString(),
                intent.getStringExtra(MessageListActivity.USERNAME1)!!,
                intent.getStringExtra(MessageListActivity.USERNAME2)!!
            )
        })
    }



    fun onChatFetched(list: MutableList<ChatMessageRepresentation>) {
        chatListAdapter.messages.clear()
        chatListAdapter.messages.addAll(list)
        chatListAdapter.notifyDataSetChanged()
    }

    fun getActiveUser(): String {
        return activeUsername
    }

}