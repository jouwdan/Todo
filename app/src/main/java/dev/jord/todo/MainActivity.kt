package dev.jord.todo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.jord.todo.databinding.ActivityMainBinding
import dev.jord.todo.ui.account.AccountFragment
import dev.jord.todo.ui.home.HomeFragment

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
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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