package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizResult
import com.example.data.model.SubjectType
import com.example.data.repository.StudyRepository
import com.example.ui.components.ProgressCircle
import com.example.ui.components.StudyTopBar
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.ChemistryEmerald
import com.example.ui.theme.MathAzure
import com.example.ui.theme.PhysicsAmber
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.SuccessGreen

@Composable
fun ProgressScreen(
  modifier: Modifier = Modifier
) {
  val userProgress by StudyRepository.userProgress.collectAsState()
  val quizHistory by StudyRepository.quizHistory.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    StudyTopBar(
      title = "Progress Dashboard",
      subtitle = "Track your JEE preparation metrics"
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // 1. Overall Completion Card
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Overall JEE Readiness",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Based on completed chapter notes and MCQ practice accuracy.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
              )
              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                color = PrimaryPurple.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = "On Track for JEE 2026",
                  style = MaterialTheme.typography.labelSmall,
                  color = PrimaryPurpleLight,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(16.dp))

            ProgressCircle(
              progress = userProgress.overallProgressPercent / 100f,
              modifier = Modifier.size(80.dp),
              strokeWidth = 8
            )
          }
        }
      }

      // 2. Metrics Grid (Questions, Accuracy, Streak, Hours)
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          MetricStatCard(
            title = "Solved",
            value = "${userProgress.totalQuestionsSolved}",
            subtitle = "Total MCQs",
            icon = Icons.Default.CheckCircle,
            color = AccentBlue,
            modifier = Modifier.weight(1f)
          )
          MetricStatCard(
            title = "Accuracy",
            value = "${userProgress.overallAccuracy}%",
            subtitle = "Average",
            icon = Icons.Default.TrendingUp,
            color = SuccessGreen,
            modifier = Modifier.weight(1f)
          )
        }
      }

      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          MetricStatCard(
            title = "Study Streak",
            value = "${userProgress.studyStreakDays} Days",
            subtitle = "Keep it burning!",
            icon = Icons.Default.LocalFireDepartment,
            color = Color(0xFFF97316),
            modifier = Modifier.weight(1f)
          )
          MetricStatCard(
            title = "Study Hours",
            value = "${userProgress.totalStudyHours} hrs",
            subtitle = "Total time",
            icon = Icons.Default.AccessTime,
            color = PrimaryPurpleLight,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // 3. Subject-Wise Progress Breakdown
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Text(
              text = "Subject-Wise Mastery",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            SubjectProgressRow(
              subject = SubjectType.PHYSICS,
              percent = 55,
              chaptersDone = "3 of 5 chapters"
            )
            Spacer(modifier = Modifier.height(14.dp))
            SubjectProgressRow(
              subject = SubjectType.CHEMISTRY,
              percent = 65,
              chaptersDone = "3 of 5 chapters"
            )
            Spacer(modifier = Modifier.height(14.dp))
            SubjectProgressRow(
              subject = SubjectType.MATHEMATICS,
              percent = 58,
              chaptersDone = "3 of 5 chapters"
            )
          }
        }
      }

      // 4. Recent Quiz Results
      item {
        Text(
          text = "Recent Quiz Results",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }

      items(quizHistory, key = { it.id }) { quiz ->
        QuizHistoryCard(quiz = quiz)
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
fun MetricStatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun SubjectProgressRow(
  subject: SubjectType,
  percent: Int,
  chaptersDone: String
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = subject.emoji, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = subject.displayName,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = chaptersDone,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "$percent%",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = subject.accentColor
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    LinearProgressIndicator(
      progress = { percent / 100f },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = subject.accentColor,
      trackColor = MaterialTheme.colorScheme.outlineVariant
    )
  }
}

@Composable
fun QuizHistoryCard(quiz: QuizResult) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = quiz.title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${quiz.subjectName} • ${quiz.correctAnswers}/${quiz.totalQuestions} Correct • ${quiz.timestamp}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Surface(
        color = if (quiz.scorePercentage >= 75) SuccessGreen.copy(alpha = 0.15f) else PhysicsAmber.copy(alpha = 0.15f),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text(
          text = "${quiz.scorePercentage}%",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = if (quiz.scorePercentage >= 75) SuccessGreen else PhysicsAmber,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }
    }
  }
}
