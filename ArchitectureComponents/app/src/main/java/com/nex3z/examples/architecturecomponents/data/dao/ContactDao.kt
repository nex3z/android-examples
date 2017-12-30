package com.nex3z.examples.architecturecomponents.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nex3z.examples.architecturecomponents.data.entity.Contact
import com.nex3z.examples.architecturecomponents.data.entity.ContactWithNumbers

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts")
    fun queryAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    fun queryById(id: String): LiveData<Contact>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    fun queryContactWithNumbersById(id: String): LiveData<ContactWithNumbers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Update
    fun update(contact: Contact)

    @Delete
    fun delete(contact: Contact)

}