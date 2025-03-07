package com.anirudh.chapterai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anirudh.chapterai.ui.theme.ChapterAITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChapterAITheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.systemBarsPadding()
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "subject_selection"
                    ) {
                        composable("subject_selection") { InitialScreen(paddingValues, navController) }
                        composable(
                            "chapter_selection/{subject}/{standard}",
                            arguments = listOf(
                                navArgument("subject") { type = NavType.StringType },
                                navArgument("standard") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            ChapterSelectionScreen(
                                paddingValues = paddingValues,
                                navController = navController,
                                subject = backStackEntry.arguments?.getString("subject") ?: "",
                                standard = backStackEntry.arguments?.getString("standard") ?: ""
                            )
                        }
                        composable(
                            "ai_explanation/{subject}/{standard}/{chapter}",
                            arguments = listOf(
                                navArgument("subject") { type = NavType.StringType },
                                navArgument("standard") { type = NavType.StringType },
                                navArgument("chapter") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            AIExplanationScreen(
                                paddingValues = paddingValues,
                                navController = navController,
                                subject = backStackEntry.arguments?.getString("subject") ?: "",
                                standard = backStackEntry.arguments?.getString("standard") ?: "",
                                chapter = backStackEntry.arguments?.getString("chapter") ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

// Data class to hold subject information
data class Subject(val name: String, val iconRes: Int, val chapters: List<String>)

// List of subjects with chapters (CBSE Std 9)
val subjects = listOf(
    Subject("Maths", R.drawable.maths, listOf(
        "Number Systems", "Polynomials", "Coordinate Geometry", "Linear Equations in Two Variables",
        "Introduction to Euclid’s Geometry", "Lines and Angles", "Triangles", "Quadrilaterals",
        "Areas of Parallelograms and Triangles", "Circles", "Constructions", "Heron’s Formula",
        "Surface Areas and Volumes", "Statistics", "Probability"
    )),
    Subject("Science", R.drawable.science, listOf(
        "Matter in Our Surroundings", "Is Matter Around Us Pure", "Atoms and Molecules", "Structure of the Atom",
        "The Fundamental Unit of Life", "Tissues", "Diversity in Living Organisms", "Motion",
        "Force and Laws of Motion", "Gravitation", "Work and Energy", "Sound", "Why Do We Fall Ill"
    )),
    Subject("English", R.drawable.eng, listOf(
        "The Fun They Had", "The Sound of Music", "The Little Girl", "The Lost Child", "The Adventures of Toto",
        "Grammar: Tenses", "Grammar: Modals", "Grammar: Subject-Verb Agreement", "Letter Writing", "Story Writing"
    )),
    Subject("Social Sc.", R.drawable.socialscience, listOf(
        "The French Revolution", "Socialism in Europe", "India - Size and Location", "Physical Features of India",
        "What is Democracy?", "Constitutional Design", "The Story of Village Palampur", "Nazism and the Rise of Hitler",
        "Drainage", "Climate", "Electoral Politics", "Working of Institutions", "People as Resource", "Poverty as a Challenge"
    )),
    Subject("Hindi", R.drawable.hindi, listOf(
        "Dukh Ka Adhikar", "Everest Meri Shikhar Yatra", "Is Jal Pralay Mein", "Mere Sang Ki Auratein",
        "Sangya", "Visheshan", "Kriya", "Vachya", "Patra Lekhan", "Nibandh Lekhan"
    )),
    Subject("Sanskrit", R.drawable.sanskrit, listOf(
        "Bharati Vasanti Vasati", "Swarnakakaha", "Sandhi", "Karak", "Vibhakti", "Shabd Roop",
        "Dhatu Roop", "Anuvad", "Patra Lekhan", "Chitra Varnan"
    )),
    Subject("Computer", R.drawable.computer, listOf(
        "Basics of Information Technology", "Cyber Safety", "Word Processing", "Spreadsheets",
        "Presentations", "Introduction to Programming", "Scratch Programming", "Python Basics"
    )),
    Subject("Painting", R.drawable.painting, listOf(
        "Elements of Art", "Principles of Design", "Indian Traditional Painting", "Modern Indian Art",
        "Sketching Techniques", "Color Theory"
    ))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(paddingValues: PaddingValues, navController: NavController) {
    var selectedStandard by remember { mutableStateOf("Std 9") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        TopAppBar(
            title = { Text("ChapterAI", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { /* Add back action later */ }
                )
            },
            actions = {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Row(
                        modifier = Modifier.clickable { expanded = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = selectedStandard, fontSize = 16.sp)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        for (std in 1..12) {
                            DropdownMenuItem(
                                text = { Text("Std $std") },
                                onClick = {
                                    selectedStandard = "Std $std"
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        )

        Text(
            text = "Select subject",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subjects) { subject ->
                SubjectButton(subject) {
                    navController.navigate("chapter_selection/${subject.name}/${selectedStandard}")
                }
            }
        }
    }
}

@Composable
fun SubjectButton(subject: Subject, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = subject.iconRes),
                contentDescription = subject.name,
                modifier = Modifier.size(48.dp)
            )
            Text(text = subject.name, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterSelectionScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    subject: String,
    standard: String
) {
    val selectedSubject = subjects.find { it.name == subject } ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        TopAppBar(
            title = { Text(text = subject, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { navController.navigateUp() }
                )
            },
            actions = { Text(text = standard, fontSize = 16.sp, modifier = Modifier.padding(end = 16.dp)) }
        )

        Text(
            text = "Select chapter",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(selectedSubject.chapters) { chapter ->
                ChapterButton(chapter) {
                    navController.navigate("ai_explanation/$subject/$standard/$chapter")
                }
            }
        }
    }
}

@Composable
fun ChapterButton(chapter: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = chapter, fontSize = 16.sp, modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIExplanationScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    subject: String,
    standard: String,
    chapter: String
) {
    var userInput by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = "Subject: $subject, Standard: $standard, Chapter: $chapter"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
//            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("ChapterAI", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { navController.navigateUp() }
                )
            }
        )

        Text(
            text = "Hello Anirudh",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF800080),
            modifier = Modifier.padding(top = 16.dp, start = 16.dp,end = 16.dp)
        )

        Text(
            text = "Enter your doubt ",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp,end = 16.dp)
        )

        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Enter your question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp,end = 16.dp)
        )

        Button(
            onClick = {
                if (userInput.isNotEmpty()) {
                    isLoading = true
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            aiResponse = GeminiApiService.sendGeminiRequest(userInput, context)
                        } catch (e: Exception) {
                            aiResponse = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    aiResponse = "Please enter a question!"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp,end = 16.dp)
        ) {
            Text("Ask AI")
        }

        if(isLoading){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color(0xFF800080)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp,end = 16.dp)
                .weight(1f, fill = false) // Allow scrolling with remaining space
        ) {
            item {
                Text(
                    text = aiResponse,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
            }
        }

        Text(
            text = aiResponse,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp,end = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun SubjectSelectionPreview() {
    ChapterAITheme {
        InitialScreen(PaddingValues(0.dp), rememberNavController())
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ChapterSelectionPreview() {
//    ChapterAITheme {
//        ChapterSelectionScreen(
//            paddingValues = PaddingValues(0.dp),
//            navController = rememberNavController(),
//            subject = "Maths",
//            standard = "Std 9"
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AIExplanationPreview() {
//    ChapterAITheme {
//        AIExplanationScreen(
//            paddingValues = PaddingValues(0.dp),
//            navController = rememberNavController(),
//            subject = "Maths",
//            standard = "Std 9",
//            chapter = "Polynomials"
//        )
//    }
//}