package com.app.multichoice;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.multichoice.filterlist.R;
import com.app.multichoice.list.SelectionListDialog;
import com.app.multichoice.model.FriendModel;
import com.app.multichoice.rv.SelectionRVDialog;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<FriendModel> mFriendModelList;
    private SelectionDialog mSelectionDialog;
    private Button textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectFriendModelsFromList();
            }
        });

        mFriendModelList = new ArrayList<>();

        mFriendModelList.add(new FriendModel("saurabh jain", false));
        mFriendModelList.add(new FriendModel("Rakesh", false));
        mFriendModelList.add(new FriendModel("ankit", false));
        mFriendModelList.add(new FriendModel("arun", false));
        mFriendModelList.add(new FriendModel("ambesh", false));
        mFriendModelList.add(new FriendModel("bhupendra", false));
        mFriendModelList.add(new FriendModel("krishnakant", false));
        mFriendModelList.add(new FriendModel("saurabh", false));
        mFriendModelList.add(new FriendModel("Rakesh Sen", false));
        mFriendModelList.add(new FriendModel("amit", false));
        mFriendModelList.add(new FriendModel("aruna", false));
        mFriendModelList.add(new FriendModel("amba", false));
        mFriendModelList.add(new FriendModel("krishn", false));
        mFriendModelList.add(new FriendModel("Shivkant", false));
        mFriendModelList.add(new FriendModel("Nitish", false));
        mFriendModelList.add(new FriendModel("Rishi", false));
        mFriendModelList.add(new FriendModel("Ashish", false));
        mFriendModelList.add(new FriendModel("Aziz", false));
        mFriendModelList.add(new FriendModel("gupta", false));
        mFriendModelList.add(new FriendModel("mishra", false));
        mFriendModelList.add(new FriendModel("vikram", false));
    }

    private void selectFriendModelsFromList() {

        if (mSelectionDialog == null) {

            Bundle bundle = getIntent().getExtras();
            int pos = bundle.getInt("choice");
            if (pos == 0) {
                mSelectionDialog = new SelectionListDialog(MainActivity.this,
                        mFriendModelList, textView);
            } else {
                mSelectionDialog = new SelectionRVDialog(MainActivity.this,
                        mFriendModelList, textView);
            }
        }
        mSelectionDialog.show();
    }
}
