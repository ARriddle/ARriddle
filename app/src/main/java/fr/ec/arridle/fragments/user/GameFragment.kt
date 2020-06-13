package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.ec.arridle.R
import fr.ec.arridle.activities.MainActivity

class GameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).createNavDrawer()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_game, container, false)
    }

}