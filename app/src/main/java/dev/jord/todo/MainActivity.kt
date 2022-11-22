package dev.jord.todo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.jord.todo.databinding.ActivityMainBinding
import dev.jord.todo.ui.account.AccountFragment
import dev.jord.todo.ui.auth.AuthViewModel
import dev.jord.todo.ui.auth.LoginFragment
import dev.jord.todo.ui.auth.WelcomeFragment
import dev.jord.todo.ui.home.HomeFragment
import dev.jord.todo.util.UiState
import dev.jord.todo.util.hide
import dev.jord.todo.util.show
import dev.jord.todo.util.snackbar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        if (user != null) {
            loadFragment(HomeFragment())
            binding.bottomNavigation.show()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            binding.bottomNavigation.hide()
        }
        setupOnClickListener()
    }

    private fun setupOnClickListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val fragment = when(it.itemId){
                R.id.home -> { HomeFragment() }
                R.id.account -> { AccountFragment() }
                else -> { HomeFragment() }
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}