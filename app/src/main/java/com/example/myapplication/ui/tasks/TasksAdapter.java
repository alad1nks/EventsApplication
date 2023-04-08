package com.example.myapplication.ui.tasks;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemTaskBinding;
import com.example.myapplication.ui.model.TaskUi;

public class TasksAdapter extends ListAdapter<TaskUi, TasksAdapter.TasksViewHolder> {
    public TasksAdapter() {
        super(DiffCallBack);
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
        holder.bind(getItem(position));
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
