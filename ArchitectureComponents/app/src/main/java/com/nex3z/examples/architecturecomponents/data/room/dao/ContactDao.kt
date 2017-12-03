package com.nex3z.examples.architecturecomponents.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nex3z.examples.architecturecomponents.data.room.entity.Contact
import com.nex3z.examples.architecturecomponents.data.room.entity.ContactWithNumbers

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact")
    fun queryAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE id = :id LIMIT 1")
    fun queryById(id: String): LiveData<Contact>

    @Query("SELECT * FROM contact WHERE id = :id LIMIT 1")
    fun queryContactWithNumbersById(id: String): LiveData<ContactWithNumbers>

    @Insert
    fun insert(contact: Contact)

    @Update
    fun update(contact: Contact)

    @Delete
    fun delete(contact: Contact)

}