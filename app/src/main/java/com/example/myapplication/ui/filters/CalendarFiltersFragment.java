package com.example.myapplication.ui.filters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.databinding.FragmentCalendarFilterBinding;
import com.example.myapplication.databinding.FragmentFiltersBinding;
import com.example.myapplication.ui.tasks.TasksViewModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarFiltersFragment extends Fragment {
    FragmentCalendarFilterBinding binding;
    private TasksViewModel tasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);

        binding = FragmentCalendarFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long time = Calendar.getInstance().getTimeInMillis();
        binding.calendarFilter.setOnDateChangeListener(
                (c, y, m, d) -> {
                    tasksViewModel.setFilterCalendar(new GregorianCalendar(y,m,d).getTimeInMillis());
                }
        );
        binding.calendarFilter.setDate(
                tasksViewModel.getFilterCalendar().getValue()
        );
        binding.saveButton.setOnClickListener(view1 -> {
            tasksViewModel.setCalendar();
            Navigation.findNavController(view1).popBackStack();
        });
        binding.close.setOnClickListener(view1 -> {
            tasksViewModel.setFilterCalendar(time - (time + 10800000) % 86400000);
            Navigation.findNavController(view1).popBackStack();
        });
    }
}
