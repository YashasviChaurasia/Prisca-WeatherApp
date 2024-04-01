package com.example.marsphotos.data


import androidx.room.PrimaryKey
import androidx.room.Entity



/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val city: String,
    val date: String,
    val mint: Number,
    val maxt: Number
)
