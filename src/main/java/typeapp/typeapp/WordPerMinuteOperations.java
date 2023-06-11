package typeapp.typeapp;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.text.DecimalFormat;
import java.util.List;

public class WordPerMinuteOperations{

    private final int selectedTime;
    private final TextFlow textFlow;
    private double appStart;
    private HBox hBox;
    Text wordCountText;
    private List<Integer> listOfLettersOfWords;
    private double wordsPerMinuteCurrent;
    private double wordsPerMinuteAverage;
    XYChart.Series<Number, Number> series;
    int elapsedTime = 0;

    WordPerMinuteOperations(List<Integer> listOfLettersOfWords, double appStart, HBox hBox, Text wordCountText, int selectedTime, TextFlow textFlow){
        this.appStart = appStart;
        this.hBox = hBox;
        this.wordCountText = wordCountText;
        this.selectedTime = selectedTime;
        this.textFlow = textFlow;
        this.series = new XYChart.Series<>();
        this.listOfLettersOfWords = listOfLettersOfWords;

    }

     void getDataforWPMGraph() {
                    series.getData().add(new XYChart.Data<>(elapsedTime,Double.valueOf(wordsPerMinuteCurrent)));
                    this.elapsedTime+=5;
    }
     void drawWMPGraph() {
        // Oś X (czas)
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        // Oś Y (słowa na minutę)
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("WPM");

        // Wykres liniowy
         final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
         lineChart.setTitle("Average WPM in this game");

        // Dodanie serii danych do wykresu
        lineChart.getData().add(series);

    }

    void countWordPerMinuteAverage(){
        wordsPerMinuteAverage +=wordsPerMinuteCurrent;
    }

    void countWordPerMinute() {
        int wordCount = 0;
        int wordLengthIndex = 0;
        int lettersCount = 0;

        for (Node node : this.textFlow.getChildren()) {
            if (node instanceof Text) {
                Text textNode = (Text) node;
                String character = textNode.getText();

                if (!character.equals(" ")) {
                    if (textNode.getFill() != Color.ORANGE && textNode.getFill() != Color.GRAY) {
                        lettersCount++;
                        System.out.println("Letter - lettersCount is now " + lettersCount + "with wordLengthIndex = " + wordLengthIndex);
                    }
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
            if (wordCount >0) {
                this.wordsPerMinuteCurrent = wordCount  * 60 / ((System.currentTimeMillis() - this.appStart)/1000);
            }else{
                this.wordsPerMinuteCurrent = (double)0;
            }
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Format z dwoma miejscami po przecinku
        String formattedWPM = decimalFormat.format(wordsPerMinuteCurrent);
        this.wordCountText.setText("Current WPM: " + formattedWPM);
        this.hBox.getChildren().set((hBox.getChildren().size() - 1), wordCountText);
        }

    private double getWordsPerMinuteAverage() {
        return wordsPerMinuteAverage/selectedTime;
    }

}








