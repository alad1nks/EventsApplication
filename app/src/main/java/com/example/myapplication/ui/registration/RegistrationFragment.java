package com.example.myapplication.ui.registration;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRegistrationBinding;
import com.example.myapplication.preferences.AppPreference;
import com.example.myapplication.preferences.AppPreferenceImpl;

public class RegistrationFragment extends Fragment {
    private FragmentRegistrationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppPreference sharedPreferences = new AppPreferenceImpl(requireContext());
        if (sharedPreferences.loggedIn()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_registration_to_navigation_main);
        }
        binding.button.setOnClickListener(
                view1 -> {
                    if (isValidEmail(binding.email.getText()) && !binding.editSurname.getText().toString().isEmpty() && !binding.editName.getText().toString().isEmpty()) {
                        sharedPreferences.logIn();
                        Navigation.findNavController(view1).navigate(R.id.action_navigation_registration_to_navigation_main);
                    }
                }
        );
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
