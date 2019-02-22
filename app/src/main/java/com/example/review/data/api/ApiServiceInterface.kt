package com.example.jogjatour.data.api

import com.example.review.utils.Constants
import com.example.review.data.model.ReviewResponse
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiServiceInterface {

    @POST("api/v1/reviews/")
    fun createReview (): Observable<ResponseBody>

    @GET("api/v1/reviews/")
    fun getReview(): Observable<ReviewResponse>

    @DELETE("api/v1/reviews/1")
    fun deleteReview(): Observable<ResponseBody>

    companion object Factory {
        fun create(): ApiServiceInterface {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.app_reference)
                .build()

            return retrofit.create(ApiServiceInterface::class.java)
        }


        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}