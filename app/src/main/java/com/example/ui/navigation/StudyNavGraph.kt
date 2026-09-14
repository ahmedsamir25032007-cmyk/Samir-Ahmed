package com.example.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SubjectsScreen
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight

@Composable
fun StudyVerseApp(
  navController: NavHostController = rememberNavController()
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Subjects,
    BottomNavItem.Practice,
    BottomNavItem.Progress,
    BottomNavItem.Profile
  )

  // Show bottom bar on the 5 core tabs
  val showBottomBar = bottomNavItems.any { item ->
    currentRoute == item.route || currentRoute?.startsWith(item.route) == true
  } && currentRoute?.startsWith("notes") != true && currentRoute != StudyDestinations.QUIZ && currentRoute != StudyDestinations.BOOKMARKS

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    bottomBar = {
      if (showBottomBar) {
        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 8.dp,
          windowInsets = WindowInsets.navigationBars,
          modifier = Modifier.testTag("study_bottom_navigation")
        ) {
          bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route ||
              (item.route != "home" && currentRoute?.startsWith(item.route) == true)

            NavigationBarItem(
              selected = isSelected,
              onClick = {
                if (currentRoute != item.route) {
                  navController.navigate(item.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                      saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                  }
                }
              },
              icon = {
                Icon(
                  imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                  contentDescription = item.title
                )
              },
              label = {
                Text(
                  text = item.title,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryPurpleLight,
                selectedTextColor = PrimaryPurpleLight,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = PrimaryPurple.copy(alpha = 0.2f)
              ),
              modifier = Modifier.testTag("bottom_nav_${item.title.lowercase()}")
            )
          }
        }
      }
    }
  ) { paddingValues ->
    NavHost(
      navController = navController,
      startDestination = StudyDestinations.HOME,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      enterTransition = { EnterTransition.None },
      exitTransition = { ExitTransition.None }
    ) {
      // 1. Home
      composable(StudyDestinations.HOME) {
        HomeScreen(
          onNavigateToSubjects = { subjectId ->
            navController.navigate(StudyDestinations.subjectsRoute(subjectId))
          },
          onNavigateToPractice = { subjectId, chapterId ->
            navController.navigate(StudyDestinations.practiceRoute(subjectId, chapterId))
          },
          onNavigateToNotes = { chapterId ->
            navController.navigate(StudyDestinations.notesRoute(chapterId))
          },
          onNavigateToQuiz = {
            navController.navigate(StudyDestinations.QUIZ)
          },
          onNavigateToBookmarks = {
            navController.navigate(StudyDestinations.BOOKMARKS)
          },
          onNavigateToProfile = {
            navController.navigate(StudyDestinations.PROFILE)
          }
        )
      }

      // 2. Subjects
      composable(
        route = StudyDestinations.SUBJECTS,
        arguments = listOf(
          navArgument("subjectId") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
          }
        )
      ) { backStackEntry ->
        val subjectId = backStackEntry.arguments?.getString("subjectId")
        SubjectsScreen(
          initialSubjectId = subjectId,
          onNavigateToNotes = { chapterId ->
            navController.navigate(StudyDestinations.notesRoute(chapterId))
          },
          onNavigateToPractice = { sId, chId ->
            navController.navigate(StudyDestinations.practiceRoute(sId, chId))
          }
        )
      }

      // 3. Practice
      composable(
        route = StudyDestinations.PRACTICE,
        arguments = listOf(
          navArgument("subjectId") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
          },
          navArgument("chapterId") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
          }
        )
      ) { backStackEntry ->
        val subjectId = backStackEntry.arguments?.getString("subjectId")
        val chapterId = backStackEntry.arguments?.getString("chapterId")
        PracticeScreen(
          initialSubjectId = subjectId,
          initialChapterId = chapterId
        )
      }

      // 4. Progress
      composable(StudyDestinations.PROGRESS) {
        ProgressScreen()
      }

      // 5. Profile
      composable(StudyDestinations.PROFILE) {
        ProfileScreen(
          onNavigateBack = { navController.popBackStack() }
        )
      }

      // 6. Notes
      composable(
        route = StudyDestinations.NOTES,
        arguments = listOf(
          navArgument("chapterId") {
            type = NavType.StringType
          }
        )
      ) { backStackEntry ->
        val chapterId = backStackEntry.arguments?.getString("chapterId") ?: "phy_kinematics"
        NotesScreen(
          chapterId = chapterId,
          onNavigateBack = { navController.popBackStack() },
          onNavigateToPractice = { sId, chId ->
            navController.navigate(StudyDestinations.practiceRoute(sId, chId))
          }
        )
      }

      // 7. Quiz
      composable(StudyDestinations.QUIZ) {
        QuizScreen(
          onNavigateBack = { navController.popBackStack() },
          onNavigateToProgress = {
            navController.navigate(StudyDestinations.PROGRESS) {
              popUpTo(StudyDestinations.HOME)
            }
          }
        )
      }

      // 8. Bookmarks
      composable(StudyDestinations.BOOKMARKS) {
        BookmarksScreen(
          onNavigateBack = { navController.popBackStack() },
          onNavigateToNotes = { chapterId ->
            navController.navigate(StudyDestinations.notesRoute(chapterId))
          }
        )
      }
    }
  }
}
