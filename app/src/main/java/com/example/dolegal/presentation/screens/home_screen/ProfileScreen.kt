package com.example.dolegal.presentation.screens.home_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.dolegal.R
import com.example.dolegal.presentation.models.Achievement
import com.example.dolegal.presentation.state.CurrentUserState
import com.example.dolegal.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: HomeViewModel,
    onLogoutClick: () -> Unit,
    currentUserState: CurrentUserState,
    modifier: Modifier
) {



    var showLogoutDialogBox by remember {
        mutableStateOf(false)
    }





    Scaffold {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF4C7BF4)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            if (showLogoutDialogBox) {
                LogoutDialogBox(
                    onDismissRequest = { showLogoutDialogBox = false },
                    onLogoutClick = {
                        viewModel.logout()
                        onLogoutClick()
                    },
                    modifier
                )
            }

            Row(
                modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                AsyncImage(
                    modifier = modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_placeholder),
                    model = if(currentUserState.user?.profile_pic == "") "https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-Image.png" else currentUserState.user?.profile_pic,
                    contentDescription = "profile_pic"
                )

                Column(
                    modifier = modifier.padding(start = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = if (currentUserState.user != null) currentUserState.user.name else "Loading...")
                    Text(text = if (currentUserState.user != null) currentUserState.user.score else "Loading...")
                }

                Image(
                    modifier = modifier.clickable {
                        showLogoutDialogBox = true
                    }.size(30.dp),
                    painter = painterResource(R.drawable.logout_ic),
                    contentDescription = "logout"
                )
            }
            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            var showBottomSheet by remember { mutableStateOf(false) }


            if (showBottomSheet) {
                BottomSheet(
                    scope,
                    viewModel,
                    sheetState,
                    onDismiss = { showBottomSheet = false },
                    userState = currentUserState,
                    modifier = modifier
                )
            }
            Box(
                modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color.White),
            ) {
                Column(
                    modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    MyAchivements(modifier)
                    Button(onClick = { showBottomSheet = true }) {
                        Text(text = "Edit profile")
                    }

                }
            }

        }

    }


}

@Composable
fun SaveDialogBox(onDismissRequest: () -> Unit, updateProfile: () -> Unit,modifier: Modifier) {
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = modifier
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(
                modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Are you sure to update your profile!!!")
                Button(onClick = { updateProfile();onDismissRequest() }) {
                    Text(text = "Yes")
                }
            }

        }

    }
}


@Composable
fun LogoutDialogBox(onDismissRequest: () -> Unit, onLogoutClick: () -> Unit,modifier: Modifier) {
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = modifier
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(
                modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Are you sure to Logout")
                Row {
                    Button(modifier = modifier.padding(8.dp), onClick = { onDismissRequest() }) {
                        Text(text = "No")
                    }
                    Button(modifier = modifier.padding(8.dp),onClick = { onLogoutClick() }) {
                        Text(text = "Yes")
                    }
                }

            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    scope: CoroutineScope,
    viewModel: HomeViewModel,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    userState: CurrentUserState,
    modifier: Modifier
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(34.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var name by remember {
                mutableStateOf("Vandan")
            }

            var email by remember {
                mutableStateOf("vandan@gmail.com")
            }

            var imageUri by remember {
                mutableStateOf<Uri?>(null)
            }

            val photoPicker =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                    imageUri = it
                }


            AsyncImage(
                modifier = modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_placeholder),
                model = if (imageUri != null) imageUri else if (userState.user?.profile_pic == "") "https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-Image.png" else userState.user?.profile_pic,
                contentDescription = "profile_pic"
            )



            LaunchedEffect(Unit) {
                name = userState.user?.name.toString()
                email = userState.user?.email.toString()
            }



            var showSaveDialogBox by remember {
                mutableStateOf(false)
            }
            if (showSaveDialogBox) {
                SaveDialogBox(
                    onDismissRequest = { showSaveDialogBox = false; onDismiss() },
                    updateProfile = {
                        viewModel.updateProfile(name, email,imageUri)
                    },modifier
                )
            }

            OutlinedTextField(
                modifier = modifier.padding(16.dp),
                value = name,
                onValueChange = { name = it },
                placeholder = { Text(text = "Enter Your Name") })
            OutlinedTextField(
                modifier = modifier.padding(16.dp),
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Enter Your Email ID") })
            Button(modifier = modifier
                .align(Alignment.Start)
                .padding(start = 34.dp), onClick = { /*TODO*/ }) {
                Text(text = "Verify Email")
            }
            Spacer(modifier = modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(34.dp)
            ) {
                Button(onClick = { onDismiss() }) {
                    Text(text = "Cancel")
                }
                Button(onClick = { showSaveDialogBox = true }) {
                    Text(text = "Save")
                }
            }


        }

    }
}

@Composable
fun MyAchivements(modifier: Modifier) {
    Text(text = "Your Achievements", modifier = modifier.padding(bottom = 16.dp))
    val achivements = listOf(
        Achievement(
            "star",
            "https://th.bing.com/th/id/OIP.rduRXCOk_28xqTE43zyNcgHaHC?w=179&h=180&c=7&r=0&o=5&pid=1.7"
        ),
        Achievement(
            "moon",
            "https://elements-cover-images-0.imgix.net/9018daf8-cceb-43f2-aeec-8d83e4926103?auto=compress&crop=edges&fit=crop&fm=jpeg&h=630&w=1200&s=4677cb441aecb7c6bbf69aa0be7d4322"
        ),
        Achievement(
            "big star",
            "https://cdn4.vectorstock.com/i/1000x1000/47/28/cartoon-astronaut-on-moon-on-a-space-vector-27344728.jpg"
        ),
        Achievement(
            "big moon",
            "https://th.bing.com/th/id/OIP.9o6Ltk_hGdXDqXgWhAQDbwHaIB?w=170&h=184&c=7&r=0&o=5&pid=1.7"
        ),
        Achievement(
            "angle",
            "https://th.bing.com/th/id/OIP.p7Vajcwtv4uixHq_3j1FygHaHa?w=172&h=180&c=7&r=0&o=5&pid=1.7"
        ),
        Achievement(
            "Upcoming lawyer",
            "https://th.bing.com/th/id/OIP.COEpJBEQj0PAjPgp3oVTfgHaHa?w=165&h=180&c=7&r=0&o=5&pid=1.7"
        ),
    )

    LazyVerticalStaggeredGrid(
        modifier = modifier.height(((achivements.size/4)*200).dp),
        columns = StaggeredGridCells.Fixed(4)
    ) {

        items(achivements) {

            AchievementItem(modifier,it)

        }

    }

}

@Composable
fun AchievementItem(modifier: Modifier,it: Achievement) {
    Box(modifier = modifier.padding(4.dp)) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = modifier
                    .size(60.dp)
                    .clip(CircleShape),
                model = it.image_url,
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            Text(text = it.title, textAlign = TextAlign.Center)
        }
    }

}
