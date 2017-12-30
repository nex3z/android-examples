package com.nex3z.examples.architecturecomponents

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.nex3z.examples.architecturecomponents.data.entity.Contact
import com.nex3z.examples.architecturecomponents.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_add_contact.*

class AddContactActivity : AppCompatActivity() {

    private lateinit var model: ContactViewModel
    private lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var contactId: String? = null
        if (intent != null) {
            contactId = intent.getStringExtra(EXTRA_CONTACT_ID)
        }
        val factory = ContactViewModel.Factory(application, contactId)
        model = ViewModelProviders.of(this, factory).get(ContactViewModel::class.java)
        model.getContact().observe(this, Observer<Contact> { contact ->
            if (contact != null) {
                this@AddContactActivity.contact = contact
                et_contact_name.setText(contact.name)
                et_contact_company.setText(contact.company)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_contact, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.action_save) {
            save()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        if (!validate()) {
            return
        }

        contact.name = et_contact_name.text.toString()
        contact.company = et_contact_company.text.toString()
        model.insertOrUpdateContact(contact)

        finish()
    }

    private fun validate(): Boolean {
        return !et_contact_name.text.isEmpty()
    }

    companion object {
        val EXTRA_CONTACT_ID = "contact_id"
    }
}
