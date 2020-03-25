package com.gmail.rxjavaexemple.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.gmail.rxjavaexemple.R
import com.gmail.rxjavaexemple.navigateTo

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.rxOperationBtn)
    fun rxOperationPressed(v: View) {
        navigateTo(RxJavaOperationsActivity::class.java)
    }

    @OnClick(R.id.helloRxJavaBtn)
    fun helloRxJavaPressed(v: View) {
        navigateTo(HelloWorldActivity::class.java)
    }

    @OnClick(R.id.sandbox)
    fun sandboxPressed(v: View) {
        navigateTo(SandboxActivity::class.java)
    }

}
