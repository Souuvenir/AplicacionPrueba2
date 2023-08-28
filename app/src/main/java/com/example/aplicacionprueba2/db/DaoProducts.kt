package com.example.aplicacionprueba2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoProducts {

    @Query("SELECT * FROM Products ORDER BY id")
    fun select(): List<Products>

    @Query("SELECT * FROM Products WHERE id = :productId")
    fun selectByIds(productId: IntArray): List<Products>

    @Query("SELECT count(*) FROM Products")
    fun countAllProducts(): Int

    @Insert
    fun insert(product: Products):Long

    @Update
    fun update(product: Products)

    @Delete
    fun delete(product: Products)

}