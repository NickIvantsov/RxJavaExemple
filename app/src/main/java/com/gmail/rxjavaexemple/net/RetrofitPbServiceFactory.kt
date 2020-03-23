package com.gmail.rxjavaexemple.net

import android.os.Parcelable
import com.gmail.rxjavaexemple.AppApplication
import com.gmail.rxjavaexemple.R
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val NOT_INIT_D = -1.0
const val NOT_INIT_I = 1

class RetrofitPbServiceFactory {
    //    перехватчик, который мы будемиспользовать для регистрации
    private val interceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.privatbank.ua/")
        .build()

    fun create(): PBService {
        return retrofit.create(PBService::class.java)
    }
}

interface PBService {
    @GET("p24api/exchange_rates?json")
    fun getRatePb(@Query("date") date: String = "01.12.2014"): Single<RatePb>
}

/*@Parcelize
data class RatePb(
    val bank: String = resString(R.string.not_init),
    val baseCurrency: Int = NOT_INIT_I,
    val baseCurrencyLit: String = resString(R.string.not_init),
    val date: String = resString(R.string.not_init),
    val exchangeRate: List<ExchangeRate> = emptyList()
) : Parcelable

@Parcelize
data class ExchangeRate(
    val baseCurrency: String = resString(R.string.not_init),
    val currency: String = resString(R.string.not_init),
    val purchaseRate: Double = NOT_INIT_D,
    val purchaseRateNB: Double = NOT_INIT_D,
    val saleRate: Double = NOT_INIT_D,
    val saleRateNB: Double = NOT_INIT_D
) : Parcelable*/

data class RatePb(
    val bank: String?,
    val baseCurrency: Int,
    val baseCurrencyLit: String?,
    val date: String?,
    val exchangeRate: List<ExchangeRate> = emptyList()
)

data class ExchangeRate(
    val baseCurrency: String?,
    val currency: String?,
    val purchaseRate: Double,
    val purchaseRateNB: Double,
    val saleRate: Double,
    val saleRateNB: Double
)

fun resString(id: Int): String = AppApplication.context?.getString(id) ?: "error"