package com.example.todo.ui.fragment.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.task.Task
import com.example.todo.databinding.TaskItemBinding

class TasksAdapter(
    private val viewModel: TasksViewModel,
    private val onItemClicked: (Task) -> Unit
) :
    ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: TasksViewModel, task: Task) {
            binding.apply {
                title.text = task.title
                description.text = task.description
                completeCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.deleteTask(task)
                    }
                }

            }
        }

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return TaskViewHolder(binding)
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask: Task = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentTask)
        }
        holder.bind(viewModel, currentTask)
    }
}