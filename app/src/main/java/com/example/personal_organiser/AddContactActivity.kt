package com.example.personal_organiser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        val dbHelper = DBHelper(this)

        val firstNameField = findViewById<EditText>(R.id.editTextFirstName)
        val lastNameField = findViewById<EditText>(R.id.editTextLastName)
        val phoneField = findViewById<EditText>(R.id.editTextPhone)
        val emailField = findViewById<EditText>(R.id.editTextEmail)
        val addButton = findViewById<Button>(R.id.buttonAddContact)

        addButton.setOnClickListener {
            val firstName = firstNameField.text.toString().trim()
            val lastName = lastNameField.text.toString().trim()
            val phone = phoneField.text.toString().trim()
            val email = emailField.text.toString().trim()


            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()) {
                val result = dbHelper.insertContact(firstName, lastName, phone, email)
                if (result != -1L) { // result is a Long, so comparison with -1L is correct
                    Toast.makeText(this, "Contact added successfully!", Toast.LENGTH_SHORT).show()
                    firstNameField.text.clear()
                    lastNameField.text.clear()
                    phoneField.text.clear()
                    emailField.text.clear()

                } else {
                    Toast.makeText(this, "Error adding contact. Ensure phone/email is unique.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
