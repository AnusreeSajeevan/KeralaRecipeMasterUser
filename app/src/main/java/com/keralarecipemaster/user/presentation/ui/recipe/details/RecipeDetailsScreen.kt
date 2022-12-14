package com.keralarecipemaster.user.presentation.ui.recipe.details

import android.content.Intent
import android.graphics.*
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.R.*
import com.keralarecipemaster.user.presentation.ui.recipe.RatingBarView
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeDestinations
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeDetailsViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.RecipeUtil
import com.keralarecipemaster.user.utils.RecipeUtil.Companion.createPdf
import com.keralarecipemaster.user.utils.UserType
import java.io.File

@Composable
fun RecipeDetailsScreen(
    recipeId: Int,
    recipeDetailsViewModel: RecipeDetailsViewModel,
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel
) {
    val context = LocalContext.current

    recipeDetailsViewModel.getRecipeDetails(recipeId)
    val lifecycleOwner = LocalLifecycleOwner.current
    val recipe = recipeDetailsViewModel.recipe
    val recipeFlowLifecycleAware = remember(recipe, lifecycleOwner) {
        recipe.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val recipeEntity by recipeFlowLifecycleAware.collectAsState(RecipeUtil.provideRecipe())

    val ratingValue = recipeDetailsViewModel.rating
    val ratingFlowLifecycleAware = remember(ratingValue, lifecycleOwner) {
        ratingValue.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val rating by ratingFlowLifecycleAware.collectAsState(Constants.INVALID_RECIPE_ID)

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        val bitmap = RecipeUtil.getBitmapFromBase64Image(recipeEntity.image)
        if (bitmap == null) {
            Image(
                painter = painterResource(
                    id = R.drawable.placeholder
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = recipeEntity.recipeName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = recipeEntity.mealType.type,
                style = TextStyle(fontSize = 14.sp)
            )

            val dietLogo = if (recipeEntity.diet.type == Diet.NON_VEG.type) {
                drawable.ic_non_veg
            } else {
                drawable.ic_veg
            }
            if (recipeEntity.addedBy == UserType.OWNER) {
                if (rating != Constants.INVALID_RECIPE_ID) {
                    RatingBarView(
                        rating = remember {
                            mutableStateOf(rating)
                        },
                        isRatingEditable = false,
                        ratedStarsColor = Color(255, 220, 0),
                        starIcon = painterResource(id = drawable.ic_star_filled),
                        unRatedStarsColor = Color.LightGray,
                        viewModel = recipeDetailsViewModel
                    )
                }
            }
            Row {
                Spacer(modifier = Modifier.size(10.dp))
                Image(
                    painter = painterResource(id = dietLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(15.dp)
                        .width(15.dp)
                        .align(Alignment.CenterVertically)

                )

                if (recipeEntity.addedBy == UserType.USER) {
                    IconButton(onClick = {
                        val name = createPdf(
                            context,
                            recipe.value,
                            chefName = authenticationViewModel.name.value
                        )
                        val outputFile = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            name
                        )
//                        val uri: Uri = Uri.fromFile(outputFile)

                        val pdfUri = FileProvider.getUriForFile(
                            context,
                            context.applicationContext.packageName.toString() + ".provider",
                            outputFile
                        )

                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, pdfUri)
                            type = "application/pdf"
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                        /*  val sendIntent: Intent = Intent().apply {
                              action = Intent.ACTION_SEND
                              putExtra(Intent.EXTRA_TEXT, RecipeUtil.generateRecipeDetailsToShare(recipeEntity))
                              type = "text/plain"
                          }

                          val shareIntent = Intent.createChooser(sendIntent, null)
                          context.startActivity(shareIntent)*/
                    }) {
                        Icon(
                            painter = painterResource(
                                id = drawable.ic_share
                            ),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(AddRecipeDestinations.AddRecipeDetails.name)
                    }) {
                        Icon(
                            painter = painterResource(
                                id = drawable.ic_edit
                            ),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))

            if (recipeEntity.description.isNotEmpty()) {
                Text(
                    text = recipeEntity.description,
                    style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
                )
                Spacer(Modifier.size(20.dp))
            }

            if (recipeEntity.addedBy == UserType.OWNER) {
                /* Restaurant Details*/
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Restaurant",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.size(4.dp))
                Text(text = recipeEntity.restaurantName)
                Text(text = recipeEntity.restaurantAddress)
                Spacer(Modifier.size(20.dp))
                /* End Restaurant Details*/
            }

            Spacer(Modifier.size(20.dp))

            Text(
                text = "Ingredients",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.size(4.dp))

            recipeEntity.ingredients.forEach {
                Text(
                    text = it.name + "  -  " + it.quantity,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(Modifier.size(2.dp))
            }

            Spacer(Modifier.size(20.dp))

            Text(
                text = "Preparation Method",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.size(4.dp))

            Text(
                text = recipeEntity.preparationMethod,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}
