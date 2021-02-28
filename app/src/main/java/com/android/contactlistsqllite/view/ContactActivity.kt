package com.android.contactlistsqllite.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.contactlistsqllite.R
import com.android.contactlistsqllite.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupContact(intent.getIntExtra("id", -1))
    }

    private fun setupContact(contactId: Int) {
        if(contactId == -1) {
            binding.btnDeleteContact.visibility = View.GONE
            return
        }
    }
}