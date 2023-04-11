package com.example.myapplication.ui.filters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.databinding.FragmentFiltersBinding;
import com.example.myapplication.ui.tasks.TasksViewModel;

public class FiltersFragment extends Fragment {
    private FragmentFiltersBinding binding;
    private TasksViewModel tasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);

        binding = FragmentFiltersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasksViewModel.getFilterTaskTag().observe(getViewLifecycleOwner(), pos -> {
            binding.spinnerTag.setSelection(pos);
        });
        tasksViewModel.getFilterTaskUrgency().observe(getViewLifecycleOwner(), pos -> {
            binding.spinnerUrgency.setSelection(pos);
        });
        tasksViewModel.getFilterTaskShifting().observe(getViewLifecycleOwner(), pos -> {
            binding.spinnerShifting.setSelection(pos);
        });
        binding.spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tasksViewModel.setFilterTaskTag(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerUrgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tasksViewModel.setFilterTaskUrgency(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerShifting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tasksViewModel.setFilterTaskShifting(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Integer tag = tasksViewModel.getFilterTaskTag().getValue();
        Integer urgency = tasksViewModel.getFilterTaskUrgency().getValue();
        Integer shifting = tasksViewModel.getFilterTaskShifting().getValue();

        binding.saveButton.setOnClickListener(
                view1 -> {
                    tasksViewModel.setFilters();
                    Navigation.findNavController(view1).popBackStack();
                }
        );

        binding.close.setOnClickListener(
                view1 -> {
                    tasksViewModel.setFilterTaskTag(tag);
                    tasksViewModel.setFilterTaskUrgency(urgency);
                    tasksViewModel.setFilterTaskShifting(shifting);
                    Navigation.findNavController(view1).popBackStack();
                }
        );
    }
}
