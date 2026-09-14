package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Chapter
import com.example.data.model.SubjectType
import com.example.data.repository.StudyRepository
import com.example.ui.components.ProgressCircle
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.ChemistryEmerald
import com.example.ui.theme.MathAzure
import com.example.ui.theme.PhysicsAmber
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight

@Composable
fun HomeScreen(
  onNavigateToSubjects: (String?) -> Unit,
  onNavigateToPractice: (String?, String?) -> Unit,
  onNavigateToNotes: (String) -> Unit,
  onNavigateToQuiz: () -> Unit,
  onNavigateToBookmarks: () -> Unit,
  onNavigateToProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  val userProgress by StudyRepository.userProgress.collectAsState()
  val lastChapter = StudyRepository.getChapterById(userProgress.lastStudiedChapterId)
    ?: StudyRepository.chapters.first()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // 1. Header Section
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Study Verse",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Learn. Practice. Improve.",
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryPurpleLight,
            fontWeight = FontWeight.Medium
          )
        }

        IconButton(
          onClick = onNavigateToProfile,
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .testTag("home_profile_button")
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    }

    // 2. Welcome Greeting & Streak Pill
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Welcome back 👋",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Ready to continue your JEE journey?",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Surface(
            color = Color(0xFFEA580C).copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF97316).copy(alpha = 0.4f))
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak",
                tint = Color(0xFFF97316),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${userProgress.studyStreakDays} Days",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFF97316),
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // 3. Today's Progress Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.5.dp,
          Brush.horizontalGradient(
            colors = listOf(PrimaryPurple.copy(alpha = 0.5f), AccentBlue.copy(alpha = 0.3f))
          )
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.TrendingUp,
                  contentDescription = null,
                  tint = PrimaryPurpleLight,
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Today's Study Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Surface(
              color = PrimaryPurple.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "Target: JEE '26",
                style = MaterialTheme.typography.labelSmall,
                color = PrimaryPurpleLight,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            ProgressCircle(
              progress = userProgress.overallProgressPercent / 100f,
              modifier = Modifier.size(76.dp),
              strokeWidth = 7
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
              modifier = Modifier.weight(1f),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Questions solved",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                Text(
                  text = "${userProgress.questionsSolvedToday} Qs",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = PhysicsAmber,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Study time today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                Text(
                  text = "${userProgress.studyTimeMinutesToday / 60}h ${userProgress.studyTimeMinutesToday % 60}m",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }

    // 4. Quick Actions
    item {
      Column {
        Text(
          text = "Quick Actions",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          QuickActionButton(
            title = "Notes",
            icon = Icons.Default.MenuBook,
            accentColor = PrimaryPurpleLight,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToNotes(lastChapter.id) }
          )
          QuickActionButton(
            title = "Practice",
            icon = Icons.Default.Quiz,
            accentColor = AccentBlue,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToPractice(null, null) }
          )
          QuickActionButton(
            title = "Quiz",
            icon = Icons.Default.AutoAwesome,
            accentColor = PhysicsAmber,
            modifier = Modifier.weight(1f),
            onClick = onNavigateToQuiz
          )
          QuickActionButton(
            title = "Bookmarks",
            icon = Icons.Default.Bookmark,
            accentColor = ChemistryEmerald,
            modifier = Modifier.weight(1f),
            onClick = onNavigateToBookmarks
          )
        }
      }
    }

    // 5. Continue Studying
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Continue Studying",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToNotes(lastChapter.id) }
            .testTag("continue_studying_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              SubjectBadge(subjectType = lastChapter.subjectType)

              Text(
                text = "${(lastChapter.progress * 100).toInt()}% Done",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = lastChapter.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${lastChapter.topicsCount} Topics • ${lastChapter.questionsCount} Practice MCQs",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
              progress = { lastChapter.progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = lastChapter.subjectType.accentColor,
              trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = { onNavigateToNotes(lastChapter.id) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("continue_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = lastChapter.subjectType.accentColor
              )
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Continue Reading Notes",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // 6. Subjects Section (Three Large Subject Cards)
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Subjects",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )

          Text(
            text = "View All",
            style = MaterialTheme.typography.labelMedium,
            color = PrimaryPurpleLight,
            modifier = Modifier
              .clickable { onNavigateToSubjects(null) }
              .padding(4.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          SubjectLargeCard(
            subjectType = SubjectType.PHYSICS,
            chaptersCount = 5,
            questionsCount = 100,
            completionPercent = 55,
            onClick = { onNavigateToSubjects(SubjectType.PHYSICS.id) }
          )
          SubjectLargeCard(
            subjectType = SubjectType.CHEMISTRY,
            chaptersCount = 5,
            questionsCount = 104,
            completionPercent = 65,
            onClick = { onNavigateToSubjects(SubjectType.CHEMISTRY.id) }
          )
          SubjectLargeCard(
            subjectType = SubjectType.MATHEMATICS,
            chaptersCount = 5,
            questionsCount = 104,
            completionPercent = 58,
            onClick = { onNavigateToSubjects(SubjectType.MATHEMATICS.id) }
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun QuickActionButton(
  title: String,
  icon: ImageVector,
  accentColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag("quick_action_${title.lowercase()}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 14.dp, horizontal = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(accentColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = accentColor,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

@Composable
fun SubjectLargeCard(
  subjectType: SubjectType,
  chaptersCount: Int,
  questionsCount: Int,
  completionPercent: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("subject_card_${subjectType.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      subjectType.accentColor.copy(alpha = 0.3f)
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(
              subjectType.accentColor.copy(alpha = 0.12f),
              Color.Transparent
            )
          )
        )
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(subjectType.accentColor.copy(alpha = 0.2f))
              .border(1.dp, subjectType.accentColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = subjectType.emoji,
              fontSize = 24.sp
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column {
            Text(
              text = subjectType.displayName,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "$chaptersCount Chapters • $questionsCount MCQs",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              LinearProgressIndicator(
                progress = { completionPercent / 100f },
                modifier = Modifier
                  .width(100.dp)
                  .height(5.dp)
                  .clip(RoundedCornerShape(3.dp)),
                color = subjectType.accentColor,
                trackColor = MaterialTheme.colorScheme.outlineVariant
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "$completionPercent%",
                style = MaterialTheme.typography.labelSmall,
                color = subjectType.accentColor,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = "Open ${subjectType.displayName}",
          tint = subjectType.accentColor,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
