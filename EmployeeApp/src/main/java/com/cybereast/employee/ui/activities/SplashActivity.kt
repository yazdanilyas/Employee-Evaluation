package com.cybereast.employee.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.cybereast.employee.R
import com.cybereast.employee.base.BaseActivity
import com.cybereast.employee.constants.Constants.SPLASH_TIME_OUT
import com.cybereast.employee.ui.activities.dashBoardActivity.DashBoardActivity
import com.cybereast.employee.utils.ActivityUtils
import com.cybereast.employee.utils.CommonKeys
import com.cybereast.employee.utils.PrefUtils

class SplashActivity : BaseActivity() {

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
        val isLoggedIn = PrefUtils.getBoolean(this, CommonKeys.KEY_IS_LOGGED_IN)
        if (isLoggedIn) {
            ActivityUtils.startActivity(this, DashBoardActivity::class.java)
        } else {
            ActivityUtils.startActivity(this, SigninActivity::class.java)
        }
    }
}