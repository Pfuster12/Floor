package com.yabu.floor.data.local.iex

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yabu.floor.data.NetworkConstants
import com.yabu.floor.data.model.iex.*
import com.yabu.floor.data.services.IEXCloudWebservice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Database(
    entities = [
        Quote::class,
        ChartHistoricalData::class,
        SearchItem::class,
        Company::class,
        KeyStats::class,
        NewsItem::class,
        SectorPerformance::class
    ],
    version = 15,
    exportSchema = false)
@TypeConverters(IEXCloudConverter::class)
abstract class IEXCloudDatabase : RoomDatabase() {
    abstract fun iexCloudDao(): IEXCloudDao
}

@Module
@InstallIn(SingletonComponent::class)
class IEXCloudModule {
    @Provides
    fun provideIEXCloudDao(db: IEXCloudDatabase): IEXCloudDao {
        return db.iexCloudDao()
    }

    @Provides
    @Singleton
    fun provideIEXCloudDatabase(@ApplicationContext appContext: Context): IEXCloudDatabase {
        return Room.databaseBuilder(
            appContext,
            IEXCloudDatabase::class.java,
            "iexcloud-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.IEX_CLOUD_PROD_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideWebService(retrofit: Retrofit): IEXCloudWebservice
            = retrofit.create(IEXCloudWebservice::class.java)

}

