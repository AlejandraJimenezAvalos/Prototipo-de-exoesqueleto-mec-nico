package com.example.exoesqueletov1.data.db

import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.models.*

@Database(
    entities = [
        UserModel::class,
        ProfileModel::class,
        UsersEntity::class,
        MessageModel::class,
        ChatsEntity::class,
        ExoskeletonModel::class,
        PatientModel::class,
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(
            from = 2,
            to = 3,
            spec = com.example.exoesqueletov1.data.db.RoomDatabase.AutoMigrationPatient::class
        ),
    ],
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun getDataDao(): Dao

    @RenameColumn(
        tableName = "patient",
        fromColumnName = "date",
        toColumnName = "yearsOld"
    )
    @DeleteColumn(
        tableName = "patient",
        columnName = "lastName"
    )
    @DeleteColumn(
        tableName = "patient",
        columnName = "school"
    )
    @DeleteColumn(
        tableName = "patient",
        columnName = "cell"
    )
    class AutoMigrationPatient : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
        }
    }
}