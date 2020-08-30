package ru.romanoval.myhabits_ca_modules.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.romanoval.data.restful.ApiService
import ru.romanoval.data.source.cloud.BaseCloudRepository
import ru.romanoval.data.source.cloud.CloudRepository
import ru.romanoval.domain.model.Habit
import ru.romanoval.myhabits_ca_modules.core.Config
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(Config.HOST)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesGson(): Gson{
        return GsonBuilder()
            .registerTypeAdapter(
                Habit::class.java,
                Habit.HabitJsonSerializer()
            )
            .registerTypeAdapter(
                Habit::class.java,
                Habit.HabitJsonDeserializer()
            )
            .create()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory{
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit) : ApiService{
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideCloudRepository(service: ApiService): BaseCloudRepository{
        return CloudRepository(service)
    }

}