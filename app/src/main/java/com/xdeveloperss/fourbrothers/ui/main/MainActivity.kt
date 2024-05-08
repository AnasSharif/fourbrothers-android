package com.xdeveloperss.fourbrothers.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.kongzue.dialogx.dialogs.MessageDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.databinding.ActivityMainBinding
import com.xdeveloperss.fourbrothers.ui.join.LoginActivity
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.xbase.XBaseActivity
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : XBaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val authViewModel: AuthViewModel by viewModel()
    override fun initViews() {
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_shop, R.id.nav_supplies, R.id.nav_expenses,
                R.id.nav_parties,  R.id.nav_kachra,  R.id.nav_products, R.id.nav_employee
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (!Prefs.isAdmin()) {
            navView.menu.findItem(R.id.nav_home).isVisible = false
            val graph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            graph.setStartDestination(R.id.nav_shop)
            navController.graph = graph
        }

        navView.menu.findItem(R.id.logout).setOnMenuItemClickListener { _ ->

            MessageDialog.show(getString(R.string.warning),
                getString(R.string.are_you_sure_to_delete_this_item),
                getString(
                    R.string.yes_sure
                ))
                .setOkButton { _, _ ->
                    authViewModel.logout()
                    Prefs.removeToken()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    false
                }
            true
        }

        //Header setup
        val user = Prefs.getUserData()
        val header = navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.header_title).text = user?.username

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}