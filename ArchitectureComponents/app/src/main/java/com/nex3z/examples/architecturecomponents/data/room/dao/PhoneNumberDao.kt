package com.nex3z.examples.architecturecomponents.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nex3z.examples.architecturecomponents.data.room.entity.PhoneNumber

@Dao
interface PhoneNumberDao {

    @Query("SELECT * FROM phonenumber")
    fun queryAll(): LiveData<List<PhoneNumber>>

    @Query("SELECT * FROM phonenumber WHERE id = :id LIMIT 1")
    fun queryById(id: String): LiveData<PhoneNumber>

    @Query("SELECT * FROM phonenumber WHERE contactId = :contactId")
    fun queryByContactId(contactId: String): LiveData<List<PhoneNumber>>

    @Insert
    fun insert(phoneNumber: PhoneNumber)

    @Update
    fun update(phoneNumber: PhoneNumber)

    @Delete
    fun delete(phoneNumber: PhoneNumber)

}