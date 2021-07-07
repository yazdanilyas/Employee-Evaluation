package com.cybereast.employeeperformancetracker.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.constants.Constants.SPLASH_TIME_OUT
import com.cybereast.employeeperformancetracker.utils.ActivityUtils
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openChooserActivity()
    }

    private fun openChooserActivity() {
        val mRunnable = Runnable {
            if (!isFinishing) {
                checkUserSession()
                finish()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(mRunnable, SPLASH_TIME_OUT)
    }

    private fun checkUserSession() {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val bundle = Bundle()
            ActivityUtils.startActivity(this, DashboardActivity::class.java)
        } else {
            ActivityUtils.startActivity(this, LoginActivity::class.java)
        }
    }

}
