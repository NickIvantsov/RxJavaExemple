package com.gmail.rxjavaexemple

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HelloWorldActivity : AppCompatActivity() {
    @BindView(R.id.hello_world_salute)
    lateinit var helloWorldSalute: TextView

    @BindView(R.id.stock_updates_recycler_view)
    lateinit var recyclerView: RecyclerView
    lateinit var rvLayoutManager: LinearLayoutManager
    lateinit var stockDataAdapter: StockDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_world)
        ButterKnife.bind(this)

        rvLayoutManager = LinearLayoutManager(this)
        stockDataAdapter = StockDataAdapter()
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = rvLayoutManager
            adapter = stockDataAdapter
        }

        Observable.just(
                StockUpdate("APPL", BigDecimal(12.43), Date()),
                StockUpdate("GOOGLE", BigDecimal(12.43), Date()),
                StockUpdate("TWTR", BigDecimal(12.43), Date())
            )
            .subscribe {
                Log.d("APP", "New update $it")
                stockDataAdapter.add(it)
            }

        Observable.just("Hello! Please use this app responsibly!")
            .subscribe { helloWorldSalute.text = it }

        Observable.just(1, 2, 3, 4, 5)
            .subscribeOn(Schedulers.io())
            .doOnNext {
                Log.d("TAG", "${Thread.currentThread().name} item = $it")
            }
            .map { it * 2 }
            .doOnNext {
                Log.d("TAG", "${Thread.currentThread().name} item = $it")
            }

            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("TAG", "${Thread.currentThread().name} item  =  $it")
            }
    }


}


class StockDataAdapter(private val data: MutableList<StockUpdate> = ArrayList()) :
    RecyclerView.Adapter<StockUpdateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockUpdateViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_update_item, parent, false)
        return StockUpdateViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: StockUpdateViewHolder, position: Int) {
        val stockUpdate = data[position]
        holder.setData(stockUpdate.stockSymbol, stockUpdate.prise, stockUpdate.date)
    }

    fun add(stockSymbol: StockUpdate) {
        data.add(stockSymbol)
        notifyItemInserted(data.size - 1)
    }
}

class StockUpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.stock_item_symbol)
    lateinit var stockSymbol: TextView

    @BindView(R.id.stock_item_price)
    lateinit var price: TextView

    @BindView(R.id.stock_item_date)
    lateinit var date: TextView

    fun setStockSymbol(stockSymbol: String) {
        this.stockSymbol.text = stockSymbol
    }

    fun setPrise(prise: BigDecimal) {
        this.price.text = DecimalFormat("#0.00").format(prise)
    }

    fun setDate(date: Date) {
        this.date.text = (SimpleDateFormat.getDateTimeInstance().format(date))
    }

    fun setData(stockSymbol: String, prise: BigDecimal, date: Date) {
        this.stockSymbol.text = stockSymbol
        this.price.text = DecimalFormat("#0.00").format(prise)
        this.date.text = (SimpleDateFormat.getDateTimeInstance().format(date))
    }

    init {
        ButterKnife.bind(this, itemView)
    }
}

@Parcelize
data class StockUpdate(val stockSymbol: String, val prise: BigDecimal, val date: Date) : Parcelable