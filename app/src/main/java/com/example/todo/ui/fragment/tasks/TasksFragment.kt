package com.example.todo.ui.fragment.tasks

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentTasksBinding
import com.example.todo.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: TasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
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
        val tasksAdapter = TasksAdapter(viewModel) {
            val action =
                TasksFragmentDirections.actionNavAllTasksToAddEditTaskFragment(taskId = it.id)
            this.findNavController().navigate(action)
        }
        recyclerView.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.tasks.observe(viewLifecycleOwner)
        { tasks -> tasks?.let { tasksAdapter.submitList(it) } }

        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_tasks, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        editText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        

        searchView.onQueryTextChanged { newText ->
            viewModel.searchQuery.value = newText
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                true
            }

            R.id.action_sort -> {
                true
            }

            R.id.sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.sort_by_creation -> {
                viewModel.sortOrder.value = SortOrder.BY_CREATION_DATE
                true
            }
            R.id.sort_by_deadline -> {
                viewModel.sortOrder.value = SortOrder.BY_DEADLINE_DATE
                true
            }

            R.id.action_important -> {
                item.isChecked = !item.isChecked
                viewModel.anchorImportant.value = item.isChecked
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
