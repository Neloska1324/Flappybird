package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class Tube(context: Context, private val gameView: GameView, private val speedMultiplier: Float = 1.0f) {

    public var tubeX = 0f
    public var tubeY = 0f
    val tubeRect: Rect = Rect()

    private val tubeHeight = 2240
    private val tubeWidth = 280

    var bitmap: Bitmap

    init {
        val options = BitmapFactory.Options().apply {
            inScaled = false
        }

        bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.tube_,
            options
        )

        bitmap = Bitmap.createScaledBitmap(bitmap, tubeWidth, tubeHeight, false)
    }

    fun initTube(screenHeight: Float){
        tubeX = 2000f
        tubeY = ((screenHeight * 0.3f).toInt()..(screenHeight * 0.75f).toInt()).random().toFloat()
    }

    fun initTubeUp(up: Float, screenHeight: Float){
        tubeX = 2000f
        tubeY = up - 600f - tubeHeight
    }

    fun update(){
        tubeX -= 25f * speedMultiplier
    }

    fun updateRect() {
        tubeRect.left = tubeX.toInt()
        tubeRect.top = tubeY.toInt()
        tubeRect.right = (tubeX + tubeWidth).toInt()
        tubeRect.bottom = (tubeY + 2240).toInt()
    }
}