package com.hadroy.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hadroy.storyapp.R
import com.hadroy.storyapp.data.ResultState
import com.hadroy.storyapp.databinding.ActivityLoginBinding
import com.hadroy.storyapp.viewmodel.LoginViewModel
import com.hadroy.storyapp.viewmodel.factory.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory.getInstance(application)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            if (validate()) {
                val email = binding.edLoginEmail.text.toString().trim()
                val password = binding.edLoginPassword.text.toString().trim()

                // Login and save token if success
                viewModel.login(email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Error -> {
                                val message = result.error
                                Log.e(TAG, "message: $message")
                                showErrorMessage(message)
                                showLoading(false)
                            }

                            ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                gotoMain()
                            }
                        }
                    }
                }

            } else {
                Toast.makeText(
                    this@LoginActivity,
                    resources.getString(R.string.invalid_input_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun gotoMain() {
        viewModel.getUserLogin().observe(this) {
            if (it.token.isNotEmpty()) {
                Log.d(TAG, "gotoMain: token: ${it.token}")
                showLoading(false)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showErrorMessage(message: String) {
        if (message.equals("Invalid password", true)) {
            Toast.makeText(this, resources.getString(R.string.invalid_password), Toast.LENGTH_SHORT)
                .show()
        } else if (message.equals("User not found", true)) {
            Toast.makeText(this, resources.getString(R.string.user_not_found), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()

        // validate email
        if (email.trim().isEmpty()) {
            binding.emailEditTextLayout.apply {
                isErrorEnabled = true
                error = resources.getString(R.string.email_cannot_empty)
            }
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditTextLayout.apply {
                isErrorEnabled = true
                error = resources.getString(R.string.invalid_email)
            }
            isValid = false
        } else {
            binding.emailEditTextLayout.isErrorEnabled = false
        }

        // validate password
        if (password.trim().isEmpty()) {
            binding.passwordEditTextLayout.apply {
                error = resources.getString(R.string.password_cannot_empty)
                errorIconDrawable = null
            }
            isValid = false
        } else {
            binding.passwordEditTextLayout.isErrorEnabled = false
        }

        return isValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val loginTitle =
            ObjectAnimator.ofFloat(binding.textViewLoginTitle, View.ALPHA, 1f).setDuration(200)
        val loginDescription =
            ObjectAnimator.ofFloat(binding.textViewLoginDescription, View.ALPHA, 1f)
                .setDuration(100)
        val emailLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val divider =
            ObjectAnimator.ofFloat(binding.divider, View.ALPHA, 1f).setDuration(100)
        val signupSuggestion =
            ObjectAnimator.ofFloat(binding.textViewSignupSuggestion, View.ALPHA, 1f)
                .setDuration(100)
        val btnSignup =
            ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                loginTitle,
                loginDescription,
                emailLayout,
                passwordLayout,
                btnLogin,
                divider,
                signupSuggestion,
                btnSignup
            )
            startDelay = 500
        }.start()
    }
}