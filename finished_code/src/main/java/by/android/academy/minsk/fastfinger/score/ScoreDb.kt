package by.android.academy.minsk.fastfinger.score

import android.content.Context
import androidx.room.*

const val LOCAL_BEST_SCORE_ID = 0

@Entity(tableName = "scores")
data class ScoreEntity(val value: Int, @PrimaryKey val id: Int)

//!!!it's the FINISHED project, switch search to start module!!! TODO(5): coroutines integration
@Dao
abstract class ScoreDaoImpl : ScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun updateScore(score: ScoreEntity)

    @Query("select value from scores where id = :id")
    protected abstract suspend fun getScore(id: Int): Int?

    override suspend fun updateLocalBestScore(value: Int) {
        updateScore(ScoreEntity(value, LOCAL_BEST_SCORE_ID))
    }

    override suspend fun getLocalBestScore(): Int? = getScore(LOCAL_BEST_SCORE_ID)
}

interface ScoreDao {
    suspend fun getLocalBestScore(): Int?
    suspend fun updateLocalBestScore(value: Int)
}

@Database(entities = [ScoreEntity::class], version = 1, exportSchema = false)
abstract class ScoreDb : RoomDatabase() {
    abstract val scoreDao: ScoreDaoImpl
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