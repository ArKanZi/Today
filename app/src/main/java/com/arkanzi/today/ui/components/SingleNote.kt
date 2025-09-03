package com.arkanzi.today.ui.components


import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.arkanzi.today.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SingleNote(
    isCompleted: Boolean,
    title: String,
    startingTime: String,
    isRevealed: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick:() -> Unit
) {

    var singleNoteOptionsWidth by remember {
        mutableFloatStateOf(0f)
    }

    val singleNoteOffSet = remember {
        Animatable(initialValue = 0f)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(isRevealed, singleNoteOptionsWidth) {
        if (isRevealed) {
            singleNoteOffSet.animateTo(-singleNoteOptionsWidth)
        } else {
            singleNoteOffSet.animateTo(0f)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onSizeChanged { singleNoteOptionsWidth = it.width.toFloat() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconContainer(
                icon = R.drawable.ic_edit,
                description = "Edit Note",
                modifier = Modifier
                    .size(36.dp)
                    .padding(2.dp),
                tint = Color(0xFF798CBE),
                onClick = {
                    scope.launch {
                        singleNoteOffSet.animateTo(0f)
                    }
                }
            )
            IconContainer(
                icon = R.drawable.ic_trash,
                description = "Delete Note",
                modifier = Modifier
                    .size(36.dp)
                    .padding(2.dp),
                tint = Color(0xFFD07589)
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(singleNoteOffSet.value.roundToInt(), 0) }
                .pointerInput(singleNoteOptionsWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (singleNoteOffSet.value + dragAmount)
                                    .coerceIn(-singleNoteOptionsWidth, 0f)
                                singleNoteOffSet.snapTo(newOffset)

                            }
                        },
                        onDragEnd = {
                            when {
                                singleNoteOffSet.value <= -singleNoteOptionsWidth / 2f -> {
                                    scope.launch {
                                        singleNoteOffSet.animateTo(-singleNoteOptionsWidth)
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        singleNoteOffSet.animateTo(0f)
                                    }
                                }
                            }

                        }
                    )
                }) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                    CustomCircleCheckbox(isCompleted, { onCheckedChange(it) }, size = 24)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp)
                        .weight(2f).clickable{onClick}
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal

                    )
                }
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        startingTime,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(20.dp)
                        .clip(
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "All Details",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                    )
                }
            }
            if (singleNoteOffSet.value < 0f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                scope.launch {
                                    singleNoteOffSet.animateTo(0f)
                                }
                            })
                        }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SingleNotePreview() {
    SingleNote(true, "Something", "10:00AM", isRevealed = false,{},{})
}