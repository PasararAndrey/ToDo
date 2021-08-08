package com.example.todo.ui.fragment.taskdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todo.ToDoApplication
import com.example.todo.data.task.Task
import com.example.todo.databinding.FragmentTaskDetailBinding
import com.example.todo.ui.fragment.tasks.TasksViewModel
import com.example.todo.ui.fragment.tasks.TasksViewModelFactory


class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val navigationArgs: TaskDetailFragmentArgs by navArgs()

    private val viewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(
            (activity?.application as ToDoApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.taskId
        viewModel.getTask(id).observe(this.viewLifecycleOwner) {
            bind(it)
        }
    }

    private fun bind(task: Task) {
        binding.apply {
            taskName.text = task.title
            taskDescription.text = task.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}