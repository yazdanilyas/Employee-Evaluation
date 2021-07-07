package com.cybereast.employee.ui.fragments.previousTaskFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.employee.R

class PreviousTaskFragment : Fragment() {

    companion object {
        fun newInstance() = PreviousTaskFragment()
    }

    private lateinit var viewModel: PreviousTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.previous_task_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PreviousTaskViewModel::class.java)
    }


}