package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
  val route: String,
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  data object Home : BottomNavItem(
    route = "home",
    title = "Home",
    selectedIcon = Icons.Filled.Home,
    unselectedIcon = Icons.Outlined.Home
  )
  data object Subjects : BottomNavItem(
    route = "subjects",
    title = "Subjects",
    selectedIcon = Icons.Filled.Book,
    unselectedIcon = Icons.Outlined.Book
  )
  data object Practice : BottomNavItem(
    route = "practice",
    title = "Practice",
    selectedIcon = Icons.Filled.Quiz,
    unselectedIcon = Icons.Outlined.Quiz
  )
  data object Progress : BottomNavItem(
    route = "progress",
    title = "Progress",
    selectedIcon = Icons.Filled.AutoGraph,
    unselectedIcon = Icons.Outlined.AutoGraph
  )
  data object Profile : BottomNavItem(
    route = "profile",
    title = "Profile",
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person
  )
}

object StudyDestinations {
  const val HOME = "home"
  const val SUBJECTS = "subjects?subjectId={subjectId}"
  const val PRACTICE = "practice?subjectId={subjectId}&chapterId={chapterId}"
  const val PROGRESS = "progress"
  const val PROFILE = "profile"
  const val NOTES = "notes/{chapterId}"
  const val QUIZ = "quiz"
  const val BOOKMARKS = "bookmarks"

  fun subjectsRoute(subjectId: String? = null): String =
    if (subjectId != null) "subjects?subjectId=$subjectId" else "subjects"

  fun practiceRoute(subjectId: String? = null, chapterId: String? = null): String {
    val params = mutableListOf<String>()
    if (!subjectId.isNullOrEmpty()) params.add("subjectId=$subjectId")
    if (!chapterId.isNullOrEmpty()) params.add("chapterId=$chapterId")
    return if (params.isNotEmpty()) "practice?${params.joinToString("&")}" else "practice"
  }

  fun notesRoute(chapterId: String): String = "notes/$chapterId"
}
