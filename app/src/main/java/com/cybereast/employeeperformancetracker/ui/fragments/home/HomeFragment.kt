package com.cybereast.employeeperformancetracker.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employeeperformancetracker.databinding.FragmentHomeBinding
import com.cybereast.employeeperformancetracker.ui.fragments.addEmployeeFragment.AddEmployeeFragment
import com.cybereast.employeeperformancetracker.ui.fragments.assignTaskFragment.AssignTaskFragment
import com.cybereast.employeeperformancetracker.ui.fragments.createTaskFragment.CreateTaskFragment
import com.cybereast.employeeperformancetracker.ui.fragments.employeeListFragment.EmployeeListFragment
import com.cybereast.employeeperformancetracker.ui.fragments.rateTaskFragment.RateTaskFragment
import com.cybereast.employeeperformancetracker.ui.fragments.taskListFragment.TaskListFragment
import com.cybereast.employeeperformancetracker.utils.ActivityUtils

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        mBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        mBinding.addEmployeeCv.setOnClickListener {
            ActivityUtils.launchFragment(requireContext(), AddEmployeeFragment::class.java.name)
        }
        mBinding.createTaskCv.setOnClickListener {
            ActivityUtils.launchFragment(requireContext(), CreateTaskFragment::class.java.name)
        }
        mBinding.assignTaskCv.setOnClickListener {
            ActivityUtils.launchFragment(requireContext(), AssignTaskFragment::class.java.name)
        }
        mBinding.empListCv.setOnClickListener {
            ActivityUtils.launchFragment(requireContext(), EmployeeListFragment::class.java.name)
        }
        mBinding.taskListCv.setOnClickListener {
            ActivityUtils.launchFragment(requireContext(), TaskListFragment::class.java.name)
        }
        mBinding.rateTaskCv.setOnClickListener {
            ActivityUtils.launchFragment(requireContext(), RateTaskFragment::class.java.name)
        }
    }
}