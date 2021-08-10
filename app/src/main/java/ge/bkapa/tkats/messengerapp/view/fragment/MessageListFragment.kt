package ge.bkapa.tkats.messengerapp.view.fragment

import android.graphics.Bitmap
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.adapter.MessageListAdapter
import ge.bkapa.tkats.messengerapp.presenter.MessageListPresenter
import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.view.MessageListActivity


class MessageListFragment(var activity: MessageListActivity) : Fragment() {

    private lateinit var messageList : MutableList<ListMessageRepresentation>

    private lateinit var listAdapter : MessageListAdapter

    private var messageListPresenter: MessageListPresenter  ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageList = mutableListOf()
        listAdapter = MessageListAdapter(messageList,activity)
        messageListPresenter = MessageListPresenter(this, activity)
    }

    fun getData() {
        if (messageListPresenter!=null){
            messageListPresenter?.getAllMessagesForUser()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_message_list, container, false)
        val list : RecyclerView = inflate.findViewById(R.id.message_list)
        list.adapter = listAdapter
        list.layoutManager =LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL, false)

        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    fun onMessageListFetched(list: MutableList<ListMessageRepresentation>) {
        messageList.clear()
        messageList.addAll(list)
        listAdapter.notifyDataSetChanged()
        closeLoader()
    }

    fun getActiveUser(function: (u: User) -> Unit) {
        messageListPresenter?.getActiveUser(function)
    }

    fun initLoader() {
        val loader = activity.findViewById<ConstraintLayout>(R.id.message_list_loader_container)
        if (loader!=null){
            loader.visibility = View.VISIBLE
        }

        val list = activity.findViewById<RecyclerView>(R.id.message_list)

        if (list!=null) {
            list.visibility = View.GONE
        }
    }

    fun closeLoader(){
        val loader = activity.findViewById<ConstraintLayout>(R.id.message_list_loader_container)
        if (loader!=null){
            loader.visibility = View.GONE
        }

        val list = activity.findViewById<RecyclerView>(R.id.message_list)

        if (list!=null) {
            list.visibility = View.VISIBLE
        }
    }

    fun getImage(userName: String, kFunction1: (result: Bitmap?) -> Unit) {
        messageListPresenter?.getImage(userName,kFunction1)
    }


}