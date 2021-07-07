package com.cybereast.employeeperformancetracker.ui.fragments.rateTaskFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.employeeperformancetracker.adapters.TaskListAdapter
import com.cybereast.employeeperformancetracker.base.BaseFragment
import com.cybereast.employeeperformancetracker.base.BaseInterface
import com.cybereast.employeeperformancetracker.constants.Constants
import com.cybereast.employeeperformancetracker.databinding.RateTaskFragmentBinding
import com.cybereast.employeeperformancetracker.enums.TaskStatus
import com.cybereast.employeeperformancetracker.listeners.RecyclerItemClickListener
import com.cybereast.employeeperformancetracker.models.Task
import com.cybereast.employeeperformancetracker.utils.ActivityUtils
import com.cybereast.employeeperformancetracker.utils.AppUtils
import com.cybereast.employeeperformancetracker.utils.CommonKeys
import java.util.*

class RateTaskFragment : BaseFragment(), BaseInterface {

    companion object {
        fun newInstance() = RateTaskFragment()
    }

    private lateinit var viewModel: RateTaskViewModel
    private lateinit var mBinding: RateTaskFragmentBinding
    private lateinit var mAdapter: TaskListAdapter
    private var mItemClickListener: RecyclerItemClickListener = object :
        RecyclerItemClickListener {
        override fun onClick(data: Any?, position: Int) {
            val task = data as Task
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.KEY_DATA, task)
            ActivityUtils.launchFragment(requireContext(), RatingFragment::class.java.name, bundle)
        }

        override fun onItemChildClick(view: View, data: Any?) {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = RateTaskFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RateTaskViewModel::class.java)
        getTaskList(TaskStatus.REVIEW.toString())
        setUpAdapter()
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun getTaskList(taskStatus: String?) {
        onProgress()
        fireStoreDbRef.collection(Constants.COLLECTION_TASK)
            .whereEqualTo("userId", mAuth.currentUser?.uid).whereEqualTo("status", taskStatus).get()
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
            TaskListAdapter(viewModel.mTaskList as ArrayList<Any>, mItemClickListener)
        mBinding.taskRecycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.taskRecycler.adapter = mAdapter
    }


}