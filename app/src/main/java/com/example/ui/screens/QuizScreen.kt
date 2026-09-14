package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizResult
import com.example.data.repository.StudyRepository
import com.example.ui.components.ProgressCircle
import com.example.ui.components.StudyTopBar
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PhysicsAmber
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
  onNavigateBack: () -> Unit,
  onNavigateToProgress: () -> Unit,
  modifier: Modifier = Modifier
) {
  val quizQuestions = remember { StudyRepository.getQuizQuestions(10) }
  var currentQuestionIndex by remember { mutableIntStateOf(0) }
  val userAnswers = remember { mutableStateMapOf<Int, Int>() } // questionIndex -> selectedOptionIndex

  var timeRemainingSeconds by remember { mutableIntStateOf(15 * 60) } // 15 mins
  var isTimerRunning by remember { mutableStateOf(true) }
  var isQuizSubmitted by remember { mutableStateOf(false) }
  var showConfirmSubmitDialog by remember { mutableStateOf(false) }

  // Countdown timer coroutine
  LaunchedEffect(isTimerRunning, isQuizSubmitted) {
    while (isTimerRunning && !isQuizSubmitted && timeRemainingSeconds > 0) {
      delay(1000L)
      timeRemainingSeconds--
      if (timeRemainingSeconds <= 0) {
        isQuizSubmitted = true
        isTimerRunning = false
      }
    }
  }

  val minutes = timeRemainingSeconds / 60
  val seconds = timeRemainingSeconds % 60
  val timerFormatted = String.format("%02d:%02d", minutes, seconds)

  // Calculate stats when submitted
  val correctCount = quizQuestions.indices.count { idx ->
    userAnswers[idx] == quizQuestions[idx].correctAnswerIndex
  }
  val incorrectCount = quizQuestions.indices.count { idx ->
    userAnswers[idx] != null && userAnswers[idx] != quizQuestions[idx].correctAnswerIndex
  }
  val unattemptedCount = quizQuestions.size - (correctCount + incorrectCount)
  val scorePercentage = if (quizQuestions.isNotEmpty()) (correctCount * 100) / quizQuestions.size else 0

  if (showConfirmSubmitDialog) {
    AlertDialog(
      onDismissRequest = { showConfirmSubmitDialog = false },
      title = {
        Text("Submit Quiz?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      },
      text = {
        Text(
          "You have answered ${userAnswers.size} of ${quizQuestions.size} questions. Are you sure you want to submit?",
          style = MaterialTheme.typography.bodyMedium
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showConfirmSubmitDialog = false
            isQuizSubmitted = true
            isTimerRunning = false

            // Record to repository
            StudyRepository.recordQuizResult(
              QuizResult(
                id = "quiz_${System.currentTimeMillis()}",
                title = "JEE Mixed Mock Drill",
                subjectName = "Mixed Subjects",
                totalQuestions = quizQuestions.size,
                correctAnswers = correctCount,
                incorrectAnswers = incorrectCount,
                unattempted = unattemptedCount,
                scorePercentage = scorePercentage,
                timeSpentSeconds = (15 * 60) - timeRemainingSeconds,
                timestamp = "Just now"
              )
            )
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
        ) {
          Text("Yes, Submit")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showConfirmSubmitDialog = false }) {
          Text("Keep Solving")
        }
      }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    if (!isQuizSubmitted) {
      // Top Bar with Timer
      StudyTopBar(
        title = "JEE Speed Quiz",
        subtitle = "10 Questions • Mixed Syllabus",
        showBack = true,
        onBackClick = onNavigateBack,
        actions = {
          Surface(
            color = if (timeRemainingSeconds < 180) ErrorRed.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (timeRemainingSeconds < 180) ErrorRed else PrimaryPurple.copy(alpha = 0.5f)
            )
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Timer",
                tint = if (timeRemainingSeconds < 180) ErrorRed else PrimaryPurpleLight,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = timerFormatted,
                style = MaterialTheme.typography.labelLarge.copy(fontFamily = FontFamily.Monospace),
                fontWeight = FontWeight.Bold,
                color = if (timeRemainingSeconds < 180) ErrorRed else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      )

      // Question Navigator Strip (1..10)
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        itemsIndexed(quizQuestions) { idx, _ ->
          val isCurrent = idx == currentQuestionIndex
          val isAnswered = userAnswers.containsKey(idx)

          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(
                when {
                  isCurrent -> PrimaryPurple
                  isAnswered -> SuccessGreen.copy(alpha = 0.25f)
                  else -> MaterialTheme.colorScheme.surfaceVariant
                }
              )
              .border(
                width = 1.dp,
                color = when {
                  isCurrent -> PrimaryPurpleLight
                  isAnswered -> SuccessGreen
                  else -> MaterialTheme.colorScheme.outline
                },
                shape = CircleShape
              )
              .clickable { currentQuestionIndex = idx }
              .testTag("quiz_jump_pill_$idx"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "${idx + 1}",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      // Active Question Card
      val activeQuestion = quizQuestions[currentQuestionIndex]
      val selectedAnswer = userAnswers[currentQuestionIndex]

      LazyColumn(
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                SubjectBadge(subjectType = activeQuestion.subjectType)
                Text(
                  text = "Question ${currentQuestionIndex + 1} of ${quizQuestions.size}",
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = activeQuestion.questionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
              )
            }
          }
        }

        // 4 Options
        items(activeQuestion.options.size) { optIdx ->
          val optText = activeQuestion.options[optIdx]
          val isSelected = selectedAnswer == optIdx

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) PrimaryPurple.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
            ),
            border = androidx.compose.foundation.BorderStroke(
              1.5.dp,
              if (isSelected) PrimaryPurple else MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                userAnswers[currentQuestionIndex] = optIdx
              }
              .testTag("quiz_option_$optIdx")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              val letters = listOf("A", "B", "C", "D")
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) PrimaryPurple else MaterialTheme.colorScheme.outlineVariant),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = letters.getOrElse(optIdx) { "?" },
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Text(
                text = optText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // Bottom Navigation & Submit Bar
      Surface(
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedButton(
            onClick = {
              if (currentQuestionIndex > 0) currentQuestionIndex--
            },
            enabled = currentQuestionIndex > 0,
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Prev")
          }

          Button(
            onClick = { showConfirmSubmitDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("submit_quiz_button")
          ) {
            Text("Submit Quiz (${userAnswers.size}/${quizQuestions.size})", fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = {
              if (currentQuestionIndex < quizQuestions.size - 1) currentQuestionIndex++
            },
            enabled = currentQuestionIndex < quizQuestions.size - 1,
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Next")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
          }
        }
      }
    } else {
      // QUIZ RESULT SCREEN
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
      ) {
        item {
          Spacer(modifier = Modifier.height(10.dp))
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(PrimaryPurple.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = "Trophy",
              tint = PhysicsAmber,
              modifier = Modifier.size(40.dp)
            )
          }
        }

        item {
          Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Here is your detailed performance breakdown",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Score Card
        item {
          Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryPurple.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              ProgressCircle(
                progress = scorePercentage / 100f,
                modifier = Modifier.size(100.dp),
                strokeWidth = 10,
                progressColor = if (scorePercentage >= 70) SuccessGreen else PhysicsAmber
              )

              Spacer(modifier = Modifier.height(18.dp))

              Text(
                text = "Score: $scorePercentage%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "$correctCount of ${quizQuestions.size} Questions Correct",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(20.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
              ) {
                ResultStatItem(label = "Correct", value = "$correctCount", color = SuccessGreen)
                ResultStatItem(label = "Incorrect", value = "$incorrectCount", color = ErrorRed)
                ResultStatItem(label = "Skipped", value = "$unattemptedCount", color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }

        // Action Buttons
        item {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
              onClick = {
                // Retake quiz
                userAnswers.clear()
                currentQuestionIndex = 0
                timeRemainingSeconds = 15 * 60
                isQuizSubmitted = false
                isTimerRunning = true
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("retake_quiz_button")
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Retake Quiz", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = onNavigateToProgress,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("view_progress_button")
            ) {
              Text("View Progress Dashboard", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
fun ResultStatItem(
  label: String,
  value: String,
  color: Color
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = color
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
