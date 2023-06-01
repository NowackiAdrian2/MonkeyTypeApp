package typeapp.typeapp;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Controller {
    private ChoiceBox<String> languageChoiceBox;
    private TextArea textArea;
    private Random random;
    String content;
    TextFlow textFlow = new TextFlow();
    int currentIndex;

    public Controller(ChoiceBox<String> languageChoiceBox, TextArea textArea) {
        this.languageChoiceBox = languageChoiceBox;
        this.textArea = textArea;
        this.random = new Random();
    }


//        public void displaySelectedTextFromFile() {
//        String selectedLanguage = languageChoiceBox.getValue();
//        String fileName = selectedLanguage + ".txt";
//        File selectedFile = new File("dictionary/" + fileName);
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
//            List<String> words = new ArrayList<>();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                words.add(line);
//            }
//
//            Collections.shuffle(words, random);
//            int numWordsToShow = Math.min(words.size(), 30);
//            List<String> randomWords = words.subList(0, numWordsToShow);
//
//            StringJoiner joiner = new StringJoiner(" ");
//            for (String word : randomWords) {
//                joiner.add(word);
//            }
//             this.content = joiner.toString();
//            textArea.setText(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void displaySelectedTextFromFile() {
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

            textFlow.getChildren().clear(); // Clear previous content
            Platform.runLater(() -> textArea.requestFocus());

            for (String word : randomWords) {
                for (char c : word.toCharArray()) {
                    Text textNode = new Text(String.valueOf(c));
                    textFlow.getChildren().add(textNode);
                }
                textFlow.getChildren().add(new Text(" ")); // Add space between words
            }

            setTextFlowToString();
            // Set the content as text in the TextArea
            textArea.clear();
            textArea.setText(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public void setTextFlowToString() {
    StringBuilder stringBuilder = new StringBuilder();
            for (Node childNode : textFlow.getChildren()) {
        if (childNode instanceof Text) {
            Text textNode = (Text) childNode;
            stringBuilder.append(textNode.getText());
        }
    }
    this.content = stringBuilder.toString();}

/*    private void handleKeyPress(KeyEvent event) {
            KeyCode keyCode = event.getCode();

            if (currentIndex > 0) {
            if (keyCode == KeyCode.BACK_SPACE) {
                    currentIndex--;
            } else if (keyCode.isLetterKey()) {
                currentIndex++;
            }else {
                return;
            }} else{
                currentIndex = 0;
            }

            char inputChar = event.getCharacter().charAt(0);
            if (currentIndex < textFlow.getChildren().size()) {
                    Text textNode = (Text) textFlow.getChildren().get(currentIndex);
                    char targetChar = textNode.getText().charAt(0);

                    if (inputChar == targetChar) {
                        // Correctly entered character, color it green
                        textNode.setFill(Color.GREEN);
                        currentIndex++;
                        updateTextFlow();
                    } else {
                        // Incorrectly entered character, color it red
                        textNode.setFill(Color.RED);
                    }
                }
            }
        }*/

    }









    // wroc do tego pozniej

    /*    public void initialize() {
        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.P) {
                    // Ctrl + Shift + P - Pause the game
                    pauseGame();
                } else if (event.getCode() == KeyCode.ENTER) {
                    if (event.isShortcutDown()) {
                        // Tab + Enter - Restart the game
                        restartGame();
                    } else {
                        // Handle other Enter key events
                        // ...
                    }
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    // Esc - End the game
                    endGame();
                }
            }
        });
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
    }*/
