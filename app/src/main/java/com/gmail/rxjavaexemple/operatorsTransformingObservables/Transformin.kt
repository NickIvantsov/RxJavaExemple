package com.gmail.rxjavaexemple.operatorsTransformingObservables

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers


//В сценариях мы можем использовать разные операторы:
//Оператор карты может использоваться, когда мы выбираем элементы с сервера и должны изменить
// его перед отправкой в ​​пользовательский интерфейс.
//Оператор FlatMap может использоваться, когда мы знаем, что порядок элементов не важен.
//SwitchMap лучше всего подходит для сценариев, таких как страница канала, когда включено
// обновление до обновления. Когда пользователь обновляет экран, более ранний ответ канала
// игнорируется, и только самые последние результаты запроса передаются в пользовательский
// интерфейс при использовании SwitchMap.

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

/**
 * Всякий раз, когда новый объект испускается Наблюдаемым, он отменяет подписку на Наблюдаемый,
 * созданный из ранее испущенного элемента, и начинает отражать только текущий. Другими словами,
 * он возвращает последнюю Наблюдаемую и испускает элементы из нее.
 *
 * Output:
onNext: 12 (или 6*2)
 */
fun switchMap() {
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
    val subscription = value.switchMap {
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
}

/**
 * Этот оператор функционирует так же, как flatMap()и разница в concatMap()том,
 * в каком порядке отправляются элементы. Одним из недостатков concatMap()является то,
 * что каждый наблюдатель ожидает завершения всей работы, пока не будет обработана следующая.
 *
 *
onSubscribe
onNext: 2
onNext: 4
onNext: 6
onNext: 8
onNext: 10
onNext: 12
onComplete
 */
fun concatMap() {
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
    val subscription = value.concatMap {
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

}

/**
 * Этот оператор делит Observable на набор Observable, каждый из которых испускает отдельную
 * группу элементов из исходного Observable, упорядоченного по ключу.
 */
fun groupBy() {

    val value = Observable.range(1, 10).groupBy {
        it % 2 == 0
    }
    val subscription = value.subscribe({ booleanIntegerGroupedObservable ->
        booleanIntegerGroupedObservable.subscribe({ integer ->
            println("onNext: $integer")
        }, { onError ->
            println("onError = ${onError.printStackTrace()}")

        }, {
            println("onComplete")
        }) {
            println("onSubscribe")
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
 * Этот оператор превращает каждый элемент в другой элемент, как вы сделали с map.
 * Но также включите «предыдущий» элемент, когда вы приступаете к выполнению преобразования.
 */
fun scan() {
    val value = Observable.range(1, 10)
        .scan { integer, integer2 ->
            println("integer = $integer integer2 = $integer2")
            integer + integer2
        }

    val subscription = value.subscribe({
        println("onNext: $it")
    }, { onError ->
        println("onError = ${onError.printStackTrace()}")
    }, {
        println("onComplete")
    }) {
        println("onSubscribe")
    }
}