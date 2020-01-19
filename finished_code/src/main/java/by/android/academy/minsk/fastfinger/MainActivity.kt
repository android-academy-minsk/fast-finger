package by.android.academy.minsk.fastfinger

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import by.android.academy.minsk.fastfinger.chat.ChatFragment
import by.android.academy.minsk.fastfinger.game.GameFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GameFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.openChat) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChatFragment())
                .commitNow()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
