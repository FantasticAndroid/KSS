package com.android.mvvmjava.view.ui;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.mvvmjava.R;
import com.android.mvvmjava.databinding.FragmentProjectListBinding;
import com.android.mvvmjava.service.model.Project;
import com.android.mvvmjava.view.adapters.ProjectListAdapter;
import com.android.mvvmjava.view.callbacks.OnProjectSelectionCallback;
import com.android.mvvmjava.view.ui.base.CoreFragment;
import com.android.mvvmjava.viewmodel.ProjectListViewModel;

import java.util.List;

/***
 *
 */
public class ProjectListFragment extends CoreFragment {

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
                    showProjectDetailFragment(project);
                }
            }
        });
        binding.projectList.setAdapter(projectListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProjectListViewModel projectListViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(mvvmApp).create(ProjectListViewModel.class);
        observeViewModel(projectListViewModel);
    }

    private void observeViewModel(ProjectListViewModel projectListViewModel) {
        projectListViewModel.getProjectListObs().observe(getViewLifecycleOwner(),
                new Observer<List<Project>>() {
                    @Override
                    public void onChanged(List<Project> projects) {
                        if (projects != null) {
                            binding.setIsLoading(false);
                            projectListAdapter.setProjectList(projects);
                        }
                    }
                });
    }

    private void showProjectDetailFragment(Project project) {
        Log.d(TAG, "showProjectDetailFragment()");
        ProjectDetailFragment projectFragment = ProjectDetailFragment.getInstance(project.name);

        coreActivity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("project")
                .replace(R.id.fragmentContainer,
                        projectFragment, null).commit();
    }
}
