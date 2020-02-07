package com.bhaskar.darwin.ui.sqldemo.sqlimpl;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhaskar.darwin.ui.sqldb.SQLHelper;
import com.bhaskar.darwin.ui.sqldemo.sqlimpl.dao.PointSyncTableDao;

/**
 * OfferSqlAdapter is a child of SQLHelper having all responsibilities of
 * instantiating , opening and closing database connection and keep an
 * SQLDatabase Object
 */
public final class OfferSqlAdapter extends SQLHelper {

    private final String TAG = OfferSqlAdapter.class.getSimpleName();

    /**
     * ***
     * Init Database with params
     *
     * @param context
     * @param dbName
     * @param dbVersion
     */
    public OfferSqlAdapter(Context context, final String dbName, final int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        createInitialTables(sqlDB);
        super.onCreate(sqlDB);
    }

    /**
     * Create individual table needed in database
     *
     * @param sqlDB
     */
    private void createInitialTables(SQLiteDatabase sqlDB) {
        try {
            PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
            pointSyncTableDao.createSqlTable(sqlDB);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    /**
     * To close Database
     */
    public void close() {
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        super.onUpgrade(arg0, arg1, arg2);
    }

    @Override
    public SQLiteDatabase getSqliteDatabase() {
        if (!super.getSQLiteDatabase().isOpen()) {
            open();
        }
        return super.getSQLiteDatabase();
    }
}

