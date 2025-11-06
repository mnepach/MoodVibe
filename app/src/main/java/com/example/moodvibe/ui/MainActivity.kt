package com.example.moodvibe.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.moodvibe.data.MoodHistoryEntity
import com.example.moodvibe.viewmodel.MoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
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
        composable("history") {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScreen(navController: NavController, viewModel: MoodViewModel = hiltViewModel()) {
    val moods by viewModel.moods.collectAsState()
    val history by viewModel.moodHistory.collectAsState()

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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "✨ Как твоё настроение?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Выбери своё состояние",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Сетка настроений 2-2-1 или 2-2-2
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Первая строка - 2 карточки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
                ) {
                    if (moods.isNotEmpty()) {
                        MoodCard(
                            mood = moods[0],
                            onClick = { navController.navigate("mood/${moods[0].name}") }
                        )
                    }
                    if (moods.size > 1) {
                        MoodCard(
                            mood = moods[1],
                            onClick = { navController.navigate("mood/${moods[1].name}") }
                        )
                    }
                }

                // Вторая строка - 2 карточки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
                ) {
                    if (moods.size > 2) {
                        MoodCard(
                            mood = moods[2],
                            onClick = { navController.navigate("mood/${moods[2].name}") }
                        )
                    }
                    if (moods.size > 3) {
                        MoodCard(
                            mood = moods[3],
                            onClick = { navController.navigate("mood/${moods[3].name}") }
                        )
                    }
                }

                // Третья строка - 2 карточки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
                ) {
                    if (moods.size > 4) {
                        MoodCard(
                            mood = moods[4],
                            onClick = { navController.navigate("mood/${moods[4].name}") }
                        )
                    }
                    if (moods.size > 5) {
                        MoodCard(
                            mood = moods[5],
                            onClick = { navController.navigate("mood/${moods[5].name}") }
                        )
                    }
                }

                // Четвертая строка - 1 карточка по центру (если есть 7-е настроение)
                if (moods.size > 6) {
                    MoodCard(
                        mood = moods[6],
                        onClick = { navController.navigate("mood/${moods[6].name}") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Кнопка истории - всегда внизу
            if (history.isNotEmpty()) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("history") }
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "📅",
                                fontSize = 28.sp,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Column {
                                Text(
                                    text = "История настроений",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${history.size} записей",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Text(
                            text = "→",
                            fontSize = 24.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
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
            AnimatedMoodImage(imageRes = mood.imageRes)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mood.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

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
fun AnimatedMoodImage(imageRes: Int) {
    var rotation by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        while (true) {
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
        label = "imageScale"
    )

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(80.dp)
            .graphicsLayer {
                rotationZ = animatedRotation
                scaleX = animatedScale
                scaleY = animatedScale
            },
        contentScale = ContentScale.Fit
    )
}

@Composable
fun MoodDetailScreen(
    moodName: String,
    viewModel: MoodViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val quote by viewModel.quote.collectAsState()
    val moods by viewModel.moods.collectAsState()
    var showShareDialog by remember { mutableStateOf(false) }
    var alpha by remember { mutableFloatStateOf(0f) }

    val currentMood = moods.find { it.name == moodName }

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

            Spacer(modifier = Modifier.weight(1f))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(alpha = animatedAlpha),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentMood?.let {
                    Image(
                        painter = painterResource(id = it.imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = moodName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF6366f1).copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "«",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.3f),
                                lineHeight = 40.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = quote,
                                fontSize = 24.sp,
                                color = Color.White,
                                lineHeight = 36.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "»",
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.3f),
                                    lineHeight = 40.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
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
                        "🔄 Получить другую цитату",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Поделиться настроением") },
            text = { Text("«$quote» — $moodName") },
            confirmButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Закрыть")
                }
            }
        )
    }
}

@Composable
fun HistoryScreen(
    viewModel: MoodViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val history by viewModel.moodHistory.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

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
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Text("←", fontSize = 24.sp, color = Color.White)
                }

                Text(
                    text = "История",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { showClearDialog = true },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Text("🗑️", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📝",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "История пуста",
                            fontSize = 20.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(history, key = { it.id }) { item ->
                        HistoryItem(
                            item = item,
                            onDelete = { viewModel.deleteHistoryItem(item.id) }
                        )
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Очистить историю?") },
            text = { Text("Все записи будут удалены безвозвратно") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun HistoryItem(
    item: MoodHistoryEntity,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("ru")) }
    val formattedDate = remember(item.timestamp) {
        dateFormat.format(Date(item.timestamp))
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.moodName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.quote,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Red.copy(alpha = 0.2f))
            ) {
                Text("✕", fontSize = 20.sp, color = Color.Red)
            }
        }
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