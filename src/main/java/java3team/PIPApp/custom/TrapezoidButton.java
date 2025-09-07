package custom;


import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TrapezoidButton extends Region {
    private final Polygon polygon = new Polygon();
    private final Label label = new Label();
    private final boolean isLeft;
    private EventHandler<ActionEvent> onAction;

    public TrapezoidButton(String text, boolean isLeft) {
        this.isLeft = isLeft;
        label.setText(text);
        label.setTextFill(Color.BLACK);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        getChildren().addAll(polygon, label);

        setPrefSize(120, 50);
        setStyle("-fx-cursor: hand;");
        polygon.setFill(isLeft ? Color.SALMON : Color.CORNFLOWERBLUE);

        setOnMouseClicked((MouseEvent e) -> {
            if (onAction != null) {
                onAction.handle(new ActionEvent(this, null));
            }
        });
    }

    public void setOnAction(EventHandler<ActionEvent> handler) {
        this.onAction = handler;
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        double slant = 20;

        if (isLeft) {
            polygon.getPoints().setAll(
                    0.0, 0.0,
                    w - slant, 0.0,
                    w, h,
                    0.0, h
            );
        } else {
            polygon.getPoints().setAll(
                    slant, 0.0,
                    w, 0.0,
                    w, h,
                    0.0, h
            );
        }

        label.autosize();
        label.relocate(
                (w - label.getWidth()) / 2,
                (h - label.getHeight()) / 2
        );
    }
}
