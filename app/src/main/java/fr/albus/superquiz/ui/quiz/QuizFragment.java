package fr.albus.superquiz.ui.quiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import fr.albus.superquiz.databinding.FragmentQuizBinding;
import fr.albus.superquiz.injection.ViewModelFactory;


public class QuizFragment extends Fragment {

    FragmentQuizBinding binding;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(QuizViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}