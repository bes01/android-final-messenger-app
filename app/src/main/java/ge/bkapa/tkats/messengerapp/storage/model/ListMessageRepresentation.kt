package ge.bkapa.tkats.messengerapp.storage.model

import android.graphics.Bitmap

data class ListMessageRepresentation(
    var userName: String,
    var message: String,
    var formattedTime: String,
    var nickNameToRender: String,
    var imageData : Bitmap ?
    //TODO [bkapa] image
)