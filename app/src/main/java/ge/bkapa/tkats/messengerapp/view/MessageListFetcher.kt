package ge.bkapa.tkats.messengerapp.view

import java.io.Serializable

interface MessageListFetcher : Serializable{

    fun fetchMessageList()

}