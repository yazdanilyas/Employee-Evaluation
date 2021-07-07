package com.cybereast.employeeperformancetracker.ui.fragments.assignTaskFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.base.BaseFragment
import com.cybereast.employeeperformancetracker.base.BaseInterface
import com.cybereast.employeeperformancetracker.constants.Constants
import com.cybereast.employeeperformancetracker.databinding.AssignTaskFragmentBinding
import com.cybereast.employeeperformancetracker.enums.TaskStatus
import com.cybereast.employeeperformancetracker.models.Employee
import com.cybereast.employeeperformancetracker.models.Task
import com.cybereast.employeeperformancetracker.ui.fragments.employeeListFragment.EmployeeListViewModel
import com.cybereast.employeeperformancetracker.ui.fragments.taskListFragment.TaskListViewModel
import com.cybereast.employeeperformancetracker.utils.AppUtils

class AssignTaskFragment : BaseFragment(), BaseInterface {

    companion object {
        fun newInstance() = AssignTaskFragment()
    }

    private lateinit var viewModel: AssignTaskViewModel
    private lateinit var empListViewModel: EmployeeListViewModel
    private lateinit var taskListViewModel: TaskListViewModel
    private lateinit var mBinding: AssignTaskFragmentBinding
    private lateinit var selectedEmployeeId: String
    private lateinit var selectedTaskId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AssignTaskFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        setListeners()
        getEmployeeList()
        getTaskList(TaskStatus.OPEN.toString())
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.assignTaskButton.isEnabled = false
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
        mBinding.assignTaskButton.isEnabled = true
    }

    private fun setListeners() {
        mBinding.assignTaskButton.setOnClickListener {
            assignTask()
        }
    }

    private fun assignTask() {
        onProgress()
        val taskMap = hashMapOf<String, Any>(
            "assigneeId" to selectedEmployeeId
        )
        doctorDocumentRef =
            fireStoreDbRef.collection(Constants.COLLECTION_TASK).document(selectedTaskId)
        doctorDocumentRef.update(taskMap).addOnCompleteListener {
            onResponse()
            AppUtils.showToast(requireContext(), getString(R.string.task_assigned))
            requireActivity().finish()
        }.addOnFailureListener {
            onResponse()
            AppUtils.showToast(requireContext(), it.message.toString())
        }
    }

    private fun initViewModels() {
        viewModel = ViewModelProvider(this).get(AssignTaskViewModel::class.java)
        empListViewModel = ViewModelProvider(this).get(EmployeeListViewModel::class.java)
        taskListViewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
    }

    private fun getEmployeeList() {
        onProgress()
        fireStoreDbRef.collection(Constants.COLLECTION_EMPLOYEE)
            .whereEqualTo("userId", mAuth.currentUser?.uid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    val task = snapshot.toObjects(Employee::class.java)
                    if (task.size > 0 && task.isNotEmpty()) {
                        empListViewModel.mEmployeeList.clear()
                        empListViewModel.mEmployeeList.addAll(task)
                        onResponse()
                        initEmployeeSpinner(empListViewModel.mEmployeeList as ArrayList<Any>)
                        Log.d("TAG", "getEmployeeList: ${task.size}")
                    } else {
                        onResponse()

                        if (empListViewModel.mEmployeeList.isNotEmpty())
                            empListViewModel.mEmployeeList.clear()
                    }

                } else {
                    onResponse()
                }
            }.addOnFailureListener {
                onResponse()
                AppUtils.showToast(requireContext(), it.message.toString())
            }

    }

    private fun initEmployeeSpinner(employees: ArrayList<Any>) {
        val employeeNames = ArrayList<String>()
        for (emp in employees) {
            (emp as Employee).name.let { employeeNames.add(it.toString()) }
        }
        Log.d("TAG", "initEmployeeSpinner: ${employeeNames.size}")
        mBinding.employeeSpinner.adapter =
            ArrayAdapter(
                requireActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                employeeNames
            )
        mBinding.employeeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    selectedEmployeeId = (employees[position] as Employee).employeeId.toString()
                    Log.d("TAG", "onItemSelected: $selectedEmployeeId")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
    }

    private fun getTaskList(taskStatus: String?) {
        onProgress()
        fireStoreDbRef.collection(Constants.COLLECTION_TASK)
            .whereEqualTo("userId", mAuth.currentUser?.uid).whereEqualTo("status", taskStatus).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    val task = snapshot.toObjects(Task::class.java)
                    if (task.size > 0 && task.isNotEmpty()) {
                        taskListViewModel.mTaskList.clear()
                        taskListViewModel.mTaskList.addAll(task)
                        initTaskSpinner(taskListViewModel.mTaskList as ArrayList<Any>)
                        onResponse()
                        Log.d("TAG", "getTaskList: ${task.size}")
                    } else {
                        onResponse()
                        if (taskListViewModel.mTaskList.isNotEmpty())
                            taskListViewModel.mTaskList.clear()
                    }

                } else {
                    onResponse()
                }
            }.addOnFailureListener {
                onResponse()
                AppUtils.showToast(requireContext(), it.message.toString())
            }

    }

    private fun initTaskSpinner(tasks: ArrayList<Any>) {

        val taskTitle = ArrayList<String>()
        for (emp in tasks) {
            (emp as Task).taskTitle?.let { taskTitle.add(it) }
        }
        mBinding.taskSpinner.adapter =
            ArrayAdapter(
                requireActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                taskTitle
            )
        mBinding.taskSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    selectedTaskId = (tasks[position] as Task).taskId.toString()
                    Log.d("TAG", "onItemSelected: $selectedTaskId")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
    }

}