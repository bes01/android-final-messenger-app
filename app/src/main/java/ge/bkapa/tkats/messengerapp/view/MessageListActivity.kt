package ge.bkapa.tkats.messengerapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.adapter.MessagePageFragmentAdapter

class MessageListActivity : AppCompatActivity(), FragmentedActivity {

    private lateinit var fragmentAdapter: MessagePageFragmentAdapter

    private lateinit var viewpager: ViewPager2

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
        viewpager.adapter = fragmentAdapter

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
            viewpager.currentItem = 1
        }
        findViewById<AppCompatImageView>(R.id.home).setOnClickListener {
            viewpager.currentItem = 0
        }
        findViewById<FloatingActionButton>(R.id.open_search_button).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra(USER_ID, userId)
            startActivity(intent)
        }
    }

    //TODO: onResume  - check user sign in status

    override fun getFragment(index: Int): Fragment {
        return fragmentAdapter.pageLists[index]
    }

    companion object {
        const val USER_ID = "USER_ID"
    }

}