package com.cybereast.employeeperformancetracker.listeners

import android.view.View

interface RecyclerItemClickListener {
    fun onClick(data: Any?, position: Int)
    fun onItemChildClick(view: View, data: Any?)

}