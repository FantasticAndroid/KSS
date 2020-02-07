package com.android.mvvmjava.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mvvmjava.R;
import com.android.mvvmjava.databinding.ItemProjectListRvBinding;
import com.android.mvvmjava.service.model.Project;
import com.android.mvvmjava.view.callbacks.OnProjectSelectionCallback;

import java.util.List;
import java.util.Objects;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectListViewHolder> {

    private LayoutInflater layoutInflater;
    private List<? extends Project> projectList;
    private OnProjectSelectionCallback callback;

    public ProjectListAdapter(Context context, OnProjectSelectionCallback callback){
        layoutInflater = LayoutInflater.from(context);
        this.callback = callback;
    }

    @NonNull
    @Override
    public ProjectListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectListViewHolder
                (DataBindingUtil.inflate(layoutInflater, R.layout.item_project_list_rv,parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListViewHolder holder, int position) {

        ItemProjectListRvBinding binding = holder.binding;
        binding.setSelectionCallback(callback);
        binding.setProject(projectList.get(position));
        binding.executePendingBindings();
    }

    /***
     *
     * @param projectList
     */
    public void setProjectList(final List<? extends Project> projectList) {
        if (this.projectList == null) {
            this.projectList = projectList;
            notifyItemRangeInserted(0, projectList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ProjectListAdapter.this.projectList.size();
                }

                @Override
                public int getNewListSize() {
                    return projectList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ProjectListAdapter.this.projectList.get(oldItemPosition).id ==
                            projectList.get(newItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Project project = projectList.get(newItemPosition);
                    Project old = projectList.get(oldItemPosition);
                    return project.id == old.id
                            && Objects.equals(project.git_url, old.git_url);
                }
            });
            this.projectList = projectList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return projectList == null ? 0 : projectList.size();
    }

    static class ProjectListViewHolder extends RecyclerView.ViewHolder {

        ItemProjectListRvBinding binding;
        private ProjectListViewHolder(ItemProjectListRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
