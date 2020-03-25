package com.gmail.rxjavaexemple.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.gmail.rxjavaexemple.R
import com.gmail.rxjavaexemple.net.ExchangeRate
import com.gmail.rxjavaexemple.net.RetrofitPbServiceFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class HelloWorldActivity : AppCompatActivity() {
    @BindView(R.id.hello_world_salute)
    lateinit var helloWorldSalute: TextView

    @BindView(R.id.stock_updates_recycler_view)
    lateinit var recyclerView: RecyclerView
    lateinit var rvLayoutManager: LinearLayoutManager
    lateinit var rateDataAdapter: RateDataAdapter
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_world)
        ButterKnife.bind(this)

        rvLayoutManager = LinearLayoutManager(this)
        rateDataAdapter = RateDataAdapter()
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = rvLayoutManager
            adapter = rateDataAdapter
        }

        val pbService = RetrofitPbServiceFactory().create()
        val pbDisposable = Observable.interval(0, 5, TimeUnit.SECONDS)
            .flatMap {
                pbService.getRatePb("01.12.2019")
                    .toObservable()
            }.map {
                rateDataAdapter.bank = it.bank ?: ""
                rateDataAdapter.baseCurrencyLit = it.baseCurrencyLit ?: ""
                rateDataAdapter.date = it.date ?: ""
                it.exchangeRate
            }.flatMap {
                Observable.fromIterable(it)
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ exchangeRate ->
                rateDataAdapter.add(exchangeRate)
            }, {
                it.printStackTrace()
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        compositeDisposable.add(pbDisposable)
        val disposable2 = Observable.just("Hello! Please use this app responsibly!")
            .subscribe { helloWorldSalute.text = it }
        compositeDisposable.add(disposable2)
        val disposable3 = Observable.just(1, 2, 3, 4, 5)
            .subscribeOn(Schedulers.io())
            .doOnNext {
                Log.d("TAG", "${Thread.currentThread().name} item = $it")
            }
            .map { it * 2 }
            .doOnNext {
                Log.d("TAG", "${Thread.currentThread().name} item = $it")
            }

            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("TAG", "${Thread.currentThread().name} item  =  $it")
            }, {
                Log.d("TAG", "${Thread.currentThread().name} item  =  $it")
            })
        compositeDisposable.add(disposable3)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

fun log(stage: String, item: String) {
    Log.d(
        "APP", stage + ":" + Thread.currentThread().name + ":" +
                item
    )
}

fun log(stage: String) {
    Log.d("APP", stage + ":" + Thread.currentThread().name)
}


class RateDataAdapter(
    private val data: MutableList<ExchangeRate> = ArrayList(),
    var bank: String = "",
    var baseCurrencyLit: String = "",
    var date: String = ""
) :
    RecyclerView.Adapter<StockUpdateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockUpdateViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_update_item, parent, false)
        return StockUpdateViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: StockUpdateViewHolder, position: Int) {
        val exchangeRate = data[position]
        holder.setBank(bank)
        holder.setDate(date)
        holder.setBaseCurrency(exchangeRate.baseCurrency)
        holder.setCurrency(exchangeRate.currency)
        holder.setPurchaseRate(exchangeRate.purchaseRate)
        holder.setSaleRate(exchangeRate.saleRate)
        holder.setPurchaseRateNB(exchangeRate.purchaseRateNB)
        holder.setSaleRateNB(exchangeRate.saleRateNB)
    }

    fun add(newExchangeRate: ExchangeRate) {
        loop@ for (exchangeRate in data) {
            when (exchangeRate.currency) {
                newExchangeRate.currency -> {
                    if (exchangeRate.purchaseRateNB == newExchangeRate.purchaseRateNB && exchangeRate.purchaseRate == newExchangeRate.purchaseRate) {
                        return
                    }
                    break@loop
                }
            }
        }

        data.add(newExchangeRate)
        notifyItemInserted(data.size - 1)
    }

    fun addAll(exchangeRate: List<ExchangeRate>) {
        data.addAll(exchangeRate)
        notifyItemInserted(data.size - 1)
    }
}

class StockUpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.baseCurrency)
    lateinit var baseCurrency: TextView

    @BindView(R.id.bank)
    lateinit var bank: TextView

    @BindView(R.id.date)
    lateinit var date: TextView

    @BindView(R.id.saleRateNB)
    lateinit var saleRateNB: TextView

    @BindView(R.id.purchaseRateNB)
    lateinit var purchaseRateNB: TextView

    @BindView(R.id.saleRate)
    lateinit var saleRate: TextView

    @BindView(R.id.purchaseRate)
    lateinit var purchaseRate: TextView

    @BindView(R.id.currency)
    lateinit var currency: TextView

    fun setBaseCurrency(baseCurrency: String?) {
        this.baseCurrency.text = baseCurrency
    }

    fun setBank(bank: String?) {
        this.bank.text = bank
    }

    fun setSaleRateNB(saleRateNB: Double) {
        this.saleRateNB.text = "${DecimalFormat("#0.00000").format(BigDecimal(saleRateNB))}"
    }

    fun setPurchaseRateNB(purchaseRateNB: Double) {
        this.purchaseRateNB.text = "${DecimalFormat("#0.00000").format(BigDecimal(purchaseRateNB))}"
    }

    fun setSaleRate(saleRate: Double) {
        this.saleRate.text = "${DecimalFormat("#0.00000").format(BigDecimal(saleRate))}"
    }

    fun setPurchaseRate(purchaseRate: Double) {
        this.purchaseRate.text = "${DecimalFormat("#0.00000").format(BigDecimal(purchaseRate))}"
    }

    fun setDate(date: String) {
        this.date.text = date
    }

    fun setCurrency(currency: String?) {
        this.currency.text = currency
    }

    init {
        ButterKnife.bind(this, itemView)
    }
}