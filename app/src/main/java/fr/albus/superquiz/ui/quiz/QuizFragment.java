package fr.albus.superquiz.ui.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import fr.albus.superquiz.R;
import fr.albus.superquiz.data.Question;
import fr.albus.superquiz.databinding.FragmentQuizBinding;
import fr.albus.superquiz.databinding.FragmentWelcomBinding;
import fr.albus.superquiz.injection.ViewModelFactory;
import fr.albus.superquiz.ui.welcome.WelcomFragment;


public class QuizFragment extends Fragment {

    public static QuizFragment newInstance() {

        return new QuizFragment();
    }



    private  FragmentQuizBinding binding;
    private QuizViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(QuizViewModel.class);



    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        viewModel.startQuiz();
        viewModel.isLastQuestion.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLastQuestion) {
               if (isLastQuestion){
                   binding.next.setText("Fini !");
               }else{
                   binding.next.setText("Next");
               }
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isLastQuestion = viewModel.isLastQuestion.getValue();
                if (isLastQuestion != null && isLastQuestion){
                    displayResultDialog();
                }else {
                    viewModel.nextQuestion();
                    resetQuestion();
                }
            }
        });
        viewModel.currentQuestion.observe(getViewLifecycleOwner(), new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                updateQuestion(question);
            }
        });

        binding.answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer1, 0);
            }
        });
        binding.answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer2, 1);
            }
        });
        binding.answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer3,2);
            }
        });
        binding.answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer4,3);
            }
        });

    }

    private void displayResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Finished !");
        Integer score = viewModel.score.getValue();
        builder.setMessage("Ton Score est "+ score);
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToWelcomeFragment();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void resetQuestion() {
        List<Button> allAnswers = Arrays.asList(binding.answer1, binding.answer2, binding.answer3, binding.answer4);
        allAnswers.forEach(answer ->{
            answer.setBackgroundColor(Color.parseColor("#6200EE"));
            });
        binding.valiityText.setVisibility(View.INVISIBLE);
        enableAllAnswers(true);
    }

    private void updateAnswer(Button button, Integer index) {
        showAnswerValidity(button, index);
        enableAllAnswers(false);
        binding.next.setVisibility(View.VISIBLE);
    }

    private void enableAllAnswers(boolean enable) {
        List<Button> allAnswers = Arrays.asList(binding.answer1, binding.answer2, binding.answer3, binding.answer4);
        allAnswers.forEach(answer -> {
            answer.setEnabled(enable);
        });

    }

    private void showAnswerValidity(Button button, Integer index) {
        Boolean isValid = viewModel.isAnswerValid(index);
        if (isValid){
            button.setBackgroundColor(Color.parseColor("#388e3c"));
            binding.valiityText.setText("Bonne Repose");

        }else {
            button.setBackgroundColor(Color.RED);
            binding.valiityText.setText("Mauvaise Réponse");
        }
        binding.valiityText.setVisibility(View.VISIBLE);

    }

    private void updateQuestion(Question question) {
        binding.question.setText(question.getQuestion());
        binding.answer1.setText(question.getChoiceList().get(0));
        binding.answer2.setText(question.getChoiceList().get(1));
        binding.answer3.setText(question.getChoiceList().get(2));
        binding.answer4.setText(question.getChoiceList().get(3));

    }
    private void goToWelcomeFragment() {
        WelcomFragment welcomFragment = WelcomFragment.newInstance();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, welcomFragment).commit();
    }


}