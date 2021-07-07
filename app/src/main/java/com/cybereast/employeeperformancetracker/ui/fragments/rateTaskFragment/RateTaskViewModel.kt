package com.cybereast.employeeperformancetracker.ui.fragments.rateTaskFragment

import androidx.lifecycle.ViewModel
import com.cybereast.employeeperformancetracker.models.Task

class RateTaskViewModel : ViewModel() {
    var mTaskList: MutableList<Any> = ArrayList()
    var mTask: Task? = null
}