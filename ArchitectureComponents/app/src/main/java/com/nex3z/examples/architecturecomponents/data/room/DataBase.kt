package com.nex3z.examples.architecturecomponents.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.nex3z.examples.architecturecomponents.data.room.dao.ContactDao
import com.nex3z.examples.architecturecomponents.data.room.dao.PhoneNumberDao
import com.nex3z.examples.architecturecomponents.data.room.entity.Contact
import com.nex3z.examples.architecturecomponents.data.room.entity.PhoneNumber

@Database(entities = [Contact::class, PhoneNumber::class], version = 1)
abstract class DataBase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    abstract fun phoneNumberDao(): PhoneNumberDao

}