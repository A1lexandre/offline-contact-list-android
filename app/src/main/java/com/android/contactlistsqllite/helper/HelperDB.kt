package com.android.contactlistsqllite.helper

import android.content.ContentValues
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
            "$COLUMN_ID INTEGER NOT NULL," +
            "$COLUMN_NAME TEXT NOT NULL," +
            "$COLUMN_TELEPHONE TEXT NOT NULL, " +
            "PRIMARY KEY ($COLUMN_ID AUTOINCREMENT))"

//    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
//            "$COLUMNS_ID INTEGER NOT NULL," +
//            "$COLUMNS_NOME TEXT NOT NULL," +
//            "$COLUMNS_TELEFONE TEXT NOT NULL," +
//            "" +
//            "PRIMARY KEY($COLUMNS_ID AUTOINCREMENT)" +
//            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion)
            db?.execSQL(DROP_TABLE_QUERY)
        onCreate(db)
    }

    fun searchContacts(name: String): List<Contact> {
        val list = mutableListOf<Contact>()
        val db = readableDatabase ?: return list

//        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME LIKE '%$name%'"
        val sql = "SELECT * FROM $TABLE_NAME"
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

    fun getContact(id: Int): Contact {
        val db = readableDatabase ?: return Contact(0,"", "")

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $id", null)

        cursor.moveToFirst()

        val contact = Contact(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(COLUMN_TELEPHONE)))

        cursor.close()
        db.close()
        return contact
    }

    fun createContact(contact: Contact) {
        val db = writableDatabase ?: return
        val content = ContentValues()
        content.put(COLUMN_NAME, contact.name)
        content.put(COLUMN_TELEPHONE, contact.telephone)
        db.insert(TABLE_NAME, null, content)
        db.close()
    }

    fun updateContact(contact: Contact) {
        val db = writableDatabase ?: return
        val content = ContentValues()
        content.put(COLUMN_ID, contact.id)
        content.put(COLUMN_NAME, contact.name)
        content.put(COLUMN_TELEPHONE, contact.telephone)

        db.update(TABLE_NAME, content, "$COLUMN_ID = ?", arrayOf(contact.id.toString()))
        db.close()
    }

    fun deleteContact(contactId: Int) {
        val db = writableDatabase ?: return
        db.delete(TABLE_NAME, "$COLUMN_ID = $contactId", null)
        db.close()
    }

    companion object {
        private val DB_NAME = "agenda.db"
        private val VERSION = 1
    }
}