package com.app.multichoice.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.multichoice.SelectionDialog;
import com.app.multichoice.filterlist.R;
import com.app.multichoice.model.FriendModel;

import java.util.ArrayList;

public final class FriendModelRecycleAdapter extends FilterRecycleAdapter<FriendModel> {

    private LayoutInflater mInflater;
    private ArrayList<FriendModel> mFriendModelList;
    private SelectionDialog mSelectionDialog;

    /***
     * @param selectionDialog
     * @param FriendModelList
     */
    public FriendModelRecycleAdapter(SelectionDialog selectionDialog, ArrayList<FriendModel> FriendModelList) {
        super(FriendModelList);
        this.mFriendModelList = FriendModelList;
        mInflater = selectionDialog.getLayoutInflater();
        mSelectionDialog = selectionDialog;
    }

    /*public ArrayList<FriendModel> getFriendModelList() {
        return mFriendModelList;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View convertView = mInflater.inflate(R.layout.listrow_friend, viewGroup, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        FriendModel FriendModel = getItem(position);
        holder.FriendModelTv.setText(FriendModel.getFriend());
        holder.checkBox.setChecked(FriendModel.isSelected());
        holder.checkBox.setTag(position);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int position = (Integer) v.getTag();
                FriendModel FriendModel = getItem(position);
                FriendModel.setSelected(cb.isChecked());
                notifyDataSetChanged();

                mSelectionDialog.checkForSelectAllCb(mFriendModelList);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public TextView FriendModelTv;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            FriendModelTv = (TextView) itemView.findViewById(R.id.tv_friend_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
