package ge.bkapa.tkats.messengerapp.storage

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

}