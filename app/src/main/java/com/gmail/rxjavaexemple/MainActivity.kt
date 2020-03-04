package com.gmail.rxjavaexemple

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gmail.rxjavaexemple.KeyTypes.*
import com.gmail.rxjavaexemple.operatorsTransformingObservables.buffer
import com.gmail.rxjavaexemple.operatorsTransformingObservables.map


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun textViewPressed(view: View) {
        publish()
    }

    fun replaySubjectPressed(view: View) {
        replay()
    }

    fun replayWithClearCashPressed(view: View) {
        replayWithClearCash()
    }

    fun replayWithTimePressed(view: View) {
        replayWithTime()
    }

    fun behaviorSubjectPressed(view: View) {
        behaviorSubject()
    }

    fun behaviorSubjectSecondPressed(view: View) {
        behaviorSubjectSecond()
    }

    fun asyncSubjectPressed(view: View) {
        asyncSubject()
    }
    fun implicitContractsPressed(view: View) {
        implicitContracts()
    }
    fun replaySubjectHandleError(view: View) {
        replayHandleError()
    }
    fun unsubscribingPressed(view: View) {
        replayDispose()
    }
    fun replayDisposeSecondExamplePressed(view: View) {
        replayDisposeSecondExample()
    }
    fun onCompleteExamplePressed(view: View) {
        onCompleteExample()
    }
    fun observableJustPressed(view: View) {
        observableJust()
    }
    fun observableEmptyPressed(view: View) {
        observableEmpty()
    }

    fun observableCreatePressed(view: View) {
        observableCreate()
    }
    fun operatorsBufferPressed(view: View) {
        buffer()
    }
    fun operatorsMapPressed(view: View) {
        map()
    }

}
