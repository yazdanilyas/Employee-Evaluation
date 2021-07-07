package com.cybereast.employeeperformancetracker.ui.fragments.taskListFragment

import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel() {
    var mTaskList: MutableList<Any> = ArrayList()
    var mEmployeeList: MutableList<Any> = ArrayList()
    var mBaseTaskList: MutableList<Any> = ArrayList()
}