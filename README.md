
# Doctor Appointment Booking Android Application

This Android application enables users to book and manage doctor appointments efficiently. It provides a seamless flow from registration to appointment confirmation, integrating multiple hospital listings, symptom-based specialist selection, and real-time appointment tracking.

## Overview

The app serves as a platform for patients to:
- Register and maintain their profiles
- Explore multiple hospitals and their specialties
- Select symptoms to identify suitable doctors
- Book appointments by selecting available time slots
- Receive confirmation and view the status of their appointments
- Edit profile details and manage bookings

## Features

- User Registration and Login
- Interactive hospital listing with details and images
- Cause-based specialist and doctor filtering
- Dynamic time slot loading based on specialist availability
- Real-time availability checks to prevent double bookings
- Token generation for each confirmed appointment
- Profile editing and appointment cancellation options
- Appointment status display: Upcoming or Met

## Technologies Used

The application is built using:
- Java (Android SDK)
- SQLite for local database management
- SharedPreferences for session management
- XML-based UI with DatePicker, Spinner, and other Android components
- Android Intent system for screen navigation

## Database Design

The app uses a local SQLite database with the following key tables:

- `info`: Stores user data such as name, date of birth, age, gender, email, address, and phone number.
- `appointments`: Stores appointment details including doctor, hospital, cause, date, time, and token.
- `doctor`: Maps hospitals to doctors and their specialties.
- `causes`: Maps symptoms to medical specialists.
- `time_table`: Stores available time slots for each specialty.

## User Flow

1. **Start at Home Page**: Browse hospitals with clickable logos.
2. **Register**: Create an account with personal and contact details.
3. **Login**: Authenticate using email and phone number.
4. **Book Appointment**:
   - Select a hospital
   - Choose a medical cause/symptom
   - View available doctors and select one
   - Choose a date and time
   - Confirm the appointment
5. **View Appointments**: See your scheduled or past appointments.
6. **Edit Profile**: Update personal information if needed.
7. **Cancel Appointments**: Cancel upcoming appointments when required.

## Future Enhancements

- Cloud-based database integration (e.g., Firebase, MySQL)
- Email and sms verification and password recovery
- Push notifications for appointment reminders
- Multi-language support
- Doctor-side interface for managing schedules

