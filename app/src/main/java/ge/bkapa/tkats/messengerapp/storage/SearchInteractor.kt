package ge.bkapa.tkats.messengerapp.storage

import ge.bkapa.tkats.messengerapp.storage.model.User

interface SearchInteractor {

    fun getUserByIdRequest(
        id: String,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    )

    fun getNextChunkOfUsers(
        lastUserId: String?,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    )

    fun getUserByNickname(
        nickname: String,
        resultKey: String,
        resultReceiver: (key: String, result: Any?) -> Unit
    )

    fun getUser(
        function1: String,
        function: (u: User) -> Unit
    )

}