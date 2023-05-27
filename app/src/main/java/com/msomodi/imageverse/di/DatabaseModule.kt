package com.msomodi.imageverse.di

import android.content.Context
import androidx.room.Room
import com.msomodi.imageverse.db.ImageverseDatabase
import com.msomodi.imageverse.db.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val IMAGEVERSE_DATABASE = "imageverse_database"

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) : ImageverseDatabase{
        return Room.databaseBuilder(
            context,
            ImageverseDatabase::class.java,
            IMAGEVERSE_DATABASE)
        .fallbackToDestructiveMigration()
        .build()
    }
}