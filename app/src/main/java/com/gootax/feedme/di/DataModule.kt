package com.gootax.feedme.di

import android.content.Context
import com.gootax.feedme.data.network.ApiService
import com.gootax.feedme.data.network.NetworkClient
import com.gootax.feedme.data.network.RetrofitNetworkClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideNetworkClient(
        @ApplicationContext context: Context,
        service: ApiService
    ): NetworkClient {
        return RetrofitNetworkClient(
            context = context,
            service = service
        )
    }

    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://suggestions.dadata.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}
