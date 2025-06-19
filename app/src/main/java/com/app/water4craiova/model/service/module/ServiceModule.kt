package com.app.water4craiova.model.service.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.app.water4craiova.model.service.AccountService
import com.app.water4craiova.model.service.LocationService
import com.app.water4craiova.model.service.DatabaseService
import com.app.water4craiova.model.service.StorageService
import com.app.water4craiova.model.service.impl.AccountServiceImpl
import com.app.water4craiova.model.service.impl.LocationServiceImpl
import com.app.water4craiova.model.service.impl.DatabaseServiceImpl
import com.app.water4craiova.model.service.impl.StorageServiceImpl
import com.app.water4craiova.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationService(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationService = LocationServiceImpl(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )

    @Provides
    @Singleton
    fun provideAccountService():AccountService = AccountServiceImpl()

    @Provides
    @Singleton
    fun provideDatabaseService():DatabaseService = DatabaseServiceImpl()

    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext appContext: Context
    ): Context = appContext

    @Singleton
    @Provides
    fun provideSharedPreferences(
        application: Application
    ): StorageService = StorageServiceImpl(
        sharedPreferences = application.getSharedPreferences(
            Constants.SHARED_PREF,
            Context.MODE_PRIVATE
        )
    )
}