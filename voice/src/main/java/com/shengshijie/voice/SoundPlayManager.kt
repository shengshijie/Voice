package com.shengshijie.voice

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.RawRes
import com.shengshijie.log.HLog
import kotlin.concurrent.thread

object SoundPlayManager {

    private var mContext: Context? = null

    private var mSoundPlayer: SoundPool = SoundPool.Builder()
        .setMaxStreams(10)
        .build()

    fun init(context: Context) {
        mContext = context
    }

    @JvmStatic
    fun play(@RawRes res: Int) {
        play(res,"") {}
    }

    @JvmStatic
    fun play(@RawRes res: Int, amount: String = "") {
        play(res,amount) {}
    }

    @JvmStatic
    fun play(@RawRes res: Int, amount: String = "", onComplete: () -> Unit = {}) {
        val list = mutableListOf<Voice>().apply {
            add(Voice(res))
            addAll(NumToCnAmountUtils.numberToWord(amount)
                .map { amountMap[it] }
                .filter { it != 0 }
                .filterNotNull()
                .map { Voice(it) })
        }.toList()
        val loadedId = mutableListOf<Int>()
        mSoundPlayer.setOnLoadCompleteListener { _, sampleId, _ ->
            loadedId.add(sampleId)
            if (loadedId.size != list.size) return@setOnLoadCompleteListener
            thread {
                list.filter { it.id > 0 }
                    .forEach {
                        mSoundPlayer.play(it.id, 1f, 1f, 0, 0, 1f)
                        mSoundPlayer.unload(it.id)
                        Thread.sleep(getDuration(it.res))
                    }
                onComplete()
            }
        }
        list.forEach {
            it.id = getSoundId(it.res)
        }
    }

    private fun getDuration(res: Int): Long {
        return try {
            MediaPlayer.create(mContext, res).duration.toLong()
        } catch (e: Exception) {
            0
        }
    }

    private fun getSoundId(res: Int): Int {
        return try {
            mSoundPlayer.load(mContext, res, 1)
        } catch (e: Exception) {
            -1
        }
    }

    fun release() {
        mSoundPlayer.release()
    }

}

data class Voice(
    var res: Int,
    var id: Int = 0
)