# Smart Desk Posture Reminder App

A simple Android application built using the Model-View-Presenter (MVP) architecture pattern. This app serves as a foundation for a Smart Desk Posture Reminder system.

## Features

- **Login Screen**: User authentication with email and password validation
- **Register Screen**: User registration with comprehensive input validation
- **Home Screen**: Displays user information and posture data in a table format
- **Temporary Data Storage**: User data is stored temporarily while the app is running
- **Dynamic User Display**: Home screen shows the actual registered user's name and email

## Architecture

This application follows the **MVP (Model-View-Presenter)** design pattern:

### Model Layer
- `User.kt`: Data class representing user information
- `PostureData.kt`: Data class representing posture-related data

### View Layer
- `LoginView.kt`: Interface defining login screen contract
- `RegisterView.kt`: Interface defining registration screen contract
- `HomeView.kt`: Interface defining home screen contract

### Presenter Layer
- `LoginPresenter.kt`: Handles login business logic and validation
- `RegisterPresenter.kt`: Handles registration business logic and validation
- `HomePresenter.kt`: Handles home screen data loading and display

### Data Management
- `UserDataManager.kt`: Manages temporary user data storage using SharedPreferences
- `SmartDeskPostureApp.kt`: Application class for initialization

### Activities
- `LoginActivity.kt`: Implements LoginView interface
- `RegisterActivity.kt`: Implements RegisterView interface
- `HomeActivity.kt`: Implements HomeView interface

## Project Structure

```
app/src/main/java/com/eldroid/smartdeskposture/
├── model/
│   ├── User.kt
│   └── PostureData.kt
├── presenter/
│   ├── LoginPresenter.kt
│   ├── RegisterPresenter.kt
│   └── HomePresenter.kt
├── view/
│   ├── LoginView.kt
│   ├── RegisterView.kt
│   └── HomeView.kt
├── data/
│   └── UserDataManager.kt
├── SmartDeskPostureApp.kt
├── LoginActivity.kt
├── RegisterActivity.kt
└── HomeActivity.kt
```

## Temporary Data Storage

The application now includes temporary data storage functionality:

- **UserDataManager**: Stores user information in memory and SharedPreferences
- **Persistent Storage**: User data persists between app sessions (until logout)
- **Dynamic Updates**: Home screen displays the actual registered user's information
- **Logout Functionality**: Users can logout and test registration/login multiple times

## How to Test

1. **Register a new user** with any name and email
2. **Login** with the registered credentials
3. **Home screen** will display the actual registered user's name and email
4. **Logout** to test the flow again with different users
5. **Data persists** between app restarts until logout

## Mock Data

The application currently uses mock data for demonstration purposes:

### Test Login Credentials
- Email: `test@gmail.com`
- Password: `123123`

### Sample Posture Data
The home screen displays sample posture data including:
- Posture type (Good, Fair, Poor)
- Duration in minutes
- Reminder count

## Future Enhancements

This application is designed to be easily extended for IoT integration:

1. **Real-time Posture Monitoring**: Integration with posture sensors
2. **Database Integration**: Replace temporary storage with real database storage
3. **Push Notifications**: Reminder system for posture correction
4. **Analytics Dashboard**: Detailed posture statistics and trends
5. **User Settings**: Customizable reminder intervals and preferences

## Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Build and run the application
4. Use the test credentials to login or register a new account

## Dependencies

The application uses standard Android libraries:
- `androidx.appcompat:appcompat`
- `androidx.recyclerview:recyclerview`
- Standard Android widgets and layouts

## Notes

- This is a foundation application that demonstrates MVP architecture
- No IoT functionality is implemented yet
- All data is currently mocked for demonstration purposes
- The app follows Material Design principles for UI consistency

## Screenshots

The application includes three main screens:
1. **Login**: Clean login interface with validation
2. **Register**: Comprehensive registration form
3. **Home**: User dashboard with posture data table

## Contributing

This project serves as a learning example for MVP architecture implementation. Feel free to extend it with additional features or improvements.
