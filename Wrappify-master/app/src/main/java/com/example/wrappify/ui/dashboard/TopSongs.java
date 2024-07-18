package com.example.wrappify.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrappify.R;
import com.example.wrappify.Song;
import com.example.wrappify.SongService;
import com.example.wrappify.Wrapped;
import com.example.wrappify.databinding.FragmentDashboardBinding;
import com.example.wrappify.ui.wrapped.WrappedGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TopSongs extends Fragment {

    private FragmentDashboardBinding binding;
    private SongService songService;
    private ArrayList<Song> topSongs = new ArrayList<>();

    private TextView textTitle;
    private ImageButton back;
    private TextView songText;
    private Button resetGame;

    TextView textWordToBeGuessed;
    String wordToBeGuessed;
    String wordDisplayedString;
    char[] wordDisplayedCharArray;
    ArrayList<String> myListOfWords;
    EditText editInput;
    TextView textLettersTried;
    String lettersTried;
    final String MESSAGE_WITH_LETTERS_TRIED = "Letters tried: ";
    TextView textTriesLeft;
    String triesLeft;
    final String WINNING_MESSAGE = "You won :)";
    final String LOSING_MESSAGE = "You lost :(";
    Animation rotateAnimation;
    Animation scaleAnimation;
    Animation scaleAndRotateAnimation;

    void revealLetterInWord(char letter) {
        int indexOfLetter = wordToBeGuessed.indexOf(letter);

        //loop if index is positive or 0
        while(indexOfLetter >= 0) {
            wordDisplayedCharArray[indexOfLetter] = wordToBeGuessed.charAt(indexOfLetter);
            indexOfLetter = wordToBeGuessed.indexOf(letter,indexOfLetter + 1);
        }

        //update the string as well
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);
    }

    void displayWordOnScreen() {
        String formattedString = "";
        for(char character : wordDisplayedCharArray) {
            formattedString += character + " ";
        }
        textWordToBeGuessed.setText(formattedString);
    }
    void initializeGame() {
        //1.WORD
        //shuffle array list and get first element, and then remove it

        Collections.shuffle(myListOfWords);
        wordToBeGuessed = myListOfWords.get(0);
        myListOfWords.remove(0);

        //initialize char array
        wordDisplayedCharArray = wordToBeGuessed.toCharArray();

        //add underscores
        for (int i = 1; i < wordDisplayedCharArray.length - 1; i++) {
            wordDisplayedCharArray[i] = '_';
        }

        //reveal all occurrences of first character
        revealLetterInWord(wordDisplayedCharArray[0]);

        //reveal all occurrences of last character
        revealLetterInWord(wordDisplayedCharArray[wordDisplayedCharArray.length - 1]);

        //initialize a string from this char array (for search purposes)
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);

        //display word
        displayWordOnScreen();

        //2.INPUT
        //clear input field
        editInput.setText("");

        //3.LETTERS TRIED
        //initialize string for letters tried with a space
        lettersTried = " ";

        //display on screen
        textLettersTried.setText(MESSAGE_WITH_LETTERS_TRIED);

        //4.TRIES LEFT
        //initialize the string for tries left
        triesLeft = " X X X X X";
        textTriesLeft.setText(triesLeft);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_top_songs, container, false);

        textTitle = root.findViewById(R.id.textTitle);
        songService = new SongService(getContext());
        resetGame = root.findViewById(R.id.buttonReset);

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGame();
            }
        });

        //initializing variables
        myListOfWords = new ArrayList<String>();
        textWordToBeGuessed = (TextView) root.findViewById(R.id.textWordToBeGuessed);
        editInput = (android.widget.EditText) root.findViewById(R.id.editInput);
        textLettersTried = (TextView) root.findViewById(R.id.textLettersTried);
        textTriesLeft = (TextView) root.findViewById(R.id.textTriesLeft);

        back = (ImageButton) root.findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.dashboardFragment_to_topSongsFragment);
            }
        });

        topSongs = WrappedGenerator.topSongs;
        for (Song s: topSongs) {
            myListOfWords.add(s.getName());
        }

        initializeGame();

        //setup the text changed listener for the edit text
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                //if there is some letter on the input field
                if (s.length() != 0) {
                    char enteredLetter = s.charAt(0);

                    if (wordToBeGuessed.indexOf(enteredLetter) < 0) {
                        decreaseAndDisplayTriesLeft();
                    }
                    checkIfLetterIsInWord(s.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songText = view.findViewById(R.id.song1);
    }

    private void getTopSongs() {
        songService.getTopSongs(() -> {
            topSongs = songService.getSongs();
            songText.setText(topSongs.get(0).toString());
        });
    }

    void checkIfLetterIsInWord(char letter) {
        // if the letter was found inside the word to be guessed
        if (wordToBeGuessed.indexOf(letter) >= 0) {
            // if the letter was NOT displayed yet
            if (wordDisplayedString.indexOf(letter) < 0) {
                // replace the underscores with that letter
                revealLetterInWord(letter);

                // update the changes on screen
                displayWordOnScreen();

                // check if the game is won
                if (!wordDisplayedString.contains("_")) {
                    textTriesLeft.setText(WINNING_MESSAGE);
                }
            }
        } else {
            // decrease the number of tries left, and we'll show it on screen
            decreaseAndDisplayTriesLeft();

            // check if the game is lost
            if (triesLeft.isEmpty()) {
                textTriesLeft.setText(LOSING_MESSAGE);
                textWordToBeGuessed.setText(wordToBeGuessed);
            }
        }

        // display the letter that was tried
        if (lettersTried.indexOf(letter) < 0) {
            lettersTried += letter + ", ";
            String messageToBeDisplayed = MESSAGE_WITH_LETTERS_TRIED + lettersTried;
            textLettersTried.setText(messageToBeDisplayed);
        }
    }


    void decreaseAndDisplayTriesLeft() {
        // if there are still some tries left
        if (!triesLeft.isEmpty()) {
            // remove one "X" from triesLeft
            triesLeft = triesLeft.substring(0, triesLeft.length() - 1);
            // display the updated triesLeft
            textTriesLeft.setText(triesLeft);
        }
    }
}