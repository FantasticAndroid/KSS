package com.bhaskar.darwin.ui.sqldemo.provider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bhaskar.darwin.ui.sqldb.SqlQueryListener;
import com.bhaskar.darwin.ui.sqldemo.MainApp;
import com.bhaskar.darwin.ui.sqldemo.sqlimpl.OfferSqlManager;
import com.bhaskar.darwin.ui.sqldemo.sqlimpl.tables.PointSyncTable;

import java.util.ArrayList;

public final class OfferPointsUtils {

    private static final String TAG = OfferPointsUtils.class.getSimpleName();

    /***
     *
     * @param mainApp
     * @param point
     */
    public static void insertOrUpdatePoints(@NonNull MainApp mainApp,
                                            int point) {
        Log.d(TAG, "insertOrUpdatePoints()");

        OfferSqlManager offerSqlManager = OfferSqlManager.getInstance(mainApp);
        offerSqlManager.insertPointInSyncModel("28-11-2019",
                50, point, new SqlQueryListener<Boolean>() {
                    @Override
                    public void onQuerySuccess(@NonNull Boolean response) {
                        Log.d(TAG, "insertOrUpdatePoints onQuerySuccess(): " + response);
                    }

                    @Override
                    public void onQueryFailed(@NonNull String message) {
                        Log.e(TAG, "insertOrUpdatePoints onQueryFailed(): " + message);
                    }
                });
    }

    /**
     * @param mainApp
     */
    public static void insertAppVisited(@NonNull MainApp mainApp) {
        Log.d(TAG, "insertAppVisited()");
        OfferSqlManager offerSqlManager = OfferSqlManager.getInstance(mainApp);

        offerSqlManager.insertAppVisitedStatus("28-11-2019", new SqlQueryListener<String>() {
            @Override
            public void onQuerySuccess(@NonNull String currentDate) {
                Log.d(TAG, "insertAppVisited(): onQuerySuccess: " + currentDate);
            }

            @Override
            public void onQueryFailed(@NonNull String message) {
                Log.e(TAG, "insertAppVisited(): onQueryFailed: " + message);
            }
        });
    }

    /***
     *
     * @param mainApp
     */
    public static void getAllPointsAndSyncToServer(@NonNull MainApp mainApp) {
        Log.d(TAG, "getAllPointsAndSyncToServer()");
        //MegaOfferSharedPref megaOfferSharedPref = MegaOfferSharedPref.getInstance(mainApp);
        OfferSqlManager offerSqlManager = OfferSqlManager.getInstance(mainApp);

        offerSqlManager.getPointsFromSyncTable(new SqlQueryListener<ArrayList<PointSyncTable>>() {
            @Override
            public void onQuerySuccess(@NonNull ArrayList<PointSyncTable> response) {
                try {
                    if (response != null && !response.isEmpty()) {


                    } else {
                        //pointsSyncCallback.onPointsNotAvailable();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getAllPointsAndSyncToServer() " + e.getMessage());
                    //pointsSyncCallback.onSyncFailed(e.getMessage() + "");
                }
            }

            @Override
            public void onQueryFailed(@NonNull String message) {
                //pointsSyncCallback.onSyncFailed(message);
            }
        });
    }


    /***
     *
     * @param mainApp
     * @param megaOfferTodayDate
     */
    static void deleteAllSyncedPoints(@NonNull MainApp mainApp, @NonNull String megaOfferTodayDate) {
        try {
            OfferSqlManager offerSqlManager = OfferSqlManager.getInstance(mainApp);
            offerSqlManager.deleteRowsNotHavingCurrentDate(megaOfferTodayDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
