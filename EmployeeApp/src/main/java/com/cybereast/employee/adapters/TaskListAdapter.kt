package com.cybereast.employee.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybereast.employee.R
import com.cybereast.employee.databinding.ItemTaskBinding
import com.cybereast.employee.listeners.RecyclerItemClickListener
import com.cybereast.employee.models.Task
import java.util.*

class TaskListAdapter(
    private val taskList: ArrayList<Any>,
    val dataType: String?,
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
        holder.binding.startWorkingBtn.setOnClickListener {
            mRecyclerListener?.onClick(taskList[position], position)
        }
//        if (dataType == TaskStatus.WORKING.toString()) {
////            mBinding.startWorkingBtn.text = context.getString(R.string.complete)
//        }
//        holder.binding.seeProfileButton.setOnClickListener {
//            mRecyclerListener.onSeeProfile(taskList, position)
//        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}