package fr.albus.superquiz.injection;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import fr.albus.superquiz.data.Question;
import fr.albus.superquiz.data.QuestionBank;
import fr.albus.superquiz.data.QuestionRepository;
import fr.albus.superquiz.ui.quiz.QuizViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final QuestionRepository questionRepository;
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(){
        if (factory == null){
            synchronized (ViewModelFactory.class){
                if (factory == null){

                    factory = new ViewModelFactory();
                }
            }

        }
        return factory;
    }
    private ViewModelFactory(){
        QuestionBank questionBank = QuestionBank.getInstance();
        this.questionRepository = new QuestionRepository(questionBank);
    }

    @Override
    @NotNull
    public <T extends ViewModel> T create(Class<T> modelClass){
        if (modelClass.isAssignableFrom(QuizViewModel.class)){
            return (T) new  QuizViewModel(questionRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
