package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = PrimaryPurpleLight,
  onPrimary = Color.White,
  primaryContainer = PrimaryPurpleContainer,
  onPrimaryContainer = PrimaryPurpleLight,
  secondary = AccentBlue,
  onSecondary = Color(0xFF003549),
  secondaryContainer = Color(0xFF083344),
  onSecondaryContainer = AccentBlueLight,
  tertiary = PhysicsAmber,
  onTertiary = Color.Black,
  background = DarkBackground,
  onBackground = TextPrimaryDark,
  surface = DarkSurface,
  onSurface = TextPrimaryDark,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = TextSecondaryDark,
  outline = DarkBorder,
  outlineVariant = Color(0xFF1E2638)
)

private val LightColorScheme = lightColorScheme(
  primary = PrimaryPurple,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFEDE9FE),
  onPrimaryContainer = PrimaryPurpleDark,
  secondary = AccentBlueDark,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0F2FE),
  onSecondaryContainer = AccentBlueDark,
  tertiary = PhysicsAmber,
  onTertiary = Color.White,
  background = LightBackground,
  onBackground = TextPrimaryLight,
  surface = LightSurface,
  onSurface = TextPrimaryLight,
  surfaceVariant = LightSurfaceElevated,
  onSurfaceVariant = TextSecondaryLight,
  outline = LightBorder,
  outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun StudyVerseTheme(
  darkTheme: Boolean = true, // Default to true as per requirements
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
