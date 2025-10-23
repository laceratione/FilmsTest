package ru.example.kolsatest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.example.kolsatest.databinding.ActivityMainBinding
import ru.example.kolsatest.presentation.filmdetail.FilmDetailsFragmentArgs

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupNavigation()
        setupDestinationChangedListener()
        setContentView(binding!!.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.filmListFragment), null)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.filmListFragment -> {
                    binding?.toolbarTitle?.setText(getString(R.string.title_fragment_film_list))
                }
                R.id.filmDetailsFragment -> {
                    val film = FilmDetailsFragmentArgs.fromBundle(arguments ?: Bundle()).film
                    binding?.toolbarTitle?.setText(film.name)
                    binding?.toolbar?.setNavigationIcon(R.drawable.ic_arrow_left)
                }
            }
        }
    }
}