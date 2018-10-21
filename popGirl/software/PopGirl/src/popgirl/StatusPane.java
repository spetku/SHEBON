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

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author Mark J Koch [popgirl at maehem dot com]
 */
public class StatusPane extends StackPane {

    private final double DEFAULT_SCALE = 0.9;
    
    private boolean horizontal = true;
    
    private final ImageView body = new ImageView(new Image(getClass().getResourceAsStream("images/body/base.png")));
    private final ImageView buns = new ImageView(new Image(getClass().getResourceAsStream("images/body/buns.png")));
    private final ImageView head = new ImageView(new Image(getClass().getResourceAsStream("images/body/head.png")));
    private final ImageView larm = new ImageView(new Image(getClass().getResourceAsStream("images/body/larm.png")));
    private final ImageView lthigh = new ImageView(new Image(getClass().getResourceAsStream("images/body/lthigh.png")));
    private final ImageView neck = new ImageView(new Image(getClass().getResourceAsStream("images/body/neck.png")));
    private final ImageView nips = new ImageView(new Image(getClass().getResourceAsStream("images/body/nips.png")));
    private final ImageView pelvis = new ImageView(new Image(getClass().getResourceAsStream("images/body/pelvis.png")));
    private final ImageView rarm = new ImageView(new Image(getClass().getResourceAsStream("images/body/rarm.png")));
    private final ImageView rthigh = new ImageView(new Image(getClass().getResourceAsStream("images/body/rthigh.png")));

    public StatusPane() {
        setScaleX(DEFAULT_SCALE);
        setScaleY(DEFAULT_SCALE);
        
        // This is fine on a Mac/PC but really slow on RaspberryPi
        // And why is this in the construtor?
//        ParallelTransition parallelTransition = new ParallelTransition();
//        parallelTransition.getChildren().addAll(
//                getFade(buns, Duration.seconds(1.0)),
//                getFade(head, Duration.seconds(0.8)),
//                getFade(larm, Duration.seconds(2.4)),
//                getFade(lthigh, Duration.seconds(0.8)),
//                getFade(neck, Duration.seconds(1.5)),
//                getFade(nips, Duration.seconds(1.2)),
//                getFade(pelvis, Duration.seconds(0.6)),
//                getFade(rarm, Duration.seconds(4.8)),
//                getFade(rthigh, Duration.seconds(1.8))
//        );
                
        buns.setOpacity(0.0);
        head.setOpacity(0.0);
        neck.setOpacity(0.0);
        larm.setOpacity(0.0);
        rarm.setOpacity(0.0);
        pelvis.setOpacity(0.0);
        nips.setOpacity(0.0);
        lthigh.setOpacity(0.0);
        rthigh.setOpacity(0.0);
        
        getChildren().addAll(body, buns, head, larm, lthigh, neck, nips, pelvis, rarm, rthigh);        
        
//        parallelTransition.play();
    }

    public void setOrientation(boolean horizontal) {
        this.horizontal = horizontal;

        RotateTransition rotateTransition
                = new RotateTransition(Duration.millis(600), this);
        rotateTransition.setByAngle(horizontal?-90f:90f);

        ScaleTransition scaleTransition
                = new ScaleTransition(Duration.millis(600), this);
        scaleTransition.setToX(horizontal?DEFAULT_SCALE:0.6);
        scaleTransition.setToY(horizontal?DEFAULT_SCALE:0.6);

        
        
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                rotateTransition,
                scaleTransition
        );

        
        // Maybe blur is slowing down animations?
//        final Animation animation = new Transition() {
//            {
//                setCycleDuration(Duration.millis(600));
//            }
//
//            protected void interpolate(double frac) {
//                double blur = horizontal?1.0-BLUR*frac:BLUR*frac;
//                BoxBlur bb = new BoxBlur();
//                bb.setWidth(blur);
//                bb.setHeight(blur);
//                bb.setIterations(2);
//
//                setEffect(bb);
//            }
//
//        };

        parallelTransition.play();
 //       animation.play();

    }
    
    public boolean isHorizontal() {
        return horizontal;
    }

    public void detailMode(double x) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), this);
        tt.setByX(x);
        tt.setCycleCount(1);

        tt.play();        
    }
    
    public void centerMode() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), this);
        tt.setByX(-getTranslateX());
        tt.setCycleCount(1);

        tt.play();                
    }
    
    private FadeTransition getFade( ImageView image, Duration d) {
        FadeTransition fade = new FadeTransition(d, image);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setAutoReverse(true);
        fade.setCycleCount(Animation.INDEFINITE);

        return fade;
    }
    
    public void setNips(double val) {
        nips.setOpacity(val);
    }
}
