package com.example.myapplication.ui.tasks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTasksBinding;
import com.google.android.material.tabs.TabLayout;


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
        Bundle bundle = new Bundle();
        bundle.putString("name", "");
        bundle.putString("description", "");
        binding.addTaskButton.setOnClickListener(
                view1 -> Navigation
                        .findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(
                                R.id.action_navigation_main_to_navigation_add_task,
                                bundle,
                                new NavOptions.Builder()
                                        .setEnterAnim(androidx.appcompat.R.anim.abc_slide_in_bottom)
                                        .setExitAnim(androidx.appcompat.R.anim.abc_slide_out_top)
                                        .setPopEnterAnim(androidx.appcompat.R.anim.abc_slide_in_top)
                                        .setPopExitAnim(androidx.appcompat.R.anim.abc_slide_out_bottom)
                                        .build()
                        )
        );
        startReceiver();


        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(requireActivity().getApplicationContext(), "notify_001");
        Intent ii = new Intent(requireActivity().getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(), 0, ii, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle("Важно!");
        bigText.setSummaryText("Новое событие!");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Новое событие");
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel_id";
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(channel);
        mBuilder.setChannelId(channelId);

        tasksViewModel.getNotification().observe(getViewLifecycleOwner(), notification -> {
            if (notification != null) {
                mBuilder.setContentText("Через 5 минут начнётся " + notification.first);
                mNotificationManager.notify(notification.second, mBuilder.build());
            }
            tasksViewModel.clearNotification();
        });

        binding.calendarButton.setOnClickListener(
                view1 -> Navigation
                        .findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(
                                R.id.action_navigation_main_to_navigation_calendar_filters,
                                bundle,
                                new NavOptions.Builder()
                                        .setEnterAnim(androidx.appcompat.R.anim.abc_slide_in_bottom)
                                        .setExitAnim(androidx.appcompat.R.anim.abc_slide_out_top)
                                        .setPopEnterAnim(androidx.appcompat.R.anim.abc_slide_in_top)
                                        .setPopExitAnim(androidx.appcompat.R.anim.abc_slide_out_bottom)
                                        .build()
                        )
        );

        binding.filterButton.setOnClickListener(
                view1 -> Navigation
                        .findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(
                                R.id.action_navigation_main_to_navigation_filters,
                                bundle,
                                new NavOptions.Builder()
                                        .setEnterAnim(androidx.appcompat.R.anim.abc_slide_in_bottom)
                                        .setExitAnim(androidx.appcompat.R.anim.abc_slide_out_top)
                                        .setPopEnterAnim(androidx.appcompat.R.anim.abc_slide_in_top)
                                        .setPopExitAnim(androidx.appcompat.R.anim.abc_slide_out_bottom)
                                        .build()
                        )
        );
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