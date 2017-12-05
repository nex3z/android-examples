package com.nex3z.examples.architecturecomponents.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.nex3z.examples.architecturecomponents.App
import com.nex3z.examples.architecturecomponents.data.entity.Contact

class ContactListViewModel(application: Application) : AndroidViewModel(application) {

    private val observableContacts: MediatorLiveData<List<Contact>> = MediatorLiveData()

    init {
        observableContacts.value = emptyList()
        val contacts = (application as App).repository.getContacts()
        observableContacts.addSource(contacts, observableContacts::setValue)
    }

    fun getContacts(): LiveData<List<Contact>> {
        return observableContacts
    }

}