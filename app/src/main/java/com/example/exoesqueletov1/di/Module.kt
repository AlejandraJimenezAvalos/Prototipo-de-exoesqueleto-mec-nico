package com.example.exoesqueletov1.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.exoesqueletov1.data.db.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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

    @Provides
    @Named("patient")
    fun provideSharedPreferencePatient(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("patient", Context.MODE_PRIVATE)
    }

    @Provides
    @Named("user")
    fun provideSharedPreferenceUser(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    @Provides
    @Named("formPatient")
    fun provideSharedPreferenceFormPatient(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("formPatient", Context.MODE_PRIVATE)
    }

    @Provides
    @Named("consultationTemporary")
    fun provideSharedPreferenceConsultationTemporary(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("consultationTemporary", Context.MODE_PRIVATE)
    }

}