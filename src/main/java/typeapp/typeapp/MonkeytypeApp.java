package typeapp.typeapp;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MonkeytypeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean countdownRunning = false;
    static Label countdownLabel;
    public static boolean editingEnabled = false;
    public static Timeline countdownTimeline; // Declare timeline as an instance variable
    WordPerMinuteOperations wordPerMinuteOperations;
    Timer timerforGettingDaraForWPMGraph = new Timer();
    Timer timerForWordPerMinute = new Timer();



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
        startButton.setBackground(Background.EMPTY);

        Border redBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
        startButton.setBorder(redBorder);

        // Creating the animation
        Duration blinkDuration = Duration.seconds(1);
        FadeTransition blinkTransition = new FadeTransition(blinkDuration, startButton);
        blinkTransition.setFromValue(0);
        blinkTransition.setToValue(1);
        blinkTransition.setCycleCount(Timeline.INDEFINITE);
        blinkTransition.setAutoReverse(true);

        // Starting the animation after 5 seconds
        PauseTransition initialDelay = new PauseTransition(Duration.seconds(5));
        initialDelay.setOnFinished(event -> blinkTransition.play());
        initialDelay.play();


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
                    languageChoiceBox.getItems().add(languageName);
                }
            }
        }

        // text box
        VBox textAreaContainer = new VBox();
        textAreaContainer.setAlignment(Pos.CENTER);
        textAreaContainer.setStyle("-fx-background-color: white;");
        textAreaContainer.setMouseTransparent(false);
        textAreaContainer.setFocusTraversable(true);
        ScrollPane scrollPaneVBOX = new ScrollPane(textAreaContainer);
        scrollPaneVBOX.setFitToWidth(true);
        scrollPaneVBOX.setFitToHeight(true);
        countdownLabel = new Label();

        // Create the top area with choice boxes and countdown label
        HBox topHBox = new HBox(10, languageChoiceBox, timeChoiceBox, countdownLabel, startButton);
        topHBox.setAlignment(Pos.CENTER);

        // Create an instance of the Controller class
        Controller controller = new Controller(timeChoiceBox, languageChoiceBox, textAreaContainer, topHBox, this);
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

        startButton.setOnAction(event -> {
            // Enable the shortcut when the Start button is pressed
            controller.setShortcutEnabled(true);
            if (!countdownRunning) { // Check if countdown is not already running
                countdownRunning = true; // Set the flag to true to indicate countdown is running
                int selectedTime = timeChoiceBox.getValue();
                this.wordPerMinuteOperations = new WordPerMinuteOperations(controller.getListOfLetterOfWords(), controller.getAppStart(), topHBox, controller.getwordCountText(), controller.getSelectedTime(), controller.getTextFlow());
                controller.startCountdown(selectedTime);

                timerForWordPerMinute.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            wordPerMinuteOperations.countWordPerMinute();
                            wordPerMinuteOperations.countWordPerMinuteAverage();
                        });
                    }
                }, 0, 1000);

                timerforGettingDaraForWPMGraph.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            wordPerMinuteOperations.getDataforWPMGraph();
                        });
                    }
                }, 0, 5000);
            }
        });

        // Create the footer
        Label footerLabel = new Label("Keyboard Shortcuts:");
        Label restartLabel = createButtonLabel("Tab + Enter - Restart Test");
        Label pauseLabel = createButtonLabel("Ctrl + Shift + P - Pause");
        Label endLabel = createButtonLabel("Esc - End Test");

        // Create the footer VBox
        VBox footerVBox = new VBox(5, footerLabel, restartLabel, pauseLabel, endLabel);
        footerVBox.setAlignment(Pos.CENTER);
        footerVBox.setOpacity(0);

        // Create the animation to fade in the footer VBox
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), footerVBox);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setDelay(Duration.seconds(2));
        // Play the animation
        fadeTransition.play();

        // Create the border pane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topHBox);
        borderPane.setCenter(scrollPaneVBOX);
        borderPane.setBottom(footerVBox);

        // Create the scene
        Scene scene = new Scene(borderPane, 700, 500);
        scene.setOnKeyPressed(controller::handleKeyPress);
        textAreaContainer.setOnKeyPressed(controller::handleShortcut);
        Rectangle overlay = new Rectangle(scene.getWidth(), scene.getHeight(), Color.rgb(0, 0, 0, 0.5));

        // Set the scene
        primaryStage.setScene(scene);
        primaryStage.show();

        Timer timerForTextJumping = new Timer();
        timerForTextJumping.schedule(new TimerTask() {
            @Override
            public void run() {
                controller.jumpText();
            }
        }, 0, 50000);

    }
    private Label createButtonLabel(String labelText) {
        Label label = new Label(labelText);
        label.setPadding(new Insets(5));
        label.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 4; -fx-font-weight: bold;");
        return label;
    }
}
