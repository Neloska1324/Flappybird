package com.example.flappybird

import android.content.Context
import android.graphics.*

class Player(private val context: Context, width: Int, height: Int, private val soundManager: SoundManager? = null) {
    var x = 0f
    var y = 0f
    var velocity = 0f
    var rotation = 0f
    var isAlive = true

    private val size = 90f
    private val gravity = 2.1f
    private val jumpStrength = -35f
    private val maxRotation = 85f
    private val minRotation = -25f
    private val srcRect = Rect()
    private val dstRect = RectF()
    var bitmap: Bitmap

    init {
        val desiredWidth = 160
        val desiredHeight = 160

        val options = BitmapFactory.Options().apply {
            inScaled = false
        }

        bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.bird,
            options
        )

        bitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, false)
    }

    fun draw(canvas: Canvas, paint : Paint) {
        canvas.save()
        canvas.rotate(rotation, x + size/2, y + size/2)
        canvas.drawBitmap(bitmap, x, y, paint)
        canvas.restore()
    }


    fun update() {
        velocity += gravity
        y += velocity

        rotation = when {
            velocity < -12 -> minRotation
            velocity > 12 -> maxRotation
            else -> velocity * 2.8f
        }
        rotation = rotation.coerceIn(minRotation, maxRotation)
    }

    fun jump() {
        velocity = jumpStrength
        soundManager?.playSound(SoundManager.SOUND_JUMP)
    }

    fun reset(screenWidth: Float, screenHeight: Float) {
        x = screenWidth / 8f
        y = screenHeight / 2.2f
        velocity = 0f
        rotation = 0f
        isAlive = true
    }

    fun getRect(): RectF {
        return RectF(
            x + size * 0.15f,
            y + size * 0.10f,
            x + size * 0.85f,
            y + size * 0.85f
        )
    }

    fun getSize(): Float = size
}