package com.android.mvvmjava.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.mvvmjava.service.model.Project;
import com.android.mvvmjava.service.repository.GitHubClient;

public class ProjectDetailViewModel extends AndroidViewModel {

    private LiveData<Project> projectLiveDataObs;

    ProjectDetailViewModel(@NonNull Application application, String projectId) {
        super(application);
        projectLiveDataObs = GitHubClient.getInstance().getProjectDetail("google", projectId);
    }

    public static class ProjectDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private @NonNull
        Application application;
        private String projectId;

        public ProjectDetailViewModelFactory(@NonNull Application application, String projectId) {
            this.application = application;
            this.projectId = projectId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ProjectDetailViewModel(application, projectId);
        }
    }

    public LiveData<Project> getProjectLiveDataObs() {
        return projectLiveDataObs;
    }
}
