package typeapp.typeapp;


import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class Controller {
    private final double appStart;
    private final HBox hBox;
    private final VBox vBox;
    private final ChoiceBox<String> languageChoiceBox;
    private final Random random;
    private  TextFlow textFlow;
    private int selectedTime;
    WordPerMinuteOperations wordPerMinuteOperations;
    Timeline timelineForWordPerMinute = new Timeline();
    Text wordCountText = new Text();
    List<String> randomWords;
    private List<Integer> listOfLettersOfWords;
    ChoiceBox<Integer> timeChoiceBox;
    private boolean editingEnabled;
    private Label countdownLabel;
    private Timeline countdownTimeline;
    private BorderPane borderPane;
    Timeline timelineForTextJumping = new Timeline();
    Statistics statistics;
    TextJumper textJumper;
    public boolean isEditingEnabled() {
        return !editingEnabled;
    }

    public Controller(ChoiceBox<Integer> timeChoiceBox, ChoiceBox<String> languageChoiceBox, VBox vBox, HBox hBox,boolean editingEnabled, Label countdown, Timeline countdownTimeline) {
        this.timeChoiceBox = timeChoiceBox;
        this.languageChoiceBox = languageChoiceBox;
        this.textFlow = new TextFlow();
        this.random = new Random();
        this.vBox = vBox;
        this.hBox = hBox;
        this.listOfLettersOfWords = new ArrayList<>();
        this.appStart = System.currentTimeMillis();
        this.countdownLabel = countdown;
        this.editingEnabled = editingEnabled;
        this.countdownTimeline = countdownTimeline;
        this.statistics = new Statistics(this.textFlow);
        this.textJumper = new TextJumper(this.textFlow);
    }
    public Timeline getCountdownTimeline() {
        return countdownTimeline;
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
            vBox.getChildren().clear();
            vBox.getChildren().add(textFlow);
            wordCountText.setText("Current WPM: " + 0);
            if (!hBox.getChildren().contains(wordCountText)) {
                hBox.getChildren().add(wordCountText);
            }else {
                hBox.getChildren().set(hBox.getChildren().size()-1, wordCountText);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }}



    public void startCountdown(int seconds) {
        this.selectedTime = seconds;
        this.wordPerMinuteOperations = new WordPerMinuteOperations(getListOfLetterOfWords(), getAppStart(), this.hBox, getwordCountText(),this.selectedTime,  this.textFlow);

        textJumper.jumpText();
        KeyFrame textJumpingKeyFrame = new KeyFrame(Duration.seconds(10), event -> textJumper.jumpText());
        timelineForTextJumping.getKeyFrames().add(textJumpingKeyFrame);
        timelineForTextJumping.setDelay(Duration.seconds(0)); // Opóźnienie przed pierwszym wykonaniem
        timelineForTextJumping.setCycleCount(Timeline.INDEFINITE); // Wykonywanie w nieskończoność
        timelineForTextJumping.play();


        KeyFrame wordPerMinuteKeyFrame = new KeyFrame(Duration.seconds(1), event -> {
            wordPerMinuteOperations.countWordPerMinute();
            wordPerMinuteOperations.countWordPerMinuteAverage();
            wordPerMinuteOperations.getDataforWPMGraph();
        });
        timelineForWordPerMinute.getKeyFrames().add(wordPerMinuteKeyFrame);
        timelineForWordPerMinute.setCycleCount(Timeline.INDEFINITE); // Execute indefinitely

        timelineForWordPerMinute.play(); // Start the timeline


        final int[] remainingSeconds = {seconds};

        countdownLabel.setText(Integer.toString(remainingSeconds[0]));

        if (countdownTimeline != null && countdownTimeline.getStatus() == Animation.Status.RUNNING) {
            countdownTimeline.stop(); // Stop the previous timeline if it's running
        }

        countdownTimeline = new Timeline();
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            remainingSeconds[0]--;
            countdownLabel.setText(Integer.toString(remainingSeconds[0]));
            // end of the game ?
            if (remainingSeconds[0] <= 0) {
                countdownTimeline.stop();
                timelineForTextJumping.stop();
                timelineForWordPerMinute.stop();
                editingEnabled = false;

                setBorderPaneWithGraph();

            }
        });
        countdownTimeline.getKeyFrames().add(keyFrame);
        countdownTimeline.play();
        countdownTimeline.setOnFinished(event -> {
            editingEnabled = false;
            disableTextFlowEditing();
        });
        countdownTimeline.play();
        editingEnabled = true; // Enable editing when the countdown starts
    }
    public void disableTextFlowEditing() {
        for (Node node : textFlow.getChildren()) {
            node.setMouseTransparent(true); // Disable mouse interaction with nodes
            node.setFocusTraversable(false); // Disable keyboard focus on nodes
        }
    }

    private void setBorderPaneWithGraph() {
        Label averageWPMLabel = new Label("  Average WPM");
        averageWPMLabel.setStyle("-fx-font-weight: bold;");
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Label averageWPMValueLabel = new Label(decimalFormat.format(wordPerMinuteOperations.getWordsPerMinuteAverage()));
        Label accuracyLabel = new Label("Accuracy");
        accuracyLabel.setStyle("-fx-font-weight: bold;");
        statistics.countLettersForStatistics();
        Label accuracyValueLabel = new Label(decimalFormat.format(statistics.calculateAndReturnAccuracy()) + " %");
        Label statisticsLabeltext = new Label("    correct/incorrect/extra/missed");
        statisticsLabeltext.setStyle("-fx-font-weight: bold;");
        Label statisticsLabelvalues = new Label(statistics.getCorrectLetters().toString() + "/"  + statistics.getIncorrectLetters().toString() + "/"  + statistics.getExtraLetters().toString() + "/"  + statistics.getMissedLetters().toString());

        VBox newLeftVBox = new VBox(10);
        newLeftVBox.getChildren().addAll(averageWPMLabel, averageWPMValueLabel, accuracyLabel, accuracyValueLabel,statisticsLabeltext,statisticsLabelvalues);

        borderPane.getChildren().clear();
        borderPane.setCenter( wordPerMinuteOperations.drawWMPGraph());
        newLeftVBox.setAlignment(Pos.CENTER);
        borderPane.setLeft(newLeftVBox);
        borderPane.setBottom(null);
    }


    public double getAppStart() {
        return appStart;
    }
    public Text getwordCountText() {
        return wordCountText;
    }
    public List<Integer> getListOfLetterOfWords() {
        return  listOfLettersOfWords;
    }
    public void addBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public TextFlow gettextFlow() {
        return textFlow;
    }

    public Timeline getTimelineForTextJumping() {
        return timelineForTextJumping;
    }

    public Timeline getTimelineForWordPerMinute() {
        return timelineForWordPerMinute;
    }
}
