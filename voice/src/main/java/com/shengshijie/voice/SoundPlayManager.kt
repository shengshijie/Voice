package com.shengshijie.voice

import android.content.Context
import android.media.SoundPool
import android.os.SystemClock
import androidx.annotation.RawRes
import kotlin.concurrent.thread

object SoundPlayManager {

    private lateinit var mContext: Context
    private lateinit var mSoundPlayer: SoundPool
    private var soundMap = mutableMapOf<Int, Int>()

    @JvmStatic
    fun init(context: Context) {
        mContext = context
        mSoundPlayer = SoundPool.Builder().setMaxStreams(10).build()
        soundMap[R.raw.tts_0] = mSoundPlayer.load(mContext, R.raw.tts_0, 1)
        soundMap[R.raw.tts_1] = mSoundPlayer.load(mContext, R.raw.tts_1, 1)
        soundMap[R.raw.tts_2] = mSoundPlayer.load(mContext, R.raw.tts_2, 1)
        soundMap[R.raw.tts_3] = mSoundPlayer.load(mContext, R.raw.tts_3, 1)
        soundMap[R.raw.tts_4] = mSoundPlayer.load(mContext, R.raw.tts_4, 1)
        soundMap[R.raw.tts_5] = mSoundPlayer.load(mContext, R.raw.tts_5, 1)
        soundMap[R.raw.tts_6] = mSoundPlayer.load(mContext, R.raw.tts_6, 1)
        soundMap[R.raw.tts_7] = mSoundPlayer.load(mContext, R.raw.tts_7, 1)
        soundMap[R.raw.tts_8] = mSoundPlayer.load(mContext, R.raw.tts_8, 1)
        soundMap[R.raw.tts_9] = mSoundPlayer.load(mContext, R.raw.tts_9, 1)
        soundMap[R.raw.tts_ten] = mSoundPlayer.load(mContext, R.raw.tts_ten, 1)
        soundMap[R.raw.tts_hundred] = mSoundPlayer.load(mContext, R.raw.tts_hundred, 1)
        soundMap[R.raw.tts_thousand] = mSoundPlayer.load(mContext, R.raw.tts_thousand, 1)
        soundMap[R.raw.tts_ten_thousand] = mSoundPlayer.load(mContext, R.raw.tts_ten_thousand, 1)
        soundMap[R.raw.tts_million] = mSoundPlayer.load(mContext, R.raw.tts_million, 1)
        soundMap[R.raw.tts_dot] = mSoundPlayer.load(mContext, R.raw.tts_dot, 1)
        soundMap[R.raw.tts_cent] = mSoundPlayer.load(mContext, R.raw.tts_cent, 1)
        soundMap[R.raw.tts_dime] = mSoundPlayer.load(mContext, R.raw.tts_dime, 1)
        soundMap[R.raw.tts_whole] = mSoundPlayer.load(mContext, R.raw.tts_whole, 1)
        soundMap[R.raw.tts_yuan] = mSoundPlayer.load(mContext, R.raw.tts_yuan, 1)
    }

    @JvmStatic
    @JvmOverloads
    fun play(@RawRes res: Int, delay: Int = 500, onComplete: () -> Unit = {}) {
        thread {
            val resId = mSoundPlayer.load(mContext, res, 1)
            mSoundPlayer.setOnLoadCompleteListener { _, sampleId, _ ->
                if (resId == sampleId) {
                    mSoundPlayer.play(sampleId, 1f, 1f, 0, 0, 1f)
                    delay(delay)
                    onComplete()
                }
            }
        }
    }

    @JvmStatic
    @JvmOverloads
    fun playAmount(amount: String = "", delay: Int = 300, onComplete: () -> Unit = {}) {
        thread {
            NumToCnAmountUtils.numberToCnAmount(amount)
                .map { amountMap[it] }
                .filter { it != 0 }
                .filterNotNull()
                .map { Voice(it, soundMap[it]) }
                .filter { it.id != null }
                .forEach {
                    mSoundPlayer.play(it.id!!, 1f, 1f, 0, 0, 1f)
                    delay(delay)
                }
            onComplete()
        }
    }

    private fun playWithAmount(@RawRes res: Int, amount: String = "", onComplete: () -> Unit = {}) {
        val resId = mSoundPlayer.load(mContext, res, 1)
        mSoundPlayer.setOnLoadCompleteListener { _, sampleId, _ ->
            thread {
                if (resId == sampleId) {
                    mSoundPlayer.play(sampleId, 1f, 1f, 0, 0, 1f)
                    delay(res)
                    NumToCnAmountUtils.numberToCnAmount(amount)
                        .map { amountMap[it] }
                        .filter { it != 0 }
                        .filterNotNull()
                        .map { Voice(it, soundMap[it]) }
                        .filter { it.id != null }
                        .forEach {
                            mSoundPlayer.play(it.id!!, 1f, 1f, 0, 0, 1f)
                            delay(it.res)
                        }
                    onComplete()
                }
            }
        }
    }

    private fun delay(res: Int) {
        SystemClock.sleep(res.toLong())
//        SystemClock.sleep(MediaPlayer.create(mContext, res).duration.toLong())
    }

    @JvmStatic
    fun release() {
        soundMap.forEach {
            mSoundPlayer.unload(it.value)
        }
        mSoundPlayer.release()
    }

}

data class Voice(
    var res: Int,
    var id: Int? = 0
)