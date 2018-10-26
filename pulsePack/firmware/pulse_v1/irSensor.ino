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

#define MAX_SAMPLES 3600


// L_ARM Connector
// Define IR LED Pin : D2
#define IRLED 48
// Define Sig Pin = 3
#define SIG_0 49




void setSigAsOutput(void) {
  pinMode(SIG_0, OUTPUT);
}

void setSigAsInput(void) {
  pinMode(SIG_0, INPUT);
}

void setSig(int v) {
  if ( v == 1 ) digitalWrite(SIG_0, HIGH);
  else    digitalWrite(SIG_0, LOW);
}

void initIrLed() {
  pinMode(IRLED, OUTPUT);
  irLedOff();
}

void irLedOn() {
  digitalWrite(IRLED, HIGH);
}

void irLedOff() {
  digitalWrite(IRLED,LOW);
}

void setLED( int v ) {
  if ( v == 1 )   digitalWrite(13, HIGH);
  else   digitalWrite(13,LOW);
}

int getSig() {
  return digitalRead(SIG_0);   // Returns 0 when object near.
}

int measureSig() {
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

