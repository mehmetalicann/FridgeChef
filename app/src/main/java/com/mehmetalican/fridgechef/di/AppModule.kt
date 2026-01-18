package com.mehmetalican.fridgechef.di

import android.content.Context
import androidx.room.Room
import com.mehmetalican.fridgechef.BuildConfig
import com.mehmetalican.fridgechef.data.local.FridgeChefDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FridgeChefDatabase {
        return Room.databaseBuilder(
            context,
            FridgeChefDatabase::class.java,
            "fridge_chef_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTranslationManager(): com.mehmetalican.fridgechef.domain.manager.TranslationManager {
        return com.mehmetalican.fridgechef.data.manager.GoogleTranslationManager()
    }
}
