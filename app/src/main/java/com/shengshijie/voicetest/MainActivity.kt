package com.shengshijie.voicetest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shengshijie.voice.SoundPlayManager
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SoundPlayManager.init(this)
    }

    fun test(view: View) {
        thread {
            SoundPlayManager.play(R.raw.tts_pay_cancel)
            SoundPlayManager.play(R.raw.tts_pay_success) {
                SoundPlayManager.playAmount("1.23") {
                    SoundPlayManager.play(R.raw.tts_pay_cancel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundPlayManager.release()
    }

}
