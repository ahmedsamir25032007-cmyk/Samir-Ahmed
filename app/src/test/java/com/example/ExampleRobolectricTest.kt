package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.SubjectType
import com.example.data.repository.StudyRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Study Verse", appName)
  }

  @Test
  fun `verify JEE subjects and chapters exist`() {
    val physicsChapters = StudyRepository.getChapters(SubjectType.PHYSICS)
    val chemistryChapters = StudyRepository.getChapters(SubjectType.CHEMISTRY)
    val mathChapters = StudyRepository.getChapters(SubjectType.MATHEMATICS)

    assertEquals(5, physicsChapters.size)
    assertEquals(5, chemistryChapters.size)
    assertEquals(5, mathChapters.size)

    assertTrue(physicsChapters.any { it.title == "Kinematics" })
    assertTrue(chemistryChapters.any { it.title == "Chemical Bonding" })
    assertTrue(mathChapters.any { it.title == "Quadratic Equations" })
  }

  @Test
  fun `verify quiz questions generated`() {
    val quizQuestions = StudyRepository.getQuizQuestions(10)
    assertEquals(10, quizQuestions.size)
    assertNotNull(quizQuestions[0].options)
    assertEquals(4, quizQuestions[0].options.size)
  }
}
