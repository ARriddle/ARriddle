package fr.ec.arridle.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import fr.ec.arridle.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val navController = this.findNavController(R.id.myNavHostFragment)
        val graph = navController.navInflater.inflate(R.navigation.navigation)

        // On vérifie si une connexion est déjà en cours
        val sharedPref = this.getSharedPreferences("connection", Context.MODE_PRIVATE)
        when (sharedPref.getString("status", null)) {
            "manager" -> {
                graph.startDestination = R.id.manageGameFragment
            }
            "user" -> {
                graph.startDestination = R.id.gameFragment
            }
            else -> {
                graph.startDestination = R.id.mainFragment
            }
        }
        navController.graph = graph
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        navController.addOnDestinationChangedListener { _, _, arguments ->
            // Turn around to get the burger icon displaying ... (very ugly)
            if (arguments?.getString("arg1") == "refresh") {
                recreate()
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item!!,
            this.findNavController(R.id.myNavHostFragment)
        )
                || super.onOptionsItemSelected(item)
    }


    // Deal with keyboard
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun createNavDrawer() {
        val sharedPref = this.getSharedPreferences("connection", Context.MODE_PRIVATE)
        when (sharedPref.getString("status", null)) {
            "manager" -> {
                navView.menu.findItem(R.id.mainFragment)?.isVisible = false
                navView.menu.findItem(R.id.gameFragment)?.isVisible = false
                navView.menu.findItem(R.id.listKeypointsFragment)?.isVisible = true
                navView.menu.findItem(R.id.listKeypointsUserFragment)?.isVisible = false
                navView.menu.findItem(R.id.mapFragment)?.isVisible = true
                navView.menu.findItem(R.id.mapUserFragment)?.isVisible = false
                navView.menu.findItem(R.id.leaderboardFragment)?.isVisible = true
                navView.menu.findItem(R.id.leaderboardUserFragment)?.isVisible = false
                navView.menu.findItem(R.id.manageGameFragment)?.isVisible = true
                navView.menu.findItem(R.id.profileFragment)?.isVisible = false
                navView.menu.findItem(R.id.settingsFragment)?.isVisible = true
                navView.menu.findItem(R.id.aproposFragment)?.isVisible = true
            }
            "user" -> {
                navView.menu.findItem(R.id.mainFragment)?.isVisible = false
                navView.menu.findItem(R.id.gameFragment)?.isVisible = true
                navView.menu.findItem(R.id.listKeypointsFragment)?.isVisible = false
                navView.menu.findItem(R.id.listKeypointsUserFragment)?.isVisible = true
                navView.menu.findItem(R.id.mapFragment)?.isVisible = false
                navView.menu.findItem(R.id.mapUserFragment)?.isVisible = true
                navView.menu.findItem(R.id.leaderboardFragment)?.isVisible = false
                navView.menu.findItem(R.id.leaderboardUserFragment)?.isVisible = true
                navView.menu.findItem(R.id.manageGameFragment)?.isVisible = false
                navView.menu.findItem(R.id.profileFragment)?.isVisible = true
                navView.menu.findItem(R.id.settingsFragment)?.isVisible = true
                navView.menu.findItem(R.id.aproposFragment)?.isVisible = true
            }
            else -> {
                navView.menu.findItem(R.id.mainFragment)?.isVisible = true
                navView.menu.findItem(R.id.gameFragment)?.isVisible = false
                navView.menu.findItem(R.id.listKeypointsFragment)?.isVisible = false
                navView.menu.findItem(R.id.listKeypointsUserFragment)?.isVisible = false
                navView.menu.findItem(R.id.mapFragment)?.isVisible = false
                navView.menu.findItem(R.id.mapUserFragment)?.isVisible = false
                navView.menu.findItem(R.id.leaderboardFragment)?.isVisible = false
                navView.menu.findItem(R.id.leaderboardUserFragment)?.isVisible = false
                navView.menu.findItem(R.id.manageGameFragment)?.isVisible = false
                navView.menu.findItem(R.id.profileFragment)?.isVisible = false
                navView.menu.findItem(R.id.settingsFragment)?.isVisible = true
                navView.menu.findItem(R.id.aproposFragment)?.isVisible = true
            }
        }
    }

}
