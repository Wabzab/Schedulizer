package com.example.schedulizer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.ContentValues.TAG

class MainActivity : AppCompatActivity() {

    // Pre-initialise variables
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var frameLayout: FrameLayout

    // ----- Override Functions ----- //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get page elements
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        frameLayout = findViewById(R.id.flFragment)

        // Fragments accessible from this activity
        val activitiesFragment = ActivitiesFragment()
        val tagsFragment = TagsFragment()
        val settingsFragment = SettingsFragment()
        val loginFragment = LoginFragment()

        // Firestore
        val db = Firebase.firestore

        // Check if User Logged in already
        if(SaveSharedPreferences.getUserName(this).isEmpty()){
            // Send to Login Fragment
            // TODO: Create Login page & Create Account page
            Log.d(TAG, "User not logged in.")
            SaveSharedPreferences.setUserName(this, "Test")
            setFrameFragment(loginFragment)
        }
        else {
            // Send to Activities Fragment
            setFrameFragment(loginFragment)
        }


        // Connect navigation buttons to drawer layout
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Switch navigation button depending on state
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle navigation item selection
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.activitiesItem ->
                    setFrameFragment(activitiesFragment)
                R.id.tagsItem ->
                    setFrameFragment(tagsFragment)
                R.id.settingsItem ->
                    setFrameFragment(settingsFragment)
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // Handle navigation drawer button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // ----- Custom Functions ----- //

    // Replace fragment view
    fun setFrameFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(fragment.toString())
            commit()
        }
    }

}
