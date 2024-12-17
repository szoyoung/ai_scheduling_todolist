# Folder Structure - /Users/kimseoyoung/AndroidStudioProjects/DoToDo
```
├── app/
│   ├── src/
│   │   ├── androidTest/
│   │   │   └── java/
│   │   │       └── com/
│   │   │           └── example/
│   │   │               └── dotodo/
│   │   │                   └── ExampleInstrumentedTest.java
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── dotodo/
│   │   │   │               ├── adapter/
│   │   │   │               │   ├── DailyEventAdapter.java
│   │   │   │               │   ├── TaskAdapter.java
│   │   │   │               │   ├── TaskViewHolder.java
│   │   │   │               │   └── WeeklyScheduleAdapter.java
│   │   │   │               ├── data/
│   │   │   │               │   ├── database/
│   │   │   │               │   │   ├── AppDatabase.java
│   │   │   │               │   │   ├── DateConverter.java
│   │   │   │               │   │   ├── ScheduleConverter.java
│   │   │   │               │   │   ├── ScheduleDao.java
│   │   │   │               │   │   └── TaskDao.java
│   │   │   │               │   ├── model/
│   │   │   │               │   │   ├── DaySchedule.java
│   │   │   │               │   │   ├── Schedule.java
│   │   │   │               │   │   ├── ScheduleEvent.java
│   │   │   │               │   │   └── Task.java
│   │   │   │               │   └── repository/
│   │   │   │               │       ├── ScheduleRepository.java
│   │   │   │               │       └── TaskRepository.java
│   │   │   │               ├── dialog/
│   │   │   │               │   ├── ScheduleConfirmDialog.java
│   │   │   │               │   └── TaskDetailDialog.java
│   │   │   │               ├── fragment/
│   │   │   │               │   ├── ScheduleFragment.java
│   │   │   │               │   ├── SettingsFragment.java
│   │   │   │               │   └── ToDoListFragment.java
│   │   │   │               ├── network/
│   │   │   │               │   ├── GeminiApiService.java
│   │   │   │               │   ├── GeminiClient.java
│   │   │   │               │   ├── GenerateContentRequest.java
│   │   │   │               │   └── GenerateContentResponse.java
│   │   │   │               ├── receiver/
│   │   │   │               │   └── AlarmReceiver.java
│   │   │   │               ├── util/
│   │   │   │               │   └── ScheduleParser.java
│   │   │   │               ├── viewmodel/
│   │   │   │               │   ├── ScheduleViewModel.java
│   │   │   │               │   └── TaskViewModel.java
│   │   │   │               ├── MainActivity.java
│   │   │   │               ├── NotificationHelper.java
│   │   │   │               └── SplashActivity.java
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── bg_btn_cancel.xml
│   │   │   │   │   ├── bg_btn_save.xml
│   │   │   │   │   ├── bg_day_label.xml
│   │   │   │   │   ├── bg_save_button.xml
│   │   │   │   │   ├── bg_schedule_item.xml
│   │   │   │   │   ├── bottom_sheet_background.xml
│   │   │   │   │   ├── edit_text_background.xml
│   │   │   │   │   ├── ic_add.xml
│   │   │   │   │   ├── ic_circle.xml
│   │   │   │   │   ├── ic_delete.xml
│   │   │   │   │   ├── ic_launcher_background.xml
│   │   │   │   │   ├── ic_launcher_foreground.xml
│   │   │   │   │   ├── ic_notification.xml
│   │   │   │   │   ├── splash_background.JPG
│   │   │   │   │   └── star_icon.png
│   │   │   │   ├── font/
│   │   │   │   │   ├── abril_fatface.xml
│   │   │   │   │   └── abril_fatface_regular.ttf
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_splash.xml
│   │   │   │   │   ├── dialog_add_task.xml
│   │   │   │   │   ├── dialog_schedule_confirm.xml
│   │   │   │   │   ├── dialog_task_detail.xml
│   │   │   │   │   ├── fragment_schedule.xml
│   │   │   │   │   ├── fragment_settings.xml
│   │   │   │   │   ├── fragment_todo_list.xml
│   │   │   │   │   ├── item_schedule.xml
│   │   │   │   │   ├── item_schedule_event.xml
│   │   │   │   │   ├── item_task.xml
│   │   │   │   │   └── layout_fragment_header.xml
│   │   │   │   ├── menu/
│   │   │   │   │   └── bottom_nav_menu.xml
│   │   │   │   ├── mipmap-anydpi/
│   │   │   │   │   ├── ic_launcher.xml
│   │   │   │   │   └── ic_launcher_round.xml
│   │   │   │   ├── mipmap-hdpi/
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-mdpi/
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-xhdpi/
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-xxhdpi/
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-xxxhdpi/
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── navigation/
│   │   │   │   │   └── nav_graph.xml
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   ├── values-night/
│   │   │   │   │   └── themes.xml
│   │   │   │   └── xml/
│   │   │   │       ├── backup_rules.xml
│   │   │   │       └── data_extraction_rules.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── example/
│   │                   └── dotodo/
│   │                       └── ExampleUnitTest.java
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml
├── build.gradle.kts
├── folder_structure_export.py
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── README.md
└── settings.gradle.kts
```
