package com.gmail.rxjavaexemple.net

import android.os.Parcelable
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class RetrofitYahooServiceFactory {
    //    перехватчик, который мы будемиспользовать для регистрации
    val interceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.privatbank.ua/")
        .build()

    fun create(): YahooService {
        return retrofit.create(YahooService::class.java)
    }
}

//https://api.privatbank.ua/p24api/exchange_rates?json&date=01.12.2014
interface YahooService {
    @GET("p24api/exchange_rates?json")
    fun yqlQuery(@Query("date") date: String): Single<RatePb>
}

@Parcelize
data class RatePb(
    val bank: String = "Not init",
    val baseCurrency: Int = -1,
    val baseCurrencyLit: String = "Not init",
    val date: String = "Not init",
    val exchangeRate: List<ExchangeRate> = emptyList()
) : Parcelable

@Parcelize
data class ExchangeRate(
    val baseCurrency: String = "Not init",
    val currency: String = "Not init",
    val purchaseRate: Double = -1.0,
    val purchaseRateNB: Double = -1.0,
    val saleRate: Double = -1.0,
    val saleRateNB: Double = -1.0
) : Parcelable