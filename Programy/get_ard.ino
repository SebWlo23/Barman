#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <Arduino_JSON.h>
#include <string>


using namespace std;
int testowa = 0;
const char* name = "Krzychunet";
const char* password = "987654321";
//String Order;
String adres_temp;
float Placed_order_array[5];
bool czy_wykonane;
int aktualne_zamowienie = 1;
int liczba_zamowien;
String Get_serwer = "http://sebwlo23.pythonanywhere.com/zlozone_zamowienia/ostatnie";
String Get_po_id = "http://sebwlo23.pythonanywhere.com/zlozone_zamowienie/";
String Put_po_id = "http://sebwlo23.pythonanywhere.com/zlozone_zamowienia/aktualizuj/";
String test_adres = "http://webhook.site/e5a9c938-095d-4471-abb1-b8657b935102";
//int numer_zamowienia = 3;  // Pobierz dane dla zamówienia o numerze 3
//String Get_serwer = "http://sebwlo23.pythonanywhere.com/zlozone_zamowienie/" + String(numer_zamowienia);


//zmienne do funkcjonalnego
// 200 kroków to 31,4 mm
const int dir = 12;
const int step = 14;
const int limitSwitch = 13; 
const int pomp1 = 5; //12V
const int pomp2 = 21; //6V
const int pomp3 = 19; //6V
const int pomp4 = 18; //6V
const int sensor = 33;


const int pozycja1 = 370;
const int pozycja2 = 665;
const int pozycja3 = 665;

int stepDelay = 2000;
const int stepsPerRevolution = 200;

String getRequest(String serverName) {
  WiFiClient client;
  HTTPClient http;
    
  http.begin(client, serverName);
  
  int httpResponseCode = http.GET();
  
  String payload = "{}"; 
  
  if (httpResponseCode>0) {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    payload = http.getString();
  }
  else {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }

  http.end();

  return payload;
}

void wykonane_serwer(String serverName, bool newValue) {
  WiFiClient client;
  HTTPClient http;
  
  // Tworzenie obiektu JSON z nową wartością
  String payload = "{\"czy_wykonane\": ";
  payload += newValue ? "true" : "false";
  payload += "}";
  
  http.begin(client, serverName);
  http.addHeader("Content-Type", "application/json");
  int httpResponseCode = http.PUT(payload);
  
  if (httpResponseCode > 0) {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
  }
  else {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
  
  http.end();
}


void bazowanie()
{
  digitalWrite(dir, LOW); 

  while (digitalRead(limitSwitch) == HIGH)
  {
    digitalWrite(step, LOW);
    delayMicroseconds(stepDelay);
    digitalWrite(step, HIGH);
    delayMicroseconds(stepDelay);
  }
  Serial.println("Było bazowanie!");
}

void pozycja_1()
{
digitalWrite(dir, HIGH); // Set direction to clockwise
  for (int i = 0; i < pozycja1; i++)
  {
    digitalWrite(step, LOW);
    delayMicroseconds(stepDelay);
    digitalWrite(step, HIGH);
    delayMicroseconds(stepDelay);
  }
  delay(500);
}

void pozycja_2(){
digitalWrite(dir, HIGH); // Set direction to clockwise
  for (int i = 0; i < pozycja2; i++)
  {
    digitalWrite(step, LOW);
    delayMicroseconds(stepDelay);
    digitalWrite(step, HIGH);
    delayMicroseconds(stepDelay);
  }
  delay(500);
}

void pozycja_3(){
digitalWrite(dir, HIGH); // Set direction to clockwise
  for (int i = 0; i < pozycja3; i++)
  {
    digitalWrite(step, LOW);
    delayMicroseconds(stepDelay);
    digitalWrite(step, HIGH);
    delayMicroseconds(stepDelay);
  }
  delay(500);
}

void lanie_vody(){

  while(digitalRead(sensor) == HIGH){
    pozycja_1();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp1, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp1, HIGH);
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
    pozycja_2();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp1, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp1, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
      break;
    }
    pozycja_3();
    if(digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp1, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp1, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
  }
  bazowanie();
}

void lanie_whiskey(){
  while(digitalRead(sensor) == HIGH){
    pozycja_1();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp2, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp2, HIGH);
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
    pozycja_2();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp2, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp2, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
      break;
    }
    pozycja_3();
    if(digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp2, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp2, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
  }
  bazowanie();
}

void lanie_coli(){
  while(digitalRead(sensor) == HIGH){
    pozycja_1();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp3, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp3, HIGH);
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
    pozycja_2();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp3, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp3, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
      break;
    }
    pozycja_3();
    if(digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp3, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp3, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
  }
  bazowanie();
}

void lanie_soku(){
  while(digitalRead(sensor) == HIGH){
    pozycja_1();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp4, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp4, HIGH);
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
    pozycja_2();
    if (digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp4, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp4, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
      break;
    }
    pozycja_3();
    if(digitalRead(sensor) == LOW){
     delay(1000);
     digitalWrite(pomp4, LOW);
     delay(Placed_order_array[3]*650);
     digitalWrite(pomp4, HIGH); 
     delay(2000);
     Serial.println("Powinno lac!");
     break;
    }
  }
  bazowanie();
}

void pobierz_ostatni(){
  if(WiFi.status()== WL_CONNECTED){
    String Order = getRequest(Get_serwer);
    JSONVar JSON_Object = JSON.parse(Order);
  
    if (JSON.typeof(JSON_Object) == "undefined") {
      Serial.println("Parsing input failed!");
      return;
    }
    
    JSONVar keys = JSON_Object.keys();
    
    for (int i = 0; i < keys.length(); i++) {
      JSONVar value = JSON_Object[keys[i]];
      Placed_order_array[i] = double(value);
      if(i==2){
        liczba_zamowien = int(value);
      }
    }
    Serial.print("Liczba zamowien: ");
    Serial.println(liczba_zamowien);
    
  }
}

bool pobierz_po_id(int id){
  if(WiFi.status()== WL_CONNECTED){
    String adres = Get_po_id + String(id);
    String Order = getRequest(adres);
    JSONVar JSON_Object = JSON.parse(Order);
    if (JSON.typeof(JSON_Object) == "undefined") {
      Serial.println("Parsing input failed!");
      return 0;
    }
    if (!JSON_Object.hasOwnProperty("id")){
      return 0;
    }  
    JSONVar keys = JSON_Object.keys();
    for (int i = 0; i < keys.length(); i++) {
      JSONVar value = JSON_Object[keys[i]];
      Placed_order_array[i] = double(value);
      if(i==1){
        czy_wykonane = bool(value);
      }
    }
    Serial.print("czy_wykonane = ");
    Serial.println(czy_wykonane);  
  }
  return 1;
}

void setup() {
   Serial.begin(9600);
   WiFi.begin(name, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(2000);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected with Wi-Fi: ");
  Serial.println(WiFi.localIP());

    // Declare pins as Outputs/Inputs
  pinMode(step, OUTPUT);
  pinMode(dir, OUTPUT);
  pinMode(limitSwitch, INPUT_PULLUP);
  pinMode(pomp1, OUTPUT);
  pinMode(pomp2, OUTPUT);
  pinMode(pomp3, OUTPUT);
  pinMode(pomp4, OUTPUT);
  pinMode(sensor, INPUT);

  digitalWrite(pomp1, HIGH);
  digitalWrite(pomp2, HIGH);
  digitalWrite(pomp3, HIGH);
  digitalWrite(pomp4, HIGH);
  bazowanie();


}

void loop() {
//bazowanie();
delay(3000);
pobierz_ostatni(); 
//program funkcjonalny

for(int i = 1; i <= liczba_zamowien; i++){

  if(!pobierz_po_id(i)){
    continue;
  }

  delay(50);

  if(czy_wykonane==0 && Placed_order_array[0]==1){
    lanie_vody();
    adres_temp = Put_po_id + String(i);
    wykonane_serwer(adres_temp,1);
    Serial.println(adres_temp);
  }
  else if(czy_wykonane==0 && Placed_order_array[0]==2){
    lanie_whiskey();
    Serial.println("Drugi");
    delay(1000);
    adres_temp = Put_po_id + String(i);
    wykonane_serwer(adres_temp,1);
    Serial.println(adres_temp);
  }
  else if(czy_wykonane==0 && Placed_order_array[0]==3){
    lanie_coli();
    Serial.println("Drugi");
    delay(1000);
    adres_temp = Put_po_id + String(i);
    wykonane_serwer(adres_temp,1);
    Serial.println(adres_temp);
  }
  else if(czy_wykonane==0 && Placed_order_array[0]==4){
    lanie_soku();
    Serial.println("Drugi");
    delay(1000);
    adres_temp = Put_po_id + String(i);
    wykonane_serwer(adres_temp,1);
    Serial.println(adres_temp);
  }
//+3 takie else ify
}
delay(1000);
}