package ge.bkapa.tkats.messengerapp.storage

import ge.bkapa.tkats.messengerapp.storage.model.User

interface ProfileInteractor {

    fun getUserByIdRequest(
        id: String,
        resultKey: String,
        resultReceiver: (key: String, result: Any) -> Unit
    )

    fun updateUser(
        id: String,
        user: User,
        resultKey: String,
        resultReceiver: (key: String, result: Any) -> Unit
    )

}