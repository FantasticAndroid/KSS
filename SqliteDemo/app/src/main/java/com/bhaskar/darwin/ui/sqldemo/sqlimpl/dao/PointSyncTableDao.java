package com.bhaskar.darwin.ui.sqldemo.sqlimpl.dao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bhaskar.darwin.ui.sqldb.BaseTableDao;
import com.bhaskar.darwin.ui.sqldb.SqlTypeTable;
import com.bhaskar.darwin.ui.sqldb.TableDao;
import com.bhaskar.darwin.ui.sqldemo.sqlimpl.tables.PointSyncTable;

import org.json.JSONException;

import java.util.ArrayList;

public final class PointSyncTableDao extends BaseTableDao {
    private static String CL_POINTS = "points";
    private final String TAG = PointSyncTableDao.class.getSimpleName();

    /**
     * To init RunsSyncTableDao (To get tableName )
     */
    public PointSyncTableDao() {
        super(PointSyncTable.class);
    }

    /***
     * classType of Table
     * @param classType
     */
    public PointSyncTableDao(Class<? extends SqlTypeTable> classType) {
        super(classType);
    }

    /***
     *
     * @param sqlDB
     * @param currentDate
     * @param maxPointsCount
     * @param point
     * @return
     * @throws Exception
     */
    public synchronized final long insertOrUpdatePoint(@NonNull SQLiteDatabase sqlDB,
                                                       @NonNull String currentDate,
                                                       int maxPointsCount, int point) throws Exception {

        PointSyncTable pointSyncTable = null;
        try {
            pointSyncTable = getRowWithPrimaryKey(PointSyncTable.class, sqlDB,
                    PointSyncTable.class.newInstance().getPrimaryKeyColumn(), currentDate);
        } catch (Exception e) {
            Log.e(TAG, "insertOrUpdatePoint(): " + e.getMessage() + " : " + pointSyncTable);
        }
        if (pointSyncTable == null) {
            pointSyncTable = new PointSyncTable(currentDate);
            pointSyncTable.updatePoints(point);
            pointSyncTable.setAppVisited();
            return insertOrUpdateRow(sqlDB, pointSyncTable, PointSyncTable.class);
        } else {
            int currentPoints = pointSyncTable.getPoints();
            if (currentPoints < maxPointsCount) {
                pointSyncTable.updatePoints(point);
                pointSyncTable.setAppVisited();
                return insertOrUpdateRow(sqlDB, pointSyncTable, PointSyncTable.class);
            } else {
                return 0L;
            }
        }
    }

    /**
     * @param sqlDB
     * @param currentDate
     * @throws JSONException
     */
    public synchronized final long insertAppVisited(@NonNull SQLiteDatabase sqlDB, @NonNull String currentDate) throws Exception {

        PointSyncTable pointSyncTable = null;
        try {
            PointSyncTable.class.newInstance().getPrimaryKeyColumn();
            pointSyncTable = getRowWithPrimaryKey(PointSyncTable.class, sqlDB,
                    PointSyncTable.class.newInstance().getPrimaryKeyColumn(), currentDate);
        } catch (Exception e) {

        }
        if (pointSyncTable == null) {
            pointSyncTable = new PointSyncTable(currentDate);
        }
        pointSyncTable.setAppVisited();

        return insertOrUpdateRow(sqlDB, pointSyncTable, PointSyncTable.class);
    }


    /**
     * @param sqlDB
     * @return
     * @throws Exception
     */
    public final ArrayList<PointSyncTable> getAllPointsFromTable(SQLiteDatabase sqlDB) throws Exception {
        return TableDao.getAllRecordFromTable(tableName, PointSyncTable.class, sqlDB);//.getDataList();
    }

    /***
     * Call this method after Successful Sync. Do not delete Current Date Row since it can be updated further.
     * Delete all rows those does not match with Current Date, since non-current date rows can not be updated once SYNCED
     * @param sqlDB
     * @param currentDate
     */
    public void deleteRowsNotHavingCurrentDate(SQLiteDatabase sqlDB, @NonNull String currentDate) throws Exception {
        deleteRowsNotHavingColumnKey(sqlDB, PointSyncTable.class.newInstance().getPrimaryKeyColumn(), currentDate);
        Log.d(TAG, "deleteAllSyncedPointsRow():");
    }

    /***
     *
     * @param sqlDB
     * @param maxPointCount
     */
    public void deleteAllSyncedPointsRow(SQLiteDatabase sqlDB, int maxPointCount) {
        String whereClause = "CAST(" + CL_POINTS + "AS UNSIGNED)>=" + maxPointCount;
        Log.d(TAG, "deleteAllSyncedPointsRow(): " + whereClause);
        sqlDB.delete(tableName, whereClause, null);
    }

//    /**
    //     * @param sqlDB
    //     * @param currentDate
    //     * @throws JSONException
    //     */
//    public synchronized final void insertPoint(@NonNull SQLiteDatabase sqlDB, @NonNull String currentDate) {
//
//        boolean isSynced = false;
//        boolean isAppVisited = true;
//        String whereClause = "CAST(" + CL_POINTS + "AS UNSIGNED)";
//
//        String columnsValues = "(" + '1' + ",'" + currentDate + "'" + ",'" + isSynced + "'" + ",'" + isAppVisited + "'" + ")";
//
//        String rawQuery = "INSERT INTO " + tableName + " (" + CL_POINTS + "," + CL_DATE + "," + CL_SYNC_STATUS + "," + CL_IS_APP_VISITED
//                + ") VALUES " + columnsValues + " ON CONFLICT(" + CL_DATE + ") DO UPDATE set " + CL_POINTS + "=" + "'" + "(SELECT (IFNULL(" + whereClause + ",0) +1)" + "'" + "FROM " +
//                tableName + " WHERE " + CL_DATE + "='" + currentDate + "')";
//
//        Log.d(TAG, "insertPoint(): " + rawQuery);
//        sqlDB.execSQL(rawQuery);
//
//        /*INSERT INTO points(point, date)
//        VALUES(1, '2019-05-15')
//        ON CONFLICT (date) DO UPDATE set point = (select(IFNULL(point, 0) + 1) from points
//        where date = '2019-05-15');*/
//    }
}
