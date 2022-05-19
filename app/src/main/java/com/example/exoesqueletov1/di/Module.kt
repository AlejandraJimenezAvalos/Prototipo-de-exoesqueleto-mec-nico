package com.example.exoesqueletov1.di

import android.content.Context
import androidx.room.Room
import com.example.exoesqueletov1.data.db.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun providerDataDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        RoomDatabase::class.java,
        "userDatabase"
    ).build()

    @Singleton
    @Provides
    fun provideDataDao(db: RoomDatabase) = db.getDataDao()
}