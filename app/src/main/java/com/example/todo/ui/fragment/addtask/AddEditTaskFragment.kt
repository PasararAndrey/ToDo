package com.example.todo.ui.fragment.addtask

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.ToDoApplication
import com.example.todo.data.task.Task
import com.example.todo.databinding.FragmentAddEditTaskBinding
import com.example.todo.ui.fragment.tasks.TasksViewModel
import com.example.todo.ui.fragment.tasks.TasksViewModelFactory

class AddEditTaskFragment : Fragment() {
    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(
            (activity?.application as ToDoApplication).repository
        )
    }

    private val navArgs: AddEditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navArgs.taskId
        if (id > 0) {
            viewModel.getTask(id).observe(viewLifecycleOwner) { selectedTask ->
                bind(selectedTask)
            }
        } else {
            binding.fab.setOnClickListener {
                addNewItem()
            }
        }
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.insertTask(
                binding.addTaskTitleEt.text.toString(),
                binding.addTaskDescEt.text.toString()
            )
            val action = AddEditTaskFragmentDirections.actionAddEditTaskFragmentToNavAllTasks()
            findNavController().navigate(action)
        }
    }

    private fun bind(task: Task) {
        binding.apply {
            addTaskTitleEt.setText(task.title)
            addTaskDescEt.setText(task.description)
            fab.setOnClickListener { updateTask() }
        }
    }

    private fun updateTask() {
        if (isEntryValid()) {
            viewModel.updateTask(
                navArgs.taskId,
                binding.addTaskTitleEt.text.toString(),
                binding.addTaskDescEt.text.toString()
            )
            val action = AddEditTaskFragmentDirections.actionAddEditTaskFragmentToNavAllTasks()
            findNavController().navigate(action)
        }
    }


    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.addTaskTitleEt.text.toString(),
            binding.addTaskDescEt.text.toString()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

        _binding = null
    }
}