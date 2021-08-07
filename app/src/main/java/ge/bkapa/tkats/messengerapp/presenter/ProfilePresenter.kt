package ge.bkapa.tkats.messengerapp.presenter

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.storage.Interactor
import ge.bkapa.tkats.messengerapp.storage.ProfileInteractor
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.view.fragment.ProfileFragment

class ProfilePresenter(private val parentFragment: ProfileFragment) {

    var interactor: ProfileInteractor = Interactor()

    fun makeGetUserByIdRequest(id: String) {
        interactor.getUserByIdRequest(id, ACTIVE_USER, this::receiveResult)
    }

    fun makeUpdateUserRequest(id: String, user: User) {
        interactor.updateUser(id, user, USER_UPDATED, this::receiveResult)
    }

    fun makeImageUploadRequest(pathName: String, imgUri: Uri) {
        interactor.uploadImage(pathName, imgUri, UPLOAD_IMAGE, this::receiveResult)
    }

    fun makeImageDownloadRequest(pathName: String) {
        interactor.downloadImage(pathName, DOWNLOAD_IMAGE, this::receiveResult)
    }

    private fun receiveResult(resultKey: String, result: Any?) {
        when (resultKey) {
            ACTIVE_USER -> {
                parentFragment.initUser(result as User)
            }
            USER_UPDATED -> {
                parentFragment.userUpdateMessage("User Info Updated!")
            }
            UPLOAD_IMAGE -> {
                parentFragment.userUpdateMessage("Image Uploaded!")
            }
            DOWNLOAD_IMAGE -> {
                if (result != null) {
                    parentFragment.changeImageIV.setImageBitmap(result as Bitmap)
                } else {
                    parentFragment.changeImageIV.setImageResource(R.drawable.avatar_image_placeholder)
                }
            }
        }
    }

    /**
     * Result keys from Interactor
     */
    companion object {
        const val ACTIVE_USER = "ACTIVE_USER"
        const val USER_UPDATED = "USER_UPDATED"
        const val UPLOAD_IMAGE = "UPLOAD_IMAGE"
        const val DOWNLOAD_IMAGE = "DOWNLOAD_IMAGE"
    }
}