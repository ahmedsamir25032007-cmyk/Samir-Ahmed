package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.data.repository.StudyRepository
import com.example.ui.navigation.StudyVerseApp
import com.example.ui.theme.StudyVerseTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val userProgress by StudyRepository.userProgress.collectAsState()
      StudyVerseTheme(darkTheme = userProgress.isDarkMode) {
        StudyVerseApp()
      }
    }
  }
}
