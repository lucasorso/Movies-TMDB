package br.com.lucasorso.movies.ui.main

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import br.com.lucasorso.movies.R
import br.com.lucasorso.movies.ui.BaseActivity

class MainActivity : BaseActivity(), MainContract {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.fragmentNavHost)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun navigateToDetails(movieId: Int?) {
        val bundle = Bundle()
        movieId?.let { bundle.putInt("id", it) }
        navController.navigate(R.id.action_upcomingFragment_to_detailsFragment, bundle)
    }

}
