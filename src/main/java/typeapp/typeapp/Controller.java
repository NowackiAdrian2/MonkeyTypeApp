package typeapp.typeapp;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller {
    private  VBox vBox;
    private ChoiceBox<String> languageChoiceBox;
    private Random random;
    private TextFlow textFlow;
    int currentIndex;
    int endOfTheWord;
    int begginningOfNextWord;
    private TextField textField;
    private Text caret = new Text("|");

    public Controller(ChoiceBox<String> languageChoiceBox, VBox vBox) {
        this.languageChoiceBox = languageChoiceBox;
        this.textFlow = new TextFlow();
        this.random = new Random();
        this.vBox = vBox;
        this.textField = new TextField();

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

            Collections.shuffle(words, random);
            int numWordsToShow = Math.min(words.size(), 30);
            List<String> randomWords = words.subList(0, numWordsToShow);


            for (String word : randomWords) {
                for (char c : word.toCharArray()) {
                    Text textNode = new Text(String.valueOf(c));
                    textNode.setFill(Color.GRAY);
                    textNode.setFont(Font.font(20));
                    textFlow.getChildren().add(textNode);
                }
                textFlow.getChildren().add(new Text(" ")); // Add space between words
            }

            vBox.getChildren().add(textFlow);

        } catch (IOException e) {
            e.printStackTrace();
        }}

    public void handleKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        System.out.println("Current index is: " + currentIndex);

        Text textNode = (Text) textFlow.getChildren().get(currentIndex);
        String character = textNode.getText();
        System.out.println("Current letter in the textFlow is: " + character);
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

        if (keyCode == KeyCode.BACK_SPACE && currentIndex > 1) {
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
            }
        } else if (keyCode.isLetterKey()) {
            currentIndex++;
        } else {
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
    }



    private void restartGame() {
        // Logic to restart the game
        System.out.println("Restarting the game...");
    }

    private void pauseGame() {
        // Logic to pause the game
        System.out.println("Pausing the game...");
    }

    private void endGame() {
        // Logic to end the game
        System.out.println("Ending the game...");
    }}
