package com.nex3z.examples.architecturecomponents.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Contact(
        @field:PrimaryKey var id: String = UUID.randomUUID().toString(),
        var name: String? = null,
        var company: String? = null
)
