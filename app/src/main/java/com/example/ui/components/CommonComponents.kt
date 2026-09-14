package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FormulaCard
import com.example.data.model.SubjectType
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight

@Composable
fun StudyTopBar(
  title: String,
  subtitle: String? = null,
  showBack: Boolean = false,
  onBackClick: () -> Unit = {},
  actions: @Composable () -> Unit = {}
) {
  Surface(
    color = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        if (showBack) {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
              .testTag("top_bar_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
        }

        Column {
          Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
          )
          if (!subtitle.isNullOrBlank()) {
            Text(
              text = subtitle,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        actions()
      }
    }
  }
}

@Composable
fun ProgressCircle(
  progress: Float,
  modifier: Modifier = Modifier,
  strokeWidth: Int = 8,
  progressColor: Color = PrimaryPurpleLight,
  trackColor: Color = MaterialTheme.colorScheme.outlineVariant
) {
  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    label = "progress_circle"
  )

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
  ) {
    CircularProgressIndicator(
      progress = { animatedProgress },
      modifier = Modifier.matchParentSize(),
      color = progressColor,
      strokeWidth = strokeWidth.dp,
      trackColor = trackColor,
      strokeCap = StrokeCap.Round
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "${(animatedProgress * 100).toInt()}%",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Done",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun SubjectBadge(
  subjectType: SubjectType,
  modifier: Modifier = Modifier
) {
  Surface(
    color = subjectType.accentColor.copy(alpha = 0.15f),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, subjectType.accentColor.copy(alpha = 0.35f)),
    modifier = modifier
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Text(
        text = subjectType.emoji,
        fontSize = 12.sp
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = subjectType.displayName,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = subjectType.accentColor
      )
    }
  }
}

@Composable
fun FormulaDisplayCard(
  formula: FormulaCard,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(PrimaryPurple.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Functions,
              contentDescription = "Formula",
              tint = PrimaryPurpleLight,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = formula.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Formula highlight box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                PrimaryPurple.copy(alpha = 0.15f),
                Color(0xFF0E1428)
              )
            )
          )
          .border(
            width = 1.dp,
            color = PrimaryPurple.copy(alpha = 0.4f),
            shape = RoundedCornerShape(10.dp)
          )
          .padding(12.dp)
      ) {
        Text(
          text = formula.formula,
          style = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.5.sp
          ),
          color = Color(0xFFF1F5F9),
          fontWeight = FontWeight.Bold
        )
      }

      if (formula.explanation.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = formula.explanation,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
