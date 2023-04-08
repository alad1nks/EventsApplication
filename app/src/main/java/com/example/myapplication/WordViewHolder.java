package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.model.TaskData;
import com.example.myapplication.databinding.ItemTaskBinding;
import com.example.myapplication.ui.model.TaskUi;

class WordViewHolder extends RecyclerView.ViewHolder {
    ItemTaskBinding binding;
    public WordViewHolder(@NonNull ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(TaskData taskUi) {
        binding.name.setText(taskUi.getName());
    }
}
