package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class GameView(context: Context, val viewModel: MainMenuViewModel) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

    private var gameThread: Thread? = null
    private var isPlaying = false
    public val paint = Paint()
    private val holderSurface: SurfaceHolder = holder
    var points = 0

    private lateinit var bird: Player
    val tubes = mutableListOf<Tube>()

    var background: Bitmap
    var tube: Bitmap

    private lateinit var scaledTubeUp: Bitmap
    private lateinit var scaledTube: Bitmap

    val typefacePixel = ResourcesCompat.getFont(context, R.font.pixel)

    private lateinit var soundManager: SoundManager

    val textPaint = Paint().apply {
        color = Color.WHITE
        alpha = 150
        textSize = 150f
        textAlign = Paint.Align.CENTER
        typeface = typefacePixel
        isAntiAlias = true
    }

    val textPaint_2 = Paint().apply {
        color = Color.WHITE
        alpha = 150
        textSize = 1000f
        textAlign = Paint.Align.CENTER
        typeface = typefacePixel
        isAntiAlias = true
    }

    init {
        holder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true

        val options = BitmapFactory.Options().apply {
            inScaled = false
        }

        tube = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.tube_,
            options
        )

        background = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.bg2_2,
            options
        )
    }

    private fun rotateBitmap(source: Bitmap): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.preScale(1f, -1f)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        soundManager = SoundManager(context)

        soundManager.loadSound(R.raw.jump, SoundManager.SOUND_JUMP)
        soundManager.loadSound(R.raw.button, SoundManager.SOUND_BUTTON)
        soundManager.loadSound(R.raw.score, SoundManager.SOUND_SCORE)
        soundManager.loadSound(R.raw.collision, SoundManager.SOUND_COLLISION)

        bird = Player(context, width, height)
        background = Bitmap.createScaledBitmap(background, width, height, false)
        scaledTube = Bitmap.createScaledBitmap(tube, 280, 2240, false)
        scaledTubeUp = rotateBitmap(scaledTube)
        createTube()

        Log.d("gameview", "surfaceCreated")
        bird.reset(width.toFloat(), height.toFloat())
        resume()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause()
    }

    override fun run() {
        while (isPlaying) {
            update()
            drawGame()
            Thread.sleep(17)
        }
    }

    private fun update() {
        if (!::bird.isInitialized) return
        for (tube in tubes) {
            tube.updateRect()
            val birdRect = bird.getRect()
            val tubeRect = Rect(
                tube.tubeRect.left,
                tube.tubeRect.top,
                tube.tubeRect.right,
                tube.tubeRect.bottom
            )
            if (Rect.intersects(birdRect.toRect(), tubeRect)) {
                Log.d("gameview", "Choque")
                restart()
                break
            }
        }

        if (bird.y > height - bird.getSize()) {
            restart()
        }

        bird.update()

        if (tubes.isNotEmpty()) {
            for (tube in tubes) {
                tube.update()
                if (tube.tubeX == 200f && tube.tubeY > 0f) {
                    shouldCreateNewTube = true
                }
            }
        }

        if (shouldCreateNewTube) {
            givePoint()
            shouldCreateNewTube = false
        }

        tubes.removeAll { it.tubeX <= -100f }
    }

    fun restart() {
        soundManager.playSound(SoundManager.SOUND_COLLISION)
        saveHighScore()
        points = 0

        bird = Player(context, width, height)
        tubes.clear()

        createTube()

        bird.reset(width.toFloat(), height.toFloat())
        shouldCreateNewTube = false
    }

    var shouldCreateNewTube = false

    private fun drawGame() {
        val canvas = holderSurface.lockCanvas() ?: return
        canvas.drawColor(Color.CYAN)
        canvas.drawBitmap(background, 0f, 0f, paint)

        val text_1 = "Points:"
        val x_1 = canvas.width / 2f
        val y_1 = canvas.height / 3f

        val text = "$points"
        val x = canvas.width / 1.85f
        val y = canvas.height / 1.5f

        canvas.drawText(text_1, x_1, y_1, textPaint)
        canvas.drawText(text, x, y, textPaint_2)

        bird.draw(canvas, paint)

        for (tube in tubes) {
            if (tubes.indexOf(tube) % 2 != 0) {
                canvas.drawBitmap(scaledTubeUp, tube.tubeX, tube.tubeY, paint)
            } else {
                canvas.drawBitmap(scaledTube, tube.tubeX, tube.tubeY, paint)
            }
        }

        holderSurface.unlockCanvasAndPost(canvas)
    }

    fun createTube() {
        tubes.add(Tube(context, this))
        tubes[tubes.count() - 1].initTube(height.toFloat())
        tubes.add(Tube(context, this))
        tubes[tubes.count() - 1].initTubeUp(tubes[tubes.count() - 2].tubeY, height.toFloat())
    }

    fun givePoint() {
        points += 1
        soundManager.playSound(SoundManager.SOUND_SCORE)
        createTube()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("gameview", "boosting")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                bird.jump()
                soundManager.playSound(SoundManager.SOUND_JUMP)
            }
        }
        return true
    }

    fun resume() {
        if (!isPlaying) {
            isPlaying = true
            gameThread = Thread(this)
            gameThread?.start()
        }
    }

    fun pause() {
        isPlaying = false
        saveHighScore()
        soundManager.release()
        try {
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        gameThread = null
    }

    fun saveHighScore() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Log.d("GameView", "No hay usuario autenticado, no se guarda score")
            return
        }

        val userId = currentUser.uid
        val db = Firebase.firestore

        db.collection("games")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val currentHighScore = document.data?.get("score") as? Long ?: 0L

                Log.d("GameView", "Highscore actual: $currentHighScore, Puntos actuales: $points")

                if (points > currentHighScore) {
                    val data = hashMapOf(
                        "score" to points,
                        "lastUpdated" to FieldValue.serverTimestamp()
                    )

                    db.collection("games")
                        .document(userId)
                        .set(data)
                        .addOnSuccessListener {
                            Log.d("GameView", "✅ Nuevo highscore guardado: $points")
                            val prefs = context.getSharedPreferences("flappy_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putInt("highscore", points).apply()
                        }
                        .addOnFailureListener { e ->
                            Log.e("GameView", "❌ Error al guardar score: ${e.message}")
                        }
                } else {
                    Log.d("GameView", "No es un nuevo récord (Actual: $currentHighScore, Nuevo: $points)")
                }
            }
            .addOnFailureListener { e ->
                Log.e("GameView", "Error al obtener highscore actual: ${e.message}")
            }
    }
}

fun RectF.toRect(): Rect {
    return Rect(
        this.left.toInt(),
        this.top.toInt(),
        this.right.toInt(),
        this.bottom.toInt()
    )
}

@Composable
fun GameScreen(viewModel: MainMenuViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadScore()
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            GameView(context, viewModel)
        }
    )
}