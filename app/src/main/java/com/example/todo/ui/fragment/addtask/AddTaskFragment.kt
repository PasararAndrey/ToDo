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
import com.example.todo.ToDoApplication
import com.example.todo.databinding.FragmentAddTaskBinding
import com.example.todo.ui.fragment.tasks.TasksViewModel
import com.example.todo.ui.fragment.tasks.TasksViewModelFactory

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(
            (activity?.application as ToDoApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFab()
    }

    private fun initFab() {
        binding.fab.setOnClickListener {
            if (isEntryValid()) {
                viewModel.insertTask(
                    binding.addTaskTitleEt.text.toString(),
                    binding.addTaskGoalEt.text.toString()
                )
                val action = AddTaskFragmentDirections.actionAddTaskFragmentToNavAllTasks()
                findNavController().navigate(action)
            }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.addTaskTitleEt.text.toString(),
            binding.addTaskGoalEt.text.toString()
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