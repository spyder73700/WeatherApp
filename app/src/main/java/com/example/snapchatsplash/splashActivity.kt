package com.example.snapchatsplash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.snapchatsplash.R.raw.splashani
import com.example.snapchatsplash.R.raw.sun

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
       Handler(Looper.getMainLooper()).postDelayed({
        //   val intent = Intent(this, MainActivity::class.java)
           val intent = Intent(this, MainActivity::class.java)

           startActivity(intent)
           finish()

       },2000)
        }

}