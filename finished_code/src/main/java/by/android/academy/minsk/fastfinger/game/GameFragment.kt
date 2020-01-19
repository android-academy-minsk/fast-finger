package by.android.academy.minsk.fastfinger.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import by.android.academy.minsk.fastfinger.R
import kotlinx.android.synthetic.main.game_fragment.*

class GameFragment : Fragment() {

    companion object {
        fun newInstance() = GameFragment()
    }

    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}
