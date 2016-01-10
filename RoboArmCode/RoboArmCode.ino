#include <Servo.h>
Servo shoulder1;
Servo shoulder2;

Servo elbow1;
Servo elbow2;

Servo baseServo;

int shoulder1Pos = 90;
int shoulder2Pos = 90;

int elbow1Pos = 90;
int elbow2Pos = 90;

int baseServoSignal = 90;

byte incomingByte;

String inputString;

void setup() {
  
  shoulder1.write(shoulder1Pos);
  shoulder2.write(shoulder2Pos);
  
  shoulder1.attach(11);
  shoulder2.attach(12);

  elbow1.write(elbow1Pos);
  elbow2.write(elbow2Pos);
  
  elbow1.attach(9);
  elbow2.attach(10);
  
  baseServo.attach(8);
  
  Serial.begin(9600);
}

void loop() {
  
  for (shoulder1Pos = 90; shoulder1Pos <= 170; shoulder1Pos += 1) { 
    if(shoulder2Pos >= 10) {
      shoulder2Pos -= 1; 
    }
    shoulder1.write(shoulder1Pos);              
    shoulder2.write(shoulder2Pos);
    delay(15);
  }
  delay(2000);
  
  for (elbow1Pos = 90; elbow1Pos <= 150; elbow1Pos += 1) { 
    if(elbow2Pos >= 30) {
      elbow2Pos -= 1; 
    }
    elbow1.write(elbow1Pos);              
    elbow2.write(elbow2Pos);
    delay(15);
  }
  delay(2000);

  baseServo.write(100);
  delay(2000);
  baseServo.write(83);
  delay(2000);
  baseServo.write(90);
  
  delay(2000);
  
  for (elbow1Pos = 150; elbow1Pos >= 105; elbow1Pos -= 1) {
    if(elbow2Pos <= 75) {
      elbow2Pos += 1; 
    }
    
    elbow1.write(elbow1Pos);
    elbow2.write(elbow2Pos);
    delay(15);
  }
  delay(2000); 
  
  for (shoulder1Pos = 170; shoulder1Pos >= 80; shoulder1Pos -= 1) {
    if(shoulder2Pos <= 100) {
      shoulder2Pos += 1; 
    }
    
    shoulder1.write(shoulder1Pos);
    shoulder2.write(shoulder2Pos);
    delay(15);
  }
  delay(2000);
  
}
