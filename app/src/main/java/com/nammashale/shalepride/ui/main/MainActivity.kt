package com.nammashale.shalepride.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.databinding.ActivityMainBinding
import com.nammashale.shalepride.ui.home.HomeFragment
import com.nammashale.shalepride.ui.meal.MealFragment
import com.nammashale.shalepride.ui.facility.FacilityFragment
import com.nammashale.shalepride.ui.stars.StarsFragment
import com.nammashale.shalepride.ui.settings.SettingsFragment
import com.nammashale.shalepride.ui.admin.AdminDashboardFragment
import com.nammashale.shalepride.util.LocaleHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: android.content.Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isAdmin = ShaleApplication.instance.sessionManager.isAdmin()

        // Update bottom nav for admin users
        if (isAdmin) {
            binding.bottomNavigation.menu.findItem(R.id.nav_settings)?.apply {
                setTitle(R.string.nav_admin)
                setIcon(R.drawable.ic_settings)
            }
        }

        setupBottomNavigation(isAdmin)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    private fun setupBottomNavigation(isAdmin: Boolean) {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_meals -> MealFragment()
                R.id.nav_facilities -> FacilityFragment()
                R.id.nav_stars -> StarsFragment()
                R.id.nav_settings -> if (isAdmin) AdminDashboardFragment() else SettingsFragment()
                else -> HomeFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
