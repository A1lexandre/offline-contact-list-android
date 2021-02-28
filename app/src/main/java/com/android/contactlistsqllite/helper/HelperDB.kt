package com.android.contactlistsqllite.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.contactlistsqllite.model.Contact

class HelperDB(context: Context): SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    val TABLE_NAME = "contact"
    val COLUMN_ID = "id"
    val COLUMN_NAME = "name"
    val COLUMN_TELEPHONE = "telephone"

    val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"

    val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME ( " +
            "$COLUMN_ID NOT NULL" +
            "$COLUMN_NAME NOT NULL" +
            "$COLUMN_TELEPHONE NOT NULL" +
            "PRIMARY KEY ($COLUMN_ID AUTOINCREMENT))"

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE DATABASE"
        db?.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion)
            db?.execSQL(DROP_TABLE_QUERY)
        onCreate(db)
    }

    fun searchContacts(name: String, isSearchById: Boolean = false): List<Contact> {
        val list = mutableListOf<Contact>()
        val db = readableDatabase ?: return list

        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME LIKE '%$name%'"
        val cursor = db.rawQuery(sql, null)

        if (cursor == null) {
            db.close()
            return list
        }

        while(cursor.moveToNext()) {
            list.add(
                Contact(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TELEPHONE)))
            )
        }

        db.close()
        cursor.close()

        return list
    }

    companion object {
        private val DB_NAME = "agenda.db"
        private val VERSION = 1
    }
}