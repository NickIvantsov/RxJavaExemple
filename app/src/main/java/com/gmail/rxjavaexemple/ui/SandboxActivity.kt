package com.gmail.rxjavaexemple.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gmail.rxjavaexemple.R
import io.reactivex.Observable

class SandboxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sandbox)
        Observable.create<String> { s ->
            s.onNext("Hello world!")
            s.onComplete()
        }.subscribe { hello ->
            println(hello)
        }

        val o = Observable.create<Int> { s ->
            s.onNext(1)
            s.onNext(2)
            s.onNext(3)
            s.onComplete()
        }

        o.map {
            "Number $it"
        }.subscribe {
            println(it)
        }

        val a = Observable.create<String> {
            Thread() {
                it.onNext("one")
                it.onNext("two")
                it.onComplete()
            }.start()
        }
        val b = Observable.create<String> {
            Thread() {
                it.onNext("three")
                it.onNext("four")
                it.onComplete()
            }.start()
        }
        val c = Observable.merge(a, b).subscribe() {
            println(it)
        }
    }
}
