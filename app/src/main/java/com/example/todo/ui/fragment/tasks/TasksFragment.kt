package com.example.todo.ui.fragment.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.ToDoApplication
import com.example.todo.databinding.FragmentAllTasksBinding

class TasksFragment : Fragment() {
    private var _binding: FragmentAllTasksBinding? = null
    val binding get() = _binding!!

    private val viewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(
            (activity?.application as ToDoApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = binding.fab
        fab.setOnClickListener {
            val action = TasksFragmentDirections
                .actionNavAllTasksToAddTaskFragment()
            findNavController().navigate(action)
        }
        val recyclerView = binding.allTasksRv
        val adapter = TasksAdapter(viewModel) {
            val action = TasksFragmentDirections.actionNavAllTasksToTaskDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allTasks.observe(viewLifecycleOwner)
        { tasks -> tasks?.let { adapter.submitList(it) } }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}