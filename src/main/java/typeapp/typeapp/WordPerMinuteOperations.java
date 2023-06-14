package typeapp.typeapp;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordPerMinuteOperations extends Region {

    private final int selectedTime;
    private final TextFlow textFlow;
    private double appStart;
    private HBox hBox;
    Text wordCountText;
    private List<Integer> listOfLettersOfWords;
    private double wordsPerMinuteCurrent;
    private double wordsPerMinuteAverage;
    private XYChart.Series<Number, Number> series;
    int elapsedTime = 0;
    Map<String, Double> word_WPM = new HashMap<>();
    private StringBuilder wordToPut = new StringBuilder();
    double currentTimeToWriteWord_WPM;

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
                    series.getData().add(new XYChart.Data<>(elapsedTime,wordsPerMinuteCurrent));
                    this.elapsedTime++;
    }
    protected LineChart drawWMPGraph() {
        // Oś X (czas)
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(selectedTime);


        // Oś Y (słowa na minutę)
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("WPM");


        // Wykres liniowy
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
         lineChart.setTitle("Average WPM in this game");

        // Dodanie serii danych do wykresu
        lineChart.getData().add(series);

        return lineChart;
    }

    void countWordPerMinuteAverage(){
        wordsPerMinuteAverage +=wordsPerMinuteCurrent;
    }

    void countWordPerMinute() {
         int wordCount = 0;
         int wordLengthIndex = 0;
         int lettersCount = 0;

        for (Node node : this.textFlow.getChildren()) {
            if (node instanceof Text textNode) {
                String character = textNode.getText();

                if (!character.equals(" ")) {
                    if (textNode.getFill() != Color.ORANGE && textNode.getFill() != Color.GRAY) {
                        wordToPut.append(textNode);
                        lettersCount++;
                    }
                } else {
                    if (wordLengthIndex < listOfLettersOfWords.size()) {
                        int wordLength = listOfLettersOfWords.get(wordLengthIndex);
                        if (lettersCount == wordLength) {
                            wordCount++;
                            if (word_WPM.isEmpty()){
                                currentTimeToWriteWord_WPM = System.currentTimeMillis() - this.appStart;
                            }else {
                                currentTimeToWriteWord_WPM = System.currentTimeMillis() - currentTimeToWriteWord_WPM;
                            }
                            word_WPM.put(wordToPut.toString(), 60 / (currentTimeToWriteWord_WPM)/1000);
                            wordToPut  = new StringBuilder();
                        }
                        wordLengthIndex++;
                        lettersCount = 0;
                    }
                }
            }
        }
        if (wordCount >0) {
            this.wordsPerMinuteCurrent = (wordCount  * 60 / ((System.currentTimeMillis() - this.appStart)/1000));
        }else{
            this.wordsPerMinuteCurrent = 0;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedWPM = decimalFormat.format(wordsPerMinuteCurrent);
        this.wordCountText.setText("Current WPM: " + formattedWPM);
        this.hBox.getChildren().set((hBox.getChildren().size() - 1), wordCountText);

        }

    double getWordsPerMinuteAverage() {
        System.out.println("wordsPerMinuteAverage/(double)selectedTime: " + wordsPerMinuteAverage/(double)selectedTime);
        return wordsPerMinuteAverage/(double)selectedTime;
    }

}








