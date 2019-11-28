package com.bhaskar.darwin.ui.sqldb;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class SQLHelper extends SQLiteOpenHelper {

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    protected SQLHelper(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    private final String className = SQLHelper.class.getSimpleName();
    private SQLiteDatabase sqliteDatabase;

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        Log.i(className, "onCreate called");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // ---opens the database---
    public SQLHelper open() throws SQLException {
        try {
            sqliteDatabase = getWritableDatabase(); /*
             * SQLiteDatabase.openDatabase
             * ( DATABASE_NAME, null,
             * SQLiteDatabase
             * .NO_LOCALIZED_COLLATORS);
             */
            // db.setLocale(Locale.US);
            Log.i(className, "Database opened");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    public void close() {
        try {
            if (sqliteDatabase != null && sqliteDatabase.isOpen()) {
                sqliteDatabase.close();
                Log.i(className, "Database closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public abstract SQLiteDatabase getSqliteDatabase();

    protected SQLiteDatabase getSQLiteDatabase() {

        /*if (sqliteDatabase != null) {
            if (!sqliteDatabase.isOpen()) {
                open();
            }
        }*/
        return sqliteDatabase;
    }
}
