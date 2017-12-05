package com.nex3z.examples.architecturecomponents.data

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nex3z.examples.architecturecomponents.R
import com.nex3z.examples.architecturecomponents.data.entity.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var contacts: List<Contact> = emptyList()
    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val itemView = inflater.inflate(R.layout.item_contact, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) {
            return
        }
        
        val contact = contacts[position]
        holder.itemView.tv_contact_name.text = contact.name
    }

    override fun getItemCount(): Int = contacts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}