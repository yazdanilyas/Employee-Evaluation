package com.cybereast.employee.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybereast.employee.R
import com.cybereast.employee.databinding.ItemEmployeeBinding
import com.cybereast.employee.listeners.RecyclerItemClickListener
import com.cybereast.employee.models.Employee
import java.util.*

class EmployeeListAdapter(
    private val dataList: ArrayList<Any>,
    private val mRecyclerListener: RecyclerItemClickListener?
) : RecyclerView.Adapter<EmployeeListAdapter.TaskViewHolder>() {
    private lateinit var mBinding: ItemEmployeeBinding

    class TaskViewHolder(val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_employee, parent, false)
        return TaskViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.obj = dataList[position] as Employee
//        holder.binding.itemWrapper.setOnClickListener {
//            mRecyclerListener.onClick(taskList[position], position)
//        }
//        holder.binding.seeProfileButton.setOnClickListener {
//            mRecyclerListener.onSeeProfile(taskList, position)
//        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}