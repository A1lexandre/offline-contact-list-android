package com.android.contactlistsqllite.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.contactlistsqllite.application.ContactApplication
import com.android.contactlistsqllite.databinding.ActivityContactBinding
import com.android.contactlistsqllite.model.Contact
import com.android.contactlistsqllite.view.MainActivity.Companion.ID

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding
    lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupContact(intent.getIntExtra(ID, -1))
        setupButtonListeners()
    }

    private fun setupContact(contactId: Int) {
        if(contactId == -1) {
            binding.btnDeleteContact.visibility = View.GONE
            return
        }

        Thread(Runnable {
            try {
                contact = ContactApplication.instance.helperDB.getContact(contactId)
            } catch (e: Exception) {
                contact = Contact(0, "", "")
                return@Runnable
            }
            runOnUiThread {
                with(binding) {
                    teetName.text = Editable.Factory.getInstance().newEditable(contact.name)
                    teetTelephone.text = Editable.Factory.getInstance().newEditable(contact.telephone)
                }
            }
        }).start()
    }

    private fun setupButtonListeners() = with(binding) {
        btnSaveContact.setOnClickListener {
            val id = intent.getIntExtra(ID, -1)
            if(id == -1)
                saveContact()
            else
                updateContact(id)
        }

        btnDeleteContact.setOnClickListener {
            deleteContact()
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
                    finish()
                }
            }).start()
        }
    }

    private fun updateContact(contactId: Int) {
        val name = binding.teetName.text.toString().trim()
        val telephone = binding.teetTelephone.text.toString().trim()

        if(name.isNotEmpty() && telephone.isNotEmpty()) {
            Thread(Runnable {
                try {
                    ContactApplication.instance.helperDB.updateContact(Contact(contactId, name, telephone))
                } catch (e: Exception) {
                    return@Runnable
                }
                runOnUiThread {
                    Toast.makeText(this, "Contact $name updated!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }).start()
        }
    }

    private fun deleteContact() {
        Thread(Runnable {
            try {
                ContactApplication.instance.helperDB.deleteContact(contact.id)
            } catch (e: Exception) {
                return@Runnable
            }
            runOnUiThread {
                Toast.makeText(this, "Contact ${contact.name} deleted!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).start()
    }
}