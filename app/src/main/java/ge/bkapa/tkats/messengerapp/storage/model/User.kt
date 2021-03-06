package ge.bkapa.tkats.messengerapp.storage.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(var username: String? = null, var nickname: String? = null, var whatIdo: String? = null)

data class UserWithId(var uid: String, var user: User)
