package typeapp.typeapp;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class TextJumper {
    private static final double DELAY  = 0.001;
    private final double JUMP_HEIGHT = 10;
    private  final Duration JUMP_DURATION = Duration.millis(100);
    TextFlow textFlow;

    TextJumper(TextFlow textFlow){
        this.textFlow= textFlow;
    }
    void jumpText() {
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < this.textFlow.getChildren().size(); i++) {
            Text text = (Text) this.textFlow.getChildren().get(i);
            animateJump(text, i, sequentialTransition);
        }
        sequentialTransition.play();
    }
    private void animateJump(Text text, int index, SequentialTransition sequentialTransition) {
        TranslateTransition jumpTransition = new TranslateTransition(JUMP_DURATION, text);
        jumpTransition.setByY(-JUMP_HEIGHT);
        jumpTransition.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition fallTransition = new TranslateTransition(JUMP_DURATION, text);
        fallTransition.setByY(JUMP_HEIGHT);
        fallTransition.setInterpolator(Interpolator.EASE_IN);

        TranslateTransition initialPositionTransition = new TranslateTransition(Duration.ZERO, text);
        initialPositionTransition.setToY(0);

        SequentialTransition letterTransition = new SequentialTransition(
                new PauseTransition(Duration.seconds( DELAY)),
                jumpTransition,
                fallTransition
        );

        sequentialTransition.getChildren().add(letterTransition);
    }
}
