package com.example.dolegal.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dolegal.core.common.CurrentUserResource
import com.example.dolegal.core.common.StoryCategoryResource
import com.example.dolegal.core.common.StoryResource
import com.example.dolegal.core.common.UsersResource
import com.example.dolegal.presentation.models.Story
import com.example.dolegal.presentation.models.StoryBanner
import com.example.dolegal.presentation.models.Users
import com.example.dolegal.presentation.screens.chatScreen.ChatsListScreen
import com.example.dolegal.presentation.state.CurrentUserState
import com.example.dolegal.presentation.state.StoryCategoryState
import com.example.dolegal.presentation.state.StoryStates
import com.example.dolegal.presentation.state.UsersState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject
import javax.inject.Named

@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    @Named("users")
    val userDatabase: CollectionReference,
    @Named("story_category")
    val bannerDatabase: CollectionReference,
    @Named("stories")
    val storyDatabase: CollectionReference,
    private val storage: FirebaseStorage,
    private val chatClient: ChatClient,
    private val auth: FirebaseAuth
) : ViewModel() {
    init {

        getAllUsers()
        getAllBanners()
        GlobalScope.launch {
            if (checkUser())
                getCurrentUser()
        }


    }

    private val _stories = MutableStateFlow(StoryStates())
    val stories: StateFlow<StoryStates>
        get() = _stories

    private val _banners = MutableStateFlow(StoryCategoryState())

    val banners: StateFlow<StoryCategoryState>
        get() = _banners

    private val _users = MutableStateFlow(UsersState())
    val users: StateFlow<UsersState>
        get() = _users


    private val _currentUser = MutableStateFlow(CurrentUserState())
    val currentUser: StateFlow<CurrentUserState>
        get() = _currentUser


    private fun _getCurrentUser(): Flow<CurrentUserResource<Users>> = flow {

        emit(CurrentUserResource.Loading())
        val result =
            userDatabase.document(auth.currentUser?.uid!!).get().await().toObject(Users::class.java)
        if (result != null)
            emit(CurrentUserResource.Success(result))
    }

    suspend fun getCurrentUser() {

        _getCurrentUser().collect {

            when (it) {

                is CurrentUserResource.Success -> {

                    _currentUser.value = CurrentUserState().copy(user = it.user)

                }

                is CurrentUserResource.Error -> {

                    _currentUser.value = CurrentUserState().copy(error = it.message.toString())

                }

                is CurrentUserResource.Loading -> {

                    _currentUser.value = CurrentUserState().copy(isLoading = true)

                }

            }

        }

    }

    fun checkUser() = auth.currentUser != null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun logout() {
        auth.signOut()
        logoutStream()
    }

    fun login(
        email: String,
        password: String,
        onLogin: () -> Unit,
        isLoadingChange: (Boolean) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Vandan_Login", "Login Successfull")
                isLoadingChange(false)
                onLogin()
                return@addOnCompleteListener
            } else {
                isLoadingChange(false)
                Log.d("Vandan_Login", task.exception?.localizedMessage.toString())
                return@addOnCompleteListener
            }
        }
    }

    fun signup(
        email: String,
        name: String,
        password: String,
        isLoadingChange: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                Log.d("Vandan_Signup", "Signup Successfull")

                val uid = auth.currentUser?.uid!!


                val user = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password,
                    "profile_pic" to "",
                    "stream_id" to "",
                    "score" to "0",
                    "token" to "",
                )
                viewModelScope.launch {
                    userDatabase.document(uid).set(user).await()
                }
                isLoadingChange(false)

                viewModelScope.launch {
                    chatClient.connectGuestUser(
                        email.removeSuffix("@gmail.com"),
                        email.removeSuffix("@gmail.com")
                    ).await().onSuccess { currentuser ->
                        Log.d("Vandan_Login_Stream", "Login Success $currentuser")
                        viewModelScope.launch {
                            userDatabase.document(auth.currentUser?.uid!!)
                                .update("sID", currentuser.user.id).await()
                            val token = chatClient.getCurrentToken()
                            val devtoken =
                                chatClient.getGuestToken(currentuser.user.id, currentuser.user.id)
                            Log.d("Vandan_Login_Stream", "Token ${token}")
                            userDatabase.document(auth.currentUser?.uid!!).update("token", token)
                                .await()
                            val user = User(id = currentuser.user.id)
                            chatClient.updateUser(user).enqueue {

                                val user = User(
                                    id = currentuser.user.id,
                                    name = currentuser.user.name,
                                    role = "user"
                                )

                            }
                        }
                    }.onError {
                        Log.d("Vandan_Login_Stream", "Login Failed $it")
                    }
                }

            } else {
                isLoadingChange(false)
                Log.d("Vandan_Signup", task.exception?.localizedMessage.toString())
            }

        }

    }

    fun updateProfile(name: String?, email: String, uri: Uri?) {
        viewModelScope.launch {
            userDatabase.document(auth.currentUser?.uid!!).update("name", name).await()
            userDatabase.document(auth.currentUser?.uid!!).update("email", email).await()
            Log.d("Vandan_Image", "$uri")
            viewModelScope.launch {
                if (uri != null && name != null) {
                    storage.getReference("$name/").putFile(uri).await()
                    storage.reference.child("$name")
                        .downloadUrl.addOnSuccessListener {
                            uploadAtProfile(it)
                        }
                }
            }
        }
    }

    private fun uploadAtProfile(uri: Uri) {

        viewModelScope.launch {
            userDatabase.document(auth.currentUser?.uid!!).update("profile_pic", uri).addOnCompleteListener {
                Log.d("Vandan_Image", "Upload Success")
            }
        }
    }

    private fun getUsers(): Flow<UsersResource<List<Users>>> = flow {

        emit(UsersResource.Loading())

        val result = userDatabase.get().await().toObjects(Users::class.java)
        emit(UsersResource.Success(result))


    }.flowOn(Dispatchers.IO)
        .catch {
            emit(UsersResource.Error(it.message.toString()))
        }

    private fun getAllUsers() {

        getUsers().onEach {

            when (it) {

                is UsersResource.Success -> {

                    _users.value = UsersState().copy(users = it.user?.sortedByDescending { it.score.toInt() })

                }

                is UsersResource.Error -> {

                    _users.value = UsersState().copy(errorMessage = it.message)

                }

                is UsersResource.Loading -> {

                    _users.value = UsersState().copy(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)

    }

    private fun getStories(category: String): Flow<StoryResource<List<Story>>> = flow {
        emit(StoryResource.Loading())

        val result = storyDatabase.document(category).collection(category).get().await()
            .toObjects(Story::class.java)
        emit(StoryResource.Success(result))

    }.flowOn(Dispatchers.IO)
        .catch {
            emit(StoryResource.Error(it.message.toString()))
        }

    fun getAllStories(category: String) {

        getStories(category).onEach {

            when (it) {

                is StoryResource.Success -> {

                    _stories.value = StoryStates().copy(stories = it.story)
                }

                is StoryResource.Error -> {

                    _stories.value = StoryStates().copy(errorMessage = it.message)
                }

                is StoryResource.Loading -> {

                    _stories.value = StoryStates().copy(isLoading = true)

                }

            }

        }.launchIn(viewModelScope)

    }


    private fun getBanners(): Flow<StoryCategoryResource<List<StoryBanner>>> = flow {

        emit(StoryCategoryResource.Loading())

        val result = bannerDatabase.get().await().toObjects(StoryBanner::class.java)
        emit(StoryCategoryResource.Success(result))


    }.flowOn(Dispatchers.IO)
        .catch {
            emit(StoryCategoryResource.Error(it.message.toString()))
        }

    private fun getAllBanners() {

        getBanners().onEach {
            when (it) {

                is StoryCategoryResource.Success -> {

                    _banners.value = StoryCategoryState().copy(categories = it.storyCategory)

                }

                is StoryCategoryResource.Error -> {

                    _banners.value = StoryCategoryState().copy(errorMessage = it.message)
                }

                is StoryCategoryResource.Loading -> {

                    _banners.value = StoryCategoryState().copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    }


//    fun connectUserInStream(username: String) {
//        viewModelScope.launch {
//            var tokn:String? = null
//            chatClient.getGuestToken(username,username).await().map {
//                tokn = it.token
//            }
//            Log.d("Vandan_Login_Stream", "Token ${tokn}")
//
//            val user = User(id = username, name = username)
//            if(tokn != null) {
//                chatClient.connectUser(user = user, token = tokn!!).enqueue {
//                    if (it.isSuccess) {
//                        Log.d("Vandan_Login_Stream", "Login Success $it")
//                    } else {
//                        Log.d("Vandan_Login_Stream", "Login Failed $it")
//                    }
//                }
//            }
//        }
//
//
//
//
//
//
//    }

    fun connectUserInStream(context: Context, usersID: String, username: String, token: String) {

        viewModelScope.launch {

            Log.d("Vandan_StreamID", usersID)
            val user = User(
                id = usersID,
                name = username,
                role = "user"
            )

            chatClient.connectUser(user = user, token = token).enqueue {
                if (it.isSuccess) {
                    Log.d("Vandan_Login_Stream", "Login Success $it")

                    val intent = Intent(context, ChatsListScreen::class.java)
                    startActivity(context, intent, null)

                } else {
                    Log.d("Vandan_Login_Stream", "Login Failed $it")
                }
            }

        }

    }

    fun logoutStream() {
        viewModelScope.launch {
            chatClient.disconnect(true).await().onSuccess {
                Log.d("Vandan_Logout_Stream", "Logout Success $it")
            }.onError {
                Log.d("Vandan_Logout_Stream", "Logout Failed $it")
            }

        }
    }


}