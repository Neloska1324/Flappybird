Our project is a Flappy Bird game. It's made in Android Studio using Jetpack Compose (Kotlin). It contains a login-register screen that stores authentication data on the cloud using Firebase to store your highest score. It also contains a screen for selecting the difficulty of the game.

The screens:
<img width="2800" height="1440" alt="Screens" src="https://github.com/user-attachments/assets/c06456e6-42ad-4936-8bfc-e66f7e89e4b5" />

The bird:
<img width="17" height="16" alt="bird" src="https://github.com/user-attachments/assets/2cb227e3-f889-4638-9b03-167641c8d24f" />
The bird sprite was made using Aseprite. It uses a script called Player that draws the bitmap, adjusts the speed, the rotation for the animation, the sounds and the rect for the collisions.

The tubes:
<img width="128" height="15" alt="tubeHorizontal" src="https://github.com/user-attachments/assets/a32721b4-e671-4788-8970-4b564902d0b6" />
The tubes use a script called Tube. When a tube is spawned it sets a random position within a certain range for the bottom tube and for the top one it sets the opposite position with a space of 600 px.

All sprites are made in Aseprite and need to have the inScaled property in BitmapFactory disabled for them not to show bluriness.

GameView: handles all the logic of the game that includes surfaceView, sounds, drawing sprites, the movement of the tubes in the screen, and keeps track of the points and highscore in a text in the background.

Animation:

The bird has a flying animation based on physics. When it falls it looks down, and when it flies up it looks up. For it to work while being drawn it uses this order:
1. canvas.save()
2. canvas.rotate(rotation, x + size/2, y + size/2)
3. canvas.drawBitmap(bitmap, x, y, paint)
4. canvas.restore()
Every line is vital in its order for the animation to show and to not cause an error.

Sound System Implementation

We implemented the sound system by creating a SoundManager class utilizing Android's SoundPool API with AudioAttributes configured for game usage.
We defined four sound constants: SOUND_JUMP, SOUND_SCORE, SOUND_COLLISION, SOUND_BUTTON. We mapped them to raw audio resources (R.raw.jump ...) via the loadSound() method. We modified the GameView class to instantiate the manager in surfaceCreated(), call soundManager.playSound() at key game events: onTouchEvent, givePoint, restart.


Difficulty System Implementation

We implemented difficulty selection using Android's SharedPreferences. Then created a DifficultyScreen composable that saves the chosen speed multiplier (0.75, 1.0, or 1.5).
We modified the GameView class to read this value in surfaceCreated(), restart(), and givePoint() using prefs.getFloat("game_speed", 1.0f) and pass it to a modified Tube constructor. We then updated the Tube.update() method to apply the multiplier.


Difficulties: 

One of the most difficult parts we encountered, coding-wise, was trying to implement a fluent creation of the tubes, and then give them diferent speeds according to the level of difficulty. But, in general, we have had more problems with the program Android Studio itself, specially making the game run in the the different emulators. 

Conclusion:
We found this project very hard to work on it because we use some new technologies and a different program that we were not familiar with, but at the same time it was very complete. We consider that all the screens that we implemented, such as the register/login screen, the game itself or saving data in firebase, made a whole game that can be on the stores.


