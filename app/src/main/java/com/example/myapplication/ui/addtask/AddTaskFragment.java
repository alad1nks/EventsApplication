package com.example.myapplication.ui.addtask;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.databinding.FragmentAddTaskBinding;
import com.example.myapplication.ui.tasks.TasksViewModel;

import java.util.GregorianCalendar;
import java.util.Objects;

public class AddTaskFragment extends Fragment {
    private FragmentAddTaskBinding binding;
    private TasksViewModel tasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.exitButton.setOnClickListener(
                view1 -> {
                    tasksViewModel.refresh();
                    Navigation.findNavController(view1).popBackStack();
                }
        );
        binding.saveText.setOnClickListener(
                view1 -> {
                    String response = tasksViewModel.insertTask();
                    if (Objects.equals(response, "0")) {
                        Toast.makeText(getContext(), "Введите название события", Toast.LENGTH_LONG).show();
                    } else if (Objects.equals(response, "2")) {
                        Toast.makeText(getContext(), "Некорректный выбор времени", Toast.LENGTH_LONG).show();
                    } else if (Objects.equals(response, "1")) {
                        Navigation.findNavController(view1).popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Перечечение по времени с cобытием " + response, Toast.LENGTH_LONG).show();
                    }
                }
        );
        binding.editName.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        tasksViewModel.setTaskName(editable.toString());
                    }
                }
        );
        binding.editDescription.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        tasksViewModel.setTaskDescription(editable.toString());
                    }
                }
        );

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tasksViewModel.setTaskType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerUrgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tasksViewModel.setTaskUrgency(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerShifting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tasksViewModel.setTaskShifting(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.calendar.setOnDateChangeListener(
                (c, y, m, d) -> {
                    tasksViewModel.setTaskDate(new GregorianCalendar(y,m,d).getTimeInMillis());
                }
        );
        binding.pickerStart.setOnTimeChangedListener(
                (v, h, m) -> tasksViewModel.setTaskStart(h * 3600000L + m * 60000L)
        );
        binding.pickerEnd.setOnTimeChangedListener(
                (v, h, m) -> tasksViewModel.setTaskEnd(h * 3600000L + m * 60000L)
        );
        assert getArguments() != null;
        binding.editName.setText(getArguments().getString("name"));
        binding.editDescription.setText(getArguments().getString("description"));
        if (tasksViewModel.getFilterCalendar() != null) {
            tasksViewModel.setTaskDate(tasksViewModel.getFilterCalendar().getValue());
            binding.calendar.setDate(tasksViewModel.getFilterCalendar().getValue());
        } else {
            tasksViewModel.setTaskDate(binding.calendar.getDate() - (binding.calendar.getDate() + 10800000) % 86400000);
        }
        tasksViewModel.setTaskStart(binding.pickerStart.getHour() * 3600000L + binding.pickerStart.getMinute() * 60000L);
        tasksViewModel.setTaskEnd(binding.pickerEnd.getHour() * 3600000L + binding.pickerEnd.getMinute() * 60000L);
    }
}
