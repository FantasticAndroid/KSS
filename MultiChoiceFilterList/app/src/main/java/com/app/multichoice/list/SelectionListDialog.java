package com.app.multichoice.list;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.ListView;

import com.app.multichoice.SelectionDialog;
import com.app.multichoice.filterlist.R;
import com.app.multichoice.model.FriendModel;

import java.util.ArrayList;
import java.util.Iterator;

public final class SelectionListDialog extends SelectionDialog {

    private ListView listView;
    private FriendModelListAdapter mFriendBeanAdapter;

    /***
     * @param activity
     * @param friendBeanList
     * @param friendBeansNameBtn
     */
    public SelectionListDialog(Activity activity, ArrayList<FriendModel> friendBeanList,
                               Button friendBeansNameBtn) {
        super(activity, friendBeanList, friendBeansNameBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_seletion_list);
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.listview_friends);
        listView.setTextFilterEnabled(true);
        setAdapter();
    }

    @Override
    protected void setAdapter() {
        mFriendBeanAdapter = new FriendModelListAdapter(this, mFriendModelList);
        listView.setAdapter(mFriendBeanAdapter);
    }

    @Override
    protected void addFriendsOnUI() {
        Iterator<FriendModel> friendBeanIterator = mFriendBeanAdapter.getObjectsList().iterator();
        addFriendModelsNameOnUI(friendBeanIterator);
    }

    @Override
    protected void applyFilter(Editable searchText) {
        mFriendBeanAdapter.getFilter().filter(searchText);
        applyListFilter(searchText.toString());
    }
}
