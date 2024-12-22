package com.example.personal_organiser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personal_organiser.ui.theme.Personal_organiserTheme


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = DBHelper(this) // SQLite Database Helper

        setContent {
            Personal_organiserTheme {
                PersonalOrganiserApp(dbHelper)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PersonalOrganiserApp(dbHelper: DBHelper) {
    var searchQuery by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf(dbHelper.getAllContacts()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Personal Organiser") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Search Bar
            SearchBar(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onSearch = {
                    contacts = if (searchQuery.isNotBlank()) {
                        dbHelper.searchContacts(searchQuery)
                    } else {
                        dbHelper.getAllContacts()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Contact Form
            AddContactForm(onAdd = { firstName, lastName, phone, email ->
                dbHelper.insertContact(firstName, lastName, phone, email)
                contacts = dbHelper.getAllContacts()
            })

            Spacer(modifier = Modifier.height(16.dp))

            // Display Contacts
            ContactsList(contacts)
        }
    }
}

@Composable
fun SearchBar(searchQuery: String, onSearchChange: (String) -> Unit, onSearch: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            singleLine = true
        )
        Button(onClick = onSearch) {
            Text("Search")
        }
    }
}

@Composable
fun AddContactForm(onAdd: (String, String, String, String) -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }


    Column {
        Text("Add New Contact", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = {
                if (firstName.isNotBlank() && lastName.isNotBlank() && phone.isNotBlank() && email.isNotBlank() ){
                    onAdd(firstName, lastName, phone, email)
                    firstName = ""
                    lastName = ""
                    phone = ""
                    email = ""

                }
            }
        ) {
            Text("Add Contact")
        }
    }
}

@Composable
fun ContactsList(contacts: List<Contact>) {
    LazyColumn {
        items(contacts.size) { index ->
            val contact = contacts[index]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Name: ${contact.firstName} ${contact.lastName}", style = MaterialTheme.typography.bodyLarge)
                Text("Phone: ${contact.phone}", style = MaterialTheme.typography.bodyMedium)
                Text("Email: ${contact.email}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalOrganiserPreview() {
    Personal_organiserTheme {
        // Replace with DBHelper() for actual data
        PersonalOrganiserApp(DBHelper(context = null))
    }
}
