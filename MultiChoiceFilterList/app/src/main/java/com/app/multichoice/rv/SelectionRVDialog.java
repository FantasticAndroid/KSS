package com.app.multichoice.rv;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.multichoice.SelectionDialog;
import com.app.multichoice.filterlist.R;
import com.app.multichoice.model.FriendModel;

import java.util.ArrayList;
import java.util.Iterator;

public final class SelectionRVDialog extends SelectionDialog {

    private RecyclerView mRecyclerView;
    private FriendModelRecycleAdapter mFriendModelAdapter;

    /***
     * @param activity
     * @param FriendModelList
     * @param FriendModelsNameBtn
     */
    public SelectionRVDialog(Activity activity, ArrayList<FriendModel> FriendModelList,
                             Button FriendModelsNameBtn) {
        super(activity, FriendModelList, FriendModelsNameBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_seletion_rv);
        super.onCreate(savedInstanceState);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_friends);
        /////mRecyclerView.setTextFilterEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setAdapter();
    }

    @Override
    protected void setAdapter() {
        mFriendModelAdapter = new FriendModelRecycleAdapter(this, mFriendModelList);
        mRecyclerView.setAdapter(mFriendModelAdapter);
    }

    @Override
    protected void addFriendsOnUI() {
        Iterator<FriendModel> FriendModelIterator = mFriendModelAdapter.getObjectsList().iterator();
        addFriendModelsNameOnUI(FriendModelIterator);
    }

    @Override
    protected void applyFilter(Editable searchText) {
        mFriendModelAdapter.getFilter().filter(searchText);
        applyListFilter(searchText.toString());
    }
}
