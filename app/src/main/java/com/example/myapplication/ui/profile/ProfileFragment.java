package com.example.myapplication.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.PermissionUtils;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.preferences.AppPreference;
import com.example.myapplication.preferences.AppPreferenceImpl;
import com.example.myapplication.ui.tasks.TasksViewModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static final int PERMISSION_STORAGE = 101;
    private TasksViewModel tasksViewModel;

    ActivityResultLauncher<Intent> filePicker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppPreference sharedPreferences = new AppPreferenceImpl(requireContext());
        binding.userName.setText(sharedPreferences.getName());
        binding.userSurname.setText(sharedPreferences.getSurname());
        binding.userUniversity.setText(sharedPreferences.getUniversity());
        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent1 = result.getData();

                        assert intent1 != null;
                        Uri uri = intent1.getData();
                        if (uri.getPath().endsWith("ics")) {
                            byte[] byteData = getBytes(requireActivity(), uri);
                            tasksViewModel.uploadTasks(new String(byteData));
                        } else {
                            Toast.makeText(requireActivity(), "Некорректный формат файла", Toast.LENGTH_LONG).show();                        }

                    }
                });


        binding.upload.setOnClickListener( view1 -> {
            if (!PermissionUtils.hasPermissions(requireActivity()))  {
                PermissionUtils.requestPermissions(requireActivity(), PERMISSION_STORAGE);
            }
            if (PermissionUtils.hasPermissions(requireActivity()))  {
                ChooseFile();
            }
        });

        binding.save.setOnClickListener(view12 -> {
            if (!PermissionUtils.hasPermissions(requireActivity()))  {
                PermissionUtils.requestPermissions(requireActivity(), PERMISSION_STORAGE);
            }
            if (PermissionUtils.hasPermissions(requireActivity()))  {
                tasksViewModel.saveTasks();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void ChooseFile() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            fileIntent.setType("text/*");
            filePicker.launch(fileIntent);
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("Recycle")
    byte[] getBytes(Context context, Uri uri) {
        InputStream inputStream;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (Exception ex) {
            Toast.makeText(context, "getBytes error:" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

}