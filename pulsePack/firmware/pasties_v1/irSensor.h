#ifndef irSensor_h
#define irSensor_h

#include "Arduino.h"

class irSensor
{
  public:
    irSensor(int pin, int irled);
    int measureSig();
    void begin();
    void irLedOn();
    void irLedOff();
  private:
    int _sig;
    int _irLED;
    void setSigAsOutput(void);
    void setSigAsInput(void);
    void setSig(int v);
    void setLED( int v );
    int getSig();
};

#endif
