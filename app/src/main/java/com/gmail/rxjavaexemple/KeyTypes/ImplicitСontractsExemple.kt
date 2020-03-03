package com.gmail.rxjavaexemple.KeyTypes

import io.reactivex.Single
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject

/**
 * Как мы уже упоминали, в Rx есть контракты, которые не очевидны в коде. Важным является то,
 * что после события завершения ( onErrorили onCompleted) события не генерируются .
 * Внедренные субъекты уважают это, и subscribeметод также предотвращает некоторые
 * нарушения контракта.
 * Выход

0
Защитные сети, подобные этим, не гарантируются во всей реализации Rx.
Лучше всего, чтобы вы не нарушали договор, поскольку это может привести к неопределенному поведению.
 */
fun implicitContracts(){
    val s  = ReplaySubject.create<Int>()
    s.subscribe({ v -> System.out.println(v) })
    s.onNext(0)
    s.onComplete()
    s.onNext(1)
    s.onNext(2)
}