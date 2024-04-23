package com.ardakazanci.circlelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ardakazanci.circlelayout.ui.theme.CircleLayoutTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CircleLayoutTheme {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)) {
                    CircleLayout()
                }
            }
        }
    }
}

enum class CircleSize {
    LARGE, MEDIUM, SMALL
}

data class CircleData(
    val size: CircleSize,
    val color: Color,
    val text: String?,
    val icon: ImageVector?,
    val description: String?
)

@Composable
fun CircleItem(circleData: CircleData, modifier: Modifier = Modifier) {
    val size = when (circleData.size) {
        CircleSize.LARGE -> 150.dp
        CircleSize.MEDIUM -> 90.dp
        CircleSize.SMALL -> 50.dp
    }

    Box(
        modifier = modifier
            .size(size)
            .background(color = circleData.color, shape = CircleShape)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (circleData.size) {
            CircleSize.LARGE -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (circleData.icon != null) Icon(circleData.icon, contentDescription = "LARGE")
                    if (circleData.text != null) Text(circleData.text)
                    if (circleData.description != null) Text(circleData.description)
                }
            }

            CircleSize.MEDIUM -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (circleData.icon != null) Icon(circleData.icon, contentDescription = "MEDIUM")
                    if (circleData.text != null) Text(circleData.text)
                }
            }

            CircleSize.SMALL -> {
                if (circleData.icon != null) Icon(circleData.icon, contentDescription = "SMALL")
            }
        }
    }
}


@Composable
fun CircleLayout() {
    val circles = listOf(
        CircleData(CircleSize.LARGE, Color.Red, "Text", Icons.Default.Home, "Test"),
        CircleData(CircleSize.MEDIUM, Color.Blue, "Text", Icons.Default.Info, null),
        CircleData(CircleSize.SMALL, Color.Black, "Text", Icons.Default.Info, null),
        CircleData(CircleSize.SMALL, Color.Gray, "Text", Icons.Default.Info, null),
        CircleData(CircleSize.LARGE, Color.Cyan, "Text", Icons.Default.Info, null),
        CircleData(CircleSize.SMALL, Color.Yellow, "Text", Icons.Default.Info, null),
    )

    BoxWithConstraints {
        val maxWidth = maxWidth
        val maxHeight = maxHeight
        val placedCircles = remember { mutableListOf<Rect>() }

        circles.forEach { circleData ->
            val size = when (circleData.size) {
                CircleSize.LARGE -> 150f
                CircleSize.MEDIUM -> 90f
                CircleSize.SMALL -> 50f
            }
            var position = Offset(0f, 0f)
            var rect = Rect(position, Size(size, size))
            var placed = false

            for (attempts in 1..100) {
                val x = Random.nextFloat() * (maxWidth.value - size)
                val y = Random.nextFloat() * (maxHeight.value - size)
                position = Offset(x, y)
                rect = Rect(position, Size(size, size))

                if (!placedCircles.any { it.overlaps(rect) }) {
                    placedCircles.add(rect)
                    placed = true
                    break
                }
            }

            if (placed) {
                CircleItem(
                    circleData,
                    modifier = Modifier.offset(x = position.x.dp, y = position.y.dp)
                )
            }
        }
    }
}

data class Size(val width: Float, val height: Float)
data class Rect(val offset: Offset, val size: Size) {
    fun overlaps(other: Rect): Boolean {
        return offset.x < other.offset.x + other.size.width &&
                offset.x + size.width > other.offset.x &&
                offset.y < other.offset.y + other.size.height &&
                offset.y + size.height > other.offset.y
    }
}