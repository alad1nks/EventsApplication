package com.example.myapplication.ui.tasks;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemTaskBinding;
import com.example.myapplication.ui.model.TaskUi;

import kotlin.Unit;

public class TasksAdapter extends ListAdapter<TaskUi, TasksAdapter.TasksViewHolder> {

    private final Activity activity;
    public TasksAdapter(Activity activity) {
        super(DiffCallBack);
        this.activity = activity;
    }


    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTaskBinding binding = ItemTaskBinding.inflate(inflater, parent, false);
        return new TasksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        String name = getItem(position).getName();
        String description = getItem(position).getDescription();
        holder.bind(getItem(position));
        holder.binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("description", description);
                Navigation
                        .findNavController(activity, R.id.nav_host_fragment_activity_main)
                        .navigate(
                                R.id.action_navigation_main_to_navigation_add_task,
                                bundle,
                                new NavOptions.Builder()
                                        .setEnterAnim(androidx.appcompat.R.anim.abc_slide_in_bottom)
                                        .setExitAnim(androidx.appcompat.R.anim.abc_slide_out_top)
                                        .setPopEnterAnim(androidx.appcompat.R.anim.abc_slide_in_top)
                                        .setPopExitAnim(androidx.appcompat.R.anim.abc_slide_out_bottom)
                                        .build()
                        );
            }
        });
    }


    public static class TasksViewHolder extends RecyclerView.ViewHolder {
        ItemTaskBinding binding;
        public TasksViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TaskUi taskUi) {
            binding.name.setText(taskUi.getName());
            binding.checkbox.setImageResource(taskUi.getTag());
            binding.priority.setImageResource(taskUi.getUrgency());
        }
    }

    public static final DiffUtil.ItemCallback<TaskUi> DiffCallBack =
            new DiffUtil.ItemCallback<TaskUi>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull TaskUi oldUser, @NonNull TaskUi newUser) {
                    return oldUser.getId() == newUser.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull TaskUi oldUser, @NonNull TaskUi newUser) {
                    return oldUser.equals(newUser);
                }
            };

}
