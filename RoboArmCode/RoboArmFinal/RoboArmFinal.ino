#include <Servo.h>

Servo shoulder1;
Servo shoulder2;
Servo elbow1;
Servo elbow2;
Servo gripper;
Servo baseServo;

int baseServoSignal = 90;
int shoulder1Pos = 90;
int shoulder2Pos = 90;
int elbow1Pos = 90;
int elbow2Pos = 90;

int gripperPos = 0;

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
  
  gripper.attach(13);
  
  delay(1000);
  Serial.begin(9600);
  
}

void loop() {
  while (Serial.available()) {
    delay(10);
    char c = Serial.read();
    while (c != 'X') {
      inputString += c;
      c = Serial.read();
    } 
    if (inputString.length() > 0) {
        Serial.println(inputString + " first");
        if(inputString.equals("L")) {
          baseServo.write(83);
          while (c != 'C') {
            c = Serial.read();
          }
          baseServo.write(90);
            
        } else if(inputString.equals("R")) {
          Serial.print("inside R");
          baseServo.write(100);
          while (c != 'C') {
            c = Serial.read();
          }
          baseServo.write(90);  
          
        } else if(inputString.equals("SU")) {
            while(c != 'C') {
              c = Serial.read();
              if(shoulder1Pos <= 170) {
                if(shoulder2Pos >= 10) {
                  shoulder2Pos -= 1;
                 }
                shoulder1Pos += 1;
              }
              shoulder1.write(shoulder1Pos);
              shoulder2.write(shoulder2Pos);
              delay(15);
            }
            
            
        } else if(inputString.equals("SD")) {
            while(c != 'C') {
              c = Serial.read();
              if(shoulder1Pos >= 105) {
                if(shoulder2Pos <= 100) {
                  shoulder2Pos += 1;
                }
               shoulder1Pos -=1;
              }
              shoulder1.write(shoulder1Pos);
              shoulder2.write(shoulder2Pos);
              delay(15);
            }
  
        } else if(inputString.equals("EU")) {
            Serial.print("Got EEEEEEEE");
            while (c != 'C') {
              c = Serial.read();
              if(elbow1Pos <= 150) {
                if(elbow2Pos >= 30) {
                  elbow2Pos -= 1;
                }
                elbow1Pos +=1;
                elbow1.write(elbow1Pos);
                elbow2.write(elbow2Pos);
                delay(15);
              }
            } 
            
        } else if(inputString.equals("ED")) {
            while (c != 'C') {
              c = Serial.read();
              if(elbow1Pos >= 105) {
                if(elbow2Pos <= 75) {
                  elbow2Pos +=1;
                }
                elbow1Pos -=1;
                elbow1.write(elbow1Pos);
                elbow2.write(elbow2Pos);
                delay(15);
              }
            } 
        } else if(inputString.equals("GO")) {
          
        } else if(inputString.equals("GE")) {
          
        }
        Serial.println(inputString + " last");
    }
    inputString = "";
  }
}
