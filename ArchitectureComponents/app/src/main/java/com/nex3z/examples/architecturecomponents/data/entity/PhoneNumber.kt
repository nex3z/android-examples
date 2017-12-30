package com.nex3z.examples.architecturecomponents.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "numbers")
data class PhoneNumber(
        @field:PrimaryKey var id: String = UUID.randomUUID().toString(),
        var contactId: String? = null,
        var number: String? = null
)
