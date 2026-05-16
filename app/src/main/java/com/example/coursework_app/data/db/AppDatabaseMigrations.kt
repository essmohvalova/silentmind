package com.example.coursework_app.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

    /**
     * Renames `title` → `emotion` on `notes`, preserves row data (legacy emotion strings / JSON).
     */
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `notes_new` (
                    `id` TEXT NOT NULL,
                    `emotion` TEXT NOT NULL,
                    `notes` TEXT NOT NULL,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent(),
            )
            db.execSQL(
                """
                INSERT INTO `notes_new` (`id`, `emotion`, `notes`)
                SELECT `id`, `title`, `notes` FROM `notes`
                """.trimIndent(),
            )
            db.execSQL("DROP TABLE `notes`")
            db.execSQL("ALTER TABLE `notes_new` RENAME TO `notes`")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_notes_id` ON `notes` (`id`)")
        }
    }
}
