package com.cybereast.employee.ui.fragments.myStatsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employee.base.BaseFragment
import com.cybereast.employee.base.BaseInterface
import com.cybereast.employee.constants.Constants
import com.cybereast.employee.databinding.MyStatsFragmentBinding
import com.cybereast.employee.enums.TaskStatus
import com.cybereast.employee.models.Task
import com.cybereast.employee.utils.AppUtils
import com.cybereast.employee.utils.CommonKeys
import com.cybereast.employee.utils.PrefUtils

class MyStatsFragment : BaseFragment(), BaseInterface {

    companion object {
        fun newInstance() = MyStatsFragment()
    }

    private lateinit var viewModel: MyStatsViewModel
    private lateinit var mBinding: MyStatsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = MyStatsFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyStatsViewModel::class.java)
        getTaskList(TaskStatus.COMPLETED.toString())

    }

    private fun setStats() {
        var points: Double? = null
        for (task in viewModel.mTaskList) {
            points = (task as Task).points
            if (points != null) {
                points += points
            }
        }
        var pointsStr = points.toString() + "/"
        val totalPoints = viewModel.mTaskList.size * Constants.TASK_TOTAL_POINTS
        mBinding.totalPointsTv.text = "$pointsStr$totalPoints"
        mBinding.totalTasksTv.text = viewModel.mTaskList.size.toString()
        Log.d("TAG", "setStats: " + viewModel.mTaskList.size)
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun getTaskList(taskStatus: String?) {
        onProgress()
        val userId = PrefUtils.getString(requireContext(), CommonKeys.KEY_USER_ID)
        Log.d("TAG", "userId: $userId $taskStatus")
        fireStoreDbRef.collection(Constants.COLLECTION_TASK)
            .whereEqualTo("assigneeId", userId).whereEqualTo("status", taskStatus).get()
            .addOnSuccessListener { snapshot ->

                if (snapshot != null) {
                    val task = snapshot.toObjects(Task::class.java)

                    if (task.size > 0 && task.isNotEmpty()) {
                        viewModel.mTaskList.clear()
                        viewModel.mTaskList.addAll(task)
                        onResponse()
                        setStats()
                        Log.d("TAG", "getTaskList: ${task.size}")
                    } else {
                        onResponse()
                        if (viewModel.mTaskList.isNotEmpty())
                            viewModel.mTaskList.clear()
                    }

                } else {
                    onResponse()
                }
            }.addOnFailureListener {
                onResponse()
                AppUtils.showToast(requireContext(), it.message.toString())
            }

    }


}