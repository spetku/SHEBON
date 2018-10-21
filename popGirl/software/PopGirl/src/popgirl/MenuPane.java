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

import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Mark J Koch [popgirl at maehem dot com]
 */
public class MenuPane extends VBox {

    private final double offset;

    public MenuPane(double offset, ArrayList<MenuButton> buttons) {
        this.offset = offset;
        
        this.setAlignment(Pos.CENTER);
        //this.relocate(offset, 0);
        this.setTranslateX(offset);
        this.setScaleX(0.9);
        this.setScaleY(0.9);
        this.setSpacing(16);
                
        //getChildren().addAll(b1, b2, b3, b4, b5, b6);
        getChildren().addAll(buttons);
    }

    void slideIn() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), this);
        tt.setByX(-offset*0.4);
        tt.setCycleCount(1);
        //tt.setAutoReverse(true);

        tt.play();
        
        System.out.println("popgirl.MenuPane.slideIn()");
    }

    void slideOut() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), this);
        tt.setByX(offset*0.4);
        tt.setCycleCount(1);
        //tt.setAutoReverse(true);

        tt.play();
        
        System.out.println("popgirl.MenuPane.slideOut()");
    }

}
