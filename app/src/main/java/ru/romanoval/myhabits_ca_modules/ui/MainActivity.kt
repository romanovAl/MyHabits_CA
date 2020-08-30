package ru.romanoval.myhabits_ca_modules.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_main_fragment.*
import kotlinx.android.synthetic.main.header_layout.view.*
import ru.romanoval.myhabits_ca_modules.R

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.mainFragment)

        val appBarConfiguration = AppBarConfiguration(navController.graph, navigationDrawerLayout)

        toolbar.setupWithNavController(navController, appBarConfiguration)

        navigationView.setupWithNavController(navController)

        if (savedInstanceState == null) {
            navController.setGraph(R.navigation.my_navigation_graph)
        }

        var navDrawer = navigationView.getHeaderView(0)
        var imgView: ImageView = navDrawer.fragmentAboutImageView

        Glide.with(this)
            .load("https://doubletapp.ru/wp-content/uploads/2018/12/logo_for_vk.png")
            .override(150, 150)
            .placeholder(ColorDrawable(Color.BLACK))
            .error(R.drawable.ic_launcher_background)
            .transform(CircleCrop())
            .into(imgView)
    }

    override fun onBackPressed() {
        if (navigationDrawerLayout != null && bottomSheetMainFragment != null) {

            val behavior = BottomSheetBehavior.from(bottomSheetMainFragment)

            if (navigationDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                navigationDrawerLayout.closeDrawer(GravityCompat.START)
            } else if (behavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

}