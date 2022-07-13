package com.example.todoapplication

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication
.ui.theme.ToDoApplicationTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GetScaffold()
                }
            }
        }
    }
}
@Composable
fun GetScaffold(){
    val scaffoldState: ScaffoldState = rememberScaffoldState(
        snackbarHostState = SnackbarHostState()
    )
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Compose - ToDo Application",color= Color.White)},
                backgroundColor = Color(0xFFFDDB33),
            )
        },
        content = {MainContent(scaffoldState)},
        backgroundColor = Color(0xFFBEDDF5),
    )
}


@Composable
fun MainContent(scaffoldState: ScaffoldState){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val model : TodoViewModel = viewModel(
        factory = ToDoViewmodelFactory(
            context.applicationContext as Application
        )
    )
    val list:List<ToDo> = model.todoList.observeAsState(listOf()).value
    var textState = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        //contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp,),

                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFFFFFFF),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                ),
                value =textState.value, onValueChange ={textState.value = it}
                ,placeholder = {
                    Text(text = "Write what do you want to do")
                },)

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Button(

                    onClick = {
                        model.insert(
                            ToDo(
                                null,
                                UUID.randomUUID().toString(),
                                textState.value
                            )
                        )
                        scope.launch{
                            textState.value= ""
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Todos added",
                            )
                        }
                    }) {
                    Text(text = "Add your todos")
                }
                Button(onClick = {
                    model.clear()
                    scope.launch{
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "All todos deleted",
                        )
                    }

                }) {
                    Text(text = "Clear")
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(list.size) { index ->
                    Card(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${list[index].id}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 12.dp)
                            )

                            Text(
                                text = " : " + list[index].fullName.take(10),
                            )

                            Text(
                                text = " : " + list[index].notes,
                                style = TextStyle(
                                    color = if (list[index].id!! >= 33)
                                        Color(0xFF3B7A57)
                                    else Color(0xFFAB274F)),
                                modifier = Modifier.weight(2F)
                            )

                            IconButton(onClick = {
                                list[index].notes = textState.value
                                model.update(list[index])
                                scope.launch{
                                    scaffoldState.snackbarHostState
                                        .showSnackbar(
                                            "Notes updated id" +
                                                    " : ${list[index].id}",
                                        )
                                    textState.value= ""
                                }
                            }) {
                                Icon(Icons.Filled.Edit,"",tint = Color.Magenta)
                            }

                            IconButton(onClick = {
                                model.delete(list[index])
                                scope.launch{
                                    scaffoldState.snackbarHostState
                                        .showSnackbar(
                                            "Notes deleted id" +
                                                    " : ${list[index].id}",
                                        )
                                    textState.value= ""
                                }

                            }) {
                                Icon(Icons.Filled.Delete,"",tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}