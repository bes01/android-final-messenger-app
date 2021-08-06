package ge.bkapa.tkats.messengerapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.service.AuthorizationService

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var nicknameEditText: EditText;
    private lateinit var passwordEditText: EditText;
    private lateinit var professionEditText: EditText;
    private lateinit var signInButton: Button;
    private lateinit var signUpPageButton: Button;
    private lateinit var signUpButton: Button;
    private var registrationPage = false
    private val authorizationService = AuthorizationService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        if (authorizationService.isAuthorized()) {
            Log.i("my_mes","Authorized")
            openHomePage(authorizationService.getCurrentUserId())
        }
        initComponents()
    }

    override fun onBackPressed() {
        if (registrationPage) {
            togglePage()
        } else {
            super.onBackPressed()
        }
    }

    private fun initComponents() {
        nicknameEditText = findViewById(R.id.nickname_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        professionEditText = findViewById(R.id.profession_edit_text)
        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            doLogin()
        }
        signUpPageButton = findViewById(R.id.sign_up_page_button)
        signUpPageButton.setOnClickListener {
            togglePage()
        }
        signUpButton = findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            doRegister()
        }
    }

    private fun doLogin() {
        val nickname = nicknameEditText.text.toString()
        val password = passwordEditText.text.toString()
        authorizationService.doLogin(nickname, password, this::openHomePage)
    }

    private fun doRegister() {
        val nickname = nicknameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val whatIDo = professionEditText.text.toString()
        authorizationService.doRegister(nickname, password, whatIDo, this::openHomePage)
    }

    private fun togglePage() {
        registrationPage = !registrationPage
        if (registrationPage) {
            professionEditText.visibility = View.VISIBLE
            signInButton.visibility = View.GONE
            signUpPageButton.visibility = View.GONE
            findViewById<TextView>(R.id.registration_hint).visibility = View.GONE
            signUpButton.visibility = View.VISIBLE
        } else {
            professionEditText.visibility = View.GONE
            signInButton.visibility = View.VISIBLE
            signUpPageButton.visibility = View.VISIBLE
            findViewById<TextView>(R.id.registration_hint).visibility = View.VISIBLE
            signUpButton.visibility = View.GONE
        }
    }

    // TODO: გადასვლის მერე ექთივითების სტეკიდან ამოსაგდებია ეს ექთივითი
    private fun openHomePage(uid: String) {
        Log.i("my_mes","Open home page")
        finish()

        val intent = Intent(this, MessageListActivity::class.java)
        intent.putExtra("uid",uid)

        startActivity(intent)
    }

}
