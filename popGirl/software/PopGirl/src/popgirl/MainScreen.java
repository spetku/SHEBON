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

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Sideways indicator screen
 *
 * @author Mark J Koch [popgirl at maehem dot com]
 */
public class MainScreen extends StackPane {

    private MenuPane menuLeft;
    private MenuPane menuRight;
    private final StatusPane statusPane;
    private boolean touchlock = false;
    private Timeline timeline;
    private GPIO gpio = null;
    private Serial serial = null;
    private final DetailScreen dsHead;
    private final DetailScreen dsLArm;
    private final DetailScreen dsNips;
    private final DetailScreen dsBuns;
    private final DetailScreen dsLThigh;
    private final DetailScreen dsNeck;
    private final DetailScreen dsRArm;
    private final DetailScreen dsPelvis;
    private final DetailScreen dsRThigh;

    public MainScreen() {

        // No GPIO if this is not a Raspberry Pi.  Allows this program to run
        // on desktop systems (Mac, PC, Linux) for testing non-GPIO features.
        if ( System.getProperty("os.name").equals("Linux") && System.getProperty("os.arch").equals("arm") ) {
            gpio = new GPIO();
        } else {
            gpio = null;
        }

        Rectangle bg = new Rectangle(800, 480, new Color(0.53, 0.973, 0.72, 1.0));
        //Color color = new Color(0.53, 0.973, 0.72, 1.0);
        statusPane = new StatusPane();
        // Start a timer to unslide the menus.
        timeline = new Timeline(new KeyFrame(
                Duration.millis(10000),
                ae -> {
                    getTimeline().play(); // Play from start?
                    getStatusPane().setOrientation(!statusPane.isHorizontal());
                    unSlideMenus();
                }));
        timeline.setOnFinished((event) -> {
            touchlock = false;
        });
        //System.out.println("Color: " + Integer.toHexString(color.hashCode()));
        // Add statusPane

        dsHead = new DetailScreen("images/icon/head_facial.png", -800, this, false);
        dsLArm = new DetailScreen("images/icon/larm_sweat.png", -800, this, false);
        dsNips = new DetailScreen("images/icon/nips.png", -800, this, false);
        dsBuns = new DetailScreen("images/icon/buns_flex.png", -800, this, false);
        dsLThigh = new DetailScreen("images/icon/thighs_temp.png", -800, this, false);

        dsNeck = new DetailScreen("images/icon/neck_flir.png", 800, this, true);
        dsRArm = new DetailScreen("images/icon/larm_sweat.png", 800, this, true);
        dsPelvis = new DetailScreen("images/icon/pelvis_heartrate.png", 800, this, true);
        dsRThigh = new DetailScreen("images/icon/thighs_temp.png", 800, this, true);

        ArrayList<MenuButton> leftButtons = new ArrayList<>();
        leftButtons.add(new MenuButton("images/button/head.png", dsHead, this, false));
        leftButtons.add(new MenuButton("images/button/larm.png", dsLArm, this, false));
        leftButtons.add(new MenuButton("images/button/nips.png", dsNips, this, false));
        leftButtons.add(new MenuButton("images/button/buns.png", dsBuns, this, false));
        leftButtons.add(new MenuButton("images/button/lthigh.png", dsLThigh, this, false));

        ArrayList<MenuButton> rightButtons = new ArrayList<>();
        rightButtons.add(new MenuButton("images/button/neck.png", dsNeck, this, true));
        rightButtons.add(new MenuButton("images/button/rarm.png", dsRArm, this, true));
        rightButtons.add(new MenuButton("images/button/pelvis.png", dsPelvis, this, true));
        rightButtons.add(new MenuButton("images/button/rthigh.png", dsRThigh, this, true));

        menuLeft = new MenuPane(-500, leftButtons);
        menuRight = new MenuPane(500, rightButtons);

        // Touch the screen to bring in the menu buttons.
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isTouchlock() && getStatusPane().isHorizontal()) {
                    System.out.println("clicked and no touch lock.");
                    getStatusPane().setOrientation(false);

                    touchlock = true;   // Don't trigger while in a transition.
                    slideMenus();
                    // Play the countdown timer to make the menu stay
                    // displayed until the timer runs out.
                    getTimeline().play();
                    event.consume();
                }
            }
        });

        getChildren().addAll(bg, statusPane,
                menuLeft, menuRight,
                dsHead, dsLArm, dsNips, dsBuns, dsLThigh,
                dsNeck, dsRArm, dsPelvis, dsRThigh
        );

        statusPane.toFront();
        
        initSerial();
    }

    public void unSlideMenus() {
        menuLeft.slideOut();
        menuRight.slideOut();

    }

    public void slideMenus() {
        System.out.println("popgirl.MainScreen.slideMenus()");
        menuLeft.slideIn();
        menuRight.slideIn();
    }

    /**
     * @return the touchlock
     */
    public boolean isTouchlock() {
        return touchlock;
    }

    /**
     * @return the timeline
     */
    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * @param timeline the timeline to set
     */
    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    /**
     * @return the statusPane
     */
    public StatusPane getStatusPane() {
        return statusPane;
    }

    private void initSerial() {
        //System.out.println("OS Name: " + System.getProperty("os.name"));
        //System.out.println("OS Arch: " + System.getProperty("os.arch"));
        //System.out.println("OS Ver:  " + System.getProperty("os.version"));
        
        if ( !(System.getProperty("os.name").equals("Linux") && System.getProperty("os.arch").equals("arm")) ) {
            return;
        }
        
        serial = SerialFactory.createInstance();

        // create and register the serial data listener
        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                // NOTE! - It is extremely important to read the data received from the
                // serial port.  If it does not sensorValue read from the receive buffer, the
                // buffer will continue to grow and consume memory.
                // print out the data received to the console
                try {
                    ShortBuffer buffer = event.getByteBuffer().asShortBuffer();

                    if ( !(buffer.get() == 0x5342 && buffer.get() == 0x4450) ) {
                        return;
                    }
                    //System.out.println("Rem: " + buffer.remaining() );
                    //buffer.get();  // Previous sensorValue does not seem to increment index.
                    int sensorValue = buffer.get()&0xFFFF;
                    dsNips.setValue(String.valueOf(sensorValue));
                    statusPane.setNips(sensorValue/3600.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            // create serial config object
            SerialConfig config = new SerialConfig();

            // set default serial settings (device, baud rate, flow control, etc)
            //
            // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
            // NOTE: this utility method will determine the default serial port for the
            //       detected platform and board/model.  For all Raspberry Pi models
            //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
            //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
            //       environment configuration.
            //config.device(SerialPort.getDefaultPort(SystemInfo.BoardType.RaspberryPi_3B))
            config.device("/dev/ttyS0")
                    .baud(Baud._38400)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            serial.open(config);

        } catch (IOException ex) {
            //console.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
            return;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedBoardType ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stop() {
        if ( serial == null ) {
            return;
        }
        
        try {
            serial.flush();
            serial.close();
        } catch (IllegalStateException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
