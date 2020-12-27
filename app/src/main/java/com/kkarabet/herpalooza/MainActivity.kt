package com.kkarabet.herpalooza

import android.Manifest
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.kkarabet.herpalooza.ui.main.MainFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.let {
                it.title = when (destination.id) {
                    R.id.settingsFragment -> getString(R.string.Settings)
                   // R.id.infoFragment -> getString(R.string.Info)
                    else -> getString(R.string.app_name)
                }
            }
        }
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settingsMenu -> {
                navHostFragment.navController.navigate(R.id.action_global_settingsFragment)
                true
            }R.id.infoMenu -> {
                navHostFragment.navController.navigate(R.id.action_global_infoFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.navHostFragment).navigateUp()

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.in_left, R.anim.out_right)

    }

    companion object {
        const val SHOW_FOUND = "show_found"
        const val SHOW_TYPE = "show_type"
        const val SORT_BY = "sort_by"
    }
}
