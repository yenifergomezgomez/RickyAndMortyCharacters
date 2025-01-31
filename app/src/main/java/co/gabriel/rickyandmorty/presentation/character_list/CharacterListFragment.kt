package co.gabriel.rickyandmorty.presentation.character_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.gabriel.rickyandmorty.core.provideCharacterRepository
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.databinding.CharacterListFragmentBinding
import co.gabriel.rickyandmorty.presentation.ScreenState
import com.google.android.material.snackbar.Snackbar

class CharacterListFragment : Fragment() {


    private lateinit var viewModel: CharacterListViewModel

    private var _binding: CharacterListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var characterAdapter: CharacterRecyclerViewAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CharacterListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.apply {

            val factory =
                CharacterListViewModelFactory(
                    characterRepository = provideCharacterRepository(),
                )
            viewModel = ViewModelProvider(
                owner = this@CharacterListFragment,
                factory = factory
            )[CharacterListViewModel::class.java]
            viewModel.findCharacters().observe(viewLifecycleOwner, Observer(::renderState))

        }

        characterAdapter = CharacterRecyclerViewAdapter(emptyList(), binding.tvTotalPrice)

        binding.characterListRecycle.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun renderState(screenState: ScreenState<Any>) {

        when (screenState) {
            is ScreenState.Render -> showTeams(screenState.data as List<Character>)
            is ScreenState.Error -> showError(screenState.message)
            is ScreenState.Loading -> showLoading()
        }

    }

    private fun showLoading() {
        Snackbar.make(this.requireView(),"Loading..", Snackbar.LENGTH_LONG).show()

    }

    private fun showError(message: String?) {
        if (message != null) {
            Snackbar.make(this.requireView(),message,Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showTeams(list: List<Character>) {
        characterAdapter.updateCharacters(list)
    }

}