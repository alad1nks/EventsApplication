package com.example.myapplication.ui.filters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentCalendarFilterBinding;
import com.example.myapplication.databinding.FragmentFiltersBinding;
import com.example.myapplication.ui.tasks.TasksViewModel;

public class CalendarFiltersFragment extends Fragment {
    FragmentCalendarFilterBinding binding;
    private TasksViewModel tasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);

        binding = FragmentCalendarFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
