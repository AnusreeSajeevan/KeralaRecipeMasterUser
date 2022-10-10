package com.keralarecipemaster.user.utils

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RecipeUtil {
    companion object {
        fun provideRecipe(
            id: Int = 0,
            recipeName: String = Constants.EMPTY_STRING,
            description: String = Constants.EMPTY_STRING,
            ingredients: List<Ingredient> = listOf(),
            image: String = Constants.EMPTY_STRING,
            restaurantName: String = Constants.EMPTY_STRING,
            restaurantLatitude: String = Constants.EMPTY_STRING,
            restaurantLongitude: String = Constants.EMPTY_STRING,
            restaurantState: String = Constants.EMPTY_STRING,
            preparationMethod: String = Constants.EMPTY_STRING,
            mealType: Meal = Meal.DINNER,
            diet: Diet = Diet.VEG,
            rating: Int = 0,
            status: String = Constants.EMPTY_STRING
        ): RecipeEntity {
            return RecipeEntity(
                id = id,
                recipeName = recipeName,
                description = description,
                ingredients = ingredients,
                image = image,
                restaurantName = restaurantName,
                restaurantLatitude = restaurantLatitude,
                restaurantLongitude = restaurantLongitude,
                restaurantAddress = restaurantState,
                preparationMethod = preparationMethod,
                mealType = Meal.valueOf(mealType.name),
                diet = Diet.valueOf(diet.name),
                addedBy = UserType.USER,
                rating = rating,
                status = status
            )
        }

        fun generateRecipeDetailsToShare(recipe: RecipeEntity): String {
            var recipeDetails = Constants.EMPTY_STRING
            recipeDetails = recipe.recipeName + "\n\n"
            recipeDetails += "Self rating - " + recipe.rating.toString() + "\n"
            recipeDetails += recipe.diet.type + "\n"
            recipeDetails += recipe.mealType.type + "\n"
            recipeDetails += recipe.description + "\n\n"
            recipeDetails += "Ingredients : " + "\n"

            recipe.ingredients.forEach {
                recipeDetails += it.name + " - " + it.quantity + "\n"
            }

            recipeDetails += "\nPreparation Method\n" + recipe.preparationMethod
            if (recipe.restaurantName != Constants.EMPTY_STRING) {
                recipeDetails += "\n\nFamous Restaurant"
                recipeDetails += "\n" + recipe.restaurantName
                recipeDetails += "\n" + recipe.restaurantAddress
            }
            return recipeDetails
        }

        var startY = 100F

        fun createPdf(
            context: Context,
            recipe: RecipeEntity,
            chefName: String
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

            // we are adding page info to our PDF file
            // in which we will be passing our pageWidth,
            // pageHeight and number of pages and after that
            // we are calling it to create our PDF.

            // we are adding page info to our PDF file
            // in which we will be passing our pageWidth,
            // pageHeight and number of pages and after that
            // we are calling it to create our PDF.
            val mypageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()

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
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

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
            val contentStart = 100F
            paint.textSize = 50f
            canvas.drawText(recipe.recipeName, 350f, getY(), paint)
            paint.textSize = 30f
            addSpace()

            canvas.drawText("${recipe.diet.type}", contentStart, getY(), paint)
            canvas.drawText("${recipe.mealType.type}", contentStart, getY(), paint)

            if (recipe.description.isNotEmpty()) {
                canvas.drawText(recipe.description, contentStart, getY(), paint)
                addSpace()
            }

            addSpace()
            paint.textSize = 40f
            canvas.drawText("Ingredients", contentStart, getY(), paint)
            paint.textSize = 30f
            recipe.ingredients.forEach {
                canvas.drawText("${it.name} - ${it.quantity}", contentStart, getY(), paint)
            }
            addSpace()

            paint.textSize = 40f
            canvas.drawText("Preparation Method", contentStart, getY(), paint)
            paint.textSize = 30f
            canvas.drawText("${recipe.preparationMethod}", contentStart, getY(), paint)
            addSpace()


            for (i in 1..5) {
                addSpace()
            }
            paint.textSize = 40f
            canvas.drawText("\n\nChef,", contentStart, getY(), paint)
            canvas.drawText("\n\n$chefName", contentStart, getY(), paint)

//            recipe.ingredients.forEach {
//                canvas.drawText(it.name + " - " + it.quantity, 209F, getY(), paint)
//            }

//
//            canvas.drawText(recipe.preparationMethod + "\n", 209F, getY(), paint)

//            canvas.drawText("\nFamous Restaurant", contentStart, getY(), paint)
//            canvas.drawText(recipe.restaurantName + "\n", contentStart, getY(), paint)
//            canvas.drawText(recipe.restaurantAddress, contentStart, getY(), paint)
            // similarly we are creating another text and in this
            // we are aligning this text to center of our PDF file.

            // similarly we are creating another text and in this
            // we are aligning this text to center of our PDF file.
            paint.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            paint.color = ContextCompat.getColor(context, R.color.black)
            paint.textSize = 15F

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

        private fun getY(): Float {
            startY += 35F
            return startY
        }

        private fun addSpace() {
            startY += 40F
        }

        fun provideRecipeRequest(
            id: Int = 0,
            recipeName: String = Constants.EMPTY_STRING,
            description: String = Constants.EMPTY_STRING,
            ingredients: List<Ingredient> = listOf(),
            image: String? = null,
            restaurantName: String = Constants.EMPTY_STRING,
            restaurantLatitude: String = Constants.EMPTY_STRING,
            restaurantLongitude: String = Constants.EMPTY_STRING,
            restaurantState: String = Constants.EMPTY_STRING,
            preparationMethod: String = Constants.EMPTY_STRING,
            mealType: Meal = Meal.DINNER,
            diet: Diet = Diet.VEG,
            rating: Int = 0,
            status: String = Constants.EMPTY_STRING
        ): RecipeRequestEntity {
            return RecipeRequestEntity(
                recipeId = id,
                recipeName = recipeName,
                description = description,
                ingredients = ingredients,
                image = image,
                restaurantName = restaurantName,
                restaurantLatitude = restaurantLatitude,
                restaurantLongitude = restaurantLongitude,
                restaurantAddress = restaurantState,
                preparationMethod = preparationMethod,
                mealType = Meal.valueOf(mealType.name),
                diet = Diet.valueOf(diet.name),
                rating = rating,
                status = status,
                addedBy = UserType.OWNER.value
            )
        }

        fun getBitmapFromBase64Image(base64: String): Bitmap? {
            val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }
    }
}
