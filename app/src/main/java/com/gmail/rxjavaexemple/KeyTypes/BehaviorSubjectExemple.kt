package com.gmail.rxjavaexemple.KeyTypes

import io.reactivex.subjects.BehaviorSubject

/**
 * Поскольку определяющая роль BehaviorSubjectвсегда заключается в том, чтобы всегда иметь значение,
 * легко доступное, необычно создавать значение без начального значения. Это также необычно,
 * чтобы прекратить один.
 *
 * BehaviorSubjectпомнит только последнее значение. Он похож на ReplaySubjectбуфер с размером 1.
 * Начальное значение может быть предоставлено при создании, поэтому гарантируется,
 * что значение всегда будет доступно сразу при подписке.
 * Выход

Late: 2
Late: 3
 */
fun behaviorSubject(){
    val s = BehaviorSubject.create<Int>()
    s.onNext(0)
    s.onNext(1)
    s.onNext(2)
    s.subscribe { v: Int -> println("Late: $v") }
    s.onNext(3)
}

/**
 * Следующий пример просто завершается, так как это последнее событие.
 */
fun behaviorSubjectSecond(){
    val s = BehaviorSubject.create<Int>()
    s.onNext(0)
    s.onNext(1)
    s.onNext(2)
    s.onComplete()
    s.subscribe(
        { v: Int -> println("Late: $v") },
        { e: Throwable? -> println("Error") }
    ) { println("Completed") }
}
/*
не работает
Начальное значение предоставляется, чтобы быть доступным, если кто-либо подписывается до того, как первое значение будет отправлено.
fun behaviorSubjectWithBefore(){
    val s: BehaviorSubject<Int> = BehaviorSubject.create(0)
    s.subscribe { v: Int? -> println(v) }
    s.onNext(1)
}*/
