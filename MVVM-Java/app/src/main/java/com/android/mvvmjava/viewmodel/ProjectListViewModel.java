package com.android.mvvmjava.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.mvvmjava.service.model.Project;
import com.android.mvvmjava.service.repository.GitHubClient;

import java.util.List;

public class ProjectListViewModel extends AndroidViewModel {

    private final LiveData<List<Project>> projectListObs;

    public ProjectListViewModel(@NonNull Application application) {
        super(application);
        projectListObs = GitHubClient.getInstance().getProjectList("google");
    }

    public LiveData<List<Project>> getProjectListObs() {
        return projectListObs;
    }
}
