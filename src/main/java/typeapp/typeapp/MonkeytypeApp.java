package typeapp.typeapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

public class MonkeytypeApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("typeApp");

        // Create the choice boxes
        ChoiceBox<Integer> timeChoiceBox = new ChoiceBox<>();
        timeChoiceBox.getItems().addAll(15, 20, 45, 60, 90, 120, 300);
        timeChoiceBox.setValue(60);

        // Create the start button
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-border-color: red; -fx-border-width: 1px; -fx-border-radius: 5px;");
//        startButton.setBackground(Background.EMPTY);

        // Create the text area
        TextArea textArea = new TextArea();
        textArea.setFont(Font.font("Arial", 14));
        textArea.setWrapText(true);
        textArea.positionCaret(0);
        textArea.requestFocus( );
        textArea.setMouseTransparent(true);


        // Choice box for language
        ChoiceBox<String> languageChoiceBox = new ChoiceBox<>();
        // Access the directory and retrieve the text file names
        File dictionaryDirectory = new File("dictionary");
        File[] files = dictionaryDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String languageName = fileName.substring(0, fileName.lastIndexOf('.'));
                    languageChoiceBox.getItems().add(languageName);                }
            }
        }
        // Create an instance of the Controller class
        Controller controller = new Controller(languageChoiceBox, textArea);
        // Add a listener to the languageChoiceBox to trigger the text display
        languageChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> controller.displaySelectedTextFromFile());
        languageChoiceBox.setValue("english");

        // Create the scroll panes
        ScrollPane timeScrollPane = new ScrollPane();
        ScrollPane languageScrollPane = new ScrollPane();

        // Set scroll panes properties
        timeScrollPane.setPrefViewportWidth(150);
        timeScrollPane.setPrefViewportHeight(150);
        languageScrollPane.setPrefViewportWidth(150);
        languageScrollPane.setPrefViewportHeight(150);

        // Create the footer
        Label footerLabel = new Label("Keyboard Shortcuts:");
        Label restartLabel = createButtonLabel("Tab + Enter - Restart Test");
        Label pauseLabel = createButtonLabel("Ctrl + Shift + P - Pause");
        Label endLabel = createButtonLabel("Esc - End Test");

        // Create the footer VBox
        VBox footerVBox = new VBox(5, footerLabel, restartLabel, pauseLabel, endLabel);
        footerVBox.setAlignment(Pos.CENTER);

        // text box
        VBox textAreaContainer = new VBox();
        textAreaContainer.getChildren().add(textArea);
        textAreaContainer.setAlignment(Pos.CENTER);

        // Create the top area with choice boxes
        HBox topHBox = new HBox(10, languageChoiceBox, timeChoiceBox);
        topHBox.setAlignment(Pos.CENTER);

        // Create the border pane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topHBox);
        borderPane.setCenter(textAreaContainer);
        borderPane.setBottom(footerVBox);

        // Create the scene
        Scene scene = new Scene(borderPane, 700, 500);

        // Set the scene
        primaryStage.setScene(scene);
        primaryStage.show();
 //       controller.startTypingPractice();
    }

    private Label createButtonLabel(String labelText) {
        Label label = new Label(labelText);
        label.setPadding(new Insets(5));
        label.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 4; -fx-font-weight: bold;");
        return label;
    }
}
