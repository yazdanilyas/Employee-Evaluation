package com.cybereast.employeeperformancetracker.models

class BaseTask(var task: Task? = null, var employee: Employee? = null) {
    override fun toString(): String {
        return "BaseTask(task=$task, employee=$employee)"
    }
}
