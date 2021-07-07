package com.cybereast.employeeperformancetracker.listeners

import android.view.View

interface SwitchStateListener {
    fun onChecked(view: View, isChecked: Boolean, data: Any?)
}