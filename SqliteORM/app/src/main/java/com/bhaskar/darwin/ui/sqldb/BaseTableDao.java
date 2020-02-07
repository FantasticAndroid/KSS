package com.bhaskar.darwin.ui.sqldb;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTableDao implements TableInterface {

    protected String tableName;
    protected Class<? extends SqlTypeTable> classType;


    /***
     * classType of Table
     * @param classType
     */
    public BaseTableDao(final Class<? extends SqlTypeTable> classType) {
        this.classType = classType;
        this.tableName = classType.getSimpleName();
    }

    /**
     * @param sqlDB
     * @param columnName
     * @param columnValue
     * @return
     */
    protected synchronized int deleteRowsWithColumnKey(SQLiteDatabase sqlDB, String columnName, String columnValue) {
        return TableDao.deleteRowsWithColumnKey(tableName, sqlDB, columnName, columnValue);
    }

    /**
     * @param sqlDB
     * @param columnName
     * @param columnValue
     * @return
     */
    protected synchronized void deleteRowsNotHavingColumnKey(SQLiteDatabase sqlDB, String columnName, String columnValue) {
        TableDao.deleteRowsNotHavingColumnKey(tableName, sqlDB, columnName, columnValue);
    }

    /***
     *
     * @param sqlDB
     */
    public synchronized final void createSqlTable(SQLiteDatabase sqlDB) throws
            InstantiationException, IllegalAccessException {

        Field[] fields = classType.getDeclaredFields();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("create table IF NOT EXISTS ").append(tableName).append(" (");
        for (Field field : fields) {

            String fieldName = field.getName();
            if (field.isSynthetic() || fieldName.equalsIgnoreCase(SERIAL_VERSION_UID)) {
                continue;
            }

            /*String fieldType = VARCHAR;

            Class<?> type = field.getType();
            if (type == Integer.class || type == Boolean.class || type == Long.class) {
                fieldType = INTEGER;
            } else if(type == Double.class || type == Float.class){
                fieldType = REAL;
            }else{
                fieldType = VARCHAR;
            }*/
            queryBuilder.append(" ").append(fieldName).append(VARCHAR).append(" , ");
        }

        SqlTypeTable sqlTypeTable = classType.newInstance();

        String primaryKey = sqlTypeTable.getPrimaryKeyColumn();

        queryBuilder.append(" " + PRIMARY_KEY + "(").append(primaryKey).append("))");

        /*String tableCreationQuery = "create table IF NOT EXISTS " + mTableName + " (" +
                COLUMN_ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + " , " + COLUMN_PATH +
                VARCHAR + UNIQUE + NOT_NULL + " , " + COLUMN_IS_UPLOADED + VARCHAR +
                NOT_NULL + " , " + COLUMN_RECKSPACE_URL + VARCHAR + DEFAULT + "\"\"" + " )";*/

        createSqlTable(sqlDB, queryBuilder.toString());
    }

    /*
     */
/**
 * Do not include "create table IF NOT EXISTS tableName" (Only Start your
 * columns name and condition in ());
 *
 * @param sqlDB
 */
/*
    protected abstract void createSqlTable(SQLiteDatabase sqlDB)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
*/


    /**
     * Do not include "create table IF NOT EXISTS tableName" (Only Start your
     * columns name and condition in ());
     *
     * @param sqlDB
     * @param tableCreationQuery
     */
    private synchronized void createSqlTable(SQLiteDatabase sqlDB, String tableCreationQuery) {
        try {
            sqlDB.execSQL(tableCreationQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized final long getTableRowCount(SQLiteDatabase sqlDB) throws Exception {
        return TableDao.getTableRowCount(sqlDB, tableName);
    }

    public synchronized final boolean isTableEmpty(SQLiteDatabase sqlDB) throws Exception {
        return TableDao.isTableEmpty(sqlDB, tableName);
    }

    /***
     *
     * @param sqlDB
     * @param objectList
     * @param objectType
     * @param <T>
     */
    public synchronized final <T extends SqlTypeTable> void insertAllRows(SQLiteDatabase sqlDB, List<T> objectList, Class<T> objectType) throws Exception {
        TableDao.insertAll(sqlDB, tableName, objectList, objectType);
    }

    /**
     * @param className
     * @param sqlDB
     * @param primaryKeyColumn
     * @param primaryKeyValue
     * @param <T>
     * @return
     * @throws Exception
     */
    protected final <T extends SqlTypeTable> T getRowWithPrimaryKey(Class<T> className, SQLiteDatabase sqlDB,
                                                                    String primaryKeyColumn, String primaryKeyValue)
            throws Exception {
        return TableDao.getRowWithPrimaryKey(tableName, className, sqlDB, primaryKeyColumn, primaryKeyValue);
    }

    /**
     * @param className
     * @param sqlDB
     * @param columnName
     * @param columnValue
     * @param <T>
     * @return
     * @throws Exception
     */
    protected synchronized final <T extends SqlTypeTable> ArrayList<T> getRowsWithColumnKey(Class<T> className, SQLiteDatabase sqlDB,
                                                                                            String columnName, String columnValue)
            throws Exception {
        return TableDao.getRowsWithColumnKey(tableName, className, sqlDB, columnName, columnValue);
    }


    /***
     *
     * @param sqlDB
     * @return
     */
    public synchronized final void deleteTableAllRows(SQLiteDatabase sqlDB) {
        TableDao.deleteTableData(sqlDB, tableName);
    }

    /* *//***
     *
     * @param modelType
     * @param sqlDB
     * @param <T>
     * @return
     *//*
    public final <T extends SqlTypeTable> ArrayList<? extends SqlTypeTable> getAllRowsFromTable(Class<T> modelType, SQLiteDatabase sqlDB) throws Exception {
        return TableDao.getAllRecordFromTable(tableName, modelType, sqlDB).getDataList();
    }*/

    /***
     *
     * @param modelType
     * @param sqlDB
     * @param <T>
     * @return
     */
    public synchronized final <T extends SqlTypeTable> ArrayList<T> getAllRowsFromTable(Class<T> modelType, SQLiteDatabase sqlDB) throws Exception {
        return TableDao.getAllRecordFromTable(tableName, modelType, sqlDB);//.getDataList();
    }

    /**
     * @param sqlDB
     * @param objectType
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public synchronized final <T extends SqlTypeTable> long insert(SQLiteDatabase sqlDB,
                                                                   T object, Class<T> objectType) throws Exception {
        return TableDao.insert(sqlDB, tableName, object, objectType);
    }

    /**
     * @param sqlDB
     * @param objectType
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    protected synchronized final <T extends SqlTypeTable> long insertOrUpdateRow(SQLiteDatabase sqlDB,
                                                                                 T object, Class<T> objectType) throws Exception {
        return TableDao.insertOrReplace(sqlDB, tableName, object, objectType);
    }

    // /*******************************************************************/
    //
    // /***
    // *
    // * @param sqlDB
    // * @param dataMapList
    // * @return long
    // */
    // protected abstract long insertDataList(SQLiteDatabase sqlDB,
    // ArrayList<HashMap<String, String>> dataMapList);
    //
    // protected abstract int deleteTableData(SQLiteDatabase sqlDB);
    //
    // /**
    // *
    // * @param sqlDB
    // * @return ArrayList<HashMap<String, String>>
    // */
    // protected abstract ArrayList<HashMap<String, String>>
    // getAllDataFromTable(
    // SQLiteDatabase sqlDB);
    //
    // /***
    // *
    // * @param sqlDB
    // * @param dataMap
    // */
    // protected abstract void insertDataMap(SQLiteDatabase sqlDB,
    // HashMap<String, String> dataMap);
    //
    // protected abstract void createTable(SQLiteDatabase sqlDB);
    //
    // /**
    // *
    // * @param sqlDB
    // * @param dataMap
    // */
    // protected abstract void insertOrUpdateDataMap(SQLiteDatabase sqlDB,
    // HashMap<String, String> dataMap);
}