package com.example.todoapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoTBL")
class ToDo( @PrimaryKey
            var id:Long?,

            @ColumnInfo(name = "uuid")
            var fullName: String,

            @ColumnInfo(name = "notes")
            var notes:String) {

}