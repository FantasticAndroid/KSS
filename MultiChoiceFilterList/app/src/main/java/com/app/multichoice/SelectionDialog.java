package com.app.multichoice;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatDialog;

import com.app.multichoice.filterlist.R;
import com.app.multichoice.model.FriendModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class SelectionDialog extends AppCompatDialog {

    private CheckBox mSelectAllCb;
    private Button mFriendModelTv;
    private EditText searchEt;
    private ImageButton removeFriendModelNameBtn;
    protected ArrayList<FriendModel> mFriendModelList;

    protected abstract void setAdapter();

    protected abstract void addFriendsOnUI();

    protected abstract void applyFilter(Editable searchText);

    /***
     * @param FriendModelsList
     */
    public void checkForSelectAllCb(ArrayList<FriendModel> FriendModelsList) {
        boolean isSelectAll = true;

        for (FriendModel FriendModel : FriendModelsList) {
            if (!FriendModel.isSelected()) {
                isSelectAll = false;
                break;
            }
        }
        mSelectAllCb.setChecked(isSelectAll);
    }

    /***
     * @param activity
     * @param FriendModelList
     * @param FriendModelsNameBtn
     */
    public SelectionDialog(Activity activity, ArrayList<FriendModel> FriendModelList,
                           Button FriendModelsNameBtn) {
        super(activity, android.R.style.Theme_Translucent_NoTitleBar);
        this.mFriendModelList = FriendModelList;
        this.mFriendModelTv = FriendModelsNameBtn;
        Collections.sort(FriendModelList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectAllCb = (CheckBox) findViewById(R.id.cb_select_all);
        searchEt = (EditText) findViewById(R.id.et_search_friend_box);
        removeFriendModelNameBtn = (ImageButton) findViewById(R.id.ibtn_remove_search_box);
        removeFriendModelNameBtn.setVisibility(View.INVISIBLE);
        removeFriendModelNameBtn.setOnClickListener(clickListener);
        mSelectAllCb.setOnClickListener(clickListener);
        findViewById(R.id.btn_add_friends).setOnClickListener(clickListener);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable searchText) {
                applyFilter(searchText);
            }
        });
    }

    /**
     * @param searchText
     */
    protected void applyListFilter(final String searchText) {

        if (searchText.length() > 0) {
            removeFriendModelNameBtn.setVisibility(View.VISIBLE);
        } else {
            removeFriendModelNameBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @param isChecked
     */
    private void setFriendModelsListState(boolean isChecked) {

        searchEt.setText("");

        for (int index = 0; index < mFriendModelList.size(); index++) {
            FriendModel FriendModel = mFriendModelList.get(index);
            FriendModel.setSelected(isChecked);
            mFriendModelList.set(index, FriendModel);
        }
        setAdapter();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_remove_search_box:
                    searchEt.setText("");
                    v.setVisibility(View.INVISIBLE);
                    break;
                case R.id.cb_select_all:
                    setFriendModelsListState(((CheckBox) v).isChecked());
                    break;
                case R.id.btn_add_friends:
                    addFriendsOnUI();
                    dismiss();
                    break;
            }
        }
    };

    protected void addFriendModelsNameOnUI(Iterator<FriendModel> mFriendModelIterator) {

        StringBuilder sb = new StringBuilder();
        while (mFriendModelIterator.hasNext()) {
            FriendModel FriendModel = mFriendModelIterator.next();
            if (FriendModel.isSelected()) {
                sb.append(FriendModel.getFriend()).append(", ");
            }
        }
        int lastCommaIndex = sb.lastIndexOf(", ");
        if (lastCommaIndex != -1) {
            sb.deleteCharAt(lastCommaIndex);
        }
        mFriendModelTv.setText(sb.toString().trim());
    }
}
