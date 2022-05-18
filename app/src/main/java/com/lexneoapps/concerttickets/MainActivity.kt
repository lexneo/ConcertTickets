package com.lexneoapps.concerttickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.lexneoapps.concerttickets.databinding.ActivityMainBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        //launch two coroutines : first for animation, second for 3s delay
        lifecycleScope.launchWhenCreated {
            val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.rotate)
            binding.hourGlassIv.startAnimation(animation)
        }

        lifecycleScope.launchWhenCreated {
            try {
                delay(3000)
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: CancellationException) {
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.coroutineContext.cancelChildren()
    }


}



