package com.example.todoapplication

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDAO {
    @Query("SELECT * FROM todoTBL ORDER BY id DESC")
    fun getTodos(): LiveData<MutableList<ToDo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo:ToDo)

    @Update
    suspend fun update(todo:ToDo)

    @Delete
    suspend fun delete(todo:ToDo)

    @Query("DELETE FROM todoTBL")
    suspend fun clear()
}




