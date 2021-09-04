package com.tromian.test.weather.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tromian.test.wether.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        loadSplash()

    }

    fun loadSplash() {
        binding.progressBar.display
        lifecycleScope.launchWhenStarted {
            delay(500)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }

}