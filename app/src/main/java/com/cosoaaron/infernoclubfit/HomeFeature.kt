package com.cosoaaron.infernoclubfit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosoaaron.infernoclubfit.ui.theme.InfernoOrange
import com.cosoaaron.infernoclubfit.ui.theme.InfernoRed

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { Text("INFERNO", color = InfernoRed, fontSize = 26.sp, fontWeight = FontWeight.Black); Spacer(modifier = Modifier.width(4.dp)); Text("SOCIAL", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Light) }
        Spacer(modifier = Modifier.height(24.dp)); Text("Guerreros Activos", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            item { Column(horizontalAlignment = Alignment.CenterHorizontally) { Box(modifier = Modifier.size(64.dp).border(2.dp, InfernoOrange, CircleShape).padding(4.dp).clip(CircleShape).background(Color(0xFF333333)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Add, null, tint = Color.White) }; Spacer(modifier = Modifier.height(4.dp)); Text("Tú", color = Color.White, fontSize = 12.sp) } }
            items(listOf("Ana", "Carlos", "Juan")) { name -> Column(horizontalAlignment = Alignment.CenterHorizontally) { Box(modifier = Modifier.size(64.dp).border(2.dp, InfernoRed, CircleShape).padding(4.dp).clip(CircleShape).background(Color.Gray), contentAlignment = Alignment.Center) { Text(name.first().toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) }; Spacer(modifier = Modifier.height(4.dp)); Text(name, color = Color.White, fontSize = 12.sp) } }
        }
        Spacer(modifier = Modifier.height(24.dp)); Divider(color = Color(0xFF222222)); Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(GlobalFeedManager.posts) { post ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), border = BorderStroke(1.dp, Color(0xFF333333))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if(post.user == GlobalUserManager.username) InfernoOrange else Color.Gray), contentAlignment = Alignment.Center) { Text(post.user.first().toString().uppercase(), color = Color.White, fontWeight = FontWeight.Bold) }; Spacer(modifier = Modifier.width(12.dp)); Column { Text(post.user, color = Color.White, fontWeight = FontWeight.Bold); Text(post.timeAgo, color = Color.Gray, fontSize = 12.sp) }; Spacer(modifier = Modifier.weight(1f)); Icon(Icons.Default.MoreHoriz, null, tint = Color.Gray) }
                        Spacer(modifier = Modifier.height(12.dp)); Row { Text("Ha completado ", color = Color.LightGray); Text(post.workoutName, color = InfernoRed, fontWeight = FontWeight.Bold) }; Spacer(modifier = Modifier.height(16.dp)); Divider(color = Color(0xFF333333)); Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = { GlobalFeedManager.toggleLike(post) }, modifier = Modifier.size(24.dp)) { Icon(if (post.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder, "Like", tint = if (post.isLiked) InfernoRed else Color.Gray) }; Spacer(modifier = Modifier.width(8.dp)); Text("${post.likes}", color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}