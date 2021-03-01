package com.android.contactlistsqllite.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.contactlistsqllite.R
import com.android.contactlistsqllite.application.ContactApplication
import com.android.contactlistsqllite.databinding.ActivityContactBinding
import com.android.contactlistsqllite.model.Contact

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupContact(intent.getIntExtra("id", -1))
        setupButtonListeners()
    }

    private fun setupContact(contactId: Int) {
        if(contactId == -1) {
            binding.btnDeleteContact.visibility = View.GONE
            return
        }
    }

    private fun setupButtonListeners() = with(binding) {
        btnSaveContact.setOnClickListener {
            saveContact()
        }
    }

    private fun saveContact() {
        val name = binding.teetName.text.toString().trim()
        val telephone = binding.teetTelephone.text.toString().trim()

        if(name.isNotEmpty() && telephone.isNotEmpty()) {
            Thread(Runnable {
                try {
                    ContactApplication.instance.helperDB.createContact(Contact(0, name, telephone))
                } catch (e: Exception) {
                    return@Runnable
                }
                runOnUiThread {
                    Toast.makeText(this, "Contact $name added!", Toast.LENGTH_SHORT).show()
                }
            }).start()
            finish()
        }
    }
}