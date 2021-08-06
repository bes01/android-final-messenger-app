package ge.bkapa.tkats.messengerapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.bkapa.tkats.messengerapp.view.MessageListActivity
import ge.bkapa.tkats.messengerapp.view.fragment.EditUserFragment
import ge.bkapa.tkats.messengerapp.view.fragment.MessageListFragment

class MessagePageFragmentAdapter(activity: MessageListActivity): FragmentStateAdapter(activity) {

    var pageLists= arrayListOf<Fragment>(MessageListFragment(activity),EditUserFragment())


    override fun getItemCount(): Int {
        return pageLists.size
    }

    override fun createFragment(position: Int): Fragment {
        return pageLists[position]
    }

}