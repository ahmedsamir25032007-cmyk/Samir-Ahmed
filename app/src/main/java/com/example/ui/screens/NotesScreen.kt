package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteTopic
import com.example.data.repository.StudyRepository
import com.example.ui.components.FormulaDisplayCard
import com.example.ui.components.StudyTopBar
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight

@Composable
fun NotesScreen(
  chapterId: String,
  onNavigateBack: () -> Unit,
  onNavigateToPractice: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val chapter = StudyRepository.getChapterById(chapterId) ?: StudyRepository.chapters.first()
  val allNotes by StudyRepository.notesList.collectAsState()
  val chapterNotes = remember(allNotes, chapterId) {
    StudyRepository.getNotesForChapter(chapterId).ifEmpty {
      // Fallback topic if not specifically authored yet
      listOf(
        NoteTopic(
          id = "fallback_${chapterId}",
          chapterId = chapterId,
          topicNumber = 1,
          title = "Core Theory & Concepts",
          contentOverview = "Comprehensive conceptual summary for ${chapter.title}. Master definitions, derivations, and application boundaries for JEE Main & Advanced examinations.",
          formulas = listOf(
            com.example.data.model.FormulaCard(
              title = "Fundamental Governing Equation",
              formula = "f(x, t) = A · sin(ωt - kx + φ)",
              explanation = "Standard form applicable across key derivations in this topic."
            )
          ),
          importantPoints = listOf(
            "Always check dimensional homogeneity before finalizing calculations.",
            "Identify symmetry conditions to simplify differential integrals.",
            "Verify boundary conditions at extremes (t=0, x=0, etc.)."
          ),
          isBookmarked = false
        )
      )
    }
  }

  var selectedTopicIndex by remember { mutableIntStateOf(0) }
  val currentTopic = chapterNotes.getOrNull(selectedTopicIndex) ?: chapterNotes.first()

  // Track as last studied
  remember(chapterId) {
    StudyRepository.setLastStudied(chapter.subjectType, chapterId)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar with Back and Bookmark
    StudyTopBar(
      title = chapter.title,
      subtitle = chapter.subjectType.displayName,
      showBack = true,
      onBackClick = onNavigateBack,
      actions = {
        IconButton(
          onClick = {
            StudyRepository.toggleBookmark(currentTopic.id)
            val msg = if (currentTopic.isBookmarked) "Removed from bookmarks" else "Saved to bookmarks"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .testTag("bookmark_topic_button")
        ) {
          Icon(
            imageVector = if (currentTopic.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = "Bookmark",
            tint = if (currentTopic.isBookmarked) PrimaryPurpleLight else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    )

    // Topic Selection Strip
    if (chapterNotes.size > 1) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        itemsIndexed(chapterNotes) { index, topic ->
          val isSelected = index == selectedTopicIndex
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) chapter.subjectType.accentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) chapter.subjectType.accentColor else MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
              .clickable { selectedTopicIndex = index }
              .testTag("topic_tab_$index")
          ) {
            Text(
              text = "Topic ${index + 1}: ${topic.title}",
              style = MaterialTheme.typography.labelMedium,
              color = if (isSelected) chapter.subjectType.accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
            )
          }
        }
      }
    }

    // Main Reading Content
    AnimatedContent(
      targetState = currentTopic,
      transitionSpec = { fadeIn() togetherWith fadeOut() },
      label = "topic_content",
      modifier = Modifier.weight(1f)
    ) { topic ->
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Topic Header Card
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
                SubjectBadge(subjectType = chapter.subjectType)
                Text(
                  text = "Topic ${selectedTopicIndex + 1} of ${chapterNotes.size}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = topic.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = topic.contentOverview,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
              )
            }
          }
        }

        // Formulas Section
        if (topic.formulas.isNotEmpty()) {
          item {
            Text(
              text = "Key Formulas & Derivations",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
          }

          items(topic.formulas.size) { fIdx ->
            FormulaDisplayCard(formula = topic.formulas[fIdx])
          }
        }

        // Important Points Section
        if (topic.importantPoints.isNotEmpty()) {
          item {
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
              ),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                AccentBlue.copy(alpha = 0.3f)
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(28.dp)
                      .clip(CircleShape)
                      .background(AccentBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Lightbulb,
                      contentDescription = "Important points",
                      tint = AccentBlue,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "High-Yield JEE Tips",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                  )
                }

                Spacer(modifier = Modifier.height(12.dp))

                topic.importantPoints.forEach { point ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                  ) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = null,
                      tint = chapter.subjectType.accentColor,
                      modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = point,
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onSurface,
                      lineHeight = 20.sp
                    )
                  }
                }
              }
            }
          }
        }

        // Practice Shortcut Button
        item {
          OutlinedButton(
            onClick = { onNavigateToPractice(chapter.subjectType.id, chapter.id) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("notes_practice_now_button"),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.6f))
          ) {
            Icon(
              imageVector = Icons.Default.Quiz,
              contentDescription = null,
              tint = PrimaryPurpleLight,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Practice ${chapter.title} MCQs",
              style = MaterialTheme.typography.labelLarge,
              color = PrimaryPurpleLight,
              fontWeight = FontWeight.Bold
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }

    // Bottom Previous / Next Topic Bar
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
            if (selectedTopicIndex > 0) selectedTopicIndex--
          },
          enabled = selectedTopicIndex > 0,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("previous_topic_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("Previous")
        }

        Text(
          text = "${selectedTopicIndex + 1} / ${chapterNotes.size}",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
          onClick = {
            if (selectedTopicIndex < chapterNotes.size - 1) selectedTopicIndex++
          },
          enabled = selectedTopicIndex < chapterNotes.size - 1,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = chapter.subjectType.accentColor
          ),
          modifier = Modifier.testTag("next_topic_button")
        ) {
          Text("Next")
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}
