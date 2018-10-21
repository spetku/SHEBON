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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 *
 * @author Mark J Koch [popgirl at maehem dot com]
 */
public class GPIO {

    private GpioPinDigitalInput button1;

    public static final Pin BUTTON_1 = RaspiPin.GPIO_26;  // TOP
    public static final Pin BUTTON_2 = RaspiPin.GPIO_23;  // 
    public static final Pin BUTTON_3 = RaspiPin.GPIO_27;  // 
    public static final Pin BUTTON_4 = RaspiPin.GPIO_25;  // BOTTOM

    public GPIO() /*throws java.lang.UnsatisfiedLinkError*/ {

        try {
            // create gpio controller
            GpioController gpio = GpioFactory.getInstance();

            button1 = gpio.provisionDigitalInputPin(BUTTON_1, PinPullResistance.PULL_UP);

            button1.setShutdownOptions(true);  // set shutdown state for this input pin

            // create and register gpio pin listener
            button1.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // display pin state on console
                    System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                }
            });
            
            System.out.println("<--Pi4J--> GPIO Listen Example ... started.");

        } catch (java.lang.UnsatisfiedLinkError e) {
            System.out.println("<--Pi4J--> GPIO Listen Example ... fake mode.");
            button1 = null;
        }
        //System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }

}
