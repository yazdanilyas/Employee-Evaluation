package com.cybereast.employee.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employee.databinding.FragmentHomeBinding
import com.cybereast.employee.enums.TaskStatus
import com.cybereast.employee.ui.fragments.myStatsFragment.MyStatsFragment
import com.cybereast.employee.ui.fragments.taskListFragment.TaskListFragment
import com.cybereast.employee.utils.ActivityUtils
import com.cybereast.employee.utils.CommonKeys

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
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        mBinding.newTasksCv.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CommonKeys.KEY_DATA, TaskStatus.OPEN.toString())
            ActivityUtils.launchFragment(
                requireContext(),
                TaskListFragment::class.java.name,
                bundle
            )
        }
        mBinding.inProgressTaskCv.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CommonKeys.KEY_DATA, TaskStatus.WORKING.toString())
            ActivityUtils.launchFragment(
                requireContext(),
                TaskListFragment::class.java.name,
                bundle
            )
        }
        mBinding.previousTaskCv.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CommonKeys.KEY_DATA, TaskStatus.COMPLETED.toString())
            ActivityUtils.launchFragment(
                requireContext(),
                TaskListFragment::class.java.name,
                bundle
            )
        }
        mBinding.myStatsCv.setOnClickListener {

            ActivityUtils.launchFragment(requireContext(), MyStatsFragment::class.java.name)
        }

    }
}