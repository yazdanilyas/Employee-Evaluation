package com.cybereast.employeeperformancetracker.utils

import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("setCheckedStatus")
fun setCheckedStatus(switch: SwitchCompat, isChecked: Boolean) {
    switch.isChecked = isChecked


}