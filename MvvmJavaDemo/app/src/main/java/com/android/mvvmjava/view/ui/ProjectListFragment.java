package com.android.mvvmjava.view.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mvvmjava.R;
import com.android.mvvmjava.databinding.FragmentProjectListBinding;
import com.android.mvvmjava.service.model.Project;
import com.android.mvvmjava.view.adapters.ProjectListAdapter;
import com.android.mvvmjava.view.callbacks.OnProjectSelectionCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectListFragment extends Fragment {

    public static final String TAG = ProjectListFragment.class.getSimpleName();
    private FragmentProjectListBinding binding;
    private ProjectListAdapter projectListAdapter;
    private Context context;

    public ProjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setIsLoading(true);
        projectListAdapter = new ProjectListAdapter(context, new OnProjectSelectionCallback() {
            @Override
            public void onProjectSelected(Project project) {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    showProjectDetailFragment();
                }
            }
        });
        binding.projectList.setAdapter(projectListAdapter);
    }

    private void showProjectDetailFragment() {
        /*ProjectFragment projectFragment = ProjectFragment.forProject(project.name);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("project")
                .replace(R.id.fragment_container,
                        projectFragment, null).commit();*/
    }
}
