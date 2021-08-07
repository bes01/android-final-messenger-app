package ge.bkapa.tkats.messengerapp.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.presenter.ProfilePresenter
import ge.bkapa.tkats.messengerapp.service.AuthorizationService
import ge.bkapa.tkats.messengerapp.storage.model.User


class ProfileFragment(private val activity: Activity, val userId: String) : Fragment() {

    lateinit var authService: AuthorizationService

    lateinit var presenter: ProfilePresenter

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authService = AuthorizationService(activity)
        presenter = ProfilePresenter(this)
        presenter.makeGetUserByIdRequest(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    fun initUser(user: User) {
        val changeNameET = activity.findViewById<EditText>(R.id.change_nickname)
        val changeWhatIDoET = activity.findViewById<EditText>(R.id.change_what_u_do)

        changeNameET.setText(user.nickname)
        changeWhatIDoET.setText(user.whatIdo)

        this.user = user
    }

    private fun initView() {
        val changeNameET = activity.findViewById<EditText>(R.id.change_nickname)
        val changeWhatIDoET = activity.findViewById<EditText>(R.id.change_what_u_do)

        activity.findViewById<Button>(R.id.sign_out_button)?.setOnClickListener {
            activity.finish()
            authService.logout()
        }
        activity.findViewById<Button>(R.id.update_button)?.setOnClickListener {
            val newNickname = changeNameET.text.toString()
            val newWhatIDo = changeWhatIDoET.text.toString()
            presenter.updateUser(userId, User(user.username, newNickname, newWhatIDo))
        }
    }

    fun userUpdateMessage() {
        Toast.makeText(activity, "Updated Successfully!", Toast.LENGTH_LONG).show()
    }

}