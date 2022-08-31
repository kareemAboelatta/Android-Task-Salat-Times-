package com.example.androidtask.di

import android.content.Context
import com.example.androidtask.core.Constants
import com.example.androidtask.data.data_source.LocationDataSource
import com.example.androidtask.data.remote.SalatTimesApi
import com.example.androidtask.data.repository.LocationRepositoryImp
import com.example.androidtask.data.repository.RepositoryImp
import com.example.androidtask.domain.repository.LocationRepository
import com.example.androidtask.domain.repository.Repository
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
object RepositoryModule {


    @Provides
    @Singleton
    fun provideRepository(api: SalatTimesApi): Repository {
        return RepositoryImp(api)
    }

    @Singleton
    @Provides
    fun providesLocationRepository(LocationDataSource: LocationDataSource): LocationRepository {
        return LocationRepositoryImp(LocationDataSource)
    }

}