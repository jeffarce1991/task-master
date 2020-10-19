package com.example.taskmaster.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.R
import com.example.taskmaster.databinding.ItemTaskBinding
import com.example.taskmaster.models.Task
import com.example.taskmaster.ui.TaskDetailActivity
import com.example.taskmaster.ui.TaskDetailFragment
import com.example.taskmaster.ui.TaskListActivity

internal class RecyclerAdapter(
    private val parentActivity: TaskListActivity,
    private var mTasks: List<Task>,
    private val twoPane: Boolean
) : RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Task
            if (twoPane) {
                val fragment = TaskDetailFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(TaskDetailFragment.ARG_TASK_ID, item.id.toString())
                        }
                    }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.task_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, TaskDetailActivity::class.java).apply {
                    putExtra(TaskDetailFragment.ARG_TASK_ID, item.id.toString())
                }
                v.context.startActivity(intent)
            }
        }
    }

    internal inner class CustomViewHolder(binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val id: TextView = binding.id
        val title: TextView = binding.title

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder{
        val binding = DataBindingUtil.inflate<ItemTaskBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_task,
            parent,
            false)

        return CustomViewHolder(binding)
    }

    fun updateList(tasks: List<Task>) {
        mTasks = tasks
        notifyDataSetChanged()
    }

    override fun getItemCount() = mTasks.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = mTasks[position]
        holder.id.text = item.id.toString()
        holder.title.text = item.title

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }

        /*Glide
            .with(context)
            .load(item.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_launcher_background)
            .into(holder.image)*/
    }
}