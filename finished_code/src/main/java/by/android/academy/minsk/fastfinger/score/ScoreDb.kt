package by.android.academy.minsk.fastfinger.score

import android.content.Context
import androidx.room.*

const val LOCAL_BEST_SCORE_ID = 0

@Entity(tableName = "scores")
data class ScoreEntity(val value: Int, @PrimaryKey val id: Int)

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateScore(score: ScoreEntity)

    @Query("select * from scores where id = :id")
    suspend fun getScore(id: Int): ScoreEntity?
}

@Database(entities = [ScoreEntity::class], version = 1, exportSchema = false)
abstract class ScoreDb : RoomDatabase() {
    abstract val scoreDao: ScoreDao
}

private lateinit var INSTANCE: ScoreDb

fun getDatabase(context: Context): ScoreDb {
    synchronized(ScoreDb::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    ScoreDb::class.java,
                    "scores_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}