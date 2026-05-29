# TravelApp

Мобильное приложение для поиска и бронирования экскурсий (TravelApp) и панель администратора (TravelAdmin).

## Структура проекта

```
TravelApp/
├── core/                          # Общие модули
│   ├── common/                    # ScreenDestination, DisplayableItem
│   ├── location/                  # LocationClient (GPS)
│   ├── uikit/                     # Общие UI-компоненты (TravelApp)
│   ├── sync/                      # (пусто)
│   └── worker/                    # (пусто)
├── applications/
│   ├── TravelApp/                 # Основное приложение (туры, поиск)
│   │   ├── core/
│   │   │   ├── network/           # Retrofit, API, модели
│   │   │   └── cache/             # Room (города)
│   │   ├── feature/
│   │   │   ├── home/              # Главный экран
│   │   │   ├── search/            # Поиск
│   │   │   ├── favorites/         # Избранное
│   │   │   ├── allproducts/       # Все туры
│   │   │   └── productdetail/     # Детали тура
│   │   └── navigation/            # NavHost, роуты
│   └── TravelAdmin/               # Админ-панель
│       ├── core/
│       │   ├── pushing/           # FCM Push (отправка)
│       │   └── uikit/             # UI-компоненты админки
│       └── feature/
│           └── notification/      # Отправка push-уведомлений
```

## Архитектура (MVI)

Каждый экран следует паттерну **Model-View-Intent**:

- **State** — единый иммутабельный `data class` с UI-состоянием
- **Action** — sealed interface для всех действий пользователя
- **ViewModel** — обрабатывает Action, обновляет State через `MutableStateFlow`
- **Screen** — `@Composable`, observes State через `collectAsStateWithLifecycle()`

```
User → Action → ViewModel → State → UI
                    ↑
              Use Case → API
```

## TravelAdmin: Push-уведомления (FCM)

Админ-панель позволяет отправлять push-уведомления через Firebase Cloud Messaging.

### Возможности
- **Отправка на все устройства** — через топик `/topics/all`
- **Отправка на конкретное устройство** — по FCM Token
- **Настраиваемый Server Key** — вводится в интерфейсе

### Как использовать
1. Получить Server Key в [Firebase Console](https://console.firebase.google.com) (Project Settings → Cloud Messaging)
2. Открыть админ-панель
3. Ввести Server Key, заголовок и текст уведомления
4. Выбрать получателя (все / конкретное устройство)
5. Нажать "Отправить"

### FCM API
```
POST https://fcm.googleapis.com/fcm/send
Authorization: key=<SERVER_KEY>
{
  "to": "/topics/all" | "<DEVICE_TOKEN>",
  "notification": {
    "title": "...",
    "body": "..."
  }
}
```

## Технологии

- **Язык:** Kotlin
- **UI:** Jetpack Compose + Material3
- **Архитектура:** MVI (Unidirectional Data Flow)
- **DI:** Hilt
- **Сеть:** Retrofit + OkHttp
- **Навигация:** Navigation Compose (type-safe routes)
- **Локальная БД:** Room
- **Изображения:** Coil
- **Push:** Firebase Cloud Messaging (FCM)
