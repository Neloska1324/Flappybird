New Updates
***********************************************************************************

Sound System Implementation

We implemented the sound system by creating a SoundManager class utilizing Android's SoundPool API with AudioAttributes configured for game usage.
We defined four sound constants: SOUND_JUMP, SOUND_SCORE, SOUND_COLLISION, SOUND_BUTTON. We mapped them to raw audio resources (R.raw.jump ...) via the loadSound() method. We modified the GameView class to instantiate the manager in surfaceCreated(), call soundManager.playSound() at key game events: onTouchEvent, givePoint, restart.

Difficulty System Implementation

We implemented difficulty selection using Android's SharedPreferences. Then created a DifficultyScreen composable that saves the chosen speed multiplier (0.75, 1.0, or 1.5).
We modified the GameView class to read this value in surfaceCreated(), restart(), and givePoint() using prefs.getFloat("game_speed", 1.0f) and pass it to a modified Tube constructor. We then updated the Tube.update() method to apply the multiplier.
