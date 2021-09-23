package com.example.todo.ui.fragment.addtask

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.data.task.Task
import com.example.todo.databinding.FragmentAddEditTaskBinding
import com.example.todo.ui.fragment.tasks.TasksViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddEditTaskFragment : Fragment() {
    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: TasksViewModel

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
                addNewTask()
            }
        }
        initDatePickerListener()
    }

    private fun initDatePickerListener() {
        val calendar = Calendar.getInstance()
        val myYear = calendar.get(Calendar.YEAR)
        val myMonth = calendar.get(Calendar.MONTH)
        val myDay = calendar.get(Calendar.DAY_OF_MONTH)
        binding.addTaskPickDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    binding.addTaskPickDate.setText(
                        DateFormat.getDateInstance().format(calendar.time)
                    )
                },
                myYear,
                myMonth,
                myDay
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun addNewTask() {
        if (isEntryValid()) {
            val date = checkCorrectDate()
            viewModel.insertTask(
                binding.addTaskTitleEt.text.toString(),
                binding.addTaskImportantCheckbox.isChecked,
                date,
                Date(System.currentTimeMillis())
            )
            val action = AddEditTaskFragmentDirections.actionAddEditTaskFragmentToNavAllTasks()
            findNavController().navigate(action)
        } else {
            showSnackbarOnInvalidEntry()
        }
    }

    private fun bind(task: Task) {
        binding.apply {
            val dateString = if (task.termDate == null) {
                ""
            } else {
                DateFormat.getDateInstance().format(task.termDate).toString()
            }
            addTaskTitleEt.setText(task.title)
            addTaskImportantCheckbox.isChecked = task.important
            addTaskPickDate.setText(
                dateString
            )
            fab.setOnClickListener { updateTask() }
        }
    }


    private fun updateTask() {
        if (isEntryValid()) {
            val date = checkCorrectDate()
            viewModel.updateTask(
                navArgs.taskId,
                binding.addTaskTitleEt.text.toString(),
                binding.addTaskImportantCheckbox.isChecked,
                date,
                Date(System.currentTimeMillis())
            )
            val action = AddEditTaskFragmentDirections.actionAddEditTaskFragmentToNavAllTasks()
            findNavController().navigate(action)
        } else {
            showSnackbarOnInvalidEntry()
        }
    }

    private fun showSnackbarOnInvalidEntry() {
        val snackbar: Snackbar =
            Snackbar.make(binding.root, R.string.invalid_entry_message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun checkCorrectDate(): Date? {
        val bindingDate = binding.addTaskPickDate.text.toString()
        return if (bindingDate.isEmpty()) {
            null
        } else {
            DateFormat.getDateInstance().parse(bindingDate)

        }
    }


    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.addTaskTitleEt.text.toString(),
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