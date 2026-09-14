package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectType
import com.example.data.repository.StudyRepository
import com.example.ui.components.FormulaDisplayCard
import com.example.ui.components.StudyTopBar
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight

@Composable
fun BookmarksScreen(
  onNavigateBack: () -> Unit,
  onNavigateToNotes: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val allNotes by StudyRepository.notesList.collectAsState()
  val bookmarkedNotes = remember(allNotes) {
    allNotes.filter { it.isBookmarked }
  }

  var selectedSubject by remember { mutableStateOf<SubjectType?>(null) }

  val filteredNotes = remember(bookmarkedNotes, selectedSubject) {
    if (selectedSubject == null) {
      bookmarkedNotes
    } else {
      bookmarkedNotes.filter { note ->
        val ch = StudyRepository.getChapterById(note.chapterId)
        ch?.subjectType == selectedSubject
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    StudyTopBar(
      title = "Bookmarks",
      subtitle = "${bookmarkedNotes.size} saved topics & formulas",
      showBack = true,
      onBackClick = onNavigateBack
    )

    // Filter Chips
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp),
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
            .clickable { selectedSubject = null }
            .testTag("bookmark_filter_all")
        ) {
          Text(
            text = "All (${bookmarkedNotes.size})",
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
            .clickable { selectedSubject = subject }
            .testTag("bookmark_filter_${subject.id}")
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

    if (filteredNotes.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bookmark,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "No Bookmarks Yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Tap the bookmark icon in any chapter notes to save key formulas and topics for rapid revision.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(filteredNotes, key = { it.id }) { note ->
          val chapter = StudyRepository.getChapterById(note.chapterId)
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onNavigateToNotes(note.chapterId) }
              .testTag("bookmark_item_${note.id}")
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                if (chapter != null) {
                  SubjectBadge(subjectType = chapter.subjectType)
                }

                IconButton(
                  onClick = { StudyRepository.toggleBookmark(note.id) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.BookmarkRemove,
                    contentDescription = "Remove bookmark",
                    tint = PrimaryPurpleLight,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              if (chapter != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "Chapter: ${chapter.title}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              // Formula preview if available
              if (note.formulas.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                FormulaDisplayCard(formula = note.formulas.first())
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Open Full Notes",
                  style = MaterialTheme.typography.labelMedium,
                  color = PrimaryPurpleLight,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  tint = PrimaryPurpleLight,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}
