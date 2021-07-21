package com.example.todo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.databinding.FragmentAllTasksBinding

class AllTasksFragment : Fragment() {

    private var _binding: FragmentAllTasksBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}