package ge.bkapa.tkats.messengerapp.view.fragment

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.adapter.MessageListAdapter
import ge.bkapa.tkats.messengerapp.presenter.MessageListPresenter
import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation
import ge.bkapa.tkats.messengerapp.storage.model.Message
import ge.bkapa.tkats.messengerapp.view.MessageListActivity


class MessageListFragment(var activity: MessageListActivity) : Fragment() {

    private lateinit var messageList:MutableList<ListMessageRepresentation>

    private lateinit var listAdapter:MessageListAdapter

    private lateinit var messageListPresenter:MessageListPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageList = mutableListOf()
        listAdapter = MessageListAdapter(messageList)
        messageListPresenter = MessageListPresenter(this, activity)

        messageListPresenter.getAllMessagesForUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_message_list, container, false)
        val list : RecyclerView = inflate.findViewById<RecyclerView>(R.id.message_list)
        list.adapter = listAdapter
        list.layoutManager =LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL, false)

        return inflate
    }

    fun onMessageListFetched(list: MutableList<ListMessageRepresentation>) {
        messageList.clear()
        messageList.addAll(list)
        listAdapter.notifyDataSetChanged()
    }


}