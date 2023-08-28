package com.example.aplicacionprueba2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Products(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var price: Int,
    var category: String,
    var bought: Boolean
    ) {
}