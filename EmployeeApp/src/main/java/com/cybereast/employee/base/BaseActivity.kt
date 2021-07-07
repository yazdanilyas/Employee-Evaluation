package com.cybereast.employee.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.cybereast.employee.models.User
import com.cybereast.employee.utils.CommonKeys
import com.cybereast.employee.utils.PrefUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseActivity : AppCompatActivity() {
    private val TAG: String = BaseActivity::class.java.name
    val mAuth = FirebaseAuth.getInstance()
    val mFireStoreDb = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    protected open fun replaceFragment(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(containerId, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

    protected fun setUpActionBar(toolBar: Toolbar, title: String, showBack: Boolean) {
        try {
            setSupportActionBar(toolBar)
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(showBack)
                actionBar.title = title
                actionBar.setDisplayShowTitleEnabled(true)
                actionBar.setDisplayShowTitleEnabled(true)
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, "setUpActionBar: Failed to setup Action bar", e)
        }
    }

    fun createSession(pContext: Context, user: User) {
        PrefUtils.setString(pContext, CommonKeys.KEY_USER_ID, user.userId)
        PrefUtils.setString(pContext, CommonKeys.KEY_USER_EMAIL, user.email)
        PrefUtils.setString(pContext, CommonKeys.KEY_USER_PASSWORD, user.password)
        PrefUtils.setBoolean(pContext, CommonKeys.KEY_IS_LOGGED_IN, true)

    }

}