package com.example.flappybird

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.RawRes

class SoundManager(private val context: Context) {

    private val soundPool: SoundPool
    private val sounds = mutableMapOf<Int, Int>()
    init {

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    fun loadSound(@RawRes resourceId: Int, soundId: Int) {
        val loadedId = soundPool.load(context, resourceId, 1)
        sounds[soundId] = loadedId
    }

    fun playSound(soundId: Int) {
        sounds[soundId]?.let { loadedId ->
            soundPool.play(loadedId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release() {
        soundPool.release()
    }

    companion object {
        const val SOUND_JUMP = 1
        const val SOUND_BUTTON = 2
        const val SOUND_SCORE = 3
        const val SOUND_COLLISION = 4
    }
}