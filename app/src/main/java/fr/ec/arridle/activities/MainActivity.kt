package fr.ec.arridle.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        when (sharedPref.getString("status", null)) {
            "manager" -> {
                navView.menu.findItem(R.id.mainFragment)?.isVisible = false
                navView.menu.findItem(R.id.listKeypointsFragment)?.isVisible = true
                navView.menu.findItem(R.id.mapFragment)?.isVisible = true
                navView.menu.findItem(R.id.leaderboardFragment)?.isVisible = true
                navView.menu.findItem(R.id.manageGameFragment)?.isVisible = true

            }
            "user" -> {
                navView.menu.findItem(R.id.mainFragment)?.isVisible = true
                navView.menu.findItem(R.id.gameFragment)?.isVisible = true
                navView.menu.findItem(R.id.listKeypointsUserFragment)?.isVisible = true
                navView.menu.findItem(R.id.mapUserFragment)?.isVisible = true
                navView.menu.findItem(R.id.leaderboardUserFragment)?.isVisible = true
                navView.menu.findItem(R.id.profileFragment)?.isVisible = true
            }
            else -> {

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

}
