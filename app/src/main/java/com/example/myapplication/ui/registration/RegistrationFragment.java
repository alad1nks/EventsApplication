package com.example.myapplication.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
                    if (!binding.editSurname.getText().toString().isEmpty() && !binding.editName.getText().toString().isEmpty() && !binding.university.getText().toString().isEmpty()) {
                        sharedPreferences.logIn();
                        sharedPreferences.saveName(binding.editName.getText().toString());
                        sharedPreferences.saveSurname(binding.editSurname.getText().toString());
                        sharedPreferences.saveUniversity(binding.university.getText().toString());
                        Navigation.findNavController(view1).navigate(R.id.action_navigation_registration_to_navigation_main);
                    } else if (binding.editSurname.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Введите фамилию", Toast.LENGTH_LONG).show();
                    } else if (binding.editName.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Введите имя", Toast.LENGTH_LONG).show();
                    } else if (binding.university.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Введите образовательное учреждение", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
