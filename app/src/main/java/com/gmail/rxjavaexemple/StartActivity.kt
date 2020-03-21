package com.gmail.rxjavaexemple

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.rxOperationBtn)
    fun rxOperationPressed(v: View) {
        val intent = Intent(applicationContext, RxJavaOperationsActivity::class.java)
        startActivity(intent)
    }
}
