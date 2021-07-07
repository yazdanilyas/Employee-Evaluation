package com.cybereast.employeeperformancetracker.ui.fragments.addEmployeeFragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.base.BaseFragment
import com.cybereast.employeeperformancetracker.base.BaseInterface
import com.cybereast.employeeperformancetracker.constants.Constants
import com.cybereast.employeeperformancetracker.databinding.AddEmployeeFragmentBinding
import com.cybereast.employeeperformancetracker.listeners.ValidationListener
import com.cybereast.employeeperformancetracker.models.Employee
import com.cybereast.employeeperformancetracker.utils.AppUtils
import com.cybereast.modernqueue.utils.InputValidation
import java.util.*

class AddEmployeeFragment : BaseFragment(), ValidationListener, BaseInterface {

    companion object {
        fun newInstance() = AddEmployeeFragment()
    }

    private lateinit var viewModel: AddEmployeeViewModel
    private lateinit var mBinding: AddEmployeeFragmentBinding
//    private var fireStoreDbRef = FirebaseFirestore.getInstance()
//    private lateinit var doctorDocumentRef: DocumentReference
//    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddEmployeeFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddEmployeeViewModel::class.java)
        setListeners()

    }

    private fun setListeners() {
        mBinding.addEmpButton.setOnClickListener {
            validateFields(
                mBinding.fullNameEt,
                mBinding.emailEt,
                mBinding.mobileNoEt,
                mBinding.passwordEt,
            )
        }
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        if (InputValidation.isValidEmail(requireContext(), mBinding.emailEt)
            && InputValidation.isValidPhone(
                requireContext(),
                mBinding.mobileNoEt
            )
            && InputValidation.isValidPassword(requireContext(), mBinding.passwordEt)
        ) {
            addEmployee()
        }
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.addEmpButton.isEnabled = false
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
        mBinding.addEmpButton.isEnabled = true
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

    private fun addEmployee() {
        onProgress()
        val employee = Employee(
            null,
            mBinding.fullNameEt.text.toString().capitalize(Locale.ENGLISH),
            mBinding.emailEt.text.toString(),
            mBinding.mobileNoEt.text.toString(),
            mBinding.passwordEt.text.toString(),
            mAuth.currentUser?.uid, 0.0
        )
        val empId =
            fireStoreDbRef.collection(Constants.COLLECTION_EMPLOYEE).document().id
        doctorDocumentRef =
            fireStoreDbRef.collection(Constants.COLLECTION_EMPLOYEE).document(empId)
        employee.employeeId = empId
        doctorDocumentRef.set(employee).addOnSuccessListener {
            onResponse()
            activity?.finish()
            AppUtils.showToast(
                requireContext(),
                getString(R.string.add_success)
            )
        }.addOnFailureListener {
            onResponse()
            AppUtils.showToast(requireContext(), it.message.toString())
            Log.d("TAG", "signUp: ${it.message}")
        }

    }

}