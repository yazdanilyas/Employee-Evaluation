package com.cybereast.employee.ui.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employee.R
import com.cybereast.employee.databinding.FragmentMoreBinding
import com.cybereast.employee.ui.activities.SigninActivity
import com.cybereast.employee.utils.ActivityUtils
import com.cybereast.employee.utils.AppUtils
import com.cybereast.employee.utils.PrefUtils

class MoreFragment : Fragment(), View.OnClickListener {

    private lateinit var moreViewModel: MoreViewModel
    lateinit var mBinding: FragmentMoreBinding

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
        mBinding.logoutCv.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.shareAppCv -> {
                AppUtils.shareApp(requireActivity())
            }

            R.id.logoutCv -> {
                logOut()
            }
        }
    }

    private fun logOut() {
        PrefUtils.clearAllPrefData(requireContext())
        ActivityUtils.startActivity(requireActivity(), SigninActivity::class.java)
        activity?.finish()
    }

}