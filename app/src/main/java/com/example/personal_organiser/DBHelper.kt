package com.example.personal_organiser



import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Contact(val firstName: String, val lastName: String, val phone: String, val email: String)

class DBHelper(context: Context?) : SQLiteOpenHelper(context, "PersonalOrganiserDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL( "CREATE TABLE Contacts " +
                "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "firstName TEXT," +
                "lastName TEXT," +
                "phone TEXT," +
                "email TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Contacts")
        onCreate(db)
    }

    fun insertContact(firstName: String, lastName: String, phone: String, email: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("firstName", firstName)
            put("lastName", lastName)
            put("phone", phone)
            put("email", email)



        }
        return db.insert("Contacts", null, values)
    }

    fun getAllContacts(): List<Contact> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Contacts", null)
        val contacts = mutableListOf<Contact>()

        if (cursor.moveToFirst()) {
            do {
                val firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"))
                val lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))


                contacts.add(Contact(firstName, lastName, phone, email))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return contacts
    }

    fun searchContacts(query: String): List<Contact> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Contacts WHERE firstName LIKE ? OR lastName LIKE ?",
            arrayOf("%$query%", "%$query%")
        )
        val contacts = mutableListOf<Contact>()

        if (cursor.moveToFirst()) {
            do {
                val firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"))
                val lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))

                contacts.add(Contact(firstName, lastName, phone, email ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return contacts
    }
}
