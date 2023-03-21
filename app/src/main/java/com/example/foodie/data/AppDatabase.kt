package com.example.foodie.data

import android.content.Context
import androidx.room.*
import com.squareup.moshi.*

/**
 * This class represents the entrypoint into the Room-based database for the app.  It is a
 * singleton class, whose single instance you can access through the `getInstance()` method.
 */
@Database(entities = [Recipe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "favoritedRecipes.db"
            ).build()

        /**
         * This method returns the singleton instance of the application database.  It creates the
         * instance (in a thread-safe way) if necessary.
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}
class Converters {
    private val ingredientAdapter = Moshi.Builder().build().adapter<List<Ingredient>?>(
        Types.newParameterizedType(List::class.java, Ingredient::class.java ))

    private val instructionAdapter = Moshi.Builder().build().adapter<List<Instruction>?>(
        Types.newParameterizedType(List::class.java, Instruction::class.java ))
    private val stepAdapter = Moshi.Builder().build().adapter<List<Step>?>(
        Types.newParameterizedType(List::class.java, Step::class.java ))
    @TypeConverter
    fun toIngredient(value: String) = ingredientAdapter.fromJson(value)

    @TypeConverter
    fun fromIngredient(value: List<Ingredient>?) = ingredientAdapter.toJson(value)
    @TypeConverter
    fun toInstruction(value: String) = instructionAdapter.fromJson(value)

    @TypeConverter
    fun fromInstruction(value: List<Instruction>?) = instructionAdapter.toJson(value)
    @TypeConverter

    fun toStep(value: String) = stepAdapter.fromJson(value)

    @TypeConverter
    fun fromStep(value: List<Step>?) = stepAdapter.toJson(value)
}
