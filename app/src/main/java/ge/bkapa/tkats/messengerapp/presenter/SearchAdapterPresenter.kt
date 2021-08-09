package ge.bkapa.tkats.messengerapp.presenter

import android.graphics.Bitmap
import ge.bkapa.tkats.messengerapp.storage.Interactor
import ge.bkapa.tkats.messengerapp.storage.SearchAdapterInteractor

class SearchAdapterPresenter {

    private val interactor: SearchAdapterInteractor = Interactor.instance

    fun makeDownloadImageRequest(path: String?, callback: (result: Bitmap?) -> Unit) {
        interactor.downloadImage(path, callback)
    }

}