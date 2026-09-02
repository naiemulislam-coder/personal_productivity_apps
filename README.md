# Personal Productivity & Focus Management App

Project scaffold for Android (Java + XML) — MVVM + Room + WorkManager + MPAndroidChart

Compile/Target SDK: 36
Package: com.naiemulislam.productivity

This branch contains a minimal starter scaffold (placeholders) implementing the repository structure, basic Room entities/DAO, ViewModel, MainActivity and Gradle module configuration.

Notes:
- I set compileSdk and targetSdk to 36 as you requested.
- Dependency versions are placeholders pinned to versions compatible with Android tooling as of 2024; you should open the project in Android Studio (2026) and accept recommended Gradle & plugin updates — Android Studio will suggest updated library versions and Gradle plugin/Gradle wrapper upgrades.
- If any dependency becomes unsupported by 2026 you will be prompted by Android Studio; tell me if you want me to update versions after you open the project and share the Gradle sync errors.

What's included:
- Android project files (app module) with minimal Java classes and XML layout
- Room: Task entity + TaskDao + AppDatabase
- ViewModel and Repository placeholders
- SchedulerEngine placeholder for deterministic scheduling logic

How I can continue next:
- Implement detailed DB schema (Goals, Routine, FixedActivity)
- Implement Scheduling algorithm class
- Add UI screens: Tasks, Goals, Reports
- Add unit tests and sample data

If you want changes (package name, minSdk, extra libs), tell me and I will update them.
