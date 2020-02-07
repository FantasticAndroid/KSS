package com.app.aidl_lib_server;

import android.os.Parcel;
import android.os.Parcelable;

public final class MyRect implements Parcelable {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public static final Parcelable.Creator<MyRect> CREATOR = new Parcelable.Creator<MyRect>() {

        public MyRect createFromParcel(Parcel in) {
            return new MyRect(in);
        }


        public MyRect[] newArray(int size) {
            return new MyRect[size];
        }
    };

    public MyRect() {
    }

    private MyRect(Parcel in) {
        readFromParcel(in);
    }

    /***
     *
     * @param top
     * @param left
     * @param right
     * @param bottom
     */
    public MyRect(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public void readFromParcel(Parcel in) {
        left = in.readInt();
        top = in.readInt();
        right = in.readInt();
        bottom = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(left);
        out.writeInt(top);
        out.writeInt(right);
        out.writeInt(bottom);
    }

    @Override
    public String toString() {
        return "MyRect{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }
}