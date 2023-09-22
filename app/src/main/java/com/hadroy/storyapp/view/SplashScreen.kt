package com.hadroy.storyapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hadroy.storyapp.R
import com.hadroy.storyapp.viewmodel.MainViewModel
import com.hadroy.storyapp.viewmodel.factory.MainViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    companion object {
        private const val DURATION_SPLASH_SCREEN = 1500L
    }

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel.getUserLogin().observe(this) {
            if (it.token.isNullOrEmpty()) {
                gotoLoginPage()
            } else {
                gotoMainPage()
            }
        }
    }

    private fun gotoLoginPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finish()
        }, DURATION_SPLASH_SCREEN)
    }

    private fun gotoMainPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }, DURATION_SPLASH_SCREEN)
    }
}