package typeapp.typeapp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


public class KeyEventsListener {
    MonkeytypeApp monkeytypeApp;
    boolean editingEnabled;
    HBox hBox;
    private TextFlow textFlow;
    private int currentIndex;
    VBox vBox;
    private boolean shortcutEnabled;
    Controller controller;
    private boolean isTabPressed = false;
    private boolean isEnterPressed = false;

    KeyEventsListener(Controller controller, HBox topHBOX, VBox textAreaContainer){
        this.monkeytypeApp = new MonkeytypeApp();
        this.controller = controller;
        this.hBox = topHBOX;
        this.vBox = textAreaContainer;
        this.textFlow = controller.gettextFlow();
    }

    public void setShortcutEnabled(boolean shortcutEnabled) {
        this.shortcutEnabled = shortcutEnabled;
    }

    public void handleKeyPress(KeyEvent event) {
        if (controller.isEditingEnabled()) {
            return; // Don't process key events if editing is not enabled
        } else {
            hBox.requestFocus();
        }

        KeyCode keyCode = event.getCode();
        Text textNode = (Text) textFlow.getChildren().get(currentIndex);
        String character = textNode.getText();
        Text textNodeOfNextCharacter = (Text) textFlow.getChildren().get(currentIndex + 1);
        String nextCharacter = textNodeOfNextCharacter.getText();

        String inputCharToString = event.getText();

        if (character.equals(" ")) {
            if (keyCode.equals(KeyCode.SPACE)) {
                currentIndex++;
                return;
            } else if (keyCode.isLetterKey()) {
                Text redundantLetter = new Text(inputCharToString);
                redundantLetter.setFill(Color.ORANGE);
                redundantLetter.setFont(Font.font(20));
                textFlow.getChildren().add(currentIndex, redundantLetter);
                currentIndex++;
                return;
            }
        }
        // To trzeba dać do controllera
        if (isLastLetterOfLastWord()) {
            generateNewTextFlowAfterReachingEnd();
            return;
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
            }

        } else if (keyCode.isLetterKey()) {
            currentIndex++;
        } else {
            textNode.setFill(Color.GRAY);
            return;
        }

        if (inputCharToString.equals(character) && !textNode.getFill().equals(Color.ORANGE)) {
            textNode.setFill(Color.GREEN);
        } else if (inputCharToString.equals(nextCharacter) && !textNode.getFill().equals(Color.ORANGE)) {
            textNode.setFill(Color.BLACK);
            textNodeOfNextCharacter.setFill(Color.GREEN);
        } else if (keyCode.isLetterKey() && !inputCharToString.equals(character) && !textNode.getFill().equals(Color.ORANGE)) {
            textNode.setFill(Color.RED);
        } else {
            return;
        }
        vBox.getChildren().clear();
        vBox.getChildren().add(textFlow);


    }

    void generateNewTextFlowAfterReachingEnd(){
        controller.displaySelectedTextFromFile();
        currentIndex = 0;
    }

    public void handleShortcut(KeyEvent event) {
        if (controller.isEditingEnabled()) {
            System.out.println("handleShortcut");
            return;}

            if (event.getCode() == KeyCode.TAB) {
                 isTabPressed = true;
            } else if (event.getCode() == KeyCode.ENTER) {
                 isEnterPressed = true;
            }

        if (isTabPressed && isEnterPressed) {
            controller.getTimelineForTextJumping().stop();
            controller.getTimelineForWordPerMinute().stop();
            controller.getCountdownTimeline().stop();
            monkeytypeApp.start(new Stage());
            isTabPressed = false;
            isEnterPressed = false;
        } else if (event.getCode() == KeyCode.P && event.isControlDown() && event.isShiftDown()) {
            event.consume();
            controller.getTimelineForTextJumping().pause();
            controller.getTimelineForWordPerMinute().pause();
            controller.getCountdownTimeline().pause();
            // Logic to set up a window and continue after pressing a button
            Stage pauseStage = new Stage();
            VBox pauseRoot = new VBox();
            pauseRoot.setAlignment(Pos.CENTER);
            Text messageText = new Text("Application paused.\nClick the button to continue.\n");
            Button continueButton = new Button("Continue");
            continueButton.setAlignment(Pos.CENTER);
            pauseRoot.getChildren().addAll(messageText,continueButton);
            Scene pauseScene = new Scene(pauseRoot, 200, 100);
            pauseStage.setScene(pauseScene);
            pauseStage.show();
            continueButton.setOnAction(e -> {
                pauseStage.close();
                controller.getTimelineForTextJumping().play();
                controller.getTimelineForWordPerMinute().play();
                controller.getCountdownTimeline().play();
                // Resume the application
            });
            } else if (event.getCode() == KeyCode.ESCAPE) {
            monkeytypeApp.exitApp();
        }
    }


    private boolean isLastLetterOfLastWord() {
        return currentIndex >= textFlow.getChildren().size() - 1;
    }

}
