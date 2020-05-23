package com.mteam.movie_trailer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mteam.movie_trailer.R
import kotlinx.android.synthetic.main.fragment_choose_example.*

class FragmentChooseExample : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_example, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mvRxButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentChooseExample_to_movieFragment)

        }
        buttonTheme.setOnClickListener {
            findNavController().navigate(R.id.toDynamicThemeFragment)
        }
        buttonPipVideo.setOnClickListener {
            findNavController().navigate(R.id.toPipFragment)
        }

        buttonPipVideoActivity.setOnClickListener {
            findNavController().navigate(R.id.topipActivity)
        }
        buttonCoroutine.setOnClickListener {
            findNavController().navigate(R.id.toFragmentCoroutineUseCase)
        }
        buttonLoginDragger.setOnClickListener {
            findNavController().navigate(R.id.toLoginActivity)
        }
        debugView.setOnClickListener {
            findNavController().navigate(R.id.toDebugView)
        }
        targetView.setOnClickListener {
            findNavController().navigate(R.id.toTagetView)
        }
    }
}