package com.cybereast.employeeperformancetracker.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.base.BaseActivity
import com.cybereast.employeeperformancetracker.base.BaseInterface
import com.cybereast.employeeperformancetracker.constants.Constants.COLLECTION_USERS
import com.cybereast.employeeperformancetracker.databinding.ActivitySignUpBinding
import com.cybereast.employeeperformancetracker.listeners.ValidationListener
import com.cybereast.employeeperformancetracker.models.User
import com.cybereast.employeeperformancetracker.utils.ActivityUtils
import com.cybereast.employeeperformancetracker.utils.AppUtils.showToast
import com.cybereast.modernqueue.utils.InputValidation.isValidEmail
import com.cybereast.modernqueue.utils.InputValidation.isValidPassword
import com.cybereast.modernqueue.utils.InputValidation.isValidPhone
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SignUpActivity : BaseActivity(), ValidationListener, BaseInterface {
    private lateinit var mBinding: ActivitySignUpBinding
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private lateinit var doctorDocumentRef: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        if (isValidEmail(applicationContext, mBinding.emailEt)
            && isValidPhone(
                applicationContext,
                mBinding.mobileNoEt
            )
            && isValidPassword(applicationContext, mBinding.passwordEt)
        ) {

            signUp()
        }
    }

    private fun signUp() {
        onProgress()
        val user = User(
            null,
            mBinding.firstNameEt.text.toString().capitalize(Locale.ENGLISH),
            mBinding.lastNameEt.text.toString().capitalize(Locale.ENGLISH),
            mBinding.emailEt.text.toString(),
            mBinding.mobileNoEt.text.toString(),
            mBinding.passwordEt.text.toString()
        )
        mAuth.createUserWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uId = it.result?.user?.uid
                    if (uId != null) {
                        user.userId = uId
                        doctorDocumentRef =
                            fireStoreDbRef.collection(COLLECTION_USERS).document(uId)
                        doctorDocumentRef.set(user).addOnSuccessListener {
                            onResponse()
                            ActivityUtils.startActivity(this, DashboardActivity::class.java)
                            this.finish()
                            showToast(
                                applicationContext,
                                getString(R.string.sign_up_success_message)
                            )
                        }.addOnFailureListener {
                            onResponse()
                            showToast(applicationContext, it.message.toString())
                            Log.d("TAG", "signUp: ${it.message}")
                        }
                    }
                } else {
                    onResponse()
                    showToast(applicationContext, it.exception?.message.toString())
                    Log.d("TAG", it.exception?.message.toString())
                }
            }

    }


    private fun setListeners() {
        mBinding.signUpButton.setOnClickListener {
            validateFields(
                mBinding.firstNameEt,
                mBinding.lastNameEt,
                mBinding.emailEt,
                mBinding.mobileNoEt,
                mBinding.passwordEt,
            )
        }
        mBinding.loginTv.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateFields(vararg editText: AppCompatEditText) {
        var isValid = true
        for (et in editText) {
            if (TextUtils.isEmpty(et.text)) {
                isValid = false
                onError(et)
            }
        }
        if (isValid) {
            onSuccess()
        }
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.signUpButton.isEnabled = false
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
        mBinding.signUpButton.isEnabled = true

    }


}