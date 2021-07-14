package ge.bkapa.tkats.messengerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var nickname: EditText;
    private lateinit var password: EditText;
    private lateinit var profession: EditText;
    private lateinit var signIn: Button;
    private lateinit var signUpPage: Button;
    private lateinit var signUp: Button;
    private var registrationPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
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
        nickname = findViewById(R.id.nickname_edit_text)
        password = findViewById(R.id.password_edit_text)
        profession = findViewById(R.id.profession_edit_text)
        signIn = findViewById(R.id.sign_in_button)
        signUpPage = findViewById(R.id.sign_up_page_button)
        signUpPage.setOnClickListener {
            togglePage()
        }
        signUp = findViewById(R.id.sign_up_button)
    }

    private fun togglePage() {
        registrationPage = !registrationPage
        if (registrationPage) {
            profession.visibility = View.VISIBLE
            signIn.visibility = View.GONE
            signUpPage.visibility = View.GONE
            findViewById<TextView>(R.id.registration_hint).visibility = View.GONE
            signUp.visibility = View.VISIBLE
        } else {
            profession.visibility = View.GONE
            signIn.visibility = View.VISIBLE
            signUpPage.visibility = View.VISIBLE
            findViewById<TextView>(R.id.registration_hint).visibility = View.VISIBLE
            signUp.visibility = View.GONE
        }
    }

}
