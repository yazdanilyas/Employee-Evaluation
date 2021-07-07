package com.cybereast.employeeperformancetracker.ui.fragments.createTaskFragment

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
import com.cybereast.employeeperformancetracker.databinding.CreateTaskFragmentBinding
import com.cybereast.employeeperformancetracker.enums.TaskStatus
import com.cybereast.employeeperformancetracker.listeners.ValidationListener
import com.cybereast.employeeperformancetracker.models.Task
import com.cybereast.employeeperformancetracker.utils.AppUtils
import java.util.*

class CreateTaskFragment : BaseFragment(), ValidationListener, BaseInterface {

    companion object {
        fun newInstance() = CreateTaskFragment()
    }

    private lateinit var viewModel: CreateTaskViewModel
    private lateinit var mBinding: CreateTaskFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = CreateTaskFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateTaskViewModel::class.java)
        setListeners()
    }

    private fun setListeners() {
        mBinding.createTaskButton.setOnClickListener {
            validateFields(mBinding.taskTitleEt, mBinding.projectNameEt)
        }
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        createTask()
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.createTaskButton.isEnabled = false
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
        mBinding.createTaskButton.isEnabled = true
    }

    private fun createTask() {
        onProgress()
        val task = Task(
            null,
            mBinding.taskTitleEt.text.toString().capitalize(Locale.ENGLISH),
            mBinding.projectNameEt.text.toString().capitalize(Locale.ENGLISH),
            mBinding.taskDescriptionEt.text.toString(),
            TaskStatus.OPEN.toString(),
            null,
            0.0,
            0.0,
            mAuth.currentUser?.uid
        )
        val taskId =
            fireStoreDbRef.collection(Constants.COLLECTION_TASK).document().id
        doctorDocumentRef =
            fireStoreDbRef.collection(Constants.COLLECTION_TASK).document(taskId)
        task.taskId = taskId
        doctorDocumentRef.set(task).addOnSuccessListener {
            onResponse()
            activity?.finish()
            AppUtils.showToast(
                requireContext(),
                getString(R.string.create_success)
            )
        }.addOnFailureListener {
            onResponse()
            AppUtils.showToast(requireContext(), it.message.toString())
            Log.d("TAG", "createTask: ${it.message}")
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