package com.example.ekiaartseller.mainView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.ekiaartseller.R
import com.example.ekiaartseller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavigationView = binding.navBar
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)



    }



}