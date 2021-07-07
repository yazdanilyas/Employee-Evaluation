package com.cybereast.employeeperformancetracker.models

import java.io.Serializable

class Task(
    var taskId: String? = null,
    var taskTitle: String? = null,
    var projectName: String? = null,
    var description: String? = null,
    var status: String? = null,
    var assigneeId: String? = null,
    var timeLocked: Double? = null,
    var points: Double? = null,
    var userId: String? = null
) : Serializable