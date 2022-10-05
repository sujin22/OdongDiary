package com.example.owndiary.di

import android.content.Context
import androidx.room.Room
import com.example.owndiary.data.DiaryDao
import com.example.owndiary.data.DiaryRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDiaryDao(diaryRoomDatabase: DiaryRoomDatabase) : DiaryDao
        = diaryRoomDatabase.diaryDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : DiaryRoomDatabase
    = Room.databaseBuilder(
        context,
        DiaryRoomDatabase::class.java,
        "diary_database")
        .fallbackToDestructiveMigration()
        .build()
}