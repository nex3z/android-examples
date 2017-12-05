package com.nex3z.examples.architecturecomponents.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nex3z.examples.architecturecomponents.data.entity.PhoneNumber

@Dao
interface PhoneNumberDao {

    @Query("SELECT * FROM numbers")
    fun queryAll(): LiveData<List<PhoneNumber>>

    @Query("SELECT * FROM numbers WHERE id = :id LIMIT 1")
    fun queryById(id: String): LiveData<PhoneNumber>

    @Query("SELECT * FROM numbers WHERE contactId = :contactId")
    fun queryByContactId(contactId: String): LiveData<List<PhoneNumber>>

    @Insert
    fun insert(phoneNumber: PhoneNumber)

    @Update
    fun update(phoneNumber: PhoneNumber)

    @Delete
    fun delete(phoneNumber: PhoneNumber)

}