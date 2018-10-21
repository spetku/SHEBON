/*
 * Copyright 2018 Sarah Petkus and Mark J Koch
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/
package popgirl;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Mark J Koch [popgirl at maehem dot com]
 */
public class DetailScreen extends VBox {

    private final double offsetX;   // Get rid of me!
    //private final double offsetY;
    private final MainScreen mainScreen;
    private final boolean rightSide;
    private boolean showing = false;
    private final Text value;

    public DetailScreen(String imagePath, double offsetX, MainScreen mainScreen, boolean rightSide) {
        this.offsetX = offsetX;
        //this.offsetY = offsetY;
        this.mainScreen = mainScreen;
        this.rightSide = rightSide;

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        value = new Text("9999.999");
        value.setFont(Font.font(60.0));
        value.setFill(new Color(1.0, 0.5, 0.8, 1.0));
        
        getChildren().addAll(image,value);
        setAlignment(rightSide?Pos.CENTER_LEFT:Pos.CENTER_RIGHT);
        //setTranslateX(rightSide ? 400 : -400);
        //setTranslateX(offsetX); // Move it out of the way so we don't trigger click events.
        setTranslateX(rightSide?800:-800);
        //setBorder(new Border(new BorderStroke(Color.BISQUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
        //setLayoutX(rightSide?400:-400);
        setOpacity(0);
        //setTranslateY(offsetY);
        //setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));

        setOnMouseClicked((event) -> {
            if ( !showing ) {
                return;
            }
            event.consume();
            hide();
            mainScreen.getTimeline().play();
            mainScreen.getStatusPane().centerMode();
        });
    }

    public final void show() {
        showing = true;
        System.out.println("popgirl.DetailScreen.show()");
        mainScreen.unSlideMenus();
        toFront();
//        TranslateTransition tt = new TranslateTransition(Duration.millis(600), this);
//        tt.setByX(-getTranslateX() / 2);
//        tt.setByY(-getTranslateY());
//        tt.setCycleCount(1);
//        //tt.setAutoReverse(true);
//        tt.play();
        setTranslateX(rightSide?getTranslateX()-400:getTranslateX()+400);
        
        FadeTransition ft = new FadeTransition(Duration.millis(1200), this);
        ft.setFromValue(this.getOpacity());
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        //ft.setAutoReverse(false);

        ft.play();

    }

    public final void hide() {
        showing = false;
        System.out.println("popgirl.DetailScreen.hide()");
        toBack();
        setTranslateX(rightSide?getTranslateX()+400:getTranslateX()-400);
        mainScreen.slideMenus();

//        TranslateTransition tt = new TranslateTransition(Duration.millis(600), this);
//        tt.setByX(offsetX - getTranslateX());
//        tt.setByY(offsetY - getTranslateY());
//        tt.setCycleCount(1);
//        //tt.setAutoReverse(true);
//
//        tt.play();
        FadeTransition ft = new FadeTransition(Duration.millis(1200), this);
        ft.setFromValue(this.getOpacity());
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        //ft.setAutoReverse(false);

        ft.play();
    }

    void setValue(String str) {
        value.setText(str);
    }
}
