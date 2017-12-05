package com.nex3z.examples.architecturecomponents.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.content.Context
import com.nex3z.examples.architecturecomponents.data.entity.Contact
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Repository(applicationContext: Context) {

    private val database: AppDataBase =
            Room.databaseBuilder(applicationContext, AppDataBase::class.java, "database")
                    .build()

    private val executor: Executor = Executors.newSingleThreadExecutor()

    fun getContacts(): LiveData<List<Contact>> {
        return database.contactDao().queryAll()
    }

    fun getContactById(id: String): LiveData<Contact> {
        return database.contactDao().queryById(id);
    }

    fun insertOrUpdateContact(contact: Contact) {
        executor.execute {
            database.contactDao().insert(contact)
        }
    }
}