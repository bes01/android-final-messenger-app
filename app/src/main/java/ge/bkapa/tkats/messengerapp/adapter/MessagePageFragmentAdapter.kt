package ge.bkapa.tkats.messengerapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.bkapa.tkats.messengerapp.view.MessageListActivity
import ge.bkapa.tkats.messengerapp.view.fragment.ProfileFragment
import ge.bkapa.tkats.messengerapp.view.fragment.MessageListFragment

class MessagePageFragmentAdapter(activity: MessageListActivity, userId: String) : FragmentStateAdapter(activity) {

    var pageLists = arrayListOf(MessageListFragment(activity), ProfileFragment(activity, userId))


    override fun getItemCount(): Int {
        return pageLists.size
    }

    override fun createFragment(position: Int): Fragment {
        return pageLists[position]
    }

}