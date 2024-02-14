

//#include <SPI.h>
//#include <Wire.h>
#include <Adafruit_GFX.h>  // Include core graphics library for the display
#include <Adafruit_SSD1306.h>  // Include Adafruit_SSD1306 library to drive the display

byte cpu = 0;

// Define SPI pins:
#define OLED_DC     6//9
#define OLED_CS     7//10
#define OLED_RESET  8


Adafruit_SSD1306 display(OLED_DC, OLED_RESET, OLED_CS);  // Create display


#include <Fonts/FreeMonoBold12pt7b.h>  // Add a custom font
#include <Fonts/FreeMono9pt7b.h>  // Add a custom font




int Variable1;  // Create a variable to have something dynamic to show on the display






void setup()  // Start of setup
{                
  
  delay(100);  // This delay is needed to let the display to initialize

  //Serial.begin(19200);

  Serial.begin(9600);

  display.begin(SSD1306_SWITCHCAPVCC);  // Initialize display
 
  display.clearDisplay();  // Clear the buffer

  //void setTextSize(uint8_t size);
  display.setTextSize(2);//scale 1x 2x

  display.setTextColor(WHITE);  // Set color of the text

  display.setRotation(0);  // Set orientation. Goes from 0, 1, 2 or 3

  display.setTextWrap(false);  // By default, long lines of text are set to automatically “wrap” back to the leftmost column.
                               // To override this behavior (so text will run off the right side of the display - useful for
                               // scrolling marquee effects), use setTextWrap(false). The normal wrapping behavior is restored
                               // with setTextWrap(true).

  display.dim(0);  //Set brightness (0 is maximun and 1 is a little dim)

}  // End of setup






void loop()  // Start of loop
{
/*
  Variable1++;  // Increase variable by 1
  if(Variable1 > 150)  // If Variable1 is greater than 150
  {
    Variable1 = 0;  // Set Variable1 to 0
  }
*/

delay(1500);

if (Serial.available()) cpu = (byte)Serial.parseInt();
  // Convert Variable1 into a string, so we can change the text alignment to the right:
  // It can be also used to add or remove decimal numbers.
  char string[4];  // Create a character array of 10 characters
  // Convert float to a string:
  dtostrf(cpu, 3, 0, string);  // (<variable>,<amount of digits we are going to use>,<amount of decimal digits>,<string name>)






  display.clearDisplay();  // Clear the display so we can refresh


  //display.setFont(&FreeMono9pt7b);  // Set a custom font
  //display.setTextSize(0);  // Set text size. We are using a custom font so you should always use the text size of 0


  // Print text:

  display.setCursor(0, 16);  // (x,y)
  display.print("CPU Usage:\n");
  display.print(cpu);
  display.print(" %");

/////////
/*
delay(250);
  display.setCursor(83, 20);  // (x,y)
  display.println(Variable1);  // Text or value to print
  */
/////////


  
  display.display();  // Print everything we set previously

}  // End of loop
