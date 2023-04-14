package com.example.myapplication.ui.taskinfo;

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

public class TaskInfoFragment extends Fragment {
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
        assert getArguments() != null;
        int id = getArguments().getInt("id");
        binding.delete.setVisibility(View.VISIBLE);
        binding.delete.setOnClickListener(
                view1 -> {
                    tasksViewModel.deleteTask(id);
                    Navigation.findNavController(view1).popBackStack();
                }
        );
        binding.exitButton.setOnClickListener(
                view1 -> {
                    tasksViewModel.refresh();
                    Navigation.findNavController(view1).popBackStack();
                }
        );
        binding.saveText.setOnClickListener(
                view1 -> {
                    String response = tasksViewModel.updateTask(id);
                    if (Objects.equals(response, "0")) {
                        Toast.makeText(getContext(), "Введите название события", Toast.LENGTH_LONG).show();
                    } else if (Objects.equals(response, "2")) {
                        Toast.makeText(getContext(), "Некорректный выбор времени", Toast.LENGTH_LONG).show();
                    } else if (Objects.equals(response, "1")) {
                        Navigation.findNavController(view1).popBackStack();
                        tasksViewModel.refresh();
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
        binding.editName.setText(getArguments().getString("name"));
        binding.editDescription.setText(getArguments().getString("description"));
        binding.calendar.setDate(getArguments().getLong("date"));
        tasksViewModel.setTaskDate(getArguments().getLong("date"));
        binding.pickerStart.setHour(getArguments().getInt("startHour"));
        binding.pickerStart.setMinute(getArguments().getInt("startMinute"));
        binding.pickerEnd.setHour(getArguments().getInt("finishHour"));
        binding.pickerEnd.setMinute(getArguments().getInt("finishMinute"));
        binding.spinnerType.setSelection(getArguments().getInt("type"));
        binding.spinnerUrgency.setSelection(getArguments().getInt("urgency"));
        binding.spinnerShifting.setSelection(getArguments().getInt("shifting"));
    }
}
