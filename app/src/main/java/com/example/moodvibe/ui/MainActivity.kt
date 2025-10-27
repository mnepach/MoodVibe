package com.example.moodvibe.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.example.moodvibe.ui.theme.MoodVibeTheme {
                MoodVibeApp()
            }
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
            MoodDetailScreen(
                moodName = moodName,
                onClose = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScreen(navController: NavController, viewModel: MoodViewModel = hiltViewModel()) {
    val moods by viewModel.moods.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1a1a2e),
                        Color(0xFF16213e),
                        Color(0xFF0f3460)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Заголовок
            Text(
                text = "✨ How are you feeling?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Choose your vibe",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Горизонтальный скролл с настроениями
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(moods) { mood ->
                    MoodCard(
                        mood = mood,
                        onClick = { navController.navigate("mood/${mood.name}") }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Нижняя информация
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💡",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Tap on a mood to get personalized quotes and insights",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MoodCard(mood: Mood, onClick: () -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }
    var isAnimating by remember { mutableStateOf(true) }

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(600, easing = EaseInOutCubic),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        while (isAnimating) {
            scale = 1.05f
            delay(2000 + Random.nextLong(500))
            scale = 1f
            delay(2000 + Random.nextLong(500))
        }
    }

    GlassCard(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .scale(animatedScale)
            .clickable {
                isAnimating = false
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Анимированное эмодзи
            AnimatedEmoji(emoji = mood.gradientColors[0])

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mood.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Индикатор
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Color.White.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

@Composable
fun AnimatedEmoji(emoji: String) {
    var rotation by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            // Качание
            rotation = -5f
            scale = 1.1f
            delay(1000)
            rotation = 5f
            scale = 0.95f
            delay(1000)
            rotation = 0f
            scale = 1f
            delay(2000)
        }
    }

    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(800, easing = EaseInOutCubic),
        label = "rotation"
    )

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(800, easing = EaseInOutCubic),
        label = "emojiScale"
    )

    Text(
        text = emoji,
        fontSize = 64.sp,
        modifier = Modifier
            .graphicsLayer {
                rotationZ = animatedRotation
                scaleX = animatedScale
                scaleY = animatedScale
            }
    )
}

@Composable
fun MoodDetailScreen(
    moodName: String,
    viewModel: MoodViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val quote by viewModel.quote.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var alpha by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(moodName) {
        viewModel.loadQuote(moodName)
        delay(100)
        alpha = 1f
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(600),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF6366f1),
                        Color(0xFF1a1a2e),
                        Color(0xFF0f0f1e)
                    ),
                    radius = 1200f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя панель с кнопками
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Text("✕", fontSize = 24.sp, color = Color.White)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Text(
                            if (isFavorite) "❤️" else "🤍",
                            fontSize = 20.sp
                        )
                    }

                    IconButton(
                        onClick = { showShareDialog = true },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Text("↗️", fontSize = 20.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Основной контент
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(alpha = animatedAlpha),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Большое эмодзи
                val moods by viewModel.moods.collectAsState()
                val currentMood = moods.find { it.name == moodName }

                currentMood?.let {
                    AnimatedEmoji(emoji = it.gradientColors[0])
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = moodName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Цитата в стеклянной карточке
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\"",
                            fontSize = 48.sp,
                            color = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.offset(y = 10.dp)
                        )

                        Text(
                            text = quote,
                            fontSize = 22.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка действия
                Button(
                    onClick = { viewModel.loadQuote(moodName) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "🔄 Get Another Quote",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    // Диалог "поделиться"
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Share your vibe") },
            text = { Text("\"$quote\" - $moodName") },
            confirmButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .blur(0.5.dp)
    ) {
        content()
    }
}