package ge.bkapa.tkats.messengerapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class MessengerExtensions {
    companion object{

        @SuppressLint("SimpleDateFormat")
        var format = SimpleDateFormat("dd/MM")

        fun toMessageListTime(milliseconds:Long):String{
            var diff = (System.currentTimeMillis() - milliseconds)/1000

            if (diff<60) return "$diff sec"

            diff /= 60
            if(diff<60) return "$diff min"

            diff /= 60
            if(diff<60) return "$diff hours"

            return format.format(Date(milliseconds))
        }
    }
}