package ge.bkapa.tkats.messengerapp.storage

import ge.bkapa.tkats.messengerapp.storage.model.User

interface AuthInteractor {

    fun addUser(uid: String, username: String, whatIdo: String)

    fun getUser(function1: String, function: (u: User) -> Unit)

}
