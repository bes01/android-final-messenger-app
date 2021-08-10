package ge.bkapa.tkats.messengerapp.view

import ge.bkapa.tkats.messengerapp.view.fragment.MessageListFragment

interface FragmentFetcher {

    fun getMessageListFragment() : MessageListFragment
}