package typeapp.typeapp;


import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static javafx.scene.input.KeyCode.getKeyCode;

public class Controller {

    private static final double DELAY  = 0.001;
    private final double appStart;
    private  HBox hBox;
    private  VBox vBox;
    private ChoiceBox<String> languageChoiceBox;
    private Random random;
    private TextFlow textFlow;
    int currentIndex;
    private TextField textField;
    private Text caret = new Text("|");
    private static final double JUMP_HEIGHT = 10;
    private static final Duration JUMP_DURATION = Duration.millis(100);
    private String wordsPerMinuteCurrent;

    Text wordCountText = new Text();
    List<String> randomWords;
    private List<Integer> listOfLettersOfWords;



    public Controller(ChoiceBox<String> languageChoiceBox, VBox vBox, HBox hBox) {
        this.languageChoiceBox = languageChoiceBox;
        this.textFlow = new TextFlow();
        this.random = new Random();
        this.vBox = vBox;
        this.hBox = hBox;
        this.textField = new TextField();
        this.listOfLettersOfWords = new ArrayList<>();
        this.appStart = System.currentTimeMillis();

    }


    public void displaySelectedTextFromFile() {
        textFlow.getChildren().clear();

        String selectedLanguage = languageChoiceBox.getValue();
        String fileName = selectedLanguage + ".txt";
        File selectedFile = new File("dictionary/" + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            List<String> words = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
            int nuberOfLettersInTheWord = 0;
            int indexOfnuberOfListOfLettersInTheWord = 0;
            Collections.shuffle(words, random);
            int numWordsToShow = Math.min(words.size(), 30);
            this.randomWords = words.subList(0, numWordsToShow);
            System.out.println("randomWords" + randomWords);
            for (String word : randomWords) {
                for (char c : word.toCharArray()) {
                    Text textNode = new Text(String.valueOf(c));
                    textNode.setFill(Color.GRAY);
                    textNode.setFont(Font.font(20));
                    textFlow.getChildren().add(textNode);
                    nuberOfLettersInTheWord++;
                }
                textFlow.getChildren().add(new Text(" ")); // Add space between words
                this.listOfLettersOfWords.add(indexOfnuberOfListOfLettersInTheWord,nuberOfLettersInTheWord);
                indexOfnuberOfListOfLettersInTheWord++;
                nuberOfLettersInTheWord= 0;
            }


            vBox.getChildren().add(textFlow);
            wordCountText.setText("Words typed: " + wordsPerMinuteCurrent);
            hBox.getChildren().add(hBox.getChildren().size(), wordCountText);

        } catch (IOException e) {
            e.printStackTrace();
        }}


    public void handleKeyPress(KeyEvent event) {

        KeyCode keyCode = event.getCode();

        Text textNode = (Text) textFlow.getChildren().get(currentIndex);
        String character = textNode.getText();
        Text textNodeOfNextCharacter = (Text) textFlow.getChildren().get(currentIndex + 1);
        String nextCharacter = textNodeOfNextCharacter.getText();

        String inputCharToString = event.getText();

        if (character.equals(" ")) {
            if (keyCode.equals(KeyCode.ENTER)) {
                currentIndex++;
                return;
            } else if (keyCode.isLetterKey()) {
                Text redundantLetter = new Text(inputCharToString);
                redundantLetter.setFill(Color.ORANGE);
                redundantLetter.setFont(Font.font(20));
                textFlow.getChildren().add(currentIndex, redundantLetter);
                currentIndex++; // Aktualizacja currentIndex
                return;
            }
        }

        if (keyCode == KeyCode.BACK_SPACE && currentIndex >= 1) {
            if (character.equals(" ")) {
                currentIndex--;
                return;
            } else if (textNode.getFill().equals(Color.ORANGE)) {
                textFlow.getChildren().remove(currentIndex);
                currentIndex--;
                return;
            } else {
                textNode.setFill(Color.GRAY);
                currentIndex--;
            };

        } else if (keyCode.isLetterKey()) {
            currentIndex++;
        } else {
            textNode.setFill(Color.GRAY);
            return;
        }

        if (inputCharToString.equals(character) && !textNode.getFill().equals(Color.ORANGE)) {
            textNode.setFill(Color.BLUE);
        } else if (inputCharToString.equals(nextCharacter) && !textNode.getFill().equals(Color.ORANGE)) {
            textNode.setFill(Color.BLACK);
            textNodeOfNextCharacter.setFill(Color.BLUE);
        } else if (keyCode.isLetterKey() && !inputCharToString.equals(character) && !textNode.getFill().equals(Color.ORANGE)) {
            textNode.setFill(Color.RED);
        } else {
            return;
        }

        vBox.getChildren().clear();
        vBox.getChildren().add(textFlow);
        if (countTypedWords()>0) {
            wordsPerMinuteCurrent = new DecimalFormat("#.00").format(countTypedWords()  * 60 / ((System.currentTimeMillis() - this.appStart)/1000));
        }else{
            wordsPerMinuteCurrent = "0";
        }
        wordCountText.setText("Words typed: " + wordsPerMinuteCurrent);
        hBox.getChildren().set((hBox.getChildren().size() - 1), wordCountText);
    }

public int countTypedWords() {
    int wordCount = 0;
    int wordLengthIndex = 0;
    int lettersCount = 0;

    for (Node node : textFlow.getChildren()) {
        if (node instanceof Text) {
            Text textNode = (Text) node;
            String character = textNode.getText();

            if (!character.equals(" ")) {
                if (textNode.getFill() != Color.ORANGE && textNode.getFill() != Color.GRAY) {
                    lettersCount++;}
            } else {
                if (wordLengthIndex < listOfLettersOfWords.size()) {
                    int wordLength = listOfLettersOfWords.get(wordLengthIndex);
                    if (lettersCount == wordLength) {
                        wordCount++;
                    }
                    wordLengthIndex++;
                    lettersCount = 0;
                }
            }
        }
    }
    System.out.println("current wordCount is + " + wordCount);
    return wordCount;
}

    void jumpText() {
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < this.textFlow.getChildren().size(); i++) {
            Text text = (Text) this.textFlow.getChildren().get(i);
            animateJump(text, i, sequentialTransition);
        }

        sequentialTransition.play();
    }

    private void animateJump(Text text, int index, SequentialTransition sequentialTransition) {
        TranslateTransition jumpTransition = new TranslateTransition(JUMP_DURATION, text);
        jumpTransition.setByY(-JUMP_HEIGHT);
        jumpTransition.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition fallTransition = new TranslateTransition(JUMP_DURATION, text);
        fallTransition.setByY(JUMP_HEIGHT);
        fallTransition.setInterpolator(Interpolator.EASE_IN);

        TranslateTransition initialPositionTransition = new TranslateTransition(Duration.ZERO, text);
        initialPositionTransition.setToY(0);

        SequentialTransition letterTransition = new SequentialTransition(
                new PauseTransition(Duration.seconds( DELAY)),
                jumpTransition,
                fallTransition
        );

        sequentialTransition.getChildren().add(letterTransition);
    }


    public void shortCutsHandler(KeyEvent event,Rectangle overlay) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.TAB && keyCode == KeyCode.ENTER) {
//            restartTest();
            }
         if (keyCode == KeyCode.CONTROL && keyCode == KeyCode.SHIFT && keyCode == KeyCode.P) {
                // Show PAUSE window
             overlay.setVisible(true);
             System.out.println("Pausing application");
//            showPauseWindow();
            }
         if (keyCode == KeyCode.ESCAPE) {
            // Exit the app
             System.out.println("Exiting application");
             System.exit(0);
//            exitApp();
        }
    }
}
