package com.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class MainProvider : ContentProvider() {

    private var mNullPointerException: NullPointerException? = null
    private var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {

        context?.let {
            val providerHelper = ProviderHelper(it)
            db = providerHelper.writableDatabase
            mNullPointerException =
                NullPointerException("There is some problem in SQLiteDatabase Object")
            return true
        } ?: run { return false }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return if (db == null) {
            throw mNullPointerException!!
        } else {
            when (uriMatcher.match(uri)) {
                uriCode1 -> {
                    val cursor = db!!.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                    )
                    if (cursor == null) {
                        throw NullPointerException("Cursor value is null")
                    } else {
                        cursor.setNotificationUri(context!!.contentResolver, uri)
                        cursor.close()
                        cursor
                    }
                }
                else -> throw IllegalArgumentException("Unknown URI $uri")
            }
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uriParam: Uri, values: ContentValues?): Uri? {
        if (db == null) {
            throw mNullPointerException!!
        } else {
            when (uriMatcher.match(uriParam)) {
                uriCode1 -> {
                    val rowID = db!!.insert(TABLE_NAME, "", values)
                    if (rowID > 0) {
                        val uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
                        context?.contentResolver?.notifyChange(uri, null)
                        return uri
                    }
                    throw SQLException("Failed to add a record into$uriParam")
                }
                else -> throw IllegalArgumentException("Unknown URI $uriParam")
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val count: Int
        if (db == null) {
            throw mNullPointerException!!
        } else {
            when (uriMatcher.match(uri)) {
                uriCode1 -> count = db!!.delete(TABLE_NAME, selection, selectionArgs)
                else -> throw IllegalArgumentException("Unknown URI $uri")
            }
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val count:Int
        if (db == null) {
            throw mNullPointerException!!
        } else {
            when (uriMatcher.match(uri)) {
                uriCode1 -> count = db!!.update(TABLE_NAME, values, selection, selectionArgs)
                else -> throw IllegalArgumentException("Unknown URI $uri")
            }
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    companion object {

        private const val AUTHORITY = "com.provider.birju.MainProvider"
        private const val prefix = "content://"
        private const val path = "provider_table"
        private const val URL = "$prefix$AUTHORITY/$path"
        val CONTENT_URI = Uri.parse(URL)!!
        private const val TABLE_NAME = "provider_table"
        const val name = "name"
        private const val uriCode1 = 1
        private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, path, uriCode1)
        }
    }
}
