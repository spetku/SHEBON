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
 
#include "Arduino.h"
#include "irSensor.h"

#define MAX_SAMPLES 3600

irSensor::irSensor(int sig, int irLED)
{
  _sig = sig;
  _irLED = irLED;
}


void irSensor::setSigAsOutput(void) {
  pinMode(_sig, OUTPUT);
}

void irSensor::setSigAsInput(void) {
  pinMode(_sig, INPUT);
}

void irSensor::setSig(int v) {
  if ( v == 1 ) digitalWrite(_sig, HIGH);
  else    digitalWrite(_sig, LOW);
}

void irSensor::begin() {
  pinMode(_irLED, OUTPUT);
  irLedOff();
}

void irSensor::irLedOn() {
  digitalWrite(_irLED, HIGH);
}

void irSensor::irLedOff() {
  digitalWrite(_irLED,LOW);
}

void irSensor::setLED( int v ) {
  if ( v == 1 )   digitalWrite(13, HIGH);
  else   digitalWrite(13,LOW);
}

int irSensor::getSig() {
  return digitalRead(_sig);   // Returns 0 when object near.
}

int irSensor::measureSig() {
  int sampleCount = 0;

  setSigAsOutput();     // Set SIG as output
  setSig(1);      // Set SIG high
  //_delay_us(11.0);    // Charge line for 11uS
  _delay_us(18.0);    // Charge line for 11uS
  setSigAsInput();    // Make SIG input (high impedance)

  // take measurements until line goes low or times out.
  while ( sampleCount < MAX_SAMPLES && getSig() ) sampleCount++;
    
  return sampleCount;
}

