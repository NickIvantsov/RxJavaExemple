package com.gmail.rxjavaexemple.operatorsTransformingObservables

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers

/**
 * http://reactivex.io/documentation/operators/buffer.html
 *
 * периодически собирайте предметы, испускаемые Обсерваторией, в пакеты и испускайте эти пакеты,
 * а не испускайте элементы по одному
 *
 *
 * Output:
onSubscribe
onNext
String: A
String: B
onNext
String: C
String: D
onNext
String: E
String: F
onComplete


 */
fun buffer() {
    val values = Observable.just("A", "B", "C", "D", "E", "F").buffer(2)
    val subscription = values.subscribe({ strings: List<String?> ->
        println("onNext")
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

/**
 * http://reactivex.io/documentation/operators/map.html
 *
 * преобразовать элементы, испускаемые Observable, применяя функцию к каждому элементу
 *
 * Оператор Map применяет выбранную вами функцию к каждому элементу, излучаемому исходной Observable,
 * и возвращает Observable, который испускает результаты этих приложений-функций.
 *
 * onSubscribe
onNext: 2
onNext: 4
onNext: 6
onNext: 8
onNext: 10
onNext: 12
onComplete
 */
fun map() {
    val integers = listOf(1, 2, 3, 4, 5, 6)
    val value = Observable.create { emitter: ObservableEmitter<Int> ->
        integers.forEach {
            when {
                !emitter.isDisposed -> emitter.onNext(it)
            }
        }
        when {
            !emitter.isDisposed -> emitter.onComplete()
        }
    }

    val subscription = value.map { it * 2 }
        .subscribeOn(Schedulers.io())
        .subscribe({ integer ->
            println("onNext: $integer")
        }, { onError ->
            println("onError = ${onError.printStackTrace()}")
        }, {
            println("onComplete")

        }) {
            println("onSubscribe")
        }
}

/**
 * http://reactivex.io/documentation/operators/flatmap.html
 *
 * преобразовать объекты, испускаемые Обсерваторией, в Наблюдаемые,
 * а затем сгладить выбросы
 * от этих объектов в единую Наблюдаем
 *
 *  onSubscribe
 onNext: 2
 onNext: 6
 onNext: 4
 onNext: 10
 onNext: 8
 onNext: 12
 onComplete
 *
 */
fun flatMap() {
    val integers = listOf(1, 2, 3, 4, 5, 6)
    val value = Observable.create { emitter: ObservableEmitter<Int> ->
        integers.forEach {
            when {
                !emitter.isDisposed -> emitter.onNext(it)
            }
        }
        when {
            !emitter.isDisposed -> emitter.onComplete()
        }
    }


    val subscription = value.flatMap {
            getModifiedObservable(it)
        }.subscribeOn(Schedulers.io())
        .subscribe({ integer ->
            println("onNext: $integer")
        }, { onError ->
            println("onError = ${onError.printStackTrace()}")
        }, {
            println("onComplete")

        }) {
            println("onSubscribe")
        }
    Thread.sleep(2000)
}

private fun getModifiedObservable(integer: Int): Observable<Int> =
    Observable.create { emitter: ObservableEmitter<Int> ->
        emitter.onNext(integer * 2)
        emitter.onComplete()
    }.subscribeOn(Schedulers.io())