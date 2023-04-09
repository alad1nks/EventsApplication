package com.example.myapplication.ui.addtask;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAddTaskBinding;
import com.example.myapplication.ui.tasks.TasksAdapter;
import com.example.myapplication.ui.tasks.TasksViewModel;

import java.util.GregorianCalendar;

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
                    if (tasksViewModel.insertTask()) {
                        Navigation.findNavController(view1).popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Некорректная дата", Toast.LENGTH_LONG).show();
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
                    Log.d("getDate", String.valueOf(new GregorianCalendar(y,m,d).getTimeInMillis()));
                }
        );
        binding.pickerStart.setOnTimeChangedListener(
                (v, h, m) -> tasksViewModel.setTaskStart(h * 3600000L + m * 60000L)
        );
        binding.pickerEnd.setOnTimeChangedListener(
                (v, h, m) -> tasksViewModel.setTaskEnd(h * 3600000L + m * 60000L)
        );
        tasksViewModel.setTaskDate(binding.calendar.getDate() - binding.calendar.getDate() % 86400000);
        tasksViewModel.setTaskStart(binding.pickerStart.getHour() * 3600000L + binding.pickerStart.getMinute() * 60000L);
        tasksViewModel.setTaskEnd(binding.pickerEnd.getHour() * 3600000L + binding.pickerEnd.getMinute() * 60000L);
        tasksViewModel.setTaskName("");
        tasksViewModel.setTaskDescription("");
    }

}
