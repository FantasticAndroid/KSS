package com.bhaskar.darwin.ui.sqldemo.sqlimpl;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bhaskar.darwin.ui.sqldb.SqlQueryListener;
import com.bhaskar.darwin.ui.sqldemo.MainApp;
import com.bhaskar.darwin.ui.sqldemo.sqlimpl.dao.PointSyncTableDao;
import com.bhaskar.darwin.ui.sqldemo.sqlimpl.tables.PointSyncTable;

import java.util.ArrayList;

public class OfferSqlManager {

    private MainApp mainApp;

    private final String TAG = OfferSqlManager.class.getSimpleName();

    private static OfferSqlManager offerSqlManager;

    /***
     *
     * @param context
     * @return
     */
    public static OfferSqlManager getInstance(@NonNull MainApp context) {
        if (offerSqlManager == null) {
            offerSqlManager = new OfferSqlManager(context);
        }
        return offerSqlManager;
    }

    /**
     * @param context
     */
    private OfferSqlManager(@NonNull MainApp context) {
        this.mainApp = context;
    }


    /***
     *
     * @param currentDate
     * @param maxPointsCount
     * @param point
     * @param sqlQueryListener
     */
    public synchronized void insertPointInSyncModel(@NonNull String currentDate, int maxPointsCount,
                                                    int point, @Nullable SqlQueryListener<Boolean> sqlQueryListener) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        if (offerSqlAdapter != null) {
            Runnable runnable = () -> {
                try {
                    PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
                  long id =  pointSyncTableDao.insertOrUpdatePoint(offerSqlAdapter.getSqliteDatabase(), currentDate, maxPointsCount, point);

                    if (sqlQueryListener != null) {
                        sqlQueryListener.onQuerySuccess(true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "insertPointInSyncModel() " + e.getMessage());
                    if (sqlQueryListener != null) {
                        sqlQueryListener.onQueryFailed(e.getMessage() + "");
                    }
                }
            };
            new Thread(runnable).start();
        }
    }

    /***
     *
     * @param currentDate
     * @param sqlQueryListener
     */
    public synchronized void insertAppVisitedStatus(@NonNull String currentDate, @Nullable SqlQueryListener<String> sqlQueryListener) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        if (offerSqlAdapter != null) {
            Runnable runnable = () -> {
                try {
                    PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
                    long rowId = pointSyncTableDao.insertAppVisited(offerSqlAdapter.getSqliteDatabase(), currentDate);

                    if (sqlQueryListener != null) {
                        if (rowId != -1) {
                            sqlQueryListener.onQuerySuccess(currentDate);
                        } else {
                            sqlQueryListener.onQueryFailed(rowId + "");
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "insertPointInSyncModel() " + e.getMessage());
                    if (sqlQueryListener != null) {
                        sqlQueryListener.onQueryFailed(e.getMessage() + "");
                    }
                }
            };
            new Thread(runnable).start();
        }
    }


    /***
     *
     * @param sqlQueryListener
     */
    public synchronized void getPointsFromSyncTable(@NonNull SqlQueryListener<ArrayList<PointSyncTable>> sqlQueryListener) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        if (offerSqlAdapter != null) {
            Runnable runnable = () -> {
                try {
                    PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
                    ArrayList<PointSyncTable> pointsSyncTableArrayList = pointSyncTableDao.getAllPointsFromTable
                            (offerSqlAdapter.getSqliteDatabase());

                    sqlQueryListener.onQuerySuccess(pointsSyncTableArrayList);
                } catch (Exception e) {
                    Log.e("getAllCoinsOptedEntries", e.getMessage() + "");
                    sqlQueryListener.onQueryFailed(e.getMessage() + "");
                }
            };
            new Thread(runnable).start();
        }
    }

    /***
     *
     * @param currentDate
     */
    public void deleteRowsNotHavingCurrentDate(String currentDate) throws Exception {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
        pointSyncTableDao.deleteRowsNotHavingCurrentDate(offerSqlAdapter.getSqliteDatabase(), currentDate);
    }

    /***
     *
     * @param maxPointCount
     */
    public void deleteAllSyncedPointsRow(int maxPointCount) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
        pointSyncTableDao.deleteAllSyncedPointsRow(offerSqlAdapter.getSqliteDatabase(), maxPointCount);
    }
}
