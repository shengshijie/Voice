package com.shengshijie.voicetest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shengshijie.voice.SoundPlayManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SoundPlayManager.init(this)
    }

    fun test(view: View) {
        SoundPlayManager.play(R.raw.tts_pay_success,"1.232")
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundPlayManager.release()
    }

}
