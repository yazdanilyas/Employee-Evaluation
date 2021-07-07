package com.cybereast.employee.application

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        FirebaseApp.initializeApp(this)
    }

    companion object {
        private lateinit var context: Context
        fun getAppContext(): Context {
            return context
        }
    }
}