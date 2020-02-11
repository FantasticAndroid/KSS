package com.app.multichoice.model;

public class FriendModel implements Comparable<FriendModel> {

    private String mFriend;
    private boolean isSelected;

    /****
     *
     * @param friend
     * @param isSelected
     */
    public FriendModel(String friend, boolean isSelected) {
        this.mFriend = friend;
        this.isSelected = isSelected;
    }

    public String getFriend() {
        return mFriend;
    }

    public void setFriend(String Friend) {
        this.mFriend = Friend;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return mFriend;
    }

    @Override
    public int compareTo(FriendModel another) {
        return this.mFriend.compareTo(another.getFriend());
    }
}
