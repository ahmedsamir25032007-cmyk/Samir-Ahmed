package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.ChemistryEmerald
import com.example.ui.theme.MathAzure
import com.example.ui.theme.PhysicsAmber

enum class SubjectType(
  val id: String,
  val displayName: String,
  val emoji: String,
  val accentColor: Color,
  val tagLine: String
) {
  PHYSICS(
    id = "physics",
    displayName = "Physics",
    emoji = "⚡",
    accentColor = PhysicsAmber,
    tagLine = "Mechanics, Thermodynamics & Electrodynamics"
  ),
  CHEMISTRY(
    id = "chemistry",
    displayName = "Chemistry",
    emoji = "🧪",
    accentColor = ChemistryEmerald,
    tagLine = "Physical, Organic & Inorganic Principles"
  ),
  MATHEMATICS(
    id = "mathematics",
    displayName = "Mathematics",
    emoji = "📐",
    accentColor = MathAzure,
    tagLine = "Calculus, Coordinate Geometry & Algebra"
  );

  companion object {
    fun fromId(id: String?): SubjectType =
      entries.find { it.id.equals(id, ignoreCase = true) } ?: PHYSICS
  }
}

data class Chapter(
  val id: String,
  val subjectType: SubjectType,
  val title: String,
  val description: String,
  val topicsCount: Int,
  val questionsCount: Int,
  val progress: Float, // 0.0 to 1.0
  val isCompleted: Boolean = false,
  val order: Int = 1
)

data class FormulaCard(
  val title: String,
  val formula: String,
  val explanation: String
)

data class NoteTopic(
  val id: String,
  val chapterId: String,
  val topicNumber: Int,
  val title: String,
  val contentOverview: String,
  val formulas: List<FormulaCard>,
  val importantPoints: List<String>,
  val isBookmarked: Boolean = false
)

data class Question(
  val id: String,
  val chapterId: String,
  val subjectType: SubjectType,
  val difficulty: String, // "Easy", "Medium", "JEE Advanced"
  val questionText: String,
  val options: List<String>,
  val correctAnswerIndex: Int,
  val explanation: String,
  val hint: String
)

data class QuizQuestionState(
  val question: Question,
  val selectedAnswerIndex: Int? = null,
  val isFlagged: Boolean = false
)

data class QuizResult(
  val id: String,
  val title: String,
  val subjectName: String,
  val totalQuestions: Int,
  val correctAnswers: Int,
  val incorrectAnswers: Int,
  val unattempted: Int,
  val scorePercentage: Int,
  val timeSpentSeconds: Int,
  val timestamp: String
)

data class UserProgress(
  val studentName: String = "Arjun Sharma",
  val examTarget: String = "JEE Main & Advanced 2026",
  val overallProgressPercent: Int = 68,
  val questionsSolvedToday: Int = 24,
  val studyTimeMinutesToday: Int = 135,
  val studyStreakDays: Int = 14,
  val totalQuestionsSolved: Int = 342,
  val overallAccuracy: Int = 82,
  val totalStudyHours: Float = 46.5f,
  val lastStudiedSubject: SubjectType = SubjectType.PHYSICS,
  val lastStudiedChapterId: String = "phy_kinematics",
  val isDarkMode: Boolean = true
)
