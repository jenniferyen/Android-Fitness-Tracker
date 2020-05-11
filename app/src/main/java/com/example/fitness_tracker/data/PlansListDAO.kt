package com.example.fitness_tracker.data

import androidx.room.*

@Dao
interface PlansListDAO {

    @Query("SELECT * FROM items")
    fun getAllItems(): List<Item>

    @Insert
    fun addItem(item: Item): Long

    @Delete
    fun deleteItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Query("DELETE FROM items")
    fun deleteAll()
}