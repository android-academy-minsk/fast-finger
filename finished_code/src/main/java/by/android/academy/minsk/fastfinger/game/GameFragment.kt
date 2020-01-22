package by.android.academy.minsk.fastfinger.game

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import by.android.academy.minsk.fastfinger.R
import by.android.academy.minsk.fastfinger.chat.ChatFragment
import kotlinx.android.synthetic.main.game_fragment.*

class GameFragment : Fragment() {

    companion object {
        fun newInstance() = GameFragment()
    }

    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(
            this,
            gameViewModelFactory(context!!)
        ).get(GameViewModel::class.java)
        if (savedInstanceState == null) {
            viewModel.onScreenOpen()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.game_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.button.observe(viewLifecycleOwner) {
            button.text = when (it) {
                ButtonState.GAME_IN_PROGRESS -> getText(R.string.game_in_progress)
                ButtonState.READY_TO_START -> getText(R.string.ready_to_start_game)
                else -> ""
            }
            button.visibility = when (it) {
                ButtonState.GAME_IN_PROGRESS, ButtonState.READY_TO_START -> View.VISIBLE
                else -> View.INVISIBLE
            }
        }
        button.setOnClickListener { viewModel.onButtonClick() }
        viewModel.message.observe(viewLifecycleOwner) {
            message.text = it
        }
        viewModel.bestLocalScore.observe(viewLifecycleOwner) {
            bestScore.text = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.openChat) {
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ChatFragment())
                    .addToBackStack(null)
                    .commit()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
