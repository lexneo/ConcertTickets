package com.lexneoapps.concerttickets.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.lexneoapps.concerttickets.data.local.AppDatabase
import com.lexneoapps.concerttickets.data.remote.ConcertTicketsApi
import com.lexneoapps.concerttickets.utils.Constants.APP_DATABASE_NAME
import com.lexneoapps.concerttickets.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, AppDatabase::class.java, APP_DATABASE_NAME)
        .fallbackToDestructiveMigration().build()


    @Provides
    fun providesDiscountedDao(db: AppDatabase) = db.discountedDao()

    @Provides
    fun providesNonDiscountedDao(db: AppDatabase) = db.nonDiscountedDao()

    @Singleton
    @Provides
    fun provideConcertTicketsApi(): ConcertTicketsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ConcertTicketsApi::class.java)
    }


}