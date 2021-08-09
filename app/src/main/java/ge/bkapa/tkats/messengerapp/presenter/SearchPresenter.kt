package ge.bkapa.tkats.messengerapp.presenter

import ge.bkapa.tkats.messengerapp.service.AuthorizationService
import ge.bkapa.tkats.messengerapp.storage.AuthInteractor
import ge.bkapa.tkats.messengerapp.storage.Interactor
import ge.bkapa.tkats.messengerapp.storage.SearchInteractor
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.storage.model.UserWithId
import ge.bkapa.tkats.messengerapp.view.SearchActivity

class SearchPresenter(private val activity: SearchActivity) {

    private var interactor: SearchInteractor = Interactor()

    private var authService = AuthorizationService(activity)

    fun makeGetUserByIdRequest(id: String) {
        interactor.getUserByIdRequest(id, ACTIVE_USER, this::receiveResult)
    }

    fun makeNextChunkRequest(lastUsername: String?) {
        interactor.getNextChunkOfUsers(lastUsername, NEXT_USERS, this::receiveResult)
    }

    fun makeUserSearchRequest(nickname: String) {
        interactor.getUserByNickname(nickname, USERS_BY_NICKNAME, this::receiveResult);
    }

    fun getCurrentUser(function: (u: User) -> Unit) {
        authService.getCurrentUser(function)
    }

    private fun receiveResult(resultKey: String, result: Any?) {
        when (resultKey) {
            ACTIVE_USER -> {
                activity.initUser(result as User)
            }
            NEXT_USERS -> {
                activity.receiveNextUserChunk(result as MutableList<UserWithId>)
            }
            USERS_BY_NICKNAME -> {
                activity.receiveNextUserChunk(result as MutableList<UserWithId>)
            }
        }
    }

    /**
     * Result keys from Interactor
     */
    companion object {
        const val ACTIVE_USER = "ACTIVE_USER"
        const val NEXT_USERS = "NEXT_USERS"
        const val USERS_BY_NICKNAME = "USERS_BY_NICKNAME"
    }

}