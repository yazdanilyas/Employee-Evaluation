package com.cybereast.employeeperformancetracker.ui.fragments.rateTaskFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.base.BaseFragment
import com.cybereast.employeeperformancetracker.base.BaseInterface
import com.cybereast.employeeperformancetracker.constants.Constants
import com.cybereast.employeeperformancetracker.databinding.FragmentRatingBinding
import com.cybereast.employeeperformancetracker.enums.TaskStatus
import com.cybereast.employeeperformancetracker.models.Task
import com.cybereast.employeeperformancetracker.utils.AppUtils
import com.cybereast.employeeperformancetracker.utils.CommonKeys
import java.util.*

class RatingFragment : BaseFragment(), BaseInterface {

    companion object {
        @JvmStatic
        fun newInstance(mBundle: Bundle) =
            RatingFragment().apply {
                arguments = mBundle

            }
    }

    private lateinit var mBinding: FragmentRatingBinding
    private lateinit var viewModel: RateTaskViewModel
    private var onTimeRating: Float = 0.0f
    private var qualityRating: Float = 0.0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentRatingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RateTaskViewModel::class.java)
        getBundleData()
        setListeners()
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun setListeners() {
        mBinding.submitRatingButton.setOnClickListener {
            val taskId = viewModel.mTask?.taskId
            val userId = viewModel.mTask?.assigneeId
            rateTask(taskId, userId)
        }
        mBinding.onTimeRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            onTimeRating = rating
            Log.d("TAG", "setListeners: $onTimeRating")
        }
        mBinding.qualityRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            qualityRating = rating
            Log.d("TAG", "setListeners: $qualityRating")
        }
    }

    private fun rateTask(taskId: String?, userId: String?) {

        onProgress()
        val points = onTimeRating + qualityRating
        var taskMap: HashMap<String, Any>? = null
        taskMap = hashMapOf<String, Any>(
            "points" to points,
            "status" to TaskStatus.COMPLETED.toString()
        )

        doctorDocumentRef =
            fireStoreDbRef.collection(Constants.COLLECTION_TASK).document(taskId.toString())
        doctorDocumentRef.update(taskMap).addOnCompleteListener {
            onResponse()
//            AppUtils.showToast(requireContext(), getString(R.string.operation_success))
//            requireActivity().finish()
            userId?.let { it1 -> updateEmployee(it1) }
        }.addOnFailureListener {
            onResponse()
            AppUtils.showToast(requireContext(), it.message.toString())
        }

    }

    private fun updateEmployee(empId: String) {
        val points = onTimeRating + qualityRating
        var taskMap: HashMap<String, Any>? = null
        taskMap = hashMapOf<String, Any>(
            "points" to points,
        )

        doctorDocumentRef =
            fireStoreDbRef.collection(Constants.COLLECTION_EMPLOYEE).document(empId)
        doctorDocumentRef.update(taskMap).addOnCompleteListener {
            AppUtils.showToast(requireContext(), getString(R.string.operation_success))
            requireActivity().finish()
        }.addOnFailureListener {
            AppUtils.showToast(requireContext(), it.message.toString())
        }

    }

    private fun getBundleData() {
        arguments.let {
            viewModel.mTask = it?.getSerializable(CommonKeys.KEY_DATA) as Task
        }
    }
}