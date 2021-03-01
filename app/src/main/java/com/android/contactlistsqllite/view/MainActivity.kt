package com.android.contactlistsqllite.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.contactlistsqllite.application.ContactApplication
import com.android.contactlistsqllite.databinding.ActivityMainBinding
import com.android.contactlistsqllite.model.Contact
import com.android.contactlistsqllite.view.adapter.ContactAdapter

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val adapter by lazy {
        ContactAdapter {
            goToContactDetail(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        setupButtonListeners()
    }

    override fun onResume() {
        super.onResume()
        getcontacts()
    }

    private fun setupRecyclerView() {
        with(binding.rvContactsList) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupButtonListeners() = with(binding) {
        btnAddContact.setOnClickListener {
            startActivity(Intent(this@MainActivity, ContactActivity::class.java))
        }

        btnSearch.setOnClickListener {
            contactSearchField.text?.let {
                if(it.toString().trim().isNotEmpty())
                    getcontacts()
            }
        }

        contactSearchField.doOnTextChanged { text, _, _, _ ->
            if(contactSearchField.isInTouchMode && text.isNullOrEmpty())
                getcontacts()
        }
    }

    private fun goToContactDetail(id: Int) {
        val contact = Intent(this, ContactActivity::class.java)
        contact.putExtra(ID, id)
        startActivity(contact)
    }

    private fun getcontacts() {
        val contactNameSearch = binding.contactSearchField.text.toString().trim()
        Thread(Runnable {
            var list: List<Contact>
            try {
                list = ContactApplication.instance.helperDB.searchContacts(contactNameSearch)
            } catch (e: Exception) {
                list = listOf()
                return@Runnable
            }
            runOnUiThread {
                adapter.list = list
                adapter.notifyDataSetChanged()
            }
        }).start()
    }

    companion object {
        const val ID = "id"
    }
}