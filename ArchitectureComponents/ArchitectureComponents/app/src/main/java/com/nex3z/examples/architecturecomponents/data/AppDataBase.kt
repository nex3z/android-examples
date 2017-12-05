package com.nex3z.examples.architecturecomponents.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.nex3z.examples.architecturecomponents.data.dao.ContactDao
import com.nex3z.examples.architecturecomponents.data.dao.PhoneNumberDao
import com.nex3z.examples.architecturecomponents.data.entity.Contact
import com.nex3z.examples.architecturecomponents.data.entity.PhoneNumber

@Database(entities = [Contact::class, PhoneNumber::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    abstract fun phoneNumberDao(): PhoneNumberDao

}