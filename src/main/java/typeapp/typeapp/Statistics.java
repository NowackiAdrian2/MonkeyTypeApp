package typeapp.typeapp;

import javafx.scene.Node;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Statistics {
    TextFlow textFlow;
    Integer correctLetters;
    Integer incorrectLetters;
    Integer extraLetters;
    Integer missedLetters;
    Statistics(TextFlow textFlow){
        this.textFlow = textFlow;
        this.correctLetters = 0;
        this.incorrectLetters = 0;
        this.extraLetters = 0;
        this.missedLetters = 0;
    }
    public Integer getCorrectLetters() {
        return correctLetters;
    }

    public Integer getIncorrectLetters() {
        return incorrectLetters;
    }

    public Integer getExtraLetters() {
        return extraLetters;
    }

    public Integer getMissedLetters() {
        return missedLetters;
    }

    void countLettersForStatistics(){
        for (Node node : this.textFlow.getChildren()) {
            if (node instanceof Text textNode) {

                if (!textNode.getText().equals(" ")) {

                    String letterColor = textNode.getFill().toString();
                    switch (letterColor) {
                        case "0x008000ff" -> // Zielony
                                correctLetters += 1;
                        case "0xff0000ff" -> // Czerwony
                                incorrectLetters += 1;
                        case "0xffa500ff" -> // Pomarańczowy
                                extraLetters += 1;
                        case "0x000000ff" -> // Czarny
                                missedLetters += 1;
                    }

                    }}
                }}
double calculateAndReturnAccuracy(){
    int letterCount = correctLetters + incorrectLetters + extraLetters + missedLetters;
    return (double)(correctLetters * 100.0) / letterCount;

}
}

