package com.gmail.rxjavaexemple

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.functions.Consumer

class HelloWorldActivity : AppCompatActivity() {
    @BindView(R.id.hello_world_salute)
    lateinit var helloWorldSalute: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_world)
        ButterKnife.bind(this)

        Observable.just("Hello! Please use this app responsibly!")
            .subscribe(Consumer {
                helloWorldSalute.text = it
            })
    }
}
