package com.yabu.floor.data.local.qoala

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.model.search.SearchHistoryItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [
    Portfolio::class,
    SearchHistoryItem::class],
    version = 4,
    exportSchema = false)
@TypeConverters(PortfolioConverter::class)
abstract class QoalaDatabase: RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao

    abstract fun searchHistoryDao(): SearchHistoryDao
}

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideSearchHistoryDao(db: QoalaDatabase): SearchHistoryDao {
        return db.searchHistoryDao()
    }
    @Provides
    fun providePortfolioDao(db: QoalaDatabase): PortfolioDao {
        return db.portfolioDao()
    }

    @Provides
    @Singleton
    fun provideQoalaDatabase(@ApplicationContext appContext: Context): QoalaDatabase {
        return Room.databaseBuilder(
            appContext,
            QoalaDatabase::class.java,
            "qoala-db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
