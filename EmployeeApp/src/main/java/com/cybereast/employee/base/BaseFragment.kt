package com.cybereast.employee.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cybereast.employee.models.User
import com.cybereast.employee.utils.CommonKeys
import com.cybereast.employee.utils.PrefUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseFragment : Fragment() {
    var fireStoreDbRef = FirebaseFirestore.getInstance()
    lateinit var doctorDocumentRef: DocumentReference
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun createSession(pContext: Context, user: User) {
        PrefUtils.setString(pContext, CommonKeys.KEY_USER_EMAIL, user.email)
        PrefUtils.setString(pContext, CommonKeys.KEY_USER_PASSWORD, user.password)
        PrefUtils.setBoolean(pContext, CommonKeys.KEY_IS_LOGGED_IN, true)

    }
}