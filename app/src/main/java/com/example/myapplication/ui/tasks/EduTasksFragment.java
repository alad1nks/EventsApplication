package com.example.myapplication.ui.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecyclerTasksBinding;

public class EduTasksFragment extends Fragment {

    private RecyclerTasksBinding binding;
    private TasksAdapter adapter;
    private TasksViewModel tasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        binding = RecyclerTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TasksAdapter(requireActivity());
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recycler.setAdapter(adapter);
        tasksViewModel.getEduTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.submitList(tasks);
        });
    }
}
