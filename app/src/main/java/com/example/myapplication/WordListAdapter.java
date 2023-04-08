package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.myapplication.data.model.TaskData;
import com.example.myapplication.databinding.ItemTaskBinding;

public class WordListAdapter extends ListAdapter<TaskData, WordViewHolder> {

    public WordListAdapter(@NonNull DiffUtil.ItemCallback<TaskData> diffCallback) {
        super(diffCallback);
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTaskBinding binding = ItemTaskBinding.inflate(inflater, parent, false);
        return new WordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class WordDiff extends DiffUtil.ItemCallback<TaskData> {

        @Override
        public boolean areItemsTheSame(@NonNull TaskData oldItem, @NonNull TaskData newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskData oldItem, @NonNull TaskData newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
