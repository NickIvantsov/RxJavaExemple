package com.gmail.rxjavaexemple.KeyTypes

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit


/**
 * ReplaySubjectимеет особенность кэширования всех значений, переданных ему. Когда создается
 * новая подписка, последовательность событий воспроизводится с самого начала для нового подписчика.
 * После наверстывания каждый подписчик получает новые события по мере их поступления.
 * Выход

Early:0
Early:1
Late: 0
Late: 1
Early:2
Late: 2
Все значения получены подписчиками, хотя один опоздал. Также обратите внимание,
что покойный подписчик переиграл все, прежде чем перейти к следующему значению.

Кэширование всего не всегда хорошая идея, так как наблюдаемая последовательность
может работать долго. Есть способы ограничить размер внутреннего буфера.
 */
fun replay() {
    val s = ReplaySubject.create<Int>()
    s.subscribe { v: Int -> println("Early:$v") }
    s.onNext(0)
    s.onNext(1)
    s.subscribe { v: Int -> println("Late: $v") }
    s.onNext(2)
}

/**

ReplaySubject.createWithSizeограничивает размер буфера,
а также ReplaySubject.createWithTimeограничивает срок хранения объекта в кэше.
Вывод:
Late: 2
Late: 3
Late: 4

Наш последний подписчик пропустил первое и второе значение, которое упало в буфере размера 2.
 */
fun replayWithClearCash() {
    val s = ReplaySubject.createWithSize<Int>(2)
    s.onNext(0)
    s.onNext(1)
    s.onNext(2)
    s.onNext(3)
    s.subscribe { v: Int -> println("Late: $v") }
    s.onNext(4)
}

/**
 *  Аналогично, старые значения со временем удаляются из буфера, когда тема создается с createWithTime
 */
fun replayWithTime() {
    val s = ReplaySubject.createWithTime<Int>(
        150, TimeUnit.MILLISECONDS,
        AndroidSchedulers.mainThread()
    )
    s.onNext(0)
    Thread.sleep(100)
    s.onNext(1)
    Thread.sleep(100)
    s.onNext(2)
    s.subscribe { v: Int -> println("Late: $v") }
    s.onNext(3)
}

/**
 * В следующем примере мы обрабатываем ошибку последовательности, которая не удалась.
 *
 * I/System.out: 0
W/System.err: java.lang.Exception: Oops

Если мы не обеспечиваем функции для обработки ошибок, OnErrorNotImplementedExceptionбудет
выброшено в точке , где s.onErrorназывается, который является стороной этого производителя.
Здесь случается, что производитель и потребитель находятся рядом, поэтому мы можем сделать
традиционную попытку. Однако в раздельной системе производитель и подписчик очень часто находятся
в разных местах. Если потребитель не предоставит дескриптор ошибок subscribe, он никогда не узнает,
что произошла ошибка и что последовательность была прервана.
 */
fun replayHandleError() {
    val s: Subject<Int> = ReplaySubject.create()
    s.subscribe(
        { v -> System.out.println(v) },
        { e -> System.err.println(e) })
    s.onNext(0)
    s.onError(Exception("Oops"))
}

//Вызов unsubscribe остановит передачу событий вашему наблюдателю.
fun replayDispose() {
    val values: Subject<Int> = ReplaySubject.create()
    val subscription = values.subscribe(
        Consumer { v: Int? -> println(v) },
        Consumer { e: Throwable? ->
            System.err.println(
                e
            )
        },
        Action { println("Done") }
    )
    values.onNext(0)
    values.onNext(1)
    subscription.dispose()
    values.onNext(2)
}

/**
 *
 * Отказ от подписки одного наблюдателя не мешает другим наблюдателям на той же самой наблюдаемой.
 *
 * Output

First: 0
Second: 0
First: 1
Second: 1
Unsubscribed first
Second: 2
 */
fun replayDisposeSecondExample() {
    val values: Subject<Int> = ReplaySubject.create()
    val subscription1 = values.subscribe(
        Consumer { v: Int -> println("First: $v") }
    )
    val subscription2 = values.subscribe(
        Consumer { v: Int -> println("Second: $v") }
    )
    values.onNext(0)
    values.onNext(1)
    subscription1.dispose()
    println("Unsubscribed first")
    values.onNext(2)
}
//onError and onCompleted
/***
 *
 * onErrorи onCompletedозначают завершение последовательности. Наблюдаемое, которое соответствует
 * контракту Rx, ничего не будет излучать после любого из этих событий. Это то, на что нужно обратить
 * внимание как при использовании в Rx, так и при реализации ваших собственных наблюдаемых.
 *
 * Output

First: 0
First: 1
Completed
 */
fun onCompleteExample() {
    val values: Subject<Int> = ReplaySubject.create()
    val subscription1 = values.subscribe(
        Consumer { v: Int -> println("First: $v") },
        Consumer { e: Throwable -> println("First: $e") },
        Action { println("Completed") }
    )
    values.onNext(0)
    values.onNext(1)
    values.onComplete()
    values.onNext(2)
}

/**
 * justМетод создает , Observable что будет излучать predifined последовательности значений, прилагаемую по созданию, а затем прекращается.
 * Выход

Received: one
Received: two
Received: three
Completed
 */
fun observableJust() {

    val values: Observable<String> = Observable.just("one", "two", "three")
    val subscription = values.subscribe(
        { v -> println("Received: $v") },
        { e -> println("Error: $e") },
        { println("Completed") }
    )
}

/**
 * Эта наблюдаемая будет испускать один onCompleted и ничего больше.
 */
fun observableEmpty() {
    val values =
        Observable.empty<String>()
    val subscription = values.subscribe(
        { v: String -> println("Received: $v") },
        { e: Throwable -> println("Error: $e") }
    ) { println("Completed") }
}

/**
 * createэто очень мощная функция для создания наблюдаемых. Давайте посмотрим на подпись.
 * Output

Received: Hello
Completed
Когда кто-то подписывается на наблюдаемое (здесь:) values, соответствующий Subscriberэкземпляр
передается вашей функции. Когда код выполняется, значения отправляются подписчику.
Обратите внимание, что вам нужно позвонить onCompletedв конце самостоятельно, если вы хотите,
чтобы последовательность сигнализировала о ее завершении.

Этот метод должен быть вашим предпочтительным способом создания настраиваемой наблюдаемой,
когда ни один из существующих сокращений не служит вашей цели. Код похож на то, как мы создали
Subjectи передали ему значения, но есть несколько важных отличий. Прежде всего, источник
событий аккуратно инкапсулирован и отделен от несвязанного кода. Во-вторых, Subjectнесут
в себе опасности, которые не очевидны: с помощью Subjectвы управляете состоянием, и любой,
имеющий доступ к экземпляру, может вставить в него значения и изменить последовательность.
Мы увидим больше об этой проблеме позже.

Другим ключевым отличием использования тем является то, что код выполняется лениво, когда и
если подписчик подписывается. В приведенном выше примере код запускается не тогда, когда создается
наблюдаемое (потому что его Subscriberеще нет ), а subscribeвызывается каждый раз . Это означает,
что каждое значение генерируется снова для каждого подписчика, аналогично ReplaySubject.
Конечный результат аналогичен a ReplaySubject, за исключением того, что кэширование не выполняется.
Однако, если бы мы использовали a ReplaySubject, а метод создания занимал много времени,
это заблокировало бы поток, который выполняет создание. Вы должны вручную создать новый поток,
чтобы вставить значения в Subject. Мы еще не представляем методы Rx для параллелизма,
но есть удобные способы onSubscribeодновременного выполнения функции.

Возможно, вы уже заметили, что можете легко реализовать любую из предыдущих наблюдаемых,
используя Observable.create. На самом деле наш пример для createэквивалентен Observable.just("hello").
 */
fun observableCreate() {
    val values =
        Observable.create { o: ObservableEmitter<String> ->
            o.onNext("Hello")
            o.onComplete()
        }
    val subscription = values.subscribe(
        { v: String -> println("Received: $v") },
        { e: Throwable -> println("Error: $e") }
    ) { println("Completed") }
}