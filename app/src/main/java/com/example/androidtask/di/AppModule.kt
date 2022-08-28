package com.example.androidtask.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.androidtask.R
import com.example.androidtask.data.data_source.LocationDataSource
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
    fun provideContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.color.black )
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )


    @Singleton
    @Provides
    fun provideLocationDataSource(@ApplicationContext context: Context): LocationDataSource =
        LocationDataSource(context)



}