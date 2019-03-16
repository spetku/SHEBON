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
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
#include <avr/power.h>
#endif

#define NEOPIXEL_PIN 6
#define NEOPIXEL_COUNT 14

// Pasty Motor
#define MTR_PIN 7
#define DIR_PIN 8


// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NEOPIXEL_COUNT, NEOPIXEL_PIN, NEO_GRB + NEO_KHZ800);

int onTime = 0;

void setup() {
  //analogReference(DEFAULT);
  strip.begin();
  strip.show(); // Initialize all pixels to 'off'

  pinMode(LED_BUILTIN, OUTPUT);

  // Motor
  pinMode( DIR_PIN, OUTPUT );
  digitalWrite( DIR_PIN, LOW );
  
  analogWrite( MTR_PIN, 180 );
  delay(1000);
  analogWrite( MTR_PIN, 0 );

  //  Fillet-O Connector
  Serial1.begin(38400);

  initIrLed();


}

void loop() {
  //  for(int i=25; i>0; i--) {
  //    colorWipe(strip.Color(i*10,i, 5), 10);
  //  }
  //  delay(200);

  
    irLedOn();
    onTime = 3600 - measureSig();
    //Serial.println(onTime);   //0 - 3600

    if ( onTime > 3600 ) {
      colorWipe(strip.Color(0,0, 0), 10);
    } else {
      //colorWipe(strip.Color(0,45, 0), 10);
      //colorWipe(strip.Color(0,(3600-onTime)/15, 0), 10);
      colorWipe(strip.Color(180*onTime/3600,0,0),6);
    }

    // Motor
    if ( onTime < 300 || onTime > 3600 ) {
      analogWrite( MTR_PIN, 0);
    } else {
      //analogWrite(MTR_PIN, 255 - (onTime/15) );
      analogWrite(MTR_PIN, 35 + (180*onTime/3600) );
    }
    irLedOff();


  //    int sensorValue = analogRead(A7)/4;
  //    colorWipe(strip.Color(0,sensorValue, 0), 10);
  delay(200);

  digitalWrite(LED_BUILTIN, LOW);   // turn the LED on (HIGH is the voltage level)
  Serial1.write( "SBDP" );  // SHE-BON Data Packet
  //Serial1.write( onTime );
  //Serial1.write( 0xBE );
  //Serial1.write( 0xEF );
  //Serial1.write( 0x00 );
  Serial1.write( (onTime >> 8) & 0xFF );
  Serial1.write( onTime & 0xFF );
  //Serial1.write( 0 );
  //Serial1.write(200);
  //Serial1.write("\n");
  
  delay(25);
  digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
}


