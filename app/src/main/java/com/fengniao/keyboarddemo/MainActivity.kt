package com.fengniao.keyboarddemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyboard.onConfirmClick = object : MyKeyboard.ConfirmClickListener {
            override fun onConfirmClick(content: String) {
                Log.d("金额",content)
            }
        }
        editText.setOnClickListener {
           keyboard.changeKeyboardStatus()
        }
    }

    override fun onBackPressed() {
        if (keyboard.isShow){
            keyboard.changeKeyboardStatus()
        }else{
            super.onBackPressed()
        }
    }


}