package com.cybereast.employee.ui.activities


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.employee.R
import com.cybereast.employee.base.BaseActivity
import com.cybereast.employee.base.BaseInterface
import com.cybereast.employee.constants.Constants
import com.cybereast.employee.databinding.ActivitySigninBinding
import com.cybereast.employee.listeners.ValidationListener
import com.cybereast.employee.models.Employee
import com.cybereast.employee.models.User
import com.cybereast.employee.ui.activities.dashBoardActivity.DashBoardActivity
import com.cybereast.employee.utils.ActivityUtils
import com.cybereast.employee.utils.AppUtils
import com.cybereast.employee.utils.InputValidation
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : BaseActivity(), ValidationListener, BaseInterface {
    private lateinit var mBinding: ActivitySigninBinding
    private lateinit var mFirebaseAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        if (InputValidation.isValidEmail(this, mBinding.userEmailEt)) {
            login(mBinding.userEmailEt.text.toString(), mBinding.userPasswordEt.text.toString())
        }
    }

    override fun onProgress() {

    }

    override fun onResponse() {

    }

    private fun login(email: String, password: String) {

        mFireStoreDb.collection(Constants.COLLECTION_EMPLOYEE).whereEqualTo("email", email)
            .whereEqualTo("password", password).get().addOnSuccessListener { snapShot ->
                if (snapShot != null) {
                    val employee = snapShot.toObjects(Employee::class.java)
                    if (employee.size > 0) {
                        createSession(
                            this,
                            User(
                                employee[0].employeeId,
                                null,
                                null,
                                mBinding.userEmailEt.text.toString(),
                                null,
                                mBinding.userPasswordEt.toString()
                            )
                        )
                        Log.d("TAG", "login: " + employee[0].employeeId)
                        ActivityUtils.startActivity(this, DashBoardActivity::class.java)
                        finish()

                    } else {
                        AppUtils.showToast(this, getString(R.string.invalid_user_or_password))
                    }
                }
            }

    }


    private fun setListeners() {
        mBinding.loginBtn.setOnClickListener {
            validateFields(mBinding.userEmailEt, mBinding.userPasswordEt)
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


}