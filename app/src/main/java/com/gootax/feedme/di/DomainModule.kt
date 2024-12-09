package com.gootax.feedme.di

import com.gootax.feedme.converter.Converter
import com.gootax.feedme.data.impl.RepositoryImpl
import com.gootax.feedme.data.network.NetworkClient
import com.gootax.feedme.domain.api.Interactor
import com.gootax.feedme.domain.impl.InteractorImpl
import com.gootax.feedme.domain.api.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideInteractor(repository: Repository): Interactor {
        return InteractorImpl(repository = repository)
    }

    @Provides
    fun provideRepository(client: NetworkClient, converter: Converter): Repository {
        return RepositoryImpl(client = client, converter = converter)
    }

}
