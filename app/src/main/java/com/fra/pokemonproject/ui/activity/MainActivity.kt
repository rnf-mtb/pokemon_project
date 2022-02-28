package com.fra.pokemonproject.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.fra.pokemonproject.R
import com.fra.pokemonproject.databinding.ActivityMainBinding
import com.fra.pokemonproject.utils.SessionDataManager

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionDataManager.init(applicationContext)

        val pokemonListViewActivityIntent = Intent(applicationContext, PokemonListViewActivity::class.java)
        startActivity(pokemonListViewActivityIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}