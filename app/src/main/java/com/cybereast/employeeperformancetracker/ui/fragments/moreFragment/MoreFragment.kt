package com.cybereast.employeeperformancetracker.ui.fragments.moreFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.base.BaseFragment
import com.cybereast.employeeperformancetracker.databinding.FragmentMoreBinding
import com.cybereast.employeeperformancetracker.ui.activities.LoginActivity
import com.cybereast.employeeperformancetracker.utils.ActivityUtils
import com.cybereast.employeeperformancetracker.utils.AppUtils
import com.google.firebase.auth.FirebaseAuth

class MoreFragment : BaseFragment(), View.OnClickListener {

    private lateinit var moreViewModel: MoreViewModel
    private lateinit var mBinding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moreViewModel =
            ViewModelProvider(this).get(MoreViewModel::class.java)
        mBinding = FragmentMoreBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        mBinding.shareAppCv.setOnClickListener(this)
        mBinding.rateAppCv.setOnClickListener(this)
        mBinding.logoutCv.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.shareAppCv -> {
                AppUtils.shareApp(requireActivity())
            }
            R.id.rateAppCv -> {
                AppUtils.rateApp(requireActivity())
            }
            R.id.logoutCv -> {
                logOut()
            }
        }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        ActivityUtils.startActivity(requireActivity(), LoginActivity::class.java)
        activity?.finish()
    }
}