package com.hadroy.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hadroy.storyapp.R
import com.hadroy.storyapp.data.ResultState
import com.hadroy.storyapp.databinding.ActivityRegisterBinding
import com.hadroy.storyapp.viewmodel.RegisterViewModel
import com.hadroy.storyapp.viewmodel.factory.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var alertDialog: AlertDialog.Builder

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alertDialog = AlertDialog.Builder(this)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            if (validateInput()) {
                val name = binding.edRegisterName.text.toString().trim()
                val email = binding.edRegisterEmail.text.toString().trim()
                val password = binding.edRegisterPassword.text.toString().trim()

                viewModel.register(name, email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                val message = result.data.message.toString()
                                showLoading(false)
                                Log.d(TAG, "message: $message")
                                showSuccessMessage()
                            }

                            is ResultState.Error -> {
                                val message = result.error
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                Log.e(TAG, "message: $message")
                                showLoading(false)
                            }

                            else -> {}
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    resources.getString(R.string.invalid_input_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun showSuccessMessage() {
        val view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_succes, null)
        alertDialog.setView(
            view
        )
        val btnLogin = view.findViewById<Button>(R.id.btn_login_from_success)
        val dialog = alertDialog.create()
        btnLogin.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun validateInput(): Boolean {
        var isValid = true

        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString().trim()
        val confirmPassword = binding.edRegisterConfirmPassword.text.toString().trim()

        // validate name
        if (name.trim().isEmpty()) {
            binding.nameEditTextLayout.apply {
                isErrorEnabled = true
                error = resources.getString(R.string.name_cannot_empty)

            }
            isValid = false
        } else {
            binding.nameEditTextLayout.isErrorEnabled = false
        }

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

        // validate confirm password
        if (confirmPassword.trim().isEmpty()) {
            binding.confirmPasswordEditTextLayout.apply {
                error = resources.getString(R.string.confirm_password_cannot_empty)
                errorIconDrawable = null
            }
            isValid = false
        } else if (!confirmPassword.equals(password, false)) {
            binding.confirmPasswordEditTextLayout.apply {
                error = resources.getString(R.string.confirm_password_not_equals_password)
                errorIconDrawable = null
            }
            isValid = false
        } else {
            binding.confirmPasswordEditTextLayout.isErrorEnabled = false
        }

        return isValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(100)
        val nameTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val confirmPasswordLayout =
            ObjectAnimator.ofFloat(binding.confirmPasswordEditTextLayout, View.ALPHA, 1f)
                .setDuration(100)
        val btnSignup =
            ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(100)
        val divider =
            ObjectAnimator.ofFloat(binding.divider, View.ALPHA, 1f).setDuration(100)
        val loginSuggestion =
            ObjectAnimator.ofFloat(binding.textViewLoginSuggestion, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextLayout,
                emailTextLayout,
                passwordTextLayout,
                confirmPasswordLayout,
                btnSignup,
                divider,
                loginSuggestion,
                btnLogin
            )
            startDelay = 500
        }.start()
    }
}