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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Mark J Koch [popgirl at maehem dot com]
 */
public class MenuButton extends StackPane {

    private static final double FONT_SIZE = 26;
    private final DetailScreen screen;
    
//    private final String buttonStyle=
////            "#bevel-grey {\n" +
//            "    -fx-border-color: #aa44aa; -fx-border-width: 5px;\n" +
//            "    -fx-background-color:  #ff77FF;\n" +
//            "    -fx-background-radius: 0,0,0;\n" +
//            "    -fx-background-insets: 3,4,5;\n" +
//            "    -fx-text-fill: #772277;\n" +
//            "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.1) , 35, 0.0 , 0 , 1 );\n" ;//+
////            "}";
    private final MainScreen mainScreen;
    private final boolean rightSide;

    public MenuButton( String imagePath, DetailScreen screen, MainScreen mainScreen, boolean rightSide ) {
        this.screen = screen;
        this.mainScreen = mainScreen;
        this.rightSide = rightSide;
        getChildren().add(new ImageView(new Image(getClass().getResourceAsStream(imagePath))));
        //setFont(new Font(FONT_SIZE));
        //setText(text);
        //setStyle(buttonStyle);
        
        setOnMouseClicked((event) -> {
            System.out.println(this.getClass().getSimpleName() + " clicked.");
            screen.show();
            mainScreen.getStatusPane().detailMode(rightSide?-200:200);
            mainScreen.getTimeline().pause();
            event.consume();
        });
    }

    /**
     * @return the rightSide
     */
    public boolean isRightSide() {
        return rightSide;
    }
    
    
    
}
