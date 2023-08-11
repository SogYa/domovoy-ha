package ru.sogya.projects.domovoy.screens

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View.*
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import dagger.hilt.android.AndroidEntryPoint
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.databinding.ActivityMainBinding
import ru.sogya.projects.domovoy.dialogs.LogOutDialogFragment
import ru.sogya.projects.domovoy.dialogs.networkconnection.NetworkConnectionDialog

@BuildCompat.PrereleaseSdkCheck
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LogOutDialogFragment.DialogFragmentListener {
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var vm: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DomovoyHA)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        setupNavigation()
        //Android 13 predictive back gesture
        setupTiramisuBackPressed()

        vm = ViewModelProvider(this)[MainVM::class.java]
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment, R.id.lockFragment,R.id.serversFragment -> {
                    supportActionBar?.hide()
                    binding.bottomNav.visibility = GONE
                }
                R.id.stateAddingFragment->
                    supportActionBar?.hide()

                else -> {
                    supportActionBar?.show()
                    binding.bottomNav.visibility = VISIBLE
                }
            }
        }
    }

    private fun setupTiramisuBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                for (i in 0 until supportFragmentManager.backStackEntryCount) {
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        vm.getNetworkStateLiveData().observe(this) {
            val dialogFragment = NetworkConnectionDialog()
            if (!it) {
                dialogFragment.show(supportFragmentManager, dialogFragment.tag)
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun positiveButtonClicked() {
        vm.logOut()
        navController.navigate(R.id.action_settingsFragment_to_serversFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}