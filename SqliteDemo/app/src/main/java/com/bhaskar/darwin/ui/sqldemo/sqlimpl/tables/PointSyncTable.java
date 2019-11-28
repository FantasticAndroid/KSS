package com.bhaskar.darwin.ui.sqldemo.sqlimpl.tables;

import androidx.annotation.NonNull;

import com.bhaskar.darwin.ui.sqldb.SqlTypeTable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointSyncTable implements SqlTypeTable {

    @SerializedName("points")
    @Expose
    private int points;

    @SerializedName("optedDate")
    @Expose
    private String optedDate; // in yyyy-MM-dd

    @SerializedName("isAppVisited")
    @Expose
    private boolean isAppVisited = false;

    //private boolean syncStatus;

    public PointSyncTable() {
    }

    /**
     * @param optedDate
     */
    public PointSyncTable(@NonNull String optedDate) {
        //this.syncStatus = false;
        this.optedDate = optedDate;
    }

    public int getPoints() {
        return points;
    }

    public String getOptedDate() {
        return optedDate;
    }

    /***
     *
     * @param point
     */
    public void updatePoints(int point) {
        this.points = points + point;
    }

    public void setAppVisited() {
        isAppVisited = true;
    }

    @Override
    public @NonNull
    String getPrimaryKeyColumn() {
        return "optedDate";
    }

    /*public void setOptedDate(String optedDate) {
        this.optedDate = optedDate;
    }*/

    /*public boolean isSynced() {
        return syncStatus;
    }*/

    @Override
    public String toString() {
        return "PointSyncTable{" +
                "points=" + points +
                ", optedDate='" + optedDate + '\'' +
                ", isAppVisited=" + isAppVisited +
                '}';
    }
}
