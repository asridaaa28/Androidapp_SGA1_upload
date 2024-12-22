package com.example.personal_organiser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_contact)

        val dbHelper = DBHelper(this)

        val searchField = findViewById<EditText>(R.id.etSearch)
        val searchButton = findViewById<Button>(R.id.btnSearch)
        val resultTextView = findViewById<TextView>(R.id.tvSearchResults)

        searchButton.setOnClickListener {
            val query = searchField.text.toString().trim()
            if (query.isNotEmpty()) {
                val results = dbHelper.searchContacts(query)
                if (results.isNotEmpty()) {
                    val resultText = results.joinToString("\n\n") { contact ->
                        """
                        Name: ${contact.firstName} ${contact.lastName}
                        Phone: ${contact.phone}
                        Email: ${contact.email}
                       
                        """.trimIndent()
                    }
                    resultTextView.text = resultText
                } else {
                    resultTextView.text = "No results found."
                }
            } else {
                resultTextView.text = "Please enter a search term."
            }
        }
    }
}
