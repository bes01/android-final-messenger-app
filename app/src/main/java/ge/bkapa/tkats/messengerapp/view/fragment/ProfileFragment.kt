package ge.bkapa.tkats.messengerapp.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.presenter.ProfilePresenter
import ge.bkapa.tkats.messengerapp.service.AuthorizationService
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.view.AuthorizationActivity


class ProfileFragment(private val activity: Activity, val userId: String) : Fragment() {

    lateinit var authService: AuthorizationService

    lateinit var presenter: ProfilePresenter

    lateinit var user: User

    private lateinit var changeNameET: EditText
    private lateinit var changeWhatIDoET: EditText
    lateinit var changeImageIV: ImageView

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
        changeNameET = activity.findViewById(R.id.change_nickname)
        changeWhatIDoET = activity.findViewById(R.id.change_what_u_do)
        changeImageIV = activity.findViewById(R.id.change_photo)
        initView()
    }

    fun initUser(user: User) {
        changeNameET.setText(user.nickname)
        changeWhatIDoET.setText(user.whatIdo)
        presenter.makeImageDownloadRequest(user.username!!)
        this.user = user
    }

    fun userUpdateMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun initView() {
        activity.findViewById<Button>(R.id.sign_out_button)?.setOnClickListener {
            authService.logout()
            val intent = Intent(activity, AuthorizationActivity::class.java)
            startActivity(intent)
            activity.finish()
        }
        activity.findViewById<Button>(R.id.update_button)?.setOnClickListener {
            val newNickname = changeNameET.text.toString()
            val newWhatIDo = changeWhatIDoET.text.toString()
            presenter.makeUpdateUserRequest(userId, User(user.username, newNickname, newWhatIDo))
        }
        changeImageIV.setOnClickListener { chooseImage() }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_RESULT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri = data.data as Uri
            changeImageIV.setImageURI(imageUri)
            presenter.makeImageUploadRequest(user.username!!, imageUri)
        }
    }

    companion object {
        const val IMAGE_RESULT_CODE = 1
    }

}