package com.gmail.rxjavaexemple.operatorsTransformingObservables

import io.reactivex.Observable


fun buffer() {
    val values = Observable.just("A", "B", "C", "D", "E", "F").buffer(2)
    val subscription = values.subscribe({ strings: List<String?> ->
        for (s in strings) {
            println("String: $s")
        }
    }, { onError ->
        println("onError = ${onError.printStackTrace()}")
    }, {
        println("onComplete")

    }) {
        println("onSubscribe")
    }
}