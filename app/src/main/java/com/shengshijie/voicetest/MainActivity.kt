package com.shengshijie.voicetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.shengshijie.log.HLog
import com.shengshijie.voice.SoundPlayManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SoundPlayManager.init(this)
        HLog.init(this,"VOICE")
    }

    fun test(view: View) {
        SoundPlayManager.play(R.raw.tts_pay_success,"1.232")
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundPlayManager.release()
    }

}
