package com.gmail.rxjavaexemple.KeyTypes

import io.reactivex.subjects.AsyncSubject

/**
 * AsyncSubject также кэширует последнее значение. Разница теперь в том, что он ничего не излучает,
 * пока последовательность не завершится. Его использование заключается в выдаче одного значения
 * и немедленном завершении.
 *  Вывод:
 * System.out: 2
 *
 * Обратите внимание, что если бы мы этого не сделали s.onCompleted();, этот пример ничего бы не печатал.
 */
fun asyncSubject(){
    val s = AsyncSubject.create<Int>()
    s.subscribe { v: Int? -> println(v) }
    s.onNext(0)
    s.onNext(1)
    s.onNext(2)
    s.onComplete()
}