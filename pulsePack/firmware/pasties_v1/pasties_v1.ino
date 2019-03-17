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
#include "irSensor.h"

#ifdef __AVR__
#include <avr/power.h>
#endif

#define NEOPIXEL_PIN 6
#define NEOPIXEL_COUNT 14

// Pasty Motor
#define MTR1_PIN 7
#define MTR2_PIN 8
#define MTR_MIN 30

// Bra Connector
//#define IR1LED 48   // L_ARM
#define IR1LED 9      // NIPS
#define IR2LED 48     // L_ARM   BRN

//#define SIG1 49   // L_ARM
#define SIG1 10     // NIPS
#define SIG2 49     // L_ARM    WBRN
 

#define MS_RANGE 3600
#define MAX_SPEED 40

// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NEOPIXEL_COUNT, NEOPIXEL_PIN, NEO_GRB + NEO_KHZ800);

irSensor sense1 = irSensor(SIG1, IR1LED);
irSensor sense2 = irSensor(SIG2, IR2LED);

int onTime1 = 0;
int motor1Moving = 0;
int onTime2 = 0;
int motor2Moving = 0;

void setup() {
  //analogReference(DEFAULT);
  strip.begin();
  strip.show(); // Initialize all pixels to 'off'

  pinMode(LED_BUILTIN, OUTPUT);

  // Motor
  //pinMode( DIR_PIN, OUTPUT );
  //digitalWrite( DIR_PIN, LOW );
  
  kickMotor1();
  delay(40);
  kickMotor2();
  
  //  Fillet-O Connector
  Serial1.begin(38400);

  sense1.begin();
  sense2.begin();



}

void loop() {
  //  for(int i=25; i>0; i--) {
  //    colorWipe(strip.Color(i*10,i, 5), 10);
  //  }
  //  delay(200);

  
    sense1.irLedOn();
    sense2.irLedOn();
    
    //onTime = 3600 - measureSig();
    onTime1 = MS_RANGE - sense1.measureSig();
    onTime2 = MS_RANGE - sense2.measureSig();
    
    //Serial.println(onTime);   //0 - 3600

    if ( onTime1 > MS_RANGE ) {
      colorWipe(strip.Color(0,0, 0), 10);
    } else {
      colorWipe(strip.Color(180*onTime1/3600,0,0),6);
    }
    
    sense1.irLedOff();
    sense2.irLedOff();

    // Motor
    if ( onTime1 < 300 || onTime1 > MS_RANGE ) {
      analogWrite( MTR1_PIN, 0);
      motor1Moving = 0;
    } else {
      //analogWrite(MTR_PIN, 255 - (onTime/15) );
      if ( !motor1Moving ) {
        kickMotor1();
      }
      analogWrite(MTR1_PIN, MTR_MIN + (MAX_SPEED*onTime1/MS_RANGE) );
    }
    // Motor
    if ( onTime2 < 300 || onTime2 > MS_RANGE ) {
      analogWrite( MTR2_PIN, 0);
      motor2Moving = 0;
    } else {
      //analogWrite(MTR_PIN, 255 - (onTime/15) );
      if ( !motor2Moving ) {
        kickMotor2();
      }
      analogWrite(MTR2_PIN, MTR_MIN + (MAX_SPEED*onTime2/MS_RANGE) );
    }

    


  //    int sensorValue = analogRead(A7)/4;
  //    colorWipe(strip.Color(0,sensorValue, 0), 10);
  delay(200);

  digitalWrite(LED_BUILTIN, LOW);   // turn the LED on (HIGH is the voltage level)
  Serial1.write( "SBDP" );  // SHE-BON Data Packet
  //Serial1.write( onTime );
  //Serial1.write( 0xBE );
  //Serial1.write( 0xEF );
  //Serial1.write( 0x00 );
  Serial1.write( (onTime1 >> 8) & 0xFF );
  Serial1.write( onTime1 & 0xFF );
  //Serial1.write( 0 );
  //Serial1.write(200);
  //Serial1.write("\n");
  
  delay(25);
  digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
}

void kickMotor1() {
  analogWrite( MTR1_PIN, 220 );
  motor1Moving = 1;  
  delay(500);
  analogWrite( MTR1_PIN, 0 );
}

void kickMotor2() {
  analogWrite( MTR2_PIN, 220 );
  motor2Moving = 1;  
  delay(500);
  analogWrite( MTR2_PIN, 0 );
}

