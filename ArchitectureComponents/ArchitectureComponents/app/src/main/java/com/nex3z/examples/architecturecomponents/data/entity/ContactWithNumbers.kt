package com.nex3z.examples.architecturecomponents.data.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class ContactWithNumbers(
        @Embedded
        var contact: Contact = Contact(),
        @Relation(parentColumn = "id", entityColumn = "contactId")
        var numbers: List<PhoneNumber> = emptyList()
)