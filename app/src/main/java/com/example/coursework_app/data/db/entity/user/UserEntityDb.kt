package com.example.coursework_app.data.db.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = [UserEntityDb.Column.ID])]
)
data class UserEntityDb(

    @PrimaryKey
    @ColumnInfo(name = Column.ID)
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "characterType")
    val characterType: CharacterTypeDb,
) {

    object Column {

        const val ID: String = "id"
    }
}
