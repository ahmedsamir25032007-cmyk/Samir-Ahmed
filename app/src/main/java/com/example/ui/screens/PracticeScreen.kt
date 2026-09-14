package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Question
import com.example.data.model.SubjectType
import com.example.data.repository.StudyRepository
import com.example.ui.components.StudyTopBar
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.SuccessGreen

@Composable
fun PracticeScreen(
  initialSubjectId: String? = null,
  initialChapterId: String? = null,
  modifier: Modifier = Modifier
) {
  var selectedSubject by remember {
    mutableStateOf(if (initialSubjectId != null) SubjectType.fromId(initialSubjectId) else null)
  }
  var selectedChapterId by remember { mutableStateOf(initialChapterId) }

  val questions = remember(selectedSubject, selectedChapterId) {
    val qList = StudyRepository.getPracticeQuestions(selectedSubject, selectedChapterId)
    if (qList.isEmpty()) StudyRepository.practiceQuestions else qList
  }

  var currentQuestionIndex by remember(questions) { mutableIntStateOf(0) }
  var selectedOptionIndex by remember(currentQuestionIndex) { mutableStateOf<Int?>(null) }
  var isSubmitted by remember(currentQuestionIndex) { mutableStateOf(false) }
  var showHint by remember(currentQuestionIndex) { mutableStateOf(false) }

  var sessionCorrectCount by remember { mutableIntStateOf(0) }
  var sessionAttemptedCount by remember { mutableIntStateOf(0) }
  var sessionStreak by remember { mutableIntStateOf(0) }

  val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: questions.first()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    StudyTopBar(
      title = "MCQ Practice",
      subtitle = "Topic drills with detailed step-by-step solutions"
    )

    // Subject Filter Pills
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        val isAll = selectedSubject == null
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isAll) PrimaryPurple.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAll) PrimaryPurple else MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier
            .clickable {
              selectedSubject = null
              selectedChapterId = null
              currentQuestionIndex = 0
            }
            .testTag("practice_filter_all")
        ) {
          Text(
            text = "All Subjects",
            style = MaterialTheme.typography.labelMedium,
            color = if (isAll) PrimaryPurpleLight else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isAll) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }
      }

      items(SubjectType.entries) { subject ->
        val isSelected = selectedSubject == subject
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) subject.accentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) subject.accentColor else MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier
            .clickable {
              selectedSubject = subject
              selectedChapterId = null
              currentQuestionIndex = 0
            }
            .testTag("practice_filter_${subject.id}")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(text = subject.emoji, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = subject.displayName,
              style = MaterialTheme.typography.labelMedium,
              color = if (isSelected) subject.accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }
    }

    // Score & Streak Tracker Bar
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Question ${currentQuestionIndex + 1}/${questions.size}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Score",
              tint = SuccessGreen,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "$sessionCorrectCount / $sessionAttemptedCount",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = SuccessGreen
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak",
              tint = Color(0xFFF97316),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "$sessionStreak Streak",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFF97316)
            )
          }
        }
      }
    }

    // Main Question & Options Content
    LazyColumn(
      modifier = Modifier.weight(1f),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Question Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              SubjectBadge(subjectType = currentQuestion.subjectType)

              Surface(
                color = PrimaryPurple.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = currentQuestion.difficulty,
                  style = MaterialTheme.typography.labelSmall,
                  color = PrimaryPurpleLight,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = currentQuestion.questionText,
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface,
              lineHeight = 24.sp
            )

            // Hint toggle
            if (currentQuestion.hint.isNotBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier
                  .clickable { showHint = !showHint }
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Lightbulb,
                  contentDescription = null,
                  tint = AccentBlue,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (showHint) "Hide Hint: ${currentQuestion.hint}" else "View Hint",
                  style = MaterialTheme.typography.labelSmall,
                  color = AccentBlue,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }
      }

      // Options
      items(currentQuestion.options.indices.toList()) { optionIndex ->
        val optionText = currentQuestion.options[optionIndex]
        val isSelected = selectedOptionIndex == optionIndex
        val isCorrectOption = optionIndex == currentQuestion.correctAnswerIndex

        val optionBgColor by animateColorAsState(
          targetValue = when {
            isSubmitted && isCorrectOption -> SuccessGreen.copy(alpha = 0.2f)
            isSubmitted && isSelected && !isCorrectOption -> ErrorRed.copy(alpha = 0.2f)
            isSelected -> PrimaryPurple.copy(alpha = 0.15f)
            else -> MaterialTheme.colorScheme.surfaceVariant
          },
          label = "option_bg"
        )

        val optionBorderColor by animateColorAsState(
          targetValue = when {
            isSubmitted && isCorrectOption -> SuccessGreen
            isSubmitted && isSelected && !isCorrectOption -> ErrorRed
            isSelected -> PrimaryPurple
            else -> MaterialTheme.colorScheme.outline
          },
          label = "option_border"
        )

        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = optionBgColor),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, optionBorderColor),
          modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isSubmitted) {
              selectedOptionIndex = optionIndex
            }
            .testTag("practice_option_$optionIndex")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Option letter (A, B, C, D)
            val optionLetters = listOf("A", "B", "C", "D")
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                  when {
                    isSubmitted && isCorrectOption -> SuccessGreen
                    isSubmitted && isSelected && !isCorrectOption -> ErrorRed
                    isSelected -> PrimaryPurple
                    else -> MaterialTheme.colorScheme.outlineVariant
                  }
                ),
              contentAlignment = Alignment.Center
            ) {
              if (isSubmitted && isCorrectOption) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Correct",
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              } else if (isSubmitted && isSelected && !isCorrectOption) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Wrong",
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              } else {
                Text(
                  text = optionLetters.getOrElse(optionIndex) { "?" },
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
              text = optionText,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      // Detailed Solution & Explanation Box when submitted
      if (isSubmitted) {
        item {
          val isCorrect = selectedOptionIndex == currentQuestion.correctAnswerIndex
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isCorrect) SuccessGreen.copy(alpha = 0.1f) else ErrorRed.copy(alpha = 0.1f)
            ),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isCorrect) SuccessGreen.copy(alpha = 0.4f) else ErrorRed.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                  contentDescription = null,
                  tint = if (isCorrect) SuccessGreen else ErrorRed,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isCorrect) "Correct Answer! Well done! 🎉" else "Incorrect! Let's examine the concept:",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = if (isCorrect) SuccessGreen else ErrorRed
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Detailed Solution:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = currentQuestion.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(8.dp))
      }
    }

    // Bottom Action Bar (Submit / Next)
    Surface(
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (!isSubmitted) {
          Button(
            onClick = {
              if (selectedOptionIndex != null) {
                isSubmitted = true
                sessionAttemptedCount++
                if (selectedOptionIndex == currentQuestion.correctAnswerIndex) {
                  sessionCorrectCount++
                  sessionStreak++
                } else {
                  sessionStreak = 0
                }
                StudyRepository.updateTodayProgress(additionalQuestions = 1, additionalMinutes = 1)
              }
            },
            enabled = selectedOptionIndex != null,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = PrimaryPurple
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("submit_answer_button")
          ) {
            Text(
              text = "Submit Answer",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold
            )
          }
        } else {
          Button(
            onClick = {
              if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
              } else {
                currentQuestionIndex = 0 // Cycle back or finish
              }
              selectedOptionIndex = null
              isSubmitted = false
              showHint = false
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = PrimaryPurple
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("next_question_button")
          ) {
            Text(
              text = if (currentQuestionIndex < questions.size - 1) "Next Question" else "Start Next Set",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
