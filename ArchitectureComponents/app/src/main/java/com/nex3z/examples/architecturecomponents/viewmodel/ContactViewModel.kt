package com.nex3z.examples.architecturecomponents.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import com.nex3z.examples.architecturecomponents.App
import com.nex3z.examples.architecturecomponents.data.entity.Contact

class ContactViewModel(
        application: Application,
        contactId: String?
) : AndroidViewModel(application) {

    private val repository =  (application as App).repository
    private val observableContact: LiveData<Contact>

    init {
        if (contactId != null) {
            observableContact = repository.getContactById(contactId)
        } else {
            observableContact = MutableLiveData()
            observableContact.value = Contact()
        }
    }

    fun getContact(): LiveData<Contact> {
        return observableContact
    }

    fun insertOrUpdateContact(contact: Contact) {
        if (observableContact is MutableLiveData) {
            observableContact.value = contact
        }
        repository.insertOrUpdateContact(contact)
    }

    class Factory(
            private val application: Application,
            private val contactId: String?
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ContactViewModel(application, contactId) as T
        }

    }
}