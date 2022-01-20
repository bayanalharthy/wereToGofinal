package com.tuwaiq.weretogo.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tuwaiq.weretogo.R
import com.tuwaiq.weretogo.databinding.ActivityMainBinding
import com.tuwaiq.weretogo.utils.hide
import com.tuwaiq.weretogo.utils.show

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (listOf(R.id.homeFragment, R.id.weatherFragment, R.id.profileFragment).contains(
                    destination.id
                )
            ) {
                binding.bottomNavigationView.show()
            } else {
                binding.bottomNavigationView.hide()
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}