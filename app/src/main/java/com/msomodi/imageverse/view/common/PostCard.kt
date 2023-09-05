package com.msomodi.imageverse.view.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.post.response.PostResponse
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.ui.layout.ContentScale
import com.msomodi.imageverse.model.user.response.UserResponse
import kotlinx.datetime.LocalDateTime

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    user: UserResponse?,
    post: PostResponse,
    navigateToEditPost: (String) -> Unit,
    onPostDelete: (String, Int) -> Unit,
    onRefresh: () -> Unit
){
    var hashtags : String = " "
    post.hashtags.forEach{hashtag ->
        hashtags += "#${hashtag.name} "
    }
    val postedAt = LocalDateTime.parse(post.postedAtDateTime.dropLast(1)+"0")
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colors.background
            ),
        shape = RoundedCornerShape(30.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = modifier.fillMaxWidth(.7f)){
                    Row (modifier = modifier.fillMaxWidth(1f), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.spacedBy(10.dp)){
                        AsyncImage(
                            modifier = modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape),
                            model = post.authorPhoto,
                            contentScale = ContentScale.FillBounds,
                            contentDescription = stringResource(R.string.profile_image),
                        )
                        Text(text = post.author, style = typography.h4)
                    }
                }
                Column(modifier = modifier.fillMaxWidth(1f)){
                    Row (modifier = modifier.fillMaxWidth(1f), verticalAlignment =  Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                        if(user != null && (post.userId == user.id || user.isAdmin)) {
                            IconButton(onClick = { navigateToEditPost(post.id); onRefresh() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = stringResource(R.string.edit_post)
                                )
                            }
                            IconButton(onClick = { onPostDelete(post.id, post.postId); onRefresh() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(R.string.delete_post)
                                )
                            }
                        }
                    }
                }
            }
            AsyncImage(
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                model = post.images.first().url,
                contentDescription = post.description,
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center
            )
            Text(text = post.description + hashtags, style = typography.h5)
            Text(text = "Posted at ${postedAt.date} ${postedAt.time.hour}:${postedAt.time.minute}", style = typography.h6)
        }
    }
}