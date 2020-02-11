package com.app.multichoice.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.multichoice.SelectionDialog;
import com.app.multichoice.filterlist.R;
import com.app.multichoice.model.FriendModel;

import java.util.ArrayList;

public final class FriendModelListAdapter extends FilterListAdapter<FriendModel> {

    private LayoutInflater mInflater;
    private ArrayList<FriendModel> mFriendModelList;
    private SelectionDialog mSelectionDialog;

    /***
     *
     * @param selectionDialog
     * @param FriendModelList
     */
    FriendModelListAdapter(SelectionDialog selectionDialog, ArrayList<FriendModel> FriendModelList) {
        super(FriendModelList);
        this.mFriendModelList = FriendModelList;
        mInflater = selectionDialog.getLayoutInflater();
        mSelectionDialog = selectionDialog;
    }

    /*public ArrayList<FriendModel> getFriendModelList() {
        return mFriendModelList;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listrow_friend, parent, false);

            holder = new ViewHolder();
            holder.FriendModelTv = (TextView) convertView.findViewById(R.id.tv_friend_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FriendModel FriendModel = getItem(position);
        holder.FriendModelTv.setText(FriendModel.getFriend());
        holder.checkBox.setChecked(FriendModel.isSelected());
        holder.checkBox.setTag(position);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int position = (Integer) v.getTag();
                FriendModel FriendModel =  getItem(position);
                FriendModel.setSelected(cb.isChecked());
                notifyDataSetChanged();

                mSelectionDialog.checkForSelectAllCb(mFriendModelList);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public TextView FriendModelTv;
        public CheckBox checkBox;
    }
}
