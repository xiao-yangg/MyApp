package com.example.myapp.data

import com.example.myapp.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var apiInstance: Retrofit? = null

    val instance: Retrofit
        get() {
            if (apiInstance === null)
                apiInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()

            return apiInstance!!
        }
}