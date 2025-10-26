package com.example.moodvibe.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moodvibe.data.Mood
import com.example.moodvibe.viewmodel.MoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint // <-- Add this annotation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodVibeApp()
        }
    }
}

@Composable
fun MoodVibeApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController = navController)
        }
        composable("mood/{moodName}") { backStackEntry ->
            val moodName = backStackEntry.arguments?.getString("moodName") ?: ""
            MoodDetailScreen(moodName = moodName)
        }
    }
}

@Composable
fun MainScreen(navController: NavController, viewModel: MoodViewModel = hiltViewModel()) {
    val moods by viewModel.moods.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose Your Mood",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(moods) { mood ->
                MoodCard(mood = mood, onClick = {
                    navController.navigate("mood/${mood.name}")
                })
            }
        }
    }
}

@Composable
fun MoodCard(mood: Mood, onClick: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }

    // This is just a simple example of an animation.
    // In a real app you might want to handle this differently.
    LaunchedEffect(Unit) {
        while (true) {
            scale = 1.05f
            delay(500)
            scale = 1f
            delay(500)
        }
    }

    Box(
        modifier = Modifier
            .size(150.dp, 200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = mood.gradientColors.map { Color(android.graphics.Color.parseColor(it)) }
                )
            )
            .clickable { onClick() }
            .scale(scale), // You can apply the scale modifier here
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = mood.name,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun MoodDetailScreen(moodName: String, viewModel: MoodViewModel = hiltViewModel()) {
    val quote by viewModel.quote.collectAsState()
    var alpha by remember { mutableStateOf(0f) }

    LaunchedEffect(moodName) {
        viewModel.loadQuote(moodName)
        alpha = 0f
        delay(100) // small delay to trigger the animation
        alpha = 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(android.graphics.Color.parseColor("#FF6F61")),
                        Color.Black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = quote,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
                // In Compose, it's better to use Modifier.alpha()
                // However, the original code used a state variable, so keeping it for consistency.
                .graphicsLayer(alpha = alpha)
        )
    }
}
