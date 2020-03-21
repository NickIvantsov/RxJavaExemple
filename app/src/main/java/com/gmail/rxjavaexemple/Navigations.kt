package com.gmail.rxjavaexemple

import android.content.Intent

fun navigateTo(classActivity: Class<*>) {
    val intent = Intent(AppApplication.context, classActivity)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    AppApplication.context?.startActivity(intent)
}