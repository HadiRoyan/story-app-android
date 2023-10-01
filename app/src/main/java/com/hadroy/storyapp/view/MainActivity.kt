package com.hadroy.storyapp.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadroy.storyapp.R
import com.hadroy.storyapp.data.model.LoginResult
import com.hadroy.storyapp.databinding.ActivityMainBinding
import com.hadroy.storyapp.view.adapter.LoadingStateAdapter
import com.hadroy.storyapp.view.adapter.StoryAdapter
import com.hadroy.storyapp.viewmodel.MainViewModel
import com.hadroy.storyapp.viewmodel.factory.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var data: LoginResult

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(application)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserLogin()

        setupAction()
        setTopBar()
        setRecyclerViewAdapter()
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreateStoryActivity::class.java))
        }
    }

    private fun setRecyclerViewAdapter() {
        storyAdapter = StoryAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
    }

    private fun observeData() {
        Log.d(TAG, "observe data with token: ${data.token}")
        viewModel.getAllStory(data.token).observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    private fun getUserLogin() {
        viewModel.getUserLogin().observe(this@MainActivity) {
            data = it
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setTopBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    showLogoutDialog()
                    true
                }

                R.id.action_change_language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                R.id.action_map -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun showLogoutDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(resources.getString(R.string.logout).uppercase())
            .setMessage(resources.getString(R.string.verify_logout_message))
            .setPositiveButton(resources.getString(R.string.yes).uppercase()) { _, _ ->
                viewModel.deleteUserLogin()
                viewModel.getUserLogin().observe(this) {
                    if (it.token.isEmpty()) {
                        Log.d(TAG, "logout: is token empty: ${it.token.isEmpty()}")
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
            .setNegativeButton(resources.getString(R.string.no).uppercase()) { dialog, _ ->
                dialog.dismiss()
            }
        alertDialog.create().show()
    }
}