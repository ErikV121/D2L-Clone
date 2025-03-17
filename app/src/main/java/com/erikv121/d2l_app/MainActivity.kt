package com.erikv121.d2l_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erikv121.d2l_app.ui.theme.D2L_appTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            D2L_appTheme {
                D2LApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun D2LApp() {
    var selectedScreen by remember { mutableStateOf("Home") }
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text(
                    "D2L Navigation",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider()

                // Home option
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = selectedScreen == "Home",
                    onClick = {
                        selectedScreen = "Home"
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                // Start Here option
                NavigationDrawerItem(
                    label = { Text("Start Here") },
                    selected = selectedScreen == "StartHere",
                    onClick = {
                        selectedScreen = "StartHere"
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                // Learning Modules option
                NavigationDrawerItem(
                    label = { Text("Learning Modules") },
                    selected = selectedScreen == "LearningModules",
                    onClick = {
                        selectedScreen = "LearningModules"
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = when(selectedScreen) {
                                    "Home" -> "D2L Dashboard"
                                    "StartHere" -> "Start Here"
                                    "LearningModules" -> "Learning Modules"
                                    else -> "D2L Dashboard"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = Color.White
                        ),
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                when (selectedScreen) {
                    "Home" -> HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        onViewAllStartHereClick = { selectedScreen = "StartHere" },
                        onViewAllLearningModulesClick = { selectedScreen = "LearningModules" }
                    )
                    "StartHere" -> StartHereScreen(modifier = Modifier.padding(innerPadding))
                    "LearningModules" -> LearningModulesScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onViewAllStartHereClick: () -> Unit,
    onViewAllLearningModulesClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard()
        }

        item {
            SectionTitle("Start Here")
        }

        items(startHereItems.take(2)) { item ->
            StartHereItemCard(item)
        }

        item {
            TextButton(
                onClick = onViewAllStartHereClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Start Here Items")
            }
        }

        item {
            SectionTitle("Learning Modules")
        }

        items(learningModules.take(2)) { module ->
            LearningModuleCard(module)

            // Show each module's submodules in preview
            module.submodules?.forEach { submodule ->
                SubmoduleCard(submodule)
            }
        }

        item {
            TextButton(
                onClick = onViewAllLearningModulesClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Learning Modules")
            }
        }
    }
}

@Composable
fun StartHereScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Start Here",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                "Welcome to your course!",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        items(startHereItems) { item ->
            StartHereItemCard(item)
        }
    }
}

@Composable
fun LearningModulesScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Learning Modules",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                "Access your course content organized by modules.",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        items(allLearningModules) { module ->
            LearningModuleCard(module)

            // Show submodules for each module
            module.submodules?.forEach { submodule ->
                SubmoduleCard(submodule)
            }
        }
    }
}

@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Welcome to Your Course",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

data class StartHereItem(
    val title: String,
    val description: String,
    val completed: Boolean = false
)

data class LearningModule(
    val title: String,
    val description: String,
    val dueDate: String? = null,
    val progress: Float = 0f,
    val submodules: List<Submodule>? = null
)

data class Submodule(
    val title: String,
    val type: String // e.g., "Reading", "Video", "Quiz"
)

val startHereItems = listOf(
    StartHereItem(
        "Welcome",
        "Review the course syllabus"
    ),
    StartHereItem(
        "Syllabus",
        "Review the course syllabus"
    ),
    StartHereItem(
        "Lab Report Instruction",
        "Review the course lab report instructions"
    ),
    StartHereItem(
        "Project Instruction",
        "Review the course project instructions"
    )
)

// First 2 learning modules for the home page preview
val learningModules = listOf(
    LearningModule(
        title = "Lesson 1",
        description = " ",
        submodules = listOf(
            Submodule("Slides","Reading"),
            Submodule("Lab","Reading")
        )
    ),
    LearningModule(
        title = "Lesson 2",
        description = " ",
        submodules = listOf(
            Submodule("Slides","Reading"),
            Submodule("Lab","Reading")
        )
    )
)

// Full list of 13 learning modules
val allLearningModules = List(13) { index ->
    LearningModule(
        title = "Lesson ${index + 1}",
        description = " ",
        progress = when {
            index < 2 -> 1.0f
            index < 4 -> 0.5f
            else -> 0.0f
        },
        submodules = listOf(
            Submodule("Slides","Reading"),
            Submodule("Lab","Reading")
        )
    )
}

@Composable
fun StartHereItemCard(item: StartHereItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon removed

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    item.description,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LearningModuleCard(module: LearningModule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Handle module click */ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    module.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                module.dueDate?.let {
                    Text(
                        "Due: $it",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                module.description,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { module.progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    "${(module.progress * 100).toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SubmoduleCard(submodule: Submodule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
            .clickable { /* Handle submodule click */ },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon removed

            Text(
                text = submodule.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun D2LAppPreview() {
    D2L_appTheme {
        D2LApp()
    }
}