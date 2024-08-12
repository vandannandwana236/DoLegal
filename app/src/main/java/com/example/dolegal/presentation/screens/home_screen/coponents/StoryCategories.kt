package com.example.dolegal.presentation.screens.home_screen.coponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dolegal.R
import com.example.dolegal.presentation.models.StoryCategory


@Composable
fun StoryCategories(onCategoryClick: (category: String) -> Unit) {

    val listStory = listOf(
        StoryCategory("mystery", "https://img.freepik.com/premium-photo/photo-fun-3d-cartoon-breton-character-high-quality-background_175994-61711.jpg?w=740"),
        StoryCategory("funny", "https://img.freepik.com/free-photo/3d-render-boy-with-orange-tshirt-sunglasses_1057-44868.jpg?t=st=1716439314~exp=1716442914~hmac=3229c11422e9c8002a89032eb45d736d33a4b8304174bc0e75f46377fba7fd2d&w=740"),
        StoryCategory("adventure", "https://img.freepik.com/free-photo/3d-rendering-cartoon-character-exploring-like-forest_23-2150991447.jpg?t=st=1716439241~exp=1716442841~hmac=12e18204c07a07fba5391d3f34dd7ad36c9b21dfef58ec924c29205cbc6ca472&w=740"),
        StoryCategory("historical", "https://img.freepik.com/free-vector/hand-drawn-paleolithic-period-illustration_23-2150146759.jpg?t=st=1716439375~exp=1716442975~hmac=4de4684d46a3cedd3ee23ca3e780826f1ef2248d724ee4c402160e74a68afb78&w=740"),
        StoryCategory("thriller", "https://img.freepik.com/free-vector/halloween-background-design_1188-30.jpg?t=st=1716439409~exp=1716443009~hmac=7ac7c85f002716e2da2005184fe726da3c271b7112e9f7228ec48cfaf730141d&w=740"),
        StoryCategory("fantasy", "https://img.freepik.com/free-photo/3d-kid-dragon-hanging-out_23-2151139342.jpg?t=st=1716439479~exp=1716443079~hmac=0d5c30e7c7f51dcfc2b048360e4bc71c560ddb622a8394cc2621414c434e75c6&w=740"),
        StoryCategory("magical", "https://img.freepik.com/premium-photo/magician-cartoon-character-illustration_1029473-204094.jpg?w=740"),
    )

    Column {

        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), modifier = Modifier.height(((listStory.size/2)*300).dp)) {

            items(listStory) { item ->

                StoryCategoryItem(item,onCategoryClick )

            }

        }

    }

}


@Composable
fun StoryCategoryItem(item: StoryCategory, onCategoryClick: (category: String) -> Unit) {
    val categoryNameFont = R.font.story_categoryy
    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {

        AsyncImage(
            placeholder = painterResource(id = R.drawable.loading_placeholder),
            modifier = Modifier.size(190.dp),
            contentScale = ContentScale.Crop,
            model = item.image_url,
            contentDescription = "category_image"
        )
        Box(modifier = Modifier.fillMaxSize()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.4f)
                    .background(Color.Black)
                    .clickable {
                        onCategoryClick(item.name)
                    }
            )
        }
        Text(
            text = item.name,
            color = Color.White,
            modifier = Modifier.padding(12.dp).align(Alignment.BottomCenter),
            fontFamily = FontFamily(Font(categoryNameFont)),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )

    }

}
