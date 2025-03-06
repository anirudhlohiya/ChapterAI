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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.material3.ExperimentalMaterial3Api
import com.anirudh.chapterai.ui.theme.ChapterAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChapterAITheme {
                Scaffold(
                    modifier = Modifier.systemBarsPadding()
                ) { paddingValues ->
                    InitialScreen(paddingValues)
                }
            }
        }
    }
}


data class Subject(val name: String, val iconRes: Int)


val subjects = listOf(
    Subject("Maths", R.drawable.maths),
    Subject("Science", R.drawable.science),
    Subject("English", R.drawable.eng),
    Subject("Social Sc.", R.drawable.socialscience),
    Subject("Hindi", R.drawable.hindi),
    Subject("Sanskrit", R.drawable.sanskrit),
    Subject("Computer", R.drawable.computer),
    Subject("Painting", R.drawable.painting),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "ChapterAI",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Back",
//                    modifier = Modifier.clickable { /* Add back action later */ }
//                )
            },
            actions = {
                var expanded by remember { mutableStateOf(false) }
                var selectedStandard by remember { mutableStateOf("Std 9") }
                Box {
                    Row(
                        modifier = Modifier.clickable { expanded = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = selectedStandard, fontSize = 16.sp)
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
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
                SubjectButton(subject)
            }
        }
    }
}

@Composable
fun SubjectButton(subject: Subject) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Add subject action later */ },
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
                alignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = subject.name,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChapterAITheme {
        InitialScreen(PaddingValues(0.dp))
    }
}