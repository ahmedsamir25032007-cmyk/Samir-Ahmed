package com.example.data.repository

import com.example.data.model.Chapter
import com.example.data.model.FormulaCard
import com.example.data.model.NoteTopic
import com.example.data.model.Question
import com.example.data.model.QuizResult
import com.example.data.model.SubjectType
import com.example.data.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object StudyRepository {

  private val _userProgress = MutableStateFlow(UserProgress())
  val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()

  private val _quizHistory = MutableStateFlow<List<QuizResult>>(
    listOf(
      QuizResult(
        id = "q1",
        title = "JEE Main Mechanics Drill",
        subjectName = "Physics",
        totalQuestions = 10,
        correctAnswers = 8,
        incorrectAnswers = 2,
        unattempted = 0,
        scorePercentage = 80,
        timeSpentSeconds = 540,
        timestamp = "Today, 10:30 AM"
      ),
      QuizResult(
        id = "q2",
        title = "Thermodynamics & Kinetics Test",
        subjectName = "Chemistry",
        totalQuestions = 10,
        correctAnswers = 9,
        incorrectAnswers = 1,
        unattempted = 0,
        scorePercentage = 90,
        timeSpentSeconds = 480,
        timestamp = "Yesterday, 4:15 PM"
      ),
      QuizResult(
        id = "q3",
        title = "Trigonometry & Sequences Speed Quiz",
        subjectName = "Mathematics",
        totalQuestions = 10,
        correctAnswers = 7,
        incorrectAnswers = 3,
        unattempted = 0,
        scorePercentage = 70,
        timeSpentSeconds = 600,
        timestamp = "2 days ago"
      )
    )
  )
  val quizHistory: StateFlow<List<QuizResult>> = _quizHistory.asStateFlow()

  val chapters: List<Chapter> = listOf(
    // Physics
    Chapter(
      id = "phy_units",
      subjectType = SubjectType.PHYSICS,
      title = "Units & Dimensions",
      description = "Fundamental units, dimensional analysis, and error propagation in experimental measurements.",
      topicsCount = 4,
      questionsCount = 18,
      progress = 0.85f,
      isCompleted = true,
      order = 1
    ),
    Chapter(
      id = "phy_kinematics",
      subjectType = SubjectType.PHYSICS,
      title = "Kinematics",
      description = "Motion in 1D & 2D, projectile motion, relative velocity, and calculus in motion.",
      topicsCount = 5,
      questionsCount = 24,
      progress = 0.65f,
      isCompleted = false,
      order = 2
    ),
    Chapter(
      id = "phy_laws_motion",
      subjectType = SubjectType.PHYSICS,
      title = "Laws of Motion",
      description = "Newton's laws, friction, impulse, momentum conservation, and pseudo forces in non-inertial frames.",
      topicsCount = 5,
      questionsCount = 22,
      progress = 0.40f,
      isCompleted = false,
      order = 3
    ),
    Chapter(
      id = "phy_work_energy",
      subjectType = SubjectType.PHYSICS,
      title = "Work, Energy & Power",
      description = "Work-energy theorem, conservative forces, potential energy curves, power, and collisions.",
      topicsCount = 4,
      questionsCount = 20,
      progress = 0.20f,
      isCompleted = false,
      order = 4
    ),
    Chapter(
      id = "phy_circular",
      subjectType = SubjectType.PHYSICS,
      title = "Circular Motion",
      description = "Centripetal & centrifugal acceleration, banking of curves, and vertical circular motion.",
      topicsCount = 4,
      questionsCount = 16,
      progress = 0.0f,
      isCompleted = false,
      order = 5
    ),

    // Chemistry
    Chapter(
      id = "chem_basic_concepts",
      subjectType = SubjectType.CHEMISTRY,
      title = "Some Basic Concepts of Chemistry",
      description = "Mole concept, empirical formulas, molarity, molality, stoichiometry, and limiting reagents.",
      topicsCount = 4,
      questionsCount = 20,
      progress = 0.90f,
      isCompleted = true,
      order = 1
    ),
    Chapter(
      id = "chem_atomic",
      subjectType = SubjectType.CHEMISTRY,
      title = "Atomic Structure",
      description = "Bohr model, quantum numbers, photoelectric effect, de Broglie relation, and orbital shapes.",
      topicsCount = 5,
      questionsCount = 22,
      progress = 0.70f,
      isCompleted = false,
      order = 2
    ),
    Chapter(
      id = "chem_periodic",
      subjectType = SubjectType.CHEMISTRY,
      title = "Periodic Table",
      description = "Periodic law, ionization enthalpy, electron gain enthalpy, electronegativity trends, and shielding.",
      topicsCount = 4,
      questionsCount = 18,
      progress = 0.50f,
      isCompleted = false,
      order = 3
    ),
    Chapter(
      id = "chem_bonding",
      subjectType = SubjectType.CHEMISTRY,
      title = "Chemical Bonding",
      description = "VSEPR theory, hybridization, dipole moments, molecular orbital theory (MOT), and hydrogen bonding.",
      topicsCount = 6,
      questionsCount = 26,
      progress = 0.35f,
      isCompleted = false,
      order = 4
    ),
    Chapter(
      id = "chem_thermo",
      subjectType = SubjectType.CHEMISTRY,
      title = "Thermodynamics",
      description = "First & second laws, enthalpy of reaction, Hess's law, entropy, and Gibbs free energy spontaneity.",
      topicsCount = 5,
      questionsCount = 20,
      progress = 0.10f,
      isCompleted = false,
      order = 5
    ),

    // Mathematics
    Chapter(
      id = "math_sets",
      subjectType = SubjectType.MATHEMATICS,
      title = "Sets",
      description = "Set operations, Venn diagrams, Cartesian products, equivalence relations, and functions.",
      topicsCount = 3,
      questionsCount = 16,
      progress = 0.95f,
      isCompleted = true,
      order = 1
    ),
    Chapter(
      id = "math_quadratic",
      subjectType = SubjectType.MATHEMATICS,
      title = "Quadratic Equations",
      description = "Roots nature, quadratic graphs, location of roots, symmetric functions, and higher degree polynomials.",
      topicsCount = 4,
      questionsCount = 22,
      progress = 0.80f,
      isCompleted = true,
      order = 2
    ),
    Chapter(
      id = "math_sequences",
      subjectType = SubjectType.MATHEMATICS,
      title = "Sequence & Series",
      description = "AP, GP, HP, Arithmetico-Geometric Progression (AGP), telescoping sums, and infinite series.",
      topicsCount = 4,
      questionsCount = 20,
      progress = 0.55f,
      isCompleted = false,
      order = 3
    ),
    Chapter(
      id = "math_trig",
      subjectType = SubjectType.MATHEMATICS,
      title = "Trigonometry",
      description = "Compound angles, multiple & submultiple formulas, trigonometric equations, and heights & distances.",
      topicsCount = 6,
      questionsCount = 28,
      progress = 0.40f,
      isCompleted = false,
      order = 4
    ),
    Chapter(
      id = "math_straight_lines",
      subjectType = SubjectType.MATHEMATICS,
      title = "Straight Lines",
      description = "Slope forms, distance formula, angle between lines, pair of straight lines, and family of lines.",
      topicsCount = 4,
      questionsCount = 18,
      progress = 0.15f,
      isCompleted = false,
      order = 5
    )
  )

  private val _notesList = MutableStateFlow<List<NoteTopic>>(
    listOf(
      // Kinematics Notes
      NoteTopic(
        id = "note_kin_1",
        chapterId = "phy_kinematics",
        topicNumber = 1,
        title = "Rectilinear Motion & Calculus",
        contentOverview = "Kinematics in 1D deals with motion along a straight line. Differentiation of position gives velocity, and differentiation of velocity gives acceleration. Integration in reverse yields displacement.",
        formulas = listOf(
          FormulaCard(
            title = "Kinematic Equations (Constant 'a')",
            formula = "v = u + at\ns = ut + ½at²\nv² = u² + 2as",
            explanation = "Valid ONLY when acceleration is constant in magnitude and direction."
          ),
          FormulaCard(
            title = "Instantaneous Relations",
            formula = "v = ds/dt\na = dv/dt = v · (dv/ds)",
            explanation = "Fundamental differential relationships between displacement, velocity, and acceleration."
          )
        ),
        importantPoints = listOf(
          "Distance is always greater than or equal to magnitude of displacement.",
          "If acceleration is perpendicular to velocity, speed remains constant while direction changes.",
          "Area under a velocity-time graph represents displacement.",
          "Slope of a displacement-time graph gives instantaneous velocity."
        ),
        isBookmarked = true
      ),
      NoteTopic(
        id = "note_kin_2",
        chapterId = "phy_kinematics",
        topicNumber = 2,
        title = "Projectile Motion in 2D",
        contentOverview = "A projectile is an object upon which the only force acting is gravity. The horizontal component of motion is uniform, while the vertical component undergoes constant gravitational acceleration.",
        formulas = listOf(
          FormulaCard(
            title = "Time of Flight & Max Height",
            formula = "T = (2u · sinθ) / g\nH_max = (u² · sin²θ) / (2g)",
            explanation = "θ is the projection angle with the horizontal plane."
          ),
          FormulaCard(
            title = "Horizontal Range & Trajectory",
            formula = "R = (u² · sin 2θ) / g\ny = x · tanθ - (gx²)/(2u² cos²θ)",
            explanation = "Maximum range occurs at θ = 45°. Complementary angles θ and (90° - θ) have equal range."
          )
        ),
        importantPoints = listOf(
          "Velocity is minimum at the highest point and equals u·cosθ (horizontal component).",
          "Acceleration is g downwards throughout the trajectory.",
          "Trajectory equation is a parabola symmetric about x = R/2."
        ),
        isBookmarked = false
      ),
      NoteTopic(
        id = "note_kin_3",
        chapterId = "phy_kinematics",
        topicNumber = 3,
        title = "Relative Velocity & River Swimmer Problems",
        contentOverview = "Motion observed from a reference frame that itself may be moving. In vector notation, the relative velocity of A with respect to B is v_AB = v_A - v_B.",
        formulas = listOf(
          FormulaCard(
            title = "Relative Velocity Vector",
            formula = "v_AB = v_A - v_B\n|v_AB| = √(v_A² + v_B² - 2·v_A·v_B·cosθ)",
            explanation = "Direction is determined using the parallelogram law of vector subtraction."
          ),
          FormulaCard(
            title = "Shortest River Crossing Path",
            formula = "sin α = v_r / v_m\nt = d / √(v_m² - v_r²)",
            explanation = "Swimmer aims upstream at angle α to compensate for river drift."
          )
        ),
        importantPoints = listOf(
          "For shortest time to cross river, swimmer must head strictly perpendicular to the flow.",
          "In rain-man problems, the apparent direction of rain is v_rain - v_man."
        ),
        isBookmarked = true
      ),

      // Physics - Units & Dimensions
      NoteTopic(
        id = "note_units_1",
        chapterId = "phy_units",
        topicNumber = 1,
        title = "Dimensional Analysis & Quantities",
        contentOverview = "Physical quantities are expressed as powers of fundamental quantities [M], [L], [T], [I], [θ], [N], [J]. The principle of homogeneity ensures equations are dimensionally consistent.",
        formulas = listOf(
          FormulaCard(
            title = "Common JEE Dimensions",
            formula = "Planck's Constant (h): [M L² T⁻¹]\nUniversal Gravitational (G): [M⁻¹ L³ T⁻²]\nPermeability (μ₀): [M L T⁻² I⁻²]",
            explanation = "Very frequently tested in JEE Main matching questions."
          )
        ),
        importantPoints = listOf(
          "Arguments of trigonometric, logarithmic, and exponential functions are dimensionless.",
          "A dimensionally correct equation may not be physically valid, but a valid equation must be dimensionally consistent."
        ),
        isBookmarked = false
      ),

      // Chemistry - Chemical Bonding
      NoteTopic(
        id = "note_chem_bond_1",
        chapterId = "chem_bonding",
        topicNumber = 1,
        title = "Hybridization & VSEPR Geometry",
        contentOverview = "Valence Shell Electron Pair Repulsion theory predicts molecular shape based on minimization of electrostatic repulsion between electron pairs in the valence shell.",
        formulas = listOf(
          FormulaCard(
            title = "Steric Number Formula",
            formula = "Steric No = ½ [V + M - C + A]",
            explanation = "V = valence electrons of central atom, M = monovalent surrounding atoms, C = cationic charge, A = anionic charge."
          ),
          FormulaCard(
            title = "Repulsion Order",
            formula = "Lone Pair - Lone Pair > Lone Pair - Bond Pair > Bond Pair - Bond Pair",
            explanation = "Governs deviation from ideal bond angles (e.g. NH₃ is 107° and H₂O is 104.5°)."
          )
        ),
        importantPoints = listOf(
          "Steric number 4 corresponds to sp³ hybridization (tetrahedral geometry).",
          "XeF₄ has steric number 6 (4 bonds + 2 lone pairs) with square planar shape.",
          "d-orbitals involved: in dsp² it is d_x²-y², in sp³d it is d_z²."
        ),
        isBookmarked = true
      ),
      NoteTopic(
        id = "note_chem_thermo_1",
        chapterId = "chem_thermo",
        topicNumber = 1,
        title = "First Law & Gibbs Energy Spontaneity",
        contentOverview = "Thermodynamics governs heat, work, and spontaneity of chemical reactions. ΔG = ΔH - TΔS dictates whether a transformation is thermodynamically favorable.",
        formulas = listOf(
          FormulaCard(
            title = "Gibbs Helmholtz Equation",
            formula = "ΔG = ΔH - T·ΔS\nΔG° = -2.303 · R · T · log₁₀(K_eq)",
            explanation = "Spontaneous if ΔG < 0, at equilibrium when ΔG = 0."
          ),
          FormulaCard(
            title = "First Law & Work Done",
            formula = "ΔU = q + w\nw_rev = -2.303 · n·R·T · log₁₀(V₂/V₁)",
            explanation = "For isothermal reversible expansion of an ideal gas."
          )
        ),
        importantPoints = listOf(
          "For an isolated system, entropy always increases (Second Law).",
          "Extensive properties depend on mass (Enthalpy, Volume); Intensive properties do not (Pressure, Temperature, Density)."
        ),
        isBookmarked = false
      ),

      // Math - Quadratic Equations
      NoteTopic(
        id = "note_math_quad_1",
        chapterId = "math_quadratic",
        topicNumber = 1,
        title = "Nature & Location of Roots",
        contentOverview = "For ax² + bx + c = 0, discriminant D = b² - 4ac dictates the nature of the roots. Location of roots establishes conditions for roots lying in specified intervals.",
        formulas = listOf(
          FormulaCard(
            title = "Vieta's Formulas & Discriminant",
            formula = "α + β = -b/a\nα·β = c/a\nD = b² - 4ac",
            explanation = "D > 0: real & distinct; D = 0: real & equal; D < 0: complex conjugate pair."
          ),
          FormulaCard(
            title = "Both Roots Greater Than 'k'",
            formula = "1) D ≥ 0\n2) -b / (2a) > k\n3) a · f(k) > 0",
            explanation = "Critical 3 conditions required for both roots to lie to the right of point k."
          )
        ),
        importantPoints = listOf(
          "If a and c have opposite signs, D is strictly positive, hence roots are always real.",
          "The vertex of the parabola is located at (-b/2a, -D/4a).",
          "If a + b + c = 0, then 1 is always a root and c/a is the other root."
        ),
        isBookmarked = true
      ),

      // Math - Trigonometry
      NoteTopic(
        id = "note_math_trig_1",
        chapterId = "math_trig",
        topicNumber = 1,
        title = "Essential Compound & Product Formulas",
        contentOverview = "Trigonometric identities form the bedrock of JEE calculus and geometry. Mastery of compound angle expansions and product-to-sum transformations is vital.",
        formulas = listOf(
          FormulaCard(
            title = "Compound Angles",
            formula = "sin(A ± B) = sin A cos B ± cos A sin B\ncos(A ± B) = cos A cos B ∓ sin A sin B\ntan(A ± B) = (tan A ± tan B) / (1 ∓ tan A tan B)",
            explanation = "Essential algebraic transformations for JEE Main."
          ),
          FormulaCard(
            title = "Transformation Formulas",
            formula = "2 sin A cos B = sin(A+B) + sin(A-B)\n2 cos A cos B = cos(A+B) + cos(A-B)\ncos C - cos D = -2 sin((C+D)/2) sin((C-D)/2)",
            explanation = "Used extensively in integration and series summation."
          )
        ),
        importantPoints = listOf(
          "Maximum value of a·sinθ + b·cosθ is √(a² + b²), minimum is -√(a² + b²).",
          "tan(A) + tan(B) + tan(C) = tan(A)·tan(B)·tan(C) whenever A + B + C = π."
        ),
        isBookmarked = false
      )
    )
  )
  val notesList: StateFlow<List<NoteTopic>> = _notesList.asStateFlow()

  val practiceQuestions: List<Question> = listOf(
    // Physics Questions
    Question(
      id = "q_phy_1",
      chapterId = "phy_kinematics",
      subjectType = SubjectType.PHYSICS,
      difficulty = "JEE Main",
      questionText = "A particle starts from rest with uniform acceleration a. If the distance covered in the first 10 seconds is s₁ and the distance covered in the first 20 seconds is s₂, what is the relationship between s₁ and s₂?",
      options = listOf(
        "s₂ = 2 s₁",
        "s₂ = 3 s₁",
        "s₂ = 4 s₁",
        "s₂ = 5 s₁"
      ),
      correctAnswerIndex = 2,
      explanation = "Using s = ½ a t²:\ns₁ = ½ a (10)² = 50 a\ns₂ = ½ a (20)² = 200 a\nDividing s₂ by s₁: s₂ / s₁ = 200 / 50 = 4. Hence, s₂ = 4 s₁.",
      hint = "Use the equation of motion for zero initial velocity."
    ),
    Question(
      id = "q_phy_2",
      chapterId = "phy_kinematics",
      subjectType = SubjectType.PHYSICS,
      difficulty = "JEE Advanced",
      questionText = "A projectile is thrown with speed u at angle θ with horizontal. The radius of curvature of its trajectory at the highest point is:",
      options = listOf(
        "u² / g",
        "(u² cos²θ) / g",
        "(u² sin²θ) / g",
        "(u² cos θ) / g"
      ),
      correctAnswerIndex = 1,
      explanation = "At the highest point, the velocity is purely horizontal: v = u cos θ. The normal acceleration perpendicular to velocity is g. Radius of curvature R = v² / a_normal = (u cos θ)² / g = (u² cos²θ) / g.",
      hint = "At apex, normal acceleration is entirely gravity g."
    ),
    Question(
      id = "q_phy_3",
      chapterId = "phy_units",
      subjectType = SubjectType.PHYSICS,
      difficulty = "JEE Main",
      questionText = "Which pair of physical quantities have the same dimensional formula?",
      options = listOf(
        "Work and Torque",
        "Force and Power",
        "Stress and Strain",
        "Angular Momentum and Linear Momentum"
      ),
      correctAnswerIndex = 0,
      explanation = "Work = Force × Displacement = [M L T⁻²][L] = [M L² T⁻²].\nTorque = Force × perpendicular distance = [M L T⁻²][L] = [M L² T⁻²].\nBoth share the dimension [M L² T⁻²].",
      hint = "Both represent energy-like quantities in terms of product of force and distance."
    ),
    Question(
      id = "q_phy_4",
      chapterId = "phy_laws_motion",
      subjectType = SubjectType.PHYSICS,
      difficulty = "JEE Main",
      questionText = "A body of mass 5 kg is suspended by a light spring balance inside a lift. What is the reading of the balance when the lift ascends with an acceleration of 2 m/s²? (g = 10 m/s²)",
      options = listOf(
        "40 N",
        "50 N",
        "60 N",
        "70 N"
      ),
      correctAnswerIndex = 2,
      explanation = "Apparent weight in an upward accelerating lift is N = m(g + a).\nN = 5 kg × (10 + 2) m/s² = 5 × 12 = 60 N.",
      hint = "In upward acceleration, pseudo force adds to the gravitational force."
    ),

    // Chemistry Questions
    Question(
      id = "q_chem_1",
      chapterId = "chem_bonding",
      subjectType = SubjectType.CHEMISTRY,
      difficulty = "JEE Main",
      questionText = "According to Molecular Orbital Theory (MOT), which of the following species has the highest bond order?",
      options = listOf(
        "O₂",
        "O₂⁺",
        "O₂⁻",
        "O₂²⁻"
      ),
      correctAnswerIndex = 1,
      explanation = "Electron count:\nO₂: 16 e⁻ → Bond order = 2.0\nO₂⁺: 15 e⁻ → Bond order = 2.5 (highest)\nO₂⁻: 17 e⁻ → Bond order = 1.5\nO₂²⁻: 18 e⁻ → Bond order = 1.0",
      hint = "Removing an electron from an antibonding π* orbital increases the bond order."
    ),
    Question(
      id = "q_chem_2",
      chapterId = "chem_basic_concepts",
      subjectType = SubjectType.CHEMISTRY,
      difficulty = "JEE Main",
      questionText = "What is the number of moles of water molecules present in 180 g of pure water?",
      options = listOf(
        "1 mole",
        "5 moles",
        "10 moles",
        "18 moles"
      ),
      correctAnswerIndex = 2,
      explanation = "Molar mass of H₂O = 2(1) + 16 = 18 g/mol.\nNumber of moles = Mass / Molar mass = 180 g / 18 g/mol = 10 moles.",
      hint = "Calculate molar mass of H₂O first."
    ),
    Question(
      id = "q_chem_3",
      chapterId = "chem_atomic",
      subjectType = SubjectType.CHEMISTRY,
      difficulty = "JEE Advanced",
      questionText = "The wavelength associated with an electron moving with a velocity of 10⁶ m/s is approximately: (h = 6.63 × 10⁻³⁴ J·s, m_e = 9.1 × 10⁻³¹ kg)",
      options = listOf(
        "0.728 nm",
        "0.125 nm",
        "1.54 nm",
        "0.053 nm"
      ),
      correctAnswerIndex = 0,
      explanation = "de Broglie wavelength λ = h / (m · v)\nλ = (6.63 × 10⁻³⁴) / (9.1 × 10⁻³¹ × 10⁶) ≈ 0.728 × 10⁻⁹ m = 0.728 nm.",
      hint = "Apply de Broglie relation λ = h / p."
    ),

    // Mathematics Questions
    Question(
      id = "q_math_1",
      chapterId = "math_quadratic",
      subjectType = SubjectType.MATHEMATICS,
      difficulty = "JEE Main",
      questionText = "If the roots of the quadratic equation x² - 8x + k = 0 differ by 2, what is the value of k?",
      options = listOf(
        "12",
        "15",
        "16",
        "18"
      ),
      correctAnswerIndex = 1,
      explanation = "Let the roots be α and β. Then α + β = 8 and α · β = k.\nGiven |α - β| = 2.\n(α - β)² = (α + β)² - 4αβ\n4 = 8² - 4k\n4k = 64 - 4 = 60 ⇒ k = 15.",
      hint = "Express (α - β)² in terms of sum and product of roots."
    ),
    Question(
      id = "q_math_2",
      chapterId = "math_sequences",
      subjectType = SubjectType.MATHEMATICS,
      difficulty = "JEE Main",
      questionText = "The sum of the infinite geometric series 1 + 1/3 + 1/9 + 1/27 + ... is:",
      options = listOf(
        "1.5",
        "2.0",
        "1.33",
        "3.0"
      ),
      correctAnswerIndex = 0,
      explanation = "First term a = 1, common ratio r = 1/3.\nSince |r| < 1, sum of infinite GP is S_∞ = a / (1 - r) = 1 / (1 - 1/3) = 1 / (2/3) = 3/2 = 1.5.",
      hint = "Formula for infinite GP is a / (1 - r)."
    ),
    Question(
      id = "q_math_3",
      chapterId = "math_trig",
      subjectType = SubjectType.MATHEMATICS,
      difficulty = "JEE Advanced",
      questionText = "The minimum and maximum values of the expression 3 sin x + 4 cos x are respectively:",
      options = listOf(
        "-7 and +7",
        "-5 and +5",
        "-1 and +1",
        "0 and 5"
      ),
      correctAnswerIndex = 1,
      explanation = "The range of expression a sin x + b cos x is [-√(a² + b²), +√(a² + b²)].\nHere a = 3, b = 4.\n√(3² + 4²) = √(9 + 16) = √25 = 5.\nTherefore, minimum is -5 and maximum is +5.",
      hint = "Convert to single harmonic form R·cos(x - α)."
    )
  )

  fun getChapters(subjectType: SubjectType? = null): List<Chapter> {
    return if (subjectType == null) chapters else chapters.filter { it.subjectType == subjectType }
  }

  fun getChapterById(id: String): Chapter? {
    return chapters.find { it.id == id }
  }

  fun getNotesForChapter(chapterId: String): List<NoteTopic> {
    return _notesList.value.filter { it.chapterId == chapterId }
  }

  fun getAllNotes(): List<NoteTopic> = _notesList.value

  fun getBookmarkedNotes(): List<NoteTopic> {
    return _notesList.value.filter { it.isBookmarked }
  }

  fun toggleBookmark(topicId: String) {
    _notesList.update { list ->
      list.map { topic ->
        if (topic.id == topicId) {
          topic.copy(isBookmarked = !topic.isBookmarked)
        } else {
          topic
        }
      }
    }
  }

  fun getPracticeQuestions(subjectType: SubjectType? = null, chapterId: String? = null): List<Question> {
    return practiceQuestions.filter { q ->
      (subjectType == null || q.subjectType == subjectType) &&
      (chapterId == null || q.chapterId == chapterId)
    }
  }

  fun getQuizQuestions(count: Int = 10): List<Question> {
    // Generate a set of 10 questions (repeating or cycling through the curated bank)
    val pool = practiceQuestions
    val result = mutableListOf<Question>()
    var index = 0
    while (result.size < count) {
      val baseQ = pool[index % pool.size]
      result.add(
        baseQ.copy(
          id = "quiz_${result.size + 1}_${baseQ.id}",
          questionText = "Q${result.size + 1}: ${baseQ.questionText}"
        )
      )
      index++
    }
    return result
  }

  fun recordQuizResult(result: QuizResult) {
    _quizHistory.update { listOf(result) + it }
    _userProgress.update { curr ->
      val newSolvedToday = curr.questionsSolvedToday + result.totalQuestions
      val newTotalSolved = curr.totalQuestionsSolved + result.totalQuestions
      curr.copy(
        questionsSolvedToday = newSolvedToday,
        totalQuestionsSolved = newTotalSolved
      )
    }
  }

  fun updateTodayProgress(additionalQuestions: Int, additionalMinutes: Int) {
    _userProgress.update { curr ->
      curr.copy(
        questionsSolvedToday = curr.questionsSolvedToday + additionalQuestions,
        studyTimeMinutesToday = curr.studyTimeMinutesToday + additionalMinutes,
        totalQuestionsSolved = curr.totalQuestionsSolved + additionalQuestions
      )
    }
  }

  fun toggleTheme(isDark: Boolean) {
    _userProgress.update { it.copy(isDarkMode = isDark) }
  }

  fun setLastStudied(subjectType: SubjectType, chapterId: String) {
    _userProgress.update {
      it.copy(
        lastStudiedSubject = subjectType,
        lastStudiedChapterId = chapterId
      )
    }
  }
}
