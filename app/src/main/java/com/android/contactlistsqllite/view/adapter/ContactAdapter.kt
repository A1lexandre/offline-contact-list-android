package com.android.contactlistsqllite.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.contactlistsqllite.databinding.ContacListItemBinding
import com.android.contactlistsqllite.model.Contact

class ContactAdapter(val onClick: (Int) -> Unit): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    var list = listOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ContacListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = list[position]
        with(holder.view) {
            contactName.text = contact.name
            contactNumber.text = contact.telephone

            root.setOnClickListener {
                onClick(contact.id)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val view: ContacListItemBinding): RecyclerView.ViewHolder(view.root)
}