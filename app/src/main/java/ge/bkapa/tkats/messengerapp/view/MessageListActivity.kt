package ge.bkapa.tkats.messengerapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.adapter.MessagePageFragmentAdapter
import ge.bkapa.tkats.messengerapp.storage.model.ListMessageRepresentation
import ge.bkapa.tkats.messengerapp.view.fragment.MessageListFragment
import java.io.Serializable

class MessageListActivity : AppCompatActivity(),
                            FragmentedActivity,
                            ChatActivityStarter,
                            Serializable,
                            MessageListFetcher {

    private lateinit var fragmentAdapter: MessagePageFragmentAdapter

    private var viewpager: ViewPager2 ? = null

    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        initView()
    }

    private fun initView() {
        userId = intent.getStringExtra(AuthorizationActivity.USER_ID) as String
        fragmentAdapter = MessagePageFragmentAdapter(this, userId)
        viewpager = findViewById(R.id.messenger_main_fragment_container)
        viewpager?.adapter = fragmentAdapter

        viewpager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                    findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.GONE
                }
                if (position == 0) {
                    findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE
                }
            }
        })

        findViewById<AppCompatImageView>(R.id.settings).setOnClickListener {
            viewpager?.currentItem = 1
        }
        findViewById<AppCompatImageView>(R.id.home).setOnClickListener {
            viewpager?.currentItem = 0
        }
        findViewById<FloatingActionButton>(R.id.open_search_button).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra(USER_ID, userId)
            startActivity(intent)
        }
        findViewById<EditText>(R.id.message_list_search).addTextChangedListener { text ->
            (getFragment(0) as MessageListFragment).searchMessage(text.toString())
        }

    }

    override fun startChatActivity(uid: ListMessageRepresentation){
        val messageListFragment = getFragment(0) as MessageListFragment

       messageListFragment.getActiveUser {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra(USERNAME1, it.username)
                intent.putExtra(USERNAME2, uid.userName)
                intent.putExtra(NAME, uid.nickNameToRender)
                startActivity(intent)
        }
    }

    override fun getMessageListFragment() : MessageListFragment {
        return getFragment(0) as MessageListFragment
    }

    override fun onResume() {
        super.onResume()
        if (viewpager!=null){
            fetchMessageList()
        }
    }


    override fun fetchMessageList() {
        (getFragment(0) as MessageListFragment).getData()
    }

    //TODO: onResume  - check user sign in status

    override fun getFragment(index: Int): Fragment {
        return fragmentAdapter.pageLists[index]
    }

    companion object {
        const val USER_ID = "USER_ID"
        const val USERNAME1 = "USERNAME1"
        const val USERNAME2 = "USERNAME2"
        const val NAME = "NAME"
    }

}