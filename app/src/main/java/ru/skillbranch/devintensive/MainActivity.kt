package ru.skillbranch.devintensive

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage : ImageView
    lateinit var textV : TextView
    lateinit var messageEt: EditText
    lateinit var sendButton: ImageView
    lateinit var benderObj: Bender



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textV = tv_text
        messageEt = et_message
        sendButton = iv_send

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        val inputText = savedInstanceState?.getString("INPUT_TEXT") ?: ""
        val textViewMessage = savedInstanceState?.getString("BENDER_MESSAGE")
        val tryCount = savedInstanceState?.getInt("TRY_COUNTER") ?: 0
        messageEt.setText(inputText)
        Log.d("M_MainActivity","onCreate $status $question" )

        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        if (textViewMessage.isNullOrBlank()) {
            textV.text = benderObj.askQuestion()
        }
        else{
            textV.text = textViewMessage
        }
        changeBenderColor(
            benderObj.status.color.first,
            benderObj.status.color.second,
            benderObj.status.color.third)

        benderObj.tryCount = tryCount

        sendButton.setOnClickListener(this)
        messageEt.setOnEditorActionListener { _, actionId, _ ->
            when(actionId){
            EditorInfo.IME_ACTION_DONE -> { hideKeyboard(); true }
            else -> false
        }
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
     Log.d("M_MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    override fun onClick(v:View){
        if(v?.id == R.id.iv_send){
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
            messageEt.setText("")
            val(r,g,b) = color
            changeBenderColor(r, g, b)
            textV.text = phrase
            if (this.isKeyboardOpen())
                hideKeyboard()
        }
    }

    private fun changeBenderColor(r: Int, g: Int, b: Int) {
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("TRY_COUNTER", benderObj.tryCount)
        outState.putString("BENDER_MESSAGE", textV.text.toString())
        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
        outState.putString("INPUT_TEXT", messageEt.text.toString())
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
    }




}


