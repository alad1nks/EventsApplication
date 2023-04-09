package com.example.myapplication.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.preferences.AppPreference;
import com.example.myapplication.preferences.AppPreferenceImpl;


public class NotificationsFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppPreference sharedPreferences = new AppPreferenceImpl(requireContext());
        binding.userName.setText(sharedPreferences.getName());
        binding.userSurname.setText(sharedPreferences.getSurname());
        binding.userUniversity.setText(sharedPreferences.getUniversity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}