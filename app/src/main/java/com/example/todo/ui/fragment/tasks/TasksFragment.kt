package com.example.todo.ui.fragment.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.SortOrder
import com.example.todo.data.task.Task
import com.example.todo.databinding.FragmentTasksBinding
import com.example.todo.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!


    private val viewModel: TasksViewModel by viewModels()

    private var _searchView: SearchView? = null
    private val searchView get() = _searchView!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTasksBinding.bind(view)


        val tasksAdapter = TasksAdapter(
            object : TasksAdapter.OnItemClickListener {
                override fun onItemClick(task: Task) {
                    val action =
                        TasksFragmentDirections.actionNavAllTasksToAddEditTaskFragment(task.id)
                    findNavController().navigate(action)
                }

                override fun onCheckBoxClick(task: Task) {
                    viewModel.deleteTask(task)
                }

            }
        )

        binding.apply {
            allTasksRv.apply {
                adapter = tasksAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fab.setOnClickListener {
                val action = TasksFragmentDirections
                    .actionNavAllTasksToAddEditTaskFragment()
                findNavController().navigate(action)
            }


            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = tasksAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteTask(task)
                }

            }).attachToRecyclerView(allTasksRv)
        }

        viewModel.tasks.observe(viewLifecycleOwner)
        { tasks -> tasksAdapter.submitList(tasks) }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect { event ->
                when (event) {
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                }

            }
        }
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _searchView = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_tasks, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)

        _searchView = searchItem.actionView as SearchView

        val currentQuery = viewModel.searchQuery.value
        if (currentQuery != null && currentQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(currentQuery, false)
        }

        searchView.onQueryTextChanged { newText ->
            viewModel.searchQuery.value = newText
        }

        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        editText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_important).isChecked =
                viewModel.preferencesFlow.first().anchorImportant
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
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.sort_by_creation -> {
                viewModel.onSortOrderSelected(SortOrder.BY_CREATION_DATE)
                true
            }
            R.id.sort_by_deadline -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DEADLINE_DATE)
                true
            }

            R.id.action_important -> {
                item.isChecked = !item.isChecked
                viewModel.onAnchorImportantClick(item.isChecked)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}
