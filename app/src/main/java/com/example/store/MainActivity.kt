package com.example.store

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationSet
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.store.data.connection.NetworkConnection
import com.example.store.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkConnection: NetworkConnection
    private var firstTime = false

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Store)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        bottomNavigationHandler()

        checkForConnection()

        clickRetry()

        bottomNavigation()


    }

    private fun bottomNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHost.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destenation, _ ->
            if (destenation.id == R.id.accountFragment
                || destenation.id == R.id.homeFragment
                || destenation.id == R.id.cartFragment
                || destenation.id == R.id.categoryFragment
                || destenation.id == R.id.loginFragment
                || destenation.id == R.id.signInFragment
            ) {

                binding.bottomNavigationView.visibility = View.VISIBLE
            } else {
                binding.bottomNavigationView.visibility = View.GONE
            }

        }
    }

    private fun checkForConnection() {
        val toastNoConnection =
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT)
        toastNoConnection.setGravity(Gravity.TOP, 0, 200)
        networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(
            this
        ) {
            if (!it) {
                binding.connectionLayout.visibility = View.VISIBLE
                binding.containerView.visibility = View.INVISIBLE
                toastNoConnection.show()
                firstTime = true

            }
        }
    }

    private fun bottomNavigationHandler() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun clickRetry() {
        val toastNoConnection =
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT)
        toastNoConnection.setGravity(Gravity.TOP, 0, 200)
        val toastConnected =
            Toast.makeText(this, getString(R.string.connected), Toast.LENGTH_SHORT)
        toastConnected.setGravity(Gravity.TOP, 0, 200)

        binding.refreshBtn.setOnClickListener {
            binding.refreshBtn.visibility = View.INVISIBLE
            networkConnection.observe(this) {
                if (!it) {
                    binding.connectionLayout.visibility = View.VISIBLE
                    binding.containerView.visibility = View.INVISIBLE
                    binding.refreshBtn.visibility = View.VISIBLE
                    toastNoConnection.show()
                    firstTime = true

                } else if (it) {
                    binding.connectionLayout.visibility = View.INVISIBLE
                    binding.containerView.visibility = View.VISIBLE
                    if (firstTime) {
                        toastConnected.show()
                    }
                }
            }
        }

    }
}