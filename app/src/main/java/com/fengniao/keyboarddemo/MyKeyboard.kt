package com.fengniao.keyboarddemo

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.keyboard.view.*
import kotlin.math.min

class MyKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    var keyValues = SparseArray<String>()
    var isShow = false
    var mInputConnection: InputConnection? = null
    var minPrice: Float = 10000f
    var onConfirmClick: ConfirmClickListener? = null

    private fun init(
        context: Context
    ) {
        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true)

        tv1.setOnClickListener(this)
        tv2.setOnClickListener(this)
        tv3.setOnClickListener(this)
        tv4.setOnClickListener(this)
        tv5.setOnClickListener(this)
        tv6.setOnClickListener(this)
        tv7.setOnClickListener(this)
        tv8.setOnClickListener(this)
        tv9.setOnClickListener(this)
        tv0.setOnClickListener(this)
        tvDelete.setOnClickListener(this)
        tvConfirm.setOnClickListener(this)
        tvDot.setOnClickListener(this)

        // map buttons IDs to input strings
        keyValues.put(R.id.tv1, "1")
        keyValues.put(R.id.tv2, "2")
        keyValues.put(R.id.tv3, "3")
        keyValues.put(R.id.tv4, "4")
        keyValues.put(R.id.tv5, "5")
        keyValues.put(R.id.tv6, "6")
        keyValues.put(R.id.tv7, "7")
        keyValues.put(R.id.tv8, "8")
        keyValues.put(R.id.tv9, "9")
        keyValues.put(R.id.tv0, "0")
        keyValues.put(R.id.tvConfirm, "\n")
        keyValues.put(R.id.tvDot, ".")

        etContent.setRawInputType(InputType.TYPE_CLASS_TEXT)
        etContent.filters = arrayOf<InputFilter>(MoneyInputFilter())
        etContent.setTextIsSelectable(true)
        etContent.addTextChangedListener {
            val content = it.toString().toFloatOrNull()
            if (content == null || content.toFloat() < minPrice) {
                tilContent.error = "金额太小"
            } else {
                tilContent.error = ""
            }
        }
        val ic = etContent.onCreateInputConnection(EditorInfo())
        setInputConnection(ic)
    }

    override fun onClick(v: View) {

        // do nothing if the InputConnection has not been set yet
        if (mInputConnection == null) return

        if (v == tvConfirm) {
            if (!tilContent.error.isNullOrEmpty()) {
                return
            }
            onConfirmClick?.onConfirmClick(etContent.text.toString())
            changeKeyboardStatus()
            return
        }

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v == tvDelete) {
            val selectedText = mInputConnection!!.getSelectedText(0)
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                mInputConnection!!.deleteSurroundingText(1, 0)
            } else {
                // delete the selection
                mInputConnection!!.commitText("", 1)
            }
        } else {
            val value = keyValues[v.id]
            mInputConnection!!.commitText(value, 1)
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    private fun setInputConnection(ic: InputConnection?) {
        mInputConnection = ic
    }

    // constructors
    init {
        init(context)
    }

    fun changeKeyboardStatus() {
        if (isShow) {
            slideDown()
        } else {
            slideUp()
        }
        isShow = !isShow
    }

    // slide the view from below itself to the current position
    private fun slideUp() {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "translationY", height.toFloat(), -height.toFloat())
            .setDuration(500)
            .start()
    }

    // slide the view from its current position to below itself
    private fun slideDown() {
        ObjectAnimator.ofFloat(this, "translationY", -height.toFloat(), height.toFloat())
            .setDuration(500)
            .start()
    }

    interface ConfirmClickListener {
        fun onConfirmClick(content: String)
    }
}