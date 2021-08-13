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
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


@AndroidEntryPoint
class TasksFragment : Fragment() {
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: TasksViewModel

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
                .actionNavAllTasksToAddEditTaskFragment()
            findNavController().navigate(action)
        }
        val recyclerView = binding.allTasksRv
        val adapter = TasksAdapter(viewModel) {
            val action =
                TasksFragmentDirections.actionNavAllTasksToAddEditTaskFragment(taskId = it.id)
            this.findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.tasks.observe(viewLifecycleOwner)
        { tasks -> tasks?.let { adapter.submitList(it) } }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}