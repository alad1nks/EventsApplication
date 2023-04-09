package com.example.myapplication.ui.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTasksBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class TasksFragment extends Fragment {

    private FragmentTasksBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TasksViewModel tasksViewModel;
    private BroadcastReceiver broadcastReceiver;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TasksStateAdapter stateAdapter = new TasksStateAdapter(getChildFragmentManager(), getLifecycle());
        viewPager = binding.viewPager;
        viewPager.setAdapter(stateAdapter);
        tabLayout = binding.tabs;
        tabLayout.addTab(tabLayout.newTab().setText("Учебные"));
        tabLayout.addTab(tabLayout.newTab().setText("Рабочие"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        binding.addTaskButton.setOnClickListener(
                view1 -> Navigation
                        .findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(
                                R.id.action_navigation_main_to_navigation_add_task,
                                null,
                                new NavOptions.Builder()
                                        .setEnterAnim(androidx.appcompat.R.anim.abc_slide_in_bottom)
                                        .setExitAnim(androidx.appcompat.R.anim.abc_slide_out_top)
                                        .setPopEnterAnim(androidx.appcompat.R.anim.abc_slide_in_top)
                                        .setPopExitAnim(androidx.appcompat.R.anim.abc_slide_out_bottom)
                                        .build()
                        )
        );
        startReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        requireActivity().unregisterReceiver(broadcastReceiver);
    }

    public void startReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tasksViewModel.refresh();
            }
        };
        requireActivity().registerReceiver(broadcastReceiver, intentFilter);
    }
}