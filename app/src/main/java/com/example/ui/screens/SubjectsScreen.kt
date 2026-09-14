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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.model.Chapter
import com.example.data.model.SubjectType
import com.example.data.repository.StudyRepository
import com.example.ui.components.StudyTopBar
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight

@Composable
fun SubjectsScreen(
  initialSubjectId: String? = null,
  onNavigateToNotes: (String) -> Unit,
  onNavigateToPractice: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedSubject by remember {
    mutableStateOf(
      if (initialSubjectId != null) SubjectType.fromId(initialSubjectId) else null
    )
  }

  val chapters = remember(selectedSubject) {
    StudyRepository.getChapters(selectedSubject)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    StudyTopBar(
      title = "Subjects & Chapters",
      subtitle = "Comprehensive syllabus for JEE Main & Advanced"
    )

    // Subject Filter Tabs
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      item {
        val isAllSelected = selectedSubject == null
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isAllSelected) PrimaryPurple.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAllSelected) PrimaryPurple else MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier
            .clickable { selectedSubject = null }
            .testTag("filter_all_subjects")
        ) {
          Text(
            text = "📚 All Subjects",
            style = MaterialTheme.typography.labelLarge,
            color = if (isAllSelected) PrimaryPurpleLight else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
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
            .clickable { selectedSubject = subject }
            .testTag("filter_subject_${subject.id}")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Text(text = subject.emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = subject.displayName,
              style = MaterialTheme.typography.labelLarge,
              color = if (isSelected) subject.accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }
    }

    // Chapters List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(chapters, key = { it.id }) { chapter ->
        ChapterCard(
          chapter = chapter,
          onReadNotes = { onNavigateToNotes(chapter.id) },
          onPractice = { onNavigateToPractice(chapter.subjectType.id, chapter.id) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
fun ChapterCard(
  chapter: Chapter,
  onReadNotes: () -> Unit,
  onPractice: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = modifier
      .fillMaxWidth()
      .testTag("chapter_card_${chapter.id}")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        SubjectBadge(subjectType = chapter.subjectType)

        if (chapter.isCompleted) {
          Surface(
            color = Color(0xFF10B981).copy(alpha = 0.15f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Completed",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF10B981),
                fontWeight = FontWeight.Bold
              )
            }
          }
        } else {
          Text(
            text = "${(chapter.progress * 100).toInt()}% Mastered",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = chapter.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = chapter.description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      LinearProgressIndicator(
        progress = { chapter.progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = chapter.subjectType.accentColor,
        trackColor = MaterialTheme.colorScheme.outlineVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = onReadNotes,
          modifier = Modifier
            .weight(1f)
            .testTag("read_notes_button_${chapter.id}"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = chapter.subjectType.accentColor
          )
        ) {
          Icon(
            imageVector = Icons.Default.MenuBook,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Notes",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
          )
        }

        OutlinedButton(
          onClick = onPractice,
          modifier = Modifier
            .weight(1f)
            .testTag("practice_button_${chapter.id}"),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            chapter.subjectType.accentColor.copy(alpha = 0.6f)
          ),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = chapter.subjectType.accentColor
          )
        ) {
          Icon(
            imageVector = Icons.Default.Quiz,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Practice",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
