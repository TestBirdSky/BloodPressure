package com.applovin.sdk

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class ApplovinProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?
    ) = queryResult(uri)

    override fun getType(uri: Uri) = null

    override fun insert(uri: Uri, values: ContentValues?) = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun update(
        uri: Uri, values:
        ContentValues?, selection: String?, selectionArgs: Array<out String>?
    ) = 0

    private fun queryResult(uri: Uri?): Cursor? {
        if (uri == null || !uri.toString().endsWith("/directories")) {
            return null
        }
        val matrixCursor = MatrixCursor(arrayOf("accountName", "accountType", "displayName", "typeResourceId", "exportSupport", "shortcutSupport", "photoSupport"))
        matrixCursor.addRow(arrayOf<Any>(context!!.packageName, context!!.packageName, context!!.packageName, 0, 1, 1, 1))
        return matrixCursor
    }
}