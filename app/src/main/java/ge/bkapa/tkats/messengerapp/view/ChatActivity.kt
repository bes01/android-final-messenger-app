package ge.bkapa.tkats.messengerapp.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        chatList.layoutManager = linearLayoutManager
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
            val editText = findViewById<EditText>(R.id.message_text)
            val text : String = editText.text.toString()
            editText.text.clear()

        // close edittext not necessary
//            val imm: InputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            var view : View? = this.currentFocus
//            if (view == null) {
//                view = View(this)
//            }
//            imm.hideSoftInputFromWindow(view.windowToken, 0)

            chatPresenter.sendMessage(
                text,
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