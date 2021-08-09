package ge.bkapa.tkats.messengerapp.storage

import android.graphics.Bitmap

interface SearchAdapterInteractor {

    fun downloadImage(
        pathName: String?,
        resultReceiver: (result: Bitmap?) -> Unit
    )

}