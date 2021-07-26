package com.example.todo.ui.fragment.addtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.ToDoApplication
import com.example.todo.databinding.FragmentAddTaskBinding

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    val binding get() = _binding!!

    val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as ToDoApplication).tasksDatabase.taskDao()
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
        _binding = null
    }
}