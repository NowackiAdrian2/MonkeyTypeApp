package typeapp.typeapp;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
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

    public Controller(ChoiceBox<String> languageChoiceBox, VBox vBox) {
        this.languageChoiceBox = languageChoiceBox;
        this.textFlow = new TextFlow();
        this.random = new Random();
        this.vBox = vBox;
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
        }
    }



public void handleKeyPress(KeyEvent event) {
            KeyCode keyCode = event.getCode();
    System.out.println("current key: " + keyCode);


    System.out.println("current index " + currentIndex);
            //event casting to String
            String  inputCharToString = String.valueOf(keyCode).toLowerCase();

            //node from text flow casting to String
            Node node = textFlow.getChildren().get(currentIndex);
            Text textNode = (Text) node;
            String character = textNode.getText();


            //node from text flow casting to String
            Node nodeOfNextCharacter = textFlow.getChildren().get(currentIndex+1);
            Text textNodeOfNextCharactere = (Text) nodeOfNextCharacter;
            String characterNodeOfNextCharactere  = textNodeOfNextCharactere.getText();

            System.out.println("our string in the textFlow is " + character + " and our placed event string is " + inputCharToString);

            if (inputCharToString.equals(character)){
                textNode.setFill(Color.BLUE);

            } else if (!(inputCharToString.equals(character)&& !inputCharToString.equals(characterNodeOfNextCharactere))) {
                textNode.setFill(Color.RED);

            } else if (inputCharToString.equals(" ")) {
               Text textAdditinalChar = new Text(inputCharToString);
               textAdditinalChar.setFill(Color.ORANGE);

            } else if (!inputCharToString.equals(character) && inputCharToString.equals(characterNodeOfNextCharactere)) {
                textNode.setFill(Color.BLACK);
                textNodeOfNextCharactere.setFill(Color.BLUE);
//
            }else {
                textNode.setFill(Color.GRAY);
            }
            vBox.getChildren().clear();
            vBox.getChildren().add(textFlow);

    if (keyCode == KeyCode.BACK_SPACE && currentIndex> 0) {
        currentIndex--;
    } else if (keyCode.isLetterKey()) {
        currentIndex++;
    }else {
        currentIndex = 0;
    }


    }
//}

//    public void handleKeyPress(KeyEvent event) {
//        KeyCode keyCode = event.getCode();
//
//        if (currentIndex > 0) {
//            if (keyCode == KeyCode.BACK_SPACE) {
//                currentIndex--;
//            } else if (keyCode.isLetterKey()) {
//                currentIndex++;
//            } else {
//                return;
//            }
//        } else {
//            currentIndex = 0;
//        }
//
//        // Get the Text node at the current index
//        Node node = textFlow.getChildren().get(currentIndex);
//        if (node instanceof Text) {
//            Text textNode = (Text) node;
//            String character = textNode.getText();
//            String inputChar = event.getCharacter();
//
//            // Compare the input character with the character at the current index
//            if (inputChar.equals(character)) {
//                textNode.setFill(Color.GREEN);
//            } else {
//                textNode.setFill(Color.RED);
//            }
//        }
//
//        // Build the updated content with color highlighting using StringBuilder
//        StringBuilder updatedContent = new StringBuilder();
//        for (Node childNode : textFlow.getChildren()) {
//            if (childNode instanceof Text) {
//                Text textNode = (Text) childNode;
//                String character = textNode.getText();
//
//                // Set the color based on the fill value
//                String color = "";
//                if (textNode.getFill() == Color.GREEN) {
//                    color = "green";
//                } else if (textNode.getFill() == Color.RED) {
//                    color = "red";
//                }
//
//                // Append the character with color styling
//                updatedContent.append("<span style=\"-fx-fill:").append(color).append(";\">").append(character).append("</span>");
//            }
//        }
//
//        // Set the updated content in the TextArea
//        textArea.clear();
//        textArea.setText(updatedContent.toString());
//    }
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
