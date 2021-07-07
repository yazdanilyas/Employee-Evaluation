package com.cybereast.employeeperformancetracker.ui.fragments.employeeListFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.employeeperformancetracker.adapters.EmployeeListAdapter
import com.cybereast.employeeperformancetracker.base.BaseFragment
import com.cybereast.employeeperformancetracker.base.BaseInterface
import com.cybereast.employeeperformancetracker.constants.Constants
import com.cybereast.employeeperformancetracker.databinding.EmployeeListFragmentBinding
import com.cybereast.employeeperformancetracker.models.Employee
import com.cybereast.employeeperformancetracker.utils.AppUtils
import java.util.*

class EmployeeListFragment : BaseFragment(), BaseInterface {

    companion object {
        fun newInstance() = EmployeeListFragment()
    }

    private lateinit var viewModel: EmployeeListViewModel
    private lateinit var mBinding: EmployeeListFragmentBinding
    private lateinit var mAdapter: EmployeeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = EmployeeListFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EmployeeListViewModel::class.java)
        getEmployeeList()
        setUpAdapter()
    }

    override fun onProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun onResponse() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun getEmployeeList() {
        onProgress()
        fireStoreDbRef.collection(Constants.COLLECTION_EMPLOYEE)
            .whereEqualTo("userId", mAuth.currentUser?.uid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    val task = snapshot.toObjects(Employee::class.java)
                    if (task.size > 0 && task.isNotEmpty()) {
                        viewModel.mEmployeeList.clear()
                        viewModel.mEmployeeList.addAll(task)
                        mAdapter.notifyDataSetChanged()
                        onResponse()
                        mBinding.noDataTv.visibility = View.GONE
                        Log.d("TAG", "getEmployeeList: ${task.size}")
                    } else {
                        onResponse()
                        mBinding.noDataTv.visibility = View.VISIBLE
                        if (viewModel.mEmployeeList.isNotEmpty())
                            viewModel.mEmployeeList.clear()
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
            EmployeeListAdapter(viewModel.mEmployeeList as ArrayList<Any>, mRecyclerListener = null)
        mBinding.employeeRecycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.employeeRecycler.adapter = mAdapter
    }

}