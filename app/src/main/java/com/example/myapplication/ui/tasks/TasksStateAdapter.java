package com.example.myapplication.ui.tasks;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TasksStateAdapter extends FragmentStateAdapter {

    public TasksStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public TasksStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public TasksStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new EduTasksFragment();
        } else {
            return new WorkTasksFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
