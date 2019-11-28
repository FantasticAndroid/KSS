package com.bhaskar.darwin.ui.sqldb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class TableDao implements TableInterface {

    /**
     * @param sqlDB
     * @param tableName
     * @param objectType
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public synchronized static <T extends SqlTypeTable> long insert(SQLiteDatabase sqlDB, String tableName,
                                                                    T object, Class<T> objectType) throws JSONException {
        ContentValues values = insert(object, objectType);
        return sqlDB.insert(tableName, null, values);
    }

    /**
     * @param sqlDB
     * @param tableName
     * @param objectType
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    synchronized static <T extends SqlTypeTable> long insertOrReplace(SQLiteDatabase sqlDB, String tableName,
                                                                      T object, Class<T> objectType) throws JSONException {
        ContentValues values = insert(object, objectType);
        return sqlDB.replace(tableName, null, values);
    }

    /***
     *
     * @param sqlDB
     * @param tableName
     * @param object
     * @param objectType
     * @param primaryKeyName
     * @param primaryKeyValue
     * @param <T>
     * @return the row ID
     * @throws JSONException
     */
    public synchronized static <T extends SqlTypeTable> int update(SQLiteDatabase sqlDB, String tableName,
                                                                   T object, Class<T> objectType, String primaryKeyName, String primaryKeyValue) throws JSONException {
        ContentValues values = insert(object, objectType);
        return sqlDB.update(tableName, values, primaryKeyName + "='" + primaryKeyValue + "'", null);
    }

    /***
     *
     * @param sqlDB
     * @param tableName
     * @param objectType
     * @param columnName
     * @param columnValue
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public synchronized static <T extends SqlTypeTable> long getPrimaryKeyRowCount(SQLiteDatabase sqlDB, String tableName, Class<T> objectType,
                                                                                   String columnName, String columnValue) throws InstantiationException, IllegalAccessException {

        // String primaryKey = objectType.newInstance().getPrimaryKeyColumn();
        /*String query = SELECT + COUNT + "(" + primaryKey + ")" + FROM + tableName + WHERE + columnName + "=" + columnValue;*/

        /////return DatabaseUtils.queryNumEntries(sqlDB, tableName, WHERE + columnName + "=?", new String[]{columnValue});

        long count = DatabaseUtils.queryNumEntries(sqlDB, tableName, columnName + "='" + columnValue + "'");
        Log.d("getPrimaryKeyRowCount", "count: " + count);
        /*return DatabaseUtils.longForQuery(sqlDB, query, null);*/
        return count;
    }


    /**
     * @param sqlDB
     * @param tableName
     * @param objectList
     * @param objectType
     */
    public synchronized static <T extends SqlTypeTable> void insertAll(SQLiteDatabase sqlDB, String tableName,
                                                                       List<T> objectList, Class<T> objectType) throws Exception {
        try {
            sqlDB.beginTransaction();
            for (T model : objectList) {
                ContentValues values = insert(model, objectType);
                sqlDB.insert(tableName, null, values);
            }
            sqlDB.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            sqlDB.endTransaction();
        }
    }

    /**
     * @param sqlDB
     * @param tableName
     * @return int, the number of rows affected if a whereClause is passed in, 0
     * otherwise. To remove all rows and get a count pass "1" as the
     * whereClause.
     */
    synchronized static void deleteTableData(SQLiteDatabase sqlDB, String tableName) {
        sqlDB.delete(tableName, null, null);
    }

    /***
     *
     * @param sqlDB
     * @param tableName
     * @return
     */
    synchronized static long getTableRowCount(SQLiteDatabase sqlDB, String tableName) throws Exception {
        //String query = SELECT + COUNT + "(" + ALL + ")" + FROM + tableName;

        long count = DatabaseUtils.queryNumEntries(sqlDB, tableName, null, null);
        Log.d("getTableRowCount", count + "");
        return count;
    }

    /***
     *
     * @param sqlDB
     * @param tableName
     * @return
     */
    synchronized static boolean isTableEmpty(SQLiteDatabase sqlDB, String tableName) throws Exception {
        long rowCount = getTableRowCount(sqlDB, tableName);
        return rowCount <= 0;
    }

    /***
     * @param tableName
     * @param sqlDB
     * @return
     */
    public synchronized static <T extends SqlTypeTable> ArrayList<T> getAllRecordFromTable(
            String tableName, Class<T> className, SQLiteDatabase sqlDB) throws Exception {

        //SqlList<T> dataList = new SqlList<>();
        ArrayList<T> arrayList = new ArrayList<>();
        if (!sqlDB.isOpen())
            return arrayList;

        Cursor c = sqlDB.rawQuery(SELECT + ALL + FROM + tableName, null);//tableName, null, null, null, null, null, null);

        if (c.getCount() == 0) {
            c.close();
            return arrayList;
        }
        c.moveToFirst();
        Gson gson = new GsonBuilder().create();
        do {
            T classType = getRowMapFromCursor(c, className, gson);
            arrayList.add(classType);
        } while (c.moveToNext());
        c.close();
        //dataList.setDataList(arrayList);
        return arrayList;
    }

    /***
     *
     * @param tableName
     * @param className
     * @param sqlDB
     * @param primaryKeyColumn
     * @param primaryKeyValue
     * @param <T>
     * @return
     * @throws Exception
     */
    synchronized static <T extends SqlTypeTable> T getRowWithPrimaryKey(
            String tableName, Class<T> className, SQLiteDatabase sqlDB, String primaryKeyColumn, String primaryKeyValue) throws Exception {

        //SqlList<T> dataList = new SqlList<>();
        if (!sqlDB.isOpen())
            return null;

        String rawQuery = SELECT + ALL + FROM + tableName + WHERE + primaryKeyColumn + "='" + primaryKeyValue + "'";

        Cursor c = sqlDB.rawQuery(rawQuery, null);//tableName, null, null, null, null, null, null);

        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        c.moveToFirst();
        Gson gson = new GsonBuilder().create();
        T classType = getRowMapFromCursor(c, className, gson);
        c.close();
        //dataList.setDataList(arrayList);
        return classType;
    }


    /***
     *
     * @param tableName
     * @param sqlDB
     * @param columnName
     * @param columnValue
     * @return
     */
    synchronized static int deleteRowsWithColumnKey(
            String tableName, SQLiteDatabase sqlDB, String columnName, String columnValue) {
        return sqlDB.delete(tableName, columnName + "='" + columnValue + "'", null);
    }

    /***
     *
     * @param tableName
     * @param sqlDB
     * @param columnName
     * @param columnValue
     * @return
     */
    synchronized static void deleteRowsNotHavingColumnKey(String tableName, SQLiteDatabase sqlDB, String columnName, String columnValue) {
        sqlDB.delete(tableName, columnName + "!='" + columnValue + "'", null);
    }

    /***
     *
     * @param tableName
     * @param className
     * @param sqlDB
     * @param columnName
     * @param columnValue
     * @param <T>
     * @return
     * @throws Exception
     */
    synchronized static <T extends SqlTypeTable> ArrayList<T> getRowsWithColumnKey(
            String tableName, Class<T> className, SQLiteDatabase sqlDB, String columnName, String columnValue) throws Exception {

        ArrayList<T> arrayList = new ArrayList<>();
        if (!sqlDB.isOpen())
            return arrayList;

        String rawQuery = SELECT + ALL + FROM + tableName + WHERE + columnName + "='" + columnValue + "'";

        Cursor c = sqlDB.rawQuery(rawQuery, null);//tableName, null, null, null, null, null, null);

        if (c.getCount() == 0) {
            c.close();
            return arrayList;
        }
        c.moveToFirst();
        Gson gson = new GsonBuilder().create();
        do {
            T classType = getRowMapFromCursor(c, className, gson);
            arrayList.add(classType);
        } while (c.moveToNext());
        c.close();
        return arrayList;
    }


    /***
     *
     * @param c
     * @param className
     * @param gson
     * @param <T>
     * @return
     * @throws NoSuchFieldException
     */
    private synchronized static <T extends SqlTypeTable> T getRowMapFromCursor(Cursor c, Class<T> className, Gson gson) throws NoSuchFieldException {
        Map<String, Object> data = new HashMap<>();
        for (int columnIndex = 0; columnIndex < c.getColumnCount(); columnIndex++) {
            String columnName = c.getColumnName(columnIndex);
            String columnValue = c.getString(columnIndex);

            Field field = className.getDeclaredField(columnName);
            if (field.isSynthetic() || columnName.equalsIgnoreCase(SERIAL_VERSION_UID)) {
                continue;
            }
            Class<?> type = field.getType();
            Object columnValueObject = null;
            if (type == int.class || type == Integer.class) {
                columnValueObject = Integer.valueOf(columnValue);
            } else if (type == double.class || type == Double.class) {
                columnValueObject = Double.valueOf(columnValue);
            } else if (type == float.class || type == Float.class) {
                columnValueObject = Integer.valueOf(columnValue);
            } else if (type == long.class || type == Long.class) {
                columnValueObject = Long.valueOf(columnValue);
            } else if (type == boolean.class || type == Boolean.class) {
                columnValueObject = Boolean.valueOf(columnValue);
            } else if (type == String.class) {
                columnValueObject = String.valueOf(columnValue);
            } else {
                columnValueObject = columnValue;
            }
            data.put(columnName, columnValueObject);
        }
        JSONObject jsonObject = new JSONObject(data);

        return gson.fromJson(jsonObject + "", className);
    }

//    /***
//     *
//     * @param c
//     * @param className
//     * @param <T>
//     * @return
//     * @throws NoSuchFieldException
//     */
//    private synchronized static <T extends SqlTypeTable> T getRowMapFromCursor(Cursor c, Class<T> className) throws NoSuchFieldException {
//        Gson gson = new GsonBuilder().create();
//        return getRowMapFromCursor(c, className, gson);
//    }


    /***Integer
     *
     * @param model
     * @param objectType
     * @param <T>
     * @return
     * @throws JSONException
     */
    private synchronized static <T extends SqlTypeTable> ContentValues insert(T model, Class<T> objectType) throws JSONException {
        String string = new GsonBuilder().create().toJson(model, objectType);

        JSONObject jsonObject = new JSONObject(string);

        Iterator<String> jsonKeysIterator = jsonObject.keys();
        ContentValues values = new ContentValues();
        while (jsonKeysIterator.hasNext()) {
            String keyForColumn = jsonKeysIterator.next();
            Object value = jsonObject.get(keyForColumn);
            values.put(keyForColumn, value + "");

            /*if (value instanceof Boolean) {
                values.put(keyForColumn, (Boolean) value);
            } else if (value instanceof Integer) {
                values.put(keyForColumn, (Integer) value);
            } else if (value instanceof Double) {
                values.put(keyForColumn, (Double) value);
            } else if (value instanceof Float) {
                values.put(keyForColumn, (Float) value);
            } else if (value instanceof Long) {
                values.put(keyForColumn, (Long) value);
            } else if (value instanceof String) {
                values.put(keyForColumn, (String) value);Integer
            }else{
                values.put(keyForColumn, value+"");
            }*/
        }

        return values;
    }

    /***Integer
     *
     * @param model
     * @param objectType
     * @param <T>
     * @return
     * @throws JSONException
     */
    private synchronized static <T extends SqlTypeTable> ContentValues update(T model, Class<T> objectType) throws JSONException {
        String string = new GsonBuilder().create().toJson(model, objectType);

        JSONObject jsonObject = new JSONObject(string);

        Iterator<String> jsonKeysIterator = jsonObject.keys();
        ContentValues values = new ContentValues();
        while (jsonKeysIterator.hasNext()) {
            String keyForColumn = jsonKeysIterator.next();
            Object value = jsonObject.get(keyForColumn);
            values.put(keyForColumn, value + "");

            /*if (value instanceof Boolean) {
                values.put(keyForColumn, (Boolean) value);
            } else if (value instanceof Integer) {
                values.put(keyForColumn, (Integer) value);
            } else if (value instanceof Double) {
                values.put(keyForColumn, (Double) value);
            } else if (value instanceof Float) {
                values.put(keyForColumn, (Float) value);
            } else if (value instanceof Long) {
                values.put(keyForColumn, (Long) value);
            } else if (value instanceof String) {
                values.put(keyForColumn, (String) value);Integer
            }else{
                values.put(keyForColumn, value+"");
            }*/
        }

        return values;
    }

    /*protected static ArrayList<HashMap<String, String>> getAllDataFromTableWithAscendingOrder(Integer
            SQLiteDatabase sqlDB, final String tableName, final String columnNameToOrderBy) {

        ArrayList<HashMap<String, String>> newsDataList = new ArrayList<HashMap<String, String>>();

        if (!sqlDB.isOpen())
            return newsDataList;

        Cursor c = sqlDB.query(tableName, null, null, null, null, null,
                columnNameToOrderBy);

        try {
            if (c.getCount() == 0) {
                c.close();
                return newsDataList;
            }
            c.moveToFirst();

            do {
                HashMap<String, String> newsData = new HashMap<String, String>();
                for (int columnIndex = 0; columnIndex < c.getColumnCount(); columnIndex++) {
                    String columnName = c.getColumnName(columnIndex);
                    String columnValue = c.getString(columnIndex);

                    newsData.put(columnName, columnValue);
                }
                newsDataList.add(newsData);
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsDataList;
    }*/
}

/*Set<String> keySet = dataMap.keySet();
        StringBuffer columnNames = new StringBuffer();
        columnNames.append("( ");

        StringBuffer values = new StringBuffer();
        values.append(" ( ");
        for (String columnName : keySet) {
            columnNames.append(columnName + ",");
            String columnValue = dataMap.get(columnName);
            columnValue = columnValue.replace("'", "''");
            values.append("'" + columnValue + "'" + ",");
        }
        columnNames.deleteCharAt(columnNames.lastIndexOf(","));
        values.deleteCharAt(values.lastIndexOf(","));

        columnNames.append(" ) ");
        values.append(" ) ");

        String rawQuery = "INSERT OR REPLACE INTO " + tableName + columnNames
                + " VALUES " + values;
        ////Log.i("insertOrUpdateDataMap, rawQuery", rawQuery);
        sqlDB.execSQL(rawQuery);*/
