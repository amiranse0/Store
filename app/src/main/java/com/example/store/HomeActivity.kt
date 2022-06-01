package com.example.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.store.data.connection.NetworkConnection
import com.example.store.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(
            this
        ) {
            if (!it) {
                binding.connectionIv.visibility = View.VISIBLE
                binding.containerView.visibility = View.INVISIBLE
            }
            else if(it) {
                binding.connectionIv.visibility = View.INVISIBLE
                binding.containerView.visibility = View.VISIBLE
            }
        }
    }
}