package ge.bkapa.tkats.messengerapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.widget.RxTextView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.adapter.SearchListAdapter
import ge.bkapa.tkats.messengerapp.presenter.SearchPresenter
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.storage.model.UserWithId
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    var presenter = SearchPresenter(this)
    var fetchedUsers = mutableListOf<User>()
    var rvAdapter: SearchListAdapter = SearchListAdapter(fetchedUsers, this)
    lateinit var rv: RecyclerView

    private lateinit var user: User
    var searchWord = ""

    // For Lazy Loading
    var lastUserId: String? = null // last user id
    var fetchedAll = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        intent.getStringExtra(MessageListActivity.USER_ID)?.let {
            presenter.makeGetUserByIdRequest(it)
        }
        initView()
    }

    fun getNextUserChunk() {
        if (!fetchedAll) {
            toggleProgressBar()
            presenter.makeNextChunkRequest(lastUserId)
        }
    }

    fun receiveNextUserChunk(users: MutableList<UserWithId>) {
        toggleProgressBar()
        if (fetchedAll && users.size == 0) {
            findViewById<TextView>(R.id.error_message).visibility = View.VISIBLE
        } else if (users.size == 0) {
            fetchedAll = true
        } else {
            lastUserId = users[users.size - 1].uid
            fetchedUsers.addAll(users.map { it.user })
            rvAdapter.notifyDataSetChanged()
        }
    }

    fun initUser(user: User) {
        this.user = user
    }

    private fun toggleProgressBar() {
        val progressBar = findViewById<ProgressBar>(R.id.user_loading)
        progressBar.visibility = if (progressBar.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun initView() {
        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            onBackPressed()
        }
        RxTextView.textChangeEvents(findViewById<EditText>(R.id.user_search_edit_text))
            .debounce(1500, TimeUnit.MILLISECONDS).subscribe {
                runOnUiThread {
                    searchWord = findViewById<EditText>(R.id.user_search_edit_text).text.toString()
                    fetchedAll = false
                    lastUserId = null
                    fetchedUsers.clear()
                    rvAdapter.notifyDataSetChanged()
                    findViewById<TextView>(R.id.error_message).visibility = View.GONE
                    if (searchWord.length > 2) {
                        fetchedAll = true
                        findUserByNickname(searchWord)
                    } else if (searchWord.isEmpty()) {
                        getNextUserChunk()
                    }
                }
            }
        rv = findViewById(R.id.search_recycle_view)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.adapter = rvAdapter
    }

    private fun findUserByNickname(searchWord: String) {
        toggleProgressBar()
        presenter.makeUserSearchRequest(searchWord)
    }

    fun startChatActivity(username: String?) {

        presenter.getCurrentUser {
            finish()
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(MessageListActivity.USERNAME1, it.username)
            intent.putExtra(MessageListActivity.USERNAME2, username)

            startActivity(intent)

        }

    }

}