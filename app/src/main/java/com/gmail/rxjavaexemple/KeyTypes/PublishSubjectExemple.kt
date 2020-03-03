package com.gmail.rxjavaexemple.KeyTypes

import io.reactivex.subjects.PublishSubject

//https://habr.com/ru/company/rambler-co/blog/280388/
//Начиная с выполнения содержимого Observable и до получения результата, все методы выполнялись в потоке, созданном Schedulers.io().
/**
 * PublishSubjectэто самый прямой вид предмета.
 * Когда значение помещается в a PublishSubject, субъект передает его каждому подписчику,
 * который подписан на него в этот момент.
 * Вывод:
 * 2
 * 3
 * 4
 *
 * Как мы видим из примера, 1не печатается, потому что мы не были подписаны, когда он был отправлен.
 * После того, как мы подписались, мы начали получать значения, которые были добавлены к теме.

Мы видим subscribeэто впервые, поэтому стоит обратить внимание на то, как его использовали.
В этом случае мы использовали перегрузку, которая ожидает одну функцию для случая onNext.
Эта функция принимает аргумент типа Integerи ничего не возвращает. Функции без типа возврата
также называются действиями. Мы можем предоставить эту функцию различными способами:

мы можем предоставить экземпляр Action1<Integer>,
неявно создать один, используя лямбда-выражение или
передать ссылку на существующий метод, который соответствует подписи. В этом случае
System.out::printlnимеет перегрузку, которая принимает Object, поэтому мы передали ссылку на него.
subscribeвызовем printlnс поступающими значениями в качестве аргумента.
 */
fun publish(){
    val subject = PublishSubject.create<Int>()
    subject.onNext(1)
    subject.subscribe { x: Int? -> println(x) }
    subject.onNext(2)
    subject.onNext(3)
    subject.onNext(4)
}