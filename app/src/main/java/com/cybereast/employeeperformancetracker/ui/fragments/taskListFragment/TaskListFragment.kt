package com.cybereast.employeeperformancetracker.ui.fragments.taskListFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.adapters.TaskListAdapter
import com.cybereast.employeeperformancetracker.base.BaseFragment
import com.cybereast.employeeperformancetracker.base.BaseInterface
import com.cybereast.employeeperformancetracker.constants.Constants
import com.cybereast.employeeperformancetracker.databinding.TaskListFragmentBinding
import com.cybereast.employeeperformancetracker.enums.TaskStatus
import com.cybereast.employeeperformancetracker.models.BaseTask
import com.cybereast.employeeperformancetracker.models.Employee
import com.cybereast.employeeperformancetracker.models.Task
import com.cybereast.employeeperformancetracker.utils.AppUtils
import com.google.android.material.tabs.TabLayout
import java.util.*

class TaskListFragment : BaseFragment(), BaseInterface {

    companion object {
        fun newInstance() = TaskListFragment()
    }

    private lateinit var viewModel: TaskListViewModel
    private lateinit var mBinding: TaskListFragmentBinding
    private lateinit var mAdapter: TaskListAdapter
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
        createTaskStatusTabs()
        getTaskList(TaskStatus.OPEN.toString())
        setUpAdapter()
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun createTaskStatusTabs() {
        val tabList = resources.getStringArray(R.array.task_tabs)
        for (category in tabList) {
            mBinding.taskStatusTabs.addTab(
                mBinding.taskStatusTabs.newTab().setText(category)
            )
        }
        mBinding.taskStatusTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                var taskStatus: String? = null
                when (position) {
                    0 -> {
                        taskStatus = TaskStatus.OPEN.toString()
                    }
                    1 -> {
                        taskStatus = TaskStatus.WORKING.toString()
                    }
                    2 -> {
                        taskStatus = TaskStatus.COMPLETED.toString()
                    }
                }
                Log.d("TAG", "onTabSelected: $taskStatus")
                getTaskList(taskStatus)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun getTaskList(taskStatus: String?) {
        onProgress()
        fireStoreDbRef.collection(Constants.COLLECTION_TASK)
            .whereEqualTo("userId", mAuth.currentUser?.uid).whereEqualTo("status", taskStatus).get()
            .addOnSuccessListener { snapshot ->
//                if (snapshot != null) {
//                    viewModel.mBaseTaskList.clear()
//                    for (document in snapshot) {
//                        val task = document.toObject(Task::class.java)
//                        val employeeId = task.assigneeId
//
//                        if (employeeId != null) {
//                            val employee = getEmployeeById(employeeId)
//                            createBaseTask(task, employee)
//                        } else {
//                            createBaseTask(task, null)
//                        }
//                    }
//                    onResponse()
//                } else {
//                    onResponse()
//                }


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

    private fun createBaseTask(task: Task, employee: Employee?) {
        val bTask = BaseTask(task, employee)
        Log.d("TAG", "createBaseTask: $bTask")
        viewModel.mBaseTaskList.add(bTask)
        if (viewModel.mBaseTaskList.size > 0) {
            mBinding.noDataTv.visibility = View.GONE
        } else {
            mBinding.noDataTv.visibility = View.VISIBLE
            if (viewModel.mBaseTaskList.isNotEmpty())
                viewModel.mTaskList.clear()
        }
        mAdapter.notifyDataSetChanged()
    }

    var employee: Employee? = null
    private fun getEmployeeById(employeeId: String?): Employee? {


        if (employeeId != null) {
            fireStoreDbRef.collection(Constants.COLLECTION_EMPLOYEE).document(employeeId)
                .addSnapshotListener { document, error ->
                    employee = document?.toObject(Employee::class.java)
                }
        }
        return employee
//                .whereEqualTo("employeeId", employeeId).get()..addOnSuccessListener { snapshot ->
//                    val snap=snapshot.documents
//                    if (snapshot != null) {
//                        val emps = snapshot.toObjects(Employee::class.java)
//                        if (emps.size > 0 && emps.isNotEmpty()) {
////                            viewModel.mEmployeeList.clear()
//                            viewModel.mEmployeeList.addAll(emps)
//                        } else {
//                            if (viewModel.mEmployeeList.isNotEmpty())
//                                viewModel.mEmployeeList.clear()
//                        }
//
//                    }
//                }
//            return viewModel.mEmployeeList[0] as Employee
//        }
//        if (viewModel.mEmployeeList.size > 0) {
//            Log.d("TAG", "getEmployeeList: ${viewModel.mEmployeeList.size}")
//            return viewModel.mEmployeeList[0] as Employee
//        } else {
//            return null
//        }
//        return null

    }

    private fun setUpAdapter() {
        mAdapter =
            TaskListAdapter(viewModel.mTaskList as ArrayList<Any>, mRecyclerListener = null)
        mBinding.taskRecycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.taskRecycler.adapter = mAdapter
    }


}