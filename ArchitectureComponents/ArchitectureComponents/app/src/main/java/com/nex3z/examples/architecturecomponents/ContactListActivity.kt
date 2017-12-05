package com.nex3z.examples.architecturecomponents

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.nex3z.examples.architecturecomponents.data.ContactAdapter
import com.nex3z.examples.architecturecomponents.data.entity.Contact
import com.nex3z.examples.architecturecomponents.viewmodel.ContactListViewModel
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.content_contact_list.*

class ContactListActivity : AppCompatActivity() {

    private var contacts: List<Contact> = emptyList()
    private val adapter: ContactAdapter = ContactAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)
        setSupportActionBar(toolbar)
        init()
    }

    private fun init() {
        initView()
        initData()
    }

    private fun initView() {
        fab.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }

        adapter.onItemClickListener = object : ContactAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val contact = contacts[position]
                val intent = Intent(this@ContactListActivity, AddContactActivity::class.java)
                intent.putExtra(AddContactActivity.EXTRA_CONTACT_ID, contact.id)
                startActivity(intent)
            }
        }

        with(rv_contact_list) {
            adapter = this@ContactListActivity.adapter
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        }
    }

    private fun initData() {
        val viewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java)
        viewModel.getContacts().observe(this, Observer<List<Contact>> { t ->
            contacts = t ?: emptyList()
            adapter.contacts = contacts
        })
    }

    companion object {
        private val LOG_TAG = ContactListActivity::class.simpleName
    }
}
