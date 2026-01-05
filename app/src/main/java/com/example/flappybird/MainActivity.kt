package com.example.flappybird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flappybird.ui.theme.FlappyBirdTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(GameView(this))

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "enter"
            ) {
                composable("enter") {
                    EnterScreen(navController)
                }
                composable("menu") {
                    MainMenu(navController)
                }
                composable("game") {
                    GameScreen()
                }
            }
        }
    }
/*
    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
*/
    override fun onPause() {
        super.onPause()
        //gameView.pause()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlappyBirdTheme {
        Greeting("Android")
    }
}