package com.mteam.example.fragment.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.mteam.example.R
import com.mteam.example.WatchlistApp
import javax.inject.Inject

/**
 * https://codelabs.developers.google.com/codelabs/android-dagger
 */
class LoginActivity : AppCompatActivity() {

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var loginViewModel: LoginViewModel

    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as WatchlistApp).appComponent.loginComponent().create().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel.loginState.observe(this, Observer<LoginViewState> { state ->
            when (state) {
                is LoginSuccess -> {

                }
                is LoginError -> errorTextView.visibility = View.VISIBLE
            }
        })

        errorTextView = findViewById(R.id.error)
        setupViews()
    }

    private fun setupViews() {
        val usernameEditText = findViewById<EditText>(R.id.username)
        usernameEditText.isEnabled = false
        usernameEditText.setText(loginViewModel.getUsername())

        val passwordEditText = findViewById<EditText>(R.id.password)
        passwordEditText.doOnTextChanged { _, _, _, _ -> errorTextView.visibility = View.INVISIBLE }

        findViewById<Button>(R.id.login).setOnClickListener {
            loginViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
        }
        findViewById<Button>(R.id.unregister).setOnClickListener {
            loginViewModel.unregister()
        }
    }
}

sealed class LoginViewState
object LoginSuccess : LoginViewState()
object LoginError : LoginViewState()
