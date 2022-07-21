package com.example.exoesqueletov1.data.db

import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.models.*
import com.example.exoesqueletov1.data.models.consultation.*

@Database(
    entities = [
        UserModel::class,
        ProfileModel::class,
        UsersEntity::class,
        MessageModel::class,
        ChatsEntity::class,
        ExoskeletonModel::class,
        PatientModel::class,
        ExpedientModel::class,
        Analisis::class,
        ConsultationData::class,
        EvaluacionMuscular::class,
        EvaluacionMusculo::class,
        EvaluacionPostura::class,
        ExploracionFisica::class,
        Marcha::class,
        Plan::class,
        ValoracionFuncional::class,
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(
            from = 2,
            to = 3,
            spec = com.example.exoesqueletov1.data.db.RoomDatabase.AutoMigrationPatient::class
        ),
        AutoMigration(
            from = 3,
            to = 4,
            spec = com.example.exoesqueletov1.data.db.RoomDatabase.AutoMigrationPatient2::class
        ),
        AutoMigration(
            from = 4,
            to = 5
        )
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

    @RenameColumn(
        tableName = "patient",
        fromColumnName = "yearsOld",
        toColumnName = "birthday"
    )
    @DeleteColumn(
        tableName = "patient",
        columnName = "familyBackground"
    )
    @DeleteColumn(
        tableName = "patient",
        columnName = "background"
    )
    class AutoMigrationPatient2 : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            super.onPostMigrate(db)
        }
    }
}