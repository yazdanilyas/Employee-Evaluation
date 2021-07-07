package com.cybereast.employee.ui.fragments.taskListFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.employee.R
import com.cybereast.employee.adapters.TaskListAdapter
import com.cybereast.employee.base.BaseFragment
import com.cybereast.employee.base.BaseInterface
import com.cybereast.employee.constants.Constants
import com.cybereast.employee.databinding.TaskListFragmentBinding
import com.cybereast.employee.enums.TaskStatus
import com.cybereast.employee.listeners.RecyclerItemClickListener
import com.cybereast.employee.models.Task
import com.cybereast.employee.utils.AppUtils
import com.cybereast.employee.utils.CommonKeys
import com.cybereast.employee.utils.PrefUtils
import java.util.*

class TaskListFragment : BaseFragment(), BaseInterface {

    companion object {
        fun newInstance() = TaskListFragment()
        fun newInstance(mBundle: Bundle) = TaskListFragment().apply {
            arguments = mBundle
        }

    }

    private lateinit var viewModel: TaskListViewModel
    private lateinit var mBinding: TaskListFragmentBinding
    private lateinit var mAdapter: TaskListAdapter
    private lateinit var mItemClickListener: RecyclerItemClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = TaskListFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
//        createTaskStatusTabs()
        getBundleData()
        taskAdapterItemClick()
        when (viewModel.dataType) {
            TaskStatus.OPEN.toString() -> {
                getTaskList(TaskStatus.OPEN.toString())
            }
            TaskStatus.WORKING.toString() -> {
                getTaskList(TaskStatus.WORKING.toString())
            }
            TaskStatus.COMPLETED.toString() -> {
                getTaskList(TaskStatus.COMPLETED.toString())
            }

        }
        setUpAdapter()
    }

    private fun getBundleData() {
        arguments.let {
            viewModel.dataType = it?.getString(CommonKeys.KEY_DATA)
            Log.d("TAG", "getBundleData: " + viewModel.dataType)
        }
    }

    private fun taskAdapterItemClick() {
        mItemClickListener = object : RecyclerItemClickListener {
            override fun onClick(data: Any?, position: Int) {
                val task = data as Task
                Log.d("TAG", "onClick: ${task.taskId}")
                task.taskId?.let { updateTaskStatus(it) }
            }

            override fun onItemChildClick(view: View, data: Any?) {

            }

            override fun onSeeProfile(data: Any?, position: Int) {

            }
        }
    }

    private fun updateTaskStatus(taskId: String) {
        onProgress()
        var taskMap: HashMap<String, Any>? = null
        if (viewModel.dataType == TaskStatus.OPEN.toString()) {
            taskMap = hashMapOf<String, Any>(
                "status" to TaskStatus.WORKING
            )
        } else {
            taskMap = hashMapOf<String, Any>(
                "status" to TaskStatus.REVIEW
            )
        }
        doctorDocumentRef =
            fireStoreDbRef.collection(Constants.COLLECTION_TASK).document(taskId)
        doctorDocumentRef.update(taskMap).addOnCompleteListener {
            onResponse()
            AppUtils.showToast(requireContext(), getString(R.string.task_assigned))
            requireActivity().finish()
        }.addOnFailureListener {
            onResponse()
            AppUtils.showToast(requireContext(), it.message.toString())
        }
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
                        mAdapter.notifyDataSetChanged()
                        onResponse()
                        mBinding.noDataTv.visibility = View.GONE
                        Log.d("TAG", "getTaskList: ${task.size}")
                    } else {
                        onResponse()
                        mBinding.noDataTv.visibility = View.VISIBLE
                        if (viewModel.mTaskList.isNotEmpty())
                            viewModel.mTaskList.clear()
                        mAdapter.notifyDataSetChanged()
                    }

                } else {
                    onResponse()
                }
            }.addOnFailureListener {
                onResponse()
                AppUtils.showToast(requireContext(), it.message.toString())
            }

    }


    private fun setUpAdapter() {
        mAdapter =
            TaskListAdapter(
                viewModel.mTaskList as ArrayList<Any>,
                viewModel.dataType,
                mItemClickListener
            )
        mBinding.taskRecycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.taskRecycler.adapter = mAdapter
    }


}
