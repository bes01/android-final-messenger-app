package ge.bkapa.tkats.messengerapp.presenter

import ge.bkapa.tkats.messengerapp.storage.Interactor
import ge.bkapa.tkats.messengerapp.storage.ProfileInteractor
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.view.fragment.ProfileFragment

class ProfilePresenter(private val parentFragment: ProfileFragment) {

    var interactor: ProfileInteractor = Interactor()

    fun makeGetUserByIdRequest(id: String) {
        interactor.getUserByIdRequest(id, ACTIVE_USER, this::receiveResult)
    }

    fun updateUser(id: String, user: User) {
        interactor.updateUser(id, user, USER_UPDATED, this::receiveResult)
    }

    private fun receiveResult(resultKey: String, result: Any) {
        if (resultKey == ACTIVE_USER) {
            parentFragment.initUser(result as User)
        } else if(resultKey == USER_UPDATED){
            parentFragment.userUpdateMessage("Updated Successfully!")
        }
    }

    /**
     * Result keys from Interactor
     */
    companion object {
        const val ACTIVE_USER = "ACTIVE_USER"
        const val USER_UPDATED = "USER_UPDATED"
    }
}