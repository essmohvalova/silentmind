// data/db/entity/UserEntity.kt
package com.example.coursework_app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "character_type")
    val characterType: String,
    @ColumnInfo(name = "onboarding_completed")
    val onboardingCompleted: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CHARACTER_TYPE = "character_type"
        const val COLUMN_ONBOARDING_COMPLETED = "onboarding_completed"
        const val COLUMN_CREATED_AT = "created_at"
    }
}