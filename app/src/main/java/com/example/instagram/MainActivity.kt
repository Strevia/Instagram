package com.example.instagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.instagram.fragments.ComposeFragment
import com.example.instagram.fragments.FeedFragment
import com.example.instagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            var fragToShow: Fragment? = null
            when (item.itemId) {
                R.id.action_home -> {
                    fragToShow = FeedFragment()
                }
                R.id.action_compose -> {
                    fragToShow = ComposeFragment()
                }
                R.id.action_profile -> {
                    fragToShow = ProfileFragment()
                }
            }
            if (fragToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragToShow).commit()
            }
            true
        }
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home




    }

    companion object {
        const val TAG = "MainActivity"
    }

}