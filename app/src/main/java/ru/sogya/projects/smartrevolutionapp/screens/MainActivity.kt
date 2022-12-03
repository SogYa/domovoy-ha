package ru.sogya.projects.smartrevolutionapp.screens

import android.os.Bundle
import android.view.MenuItem
import android.view.View.*
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.sogya.data.utils.Constants
import ru.sogya.projects.smartrevolutionapp.R
import ru.sogya.projects.smartrevolutionapp.databinding.ActivityMainBinding
import ru.sogya.projects.smartrevolutionapp.dialogs.LogOutDialogFragment
import ru.sogya.projects.smartrevolutionapp.needtoremove.SPControl

class MainActivity : AppCompatActivity(), LogOutDialogFragment.DialogFragmentListener {
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var baseUrl: TextView
    private val vm: MainVM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SmartRevolutionApp)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupNavigation()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment -> {
                    supportActionBar?.hide()
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> supportActionBar?.show()
            }
        }
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_logout -> {
                    binding.drawerLayout.close()
                    LogOutDialogFragment().show(supportFragmentManager, "LogOutDialog")

                }
            }
            true
        }
    }


    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfig = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.settingsFragment),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfig)
        binding.navView.setupWithNavController(navController)
        //Передача URL в текст заголовка меню
        baseUrl = binding.navView.getHeaderView(0).findViewById(R.id.baseUrl)
        baseUrl.text =
            SPControl.getInstance().getStringPrefs(Constants.URI)
        baseUrl.isSelected = true
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
        navController.navigate(R.id.action_homeFragment_to_authFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}