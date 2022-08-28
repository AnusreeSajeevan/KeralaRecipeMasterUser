package com.keralarecipemaster.user.presentation.ui.recipe.details

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Environment
import android.widget.Toast
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.R.*
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.presentation.ui.recipe.RatingBarView
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeDestinations
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeDetailsViewModel
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.RecipeUtil
import com.keralarecipemaster.user.utils.UserType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun RecipeDetailsScreen(
    recipeId: Int,
    recipeDetailsViewModel: RecipeDetailsViewModel,
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController
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

    val rating by ratingFlowLifecycleAware.collectAsState(0)

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Box {
            Image(
                painter = painterResource(
                    id = drawable.chicken_biriyani
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            if (recipeEntity.addedBy == UserType.USER.name) {
                IconButton(onClick = {
                    navController.navigate(AddRecipeDestinations.AddRecipeDetails.name)
                }, modifier = Modifier.align(Alignment.BottomEnd)) {
                    Icon(
                        painter = painterResource(
                            id = drawable.ic_edit
                        ),
                        contentDescription = null
                    )
                }
            }
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

            Row {
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
                Spacer(modifier = Modifier.size(10.dp))
                Image(
                    painter = painterResource(id = dietLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(15.dp)
                        .width(15.dp)
                        .align(Alignment.CenterVertically)

                )

                if (recipeEntity.addedBy == UserType.USER.name) {
                    IconButton(onClick = {
                        val name = createPdf(
                            context,
                            recipe.value
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
                }
            }
            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = recipeEntity.description,
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
            )

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

            // Restaurant Details
            Spacer(Modifier.size(16.dp))
            Text(
                text = "Famous Restaurant",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 14.sp)
            )
            Text(text = recipeEntity.restaurantName)
            Text(text = recipeEntity.restaurantAddress)
        }
    }
}

fun createPdf(
    context: Context,
    recipe: RecipeEntity
): String {
//    val bmp = BitmapFactory.decodeResource(context.resources, drawable.pin)
//    val scaledbmp: Bitmap = Bitmap.createScaledBitmap(bmp, 140, 140, false)
    // creating a bitmap variable
    // for storing our images
    // creating a bitmap variable
    // for storing our images

    // declaring width and height
    // for our PDF file.

    // declaring width and height
    // for our PDF file.
    val pageHeight = 1120
    val pagewidth = 792

    // creating an object variable
    // for our PDF document.
    // creating an object variable
    // for our PDF document.
    val pdfDocument = PdfDocument()

    // two variables for paint "paint" is used
    // for drawing shapes and we will use "title"
    // for adding text in our PDF file.

    // two variables for paint "paint" is used
    // for drawing shapes and we will use "title"
    // for adding text in our PDF file.
    val paint = Paint()
    val title = Paint()

    // we are adding page info to our PDF file
    // in which we will be passing our pageWidth,
    // pageHeight and number of pages and after that
    // we are calling it to create our PDF.

    // we are adding page info to our PDF file
    // in which we will be passing our pageWidth,
    // pageHeight and number of pages and after that
    // we are calling it to create our PDF.
    val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()

    // below line is used for setting
    // start page for our PDF file.

    // below line is used for setting
    // start page for our PDF file.
    val myPage = pdfDocument.startPage(mypageInfo)

    // creating a variable for canvas
    // from our page of PDF.

    // creating a variable for canvas
    // from our page of PDF.
    val canvas: Canvas = myPage.canvas

    // below line is used to draw our image on our PDF file.
    // the first parameter of our drawbitmap method is
    // our bitmap
    // second parameter is position from left
    // third parameter is position from top and last
    // one is our variable for paint.

    // below line is used to draw our image on our PDF file.
    // the first parameter of our drawbitmap method is
    // our bitmap
    // second parameter is position from left
    // third parameter is position from top and last
    // one is our variable for paint.
//    canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

    // below line is used for adding typeface for
    // our text which we will be adding in our PDF file.

    // below line is used for adding typeface for
    // our text which we will be adding in our PDF file.
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

    // below line is used for setting text size
    // which we will be displaying in our PDF file.

    // below line is used for setting text size
    // which we will be displaying in our PDF file.
//    title.setTextSize(15);

    // below line is sued for setting color
    // of our text inside our PDF file.

    // below line is sued for setting color
    // of our text inside our PDF file.
//     title.setColor(ContextCompat.getColor(this, R.color.purple_200))

    // below line is used to draw text in our PDF file.
    // the first parameter is our text, second parameter
    // is position from start, third parameter is position from top
    // and then we are passing our variable of paint which is title.

    // below line is used to draw text in our PDF file.
    // the first parameter is our text, second parameter
    // is position from start, third parameter is position from top
    // and then we are passing our variable of paint which is title.
    canvas.drawText(recipe.recipeName, 209F, 100F, title)
    canvas.drawText(recipe.description, 209F, 110F, title)
    canvas.drawText("Self rating - " + recipe.rating.toString(), 209F, 120F, title)
    canvas.drawText(recipe.diet.type, 209F, 140F, title)
    canvas.drawText(recipe.mealType.type, 209F, 160F, title)
    recipe.ingredients.forEach {
        canvas.drawText(it.name + " - " + it.quantity, 209F, 160F, title)
    }

    // similarly we are creating another text and in this
    // we are aligning this text to center of our PDF file.

    // similarly we are creating another text and in this
    // we are aligning this text to center of our PDF file.
    title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    title.color = ContextCompat.getColor(context, R.color.black)
    title.textSize = 15F

    // below line is used for setting
    // our text to center of PDF.

    // below line is used for setting
    // our text to center of PDF.
//    title.textAlign = Paint.Align.CENTER

    // after adding all attributes to our
    // PDF file we will be finishing our page.

    // after adding all attributes to our
    // PDF file we will be finishing our page.
    pdfDocument.finishPage(myPage)
    // below line is used to set the name of
    // our PDF file and its path.
    // below line is used to set the name of
    // our PDF file and its path.

    val tsLong = System.currentTimeMillis() / 1000
    val ts = tsLong.toString()
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "$ts.pdf"
    )

    try {
        // after creating a file name we will
        // write our PDF file to that location.
        pdfDocument.writeTo(FileOutputStream(file))

        // below line is to print toast message
        // on completion of PDF generation.
        Toast.makeText(context, "PDF file generated successfully.", Toast.LENGTH_SHORT)
            .show()
    } catch (e: IOException) {
        // below line is used
        // to handle error
        e.printStackTrace()
    }
    // after storing our pdf to that
    // location we are closing our PDF file.
    // after storing our pdf to that
    // location we are closing our PDF file.
    pdfDocument.close()
    return "$ts.pdf"
}
