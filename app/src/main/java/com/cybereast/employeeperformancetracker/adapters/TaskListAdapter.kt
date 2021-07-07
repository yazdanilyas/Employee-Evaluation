package com.cybereast.employeeperformancetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybereast.employeeperformancetracker.R
import com.cybereast.employeeperformancetracker.databinding.ItemTaskBinding
import com.cybereast.employeeperformancetracker.listeners.RecyclerItemClickListener
import com.cybereast.employeeperformancetracker.models.Task
import java.util.*

class TaskListAdapter(
    private val taskList: ArrayList<Any>,
    private val mRecyclerListener: RecyclerItemClickListener?
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    private lateinit var mBinding: ItemTaskBinding

    class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_task, parent, false)
        return TaskViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.obj = taskList[position] as Task
        holder.binding.itemWrapper.setOnClickListener {
            mRecyclerListener?.onClick(taskList[position], position)
        }


    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}