package com.android.mvvmjava.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.mvvmjava.R;
import com.android.mvvmjava.databinding.FragmentProjectDetailBinding;
import com.android.mvvmjava.service.model.Project;
import com.android.mvvmjava.view.ui.base.CoreFragment;
import com.android.mvvmjava.viewmodel.ProjectDetailViewModel;

public final class ProjectDetailFragment extends CoreFragment {

    FragmentProjectDetailBinding fragmentProjectDetailBinding;

    static ProjectDetailFragment getInstance(String projectId) {
        ProjectDetailFragment projectDetailFragment = new ProjectDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("projectId", projectId);
        projectDetailFragment.setArguments(bundle);
        return projectDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentProjectDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_detail, container, false);
        return fragmentProjectDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentProjectDetailBinding.setIsLoading(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle!=null){
            ProjectDetailViewModel.ProjectDetailViewModelFactory factory =
                    new ProjectDetailViewModel.ProjectDetailViewModelFactory(mvvmApp,bundle.getString("projectId"));

            ProjectDetailViewModel projectDetailViewModel = factory.create(ProjectDetailViewModel.class);
            observeViewModel(projectDetailViewModel);
        }
    }

    private void observeViewModel(ProjectDetailViewModel projectDetailViewModel) {

        projectDetailViewModel.getProjectLiveDataObs().observe(getViewLifecycleOwner(), new Observer<Project>() {
            @Override
            public void onChanged(Project project) {
                fragmentProjectDetailBinding.setIsLoading(false);
                fragmentProjectDetailBinding.setProject(project);
            }
        });
    }
}
