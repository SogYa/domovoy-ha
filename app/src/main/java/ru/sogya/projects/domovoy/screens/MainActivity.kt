package ru.sogya.projects.domovoy.screens

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
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
    private var actionBarTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DomovoyHA)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupNavigation()
        setupBackPressed()
        vm = ViewModelProvider(this)[MainVM::class.java]
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                 R.id.lockFragment, R.id.serversFragment -> {
                    supportActionBar?.show()
                    binding.bottomNav.visibility = GONE
                    binding.actionBarTitle.text =
                        resources.getString(R.string.default_action_bar_title)
                }

                R.id.stateAddingFragment -> {
                    supportActionBar?.hide()
                }

                R.id.groupFragment, R.id.settingsFragment -> {
                    supportActionBar?.show()
                    if (actionBarTitle == null) {
                        actionBarTitle = vm.getServerName()
                    }
                    binding.actionBarTitle.text = actionBarTitle
                    binding.bottomNav.visibility = VISIBLE
                }

                else -> {
                    supportActionBar?.show()
                    binding.actionBarTitle.text = destination.label
                }
            }
        }
    }

    private fun setupBackPressed() {
        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupNavigation() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        vm.getNetworkStateLiveData().observe(this) {
            val dialogFragment = NetworkConnectionDialog()
            if (!it) {
                dialogFragment.show(supportFragmentManager, dialogFragment.tag)
            }
        }
        vm
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