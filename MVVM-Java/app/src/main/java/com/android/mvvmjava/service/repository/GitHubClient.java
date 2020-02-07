package com.android.mvvmjava.service.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.mvvmjava.service.model.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubClient {

    public static GitHubClient gitHubClient;
    private GitHubService gitHubService;

    private GitHubClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GitHubService.HTTPS_API_GITHUB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubService = retrofit.create(GitHubService.class);
    }

    public synchronized static GitHubClient getInstance() {
        //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
        if (gitHubClient == null) {
            gitHubClient = new GitHubClient();
        }
        return gitHubClient;
    }

    public LiveData<List<Project>> getProjectList(String userId) {
        final MutableLiveData<List<Project>> mutableData = new MutableLiveData<>();
        gitHubService.getProjectList(userId).enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                mutableData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.e("getProjectList", t.getMessage() + "onFailure");
            }
        });
        return mutableData;
    }

    public LiveData<Project> getProjectDetail(String userId, String projectId) {
        final MutableLiveData<Project> mutableLiveData = new MutableLiveData<>();
        gitHubService.getProjectDetails(userId,projectId).enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                Log.e("getProjectDetail", t.getMessage() + "onFailure");
            }
        });
        return mutableLiveData;
    }
}
