#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

// ====== WiFi Credentials (Wokwi Default) ======
#define WIFI_SSID "Wokwi-GUEST"
#define WIFI_PASSWORD ""

// ====== Firebase Configuration ======
#define FIREBASE_HOST "https://smart-desk-posture-default-rtdb.asia-southeast1.firebasedatabase.app/"
#define FIREBASE_AUTH "6yPRRp6z6b1uHrK7vl2YbRBxZAidbRFoDlXDaSrT"

// ====== Hardware Pins ======
const int trigPin = 5;
const int echoPin = 18;
const int buzzerPin = 23;
const int ledPin = 2; // Built-in LED for status indication

// ====== Configuration ======
const int badPostureThreshold = 30; // <30 cm = slouching
const int maxDistance = 200; // Maximum valid distance (cm)
const int minDistance = 2;   // Minimum valid distance (cm)
const int readingInterval = 2000; // 2 seconds between readings
const int buzzerDuration = 500; // Buzzer beep duration (ms)

// ====== Global Variables ======
unsigned long lastReading = 0;
int consecutiveBadReadings = 0;
int consecutiveGoodReadings = 0;
bool buzzerActive = false;
unsigned long buzzerStartTime = 0;
String currentUserID = "ryan_test"; // Default user for simulation
// TODO: In real implementation, get this from user login or device registration

// ====== Posture Data Structure ======
struct PostureData {
  long distance;
  String status;
  unsigned long timestamp;
  String userID;
};

void setup() {
  Serial.begin(115200);
  delay(1000);
  
  Serial.println("🚀 Smart Desk Posture Monitor Starting...");
  
  // Initialize pins
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(buzzerPin, OUTPUT);
  pinMode(ledPin, OUTPUT);
  
  // Initialize pins to safe states
  digitalWrite(trigPin, LOW);
  digitalWrite(buzzerPin, LOW);
  digitalWrite(ledPin, LOW);
  
  // Connect to WiFi
  connectToWiFi();
  
  // Calibrate sensor
  calibrateSensor();
  
  Serial.println("✅ System Ready!");
  blinkLED(3, 200); // 3 quick blinks to indicate ready
}

void connectToWiFi() {
  Serial.print("📡 Connecting to WiFi: ");
  Serial.println(WIFI_SSID);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  int attempts = 0;
  while (WiFi.status() != WL_CONNECTED && attempts < 20) {
    delay(500);
    Serial.print(".");
    attempts++;
  }
  
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("\n✅ WiFi Connected!");
    Serial.print("📶 IP Address: ");
    Serial.println(WiFi.localIP());
    digitalWrite(ledPin, HIGH); // LED on when connected
  } else {
    Serial.println("\n❌ WiFi Connection Failed!");
    Serial.println("🔄 Retrying in 10 seconds...");
    delay(10000);
    ESP.restart(); // Restart if WiFi fails
  }
}

void calibrateSensor() {
  Serial.println("🔧 Calibrating ultrasonic sensor...");
  
  // Take 5 readings and average them
  long totalDistance = 0;
  int validReadings = 0;
  
  for (int i = 0; i < 10; i++) {
    long distance = getDistanceCM();
    if (distance >= minDistance && distance <= maxDistance) {
      totalDistance += distance;
      validReadings++;
    }
    delay(100);
  }
  
  if (validReadings > 0) {
    long avgDistance = totalDistance / validReadings;
    Serial.print("📏 Calibrated distance: ");
    Serial.print(avgDistance);
    Serial.println(" cm");
  } else {
    Serial.println("⚠️ Sensor calibration failed - using default values");
  }
}

long getDistanceCM() {
  // Send ultrasonic pulse
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  // Read echo duration
  long duration = pulseIn(echoPin, HIGH, 30000); // 30ms timeout
  
  if (duration == 0) {
    Serial.println("⚠️ Sensor timeout - no echo received");
    return -1; // Error value
  }
  
  // Calculate distance
  long distance = duration * 0.034 / 2;
  
  // Validate distance
  if (distance < minDistance || distance > maxDistance) {
    Serial.println("⚠️ Invalid distance reading: " + String(distance) + " cm");
    return -1; // Error value
  }
  
  return distance;
}

String determinePostureStatus(long distance) {
  if (distance == -1) {
    return "Error";
  } else if (distance < badPostureThreshold) {
    return "Bad";
  } else if (distance > 80) {
    return "Too Far";
  } else {
    return "Good";
  }
}

void handleBuzzer(String status) {
  if (status == "Bad") {
    if (!buzzerActive) {
      digitalWrite(buzzerPin, HIGH);
      buzzerActive = true;
      buzzerStartTime = millis();
      Serial.println("🔔 Buzzer ON - Bad posture detected!");
    }
  } else {
    if (buzzerActive) {
      digitalWrite(buzzerPin, LOW);
      buzzerActive = false;
      Serial.println("🔇 Buzzer OFF");
    }
  }
  
  // Auto-turn off buzzer after duration
  if (buzzerActive && (millis() - buzzerStartTime > buzzerDuration)) {
    digitalWrite(buzzerPin, LOW);
    buzzerActive = false;
  }
}

void updateFirebase(PostureData data) {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("❌ WiFi not connected - skipping upload");
    return;
  }
  
  // Create JSON payload
  DynamicJsonDocument doc(1024);
  doc["distance"] = data.distance;
  doc["status"] = data.status;
  doc["timestamp"] = data.timestamp;
  doc["userID"] = data.userID;
  
  String jsonString;
  serializeJson(doc, jsonString);
  
  // Send to Firebase using HTTP - User-specific paths
  HTTPClient http;
  
  // 1. Store historical data in user-specific path
  String url1 = String(FIREBASE_HOST) + "users/" + data.userID + "/postureData/" + String(data.timestamp) + ".json?auth=" + String(FIREBASE_AUTH);
  http.begin(url1);
  http.addHeader("Content-Type", "application/json");
  
  int httpResponseCode = http.PUT(jsonString);
  
  if (httpResponseCode > 0) {
    String response = http.getString();
    Serial.println("📤 Data uploaded: " + data.status + " (" + String(data.distance) + "cm)");
    Serial.println("Response: " + response);
  } else {
    Serial.println("❌ Firebase upload failed. Error code: " + String(httpResponseCode));
  }
  
  http.end();
  
  // 2. Update user-specific real-time status
  String url2 = String(FIREBASE_HOST) + "users/" + data.userID + "/posture/status.json?auth=" + String(FIREBASE_AUTH);
  http.begin(url2);
  http.addHeader("Content-Type", "application/json");
  http.PUT("\"" + data.status + "\"");
  http.end();
  
  String url3 = String(FIREBASE_HOST) + "users/" + data.userID + "/posture/distance.json?auth=" + String(FIREBASE_AUTH);
  http.begin(url3);
  http.addHeader("Content-Type", "application/json");
  http.PUT(String(data.distance));
  http.end();
  
  // 3. Update global posture status (for admin/monitoring)
  String url4 = String(FIREBASE_HOST) + "global/activeUsers/" + data.userID + ".json?auth=" + String(FIREBASE_AUTH);
  http.begin(url4);
  http.addHeader("Content-Type", "application/json");
  http.PUT("{\"status\":\"" + data.status + "\",\"distance\":" + String(data.distance) + ",\"lastSeen\":" + String(data.timestamp) + "}");
  http.end();
}

void blinkLED(int times, int delayMs) {
  for (int i = 0; i < times; i++) {
    digitalWrite(ledPin, HIGH);
    delay(delayMs);
    digitalWrite(ledPin, LOW);
    delay(delayMs);
  }
}

void loop() {
  // Check if it's time for a new reading
  if (millis() - lastReading >= readingInterval) {
    lastReading = millis();
    
    // Get distance reading
    long distance = getDistanceCM();
    
    if (distance != -1) {
      // Determine posture status
      String status = determinePostureStatus(distance);
      
      // Handle buzzer based on status
      handleBuzzer(status);
      
      // Create posture data
      PostureData data;
      data.distance = distance;
      data.status = status;
      data.timestamp = millis();
      data.userID = currentUserID;
      
      // Update Firebase
      updateFirebase(data);
      
      // Update consecutive readings counter
      if (status == "Bad") {
        consecutiveBadReadings++;
        consecutiveGoodReadings = 0;
      } else {
        consecutiveGoodReadings++;
        consecutiveBadReadings = 0;
      }
      
      // Print status
      Serial.print("📊 Distance: ");
      Serial.print(distance);
      Serial.print(" cm | Status: ");
      Serial.print(status);
      Serial.print(" | Bad streak: ");
      Serial.println(consecutiveBadReadings);
      
    } else {
      Serial.println("❌ Invalid sensor reading - skipping this cycle");
    }
  }
  
  // Check WiFi connection
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("❌ WiFi disconnected - attempting reconnection...");
    connectToWiFi();
  }
  
  // Update system status every 30 seconds
  static unsigned long lastStatusUpdate = 0;
  if (millis() - lastStatusUpdate > 30000) {
    lastStatusUpdate = millis();
    Serial.println("🔄 System status updated");
  }
  
  // Small delay to prevent overwhelming the system
  delay(100);
}
