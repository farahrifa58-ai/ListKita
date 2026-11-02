package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // "Produk", "Makanan", "Belanja", "Stok", "Acara", "Umum"
    val customColumnsJson: String = "[]", // JSON array of custom column names, e.g. ["Harga", "Lokasi"]
    val isTemplate: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
