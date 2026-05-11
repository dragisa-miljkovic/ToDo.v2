# Тренутно стање апликације (ToDotmpV4)

Овај документ описује **тренутно, радно стање** апликације након преласка на Compose-first приступ, Room шему са `String`/`Long` моделом и исправно учитавање базе из `assets`.

---

## 1) Техничка основа

- Језик и алати:
  - Kotlin `2.1.20`
  - KSP `2.1.20-2.0.0`
  - AGP `8.8.1`
- UI:
  - Jetpack Compose
  - Material 3
  - Navigation Compose
- Подаци:
  - Room `2.7.0`
  - Coroutines + Flow
- Build подешавања:
  - Java source/target: `17`
  - Kotlin JVM target: `17`

Релевантни фајлови:
- `build.gradle.kts`
- `gradle/libs.versions.toml`
- `app/build.gradle.kts`

---

## 2) Архитектура апликације

Апликација је организована у класичан modern Android ток:

`Room -> Repository -> ViewModel (StateFlow) -> Compose UI`

- `MainActivity` је host и користи `setContent`.
- `ToDoApp` подиже `ToDoNavHost`.
- Екрани су Compose функције, нема `Fragment`/XML UI пута.

Релевантни фајлови:
- `app/src/main/java/rs/ac/ftn/todotmp/MainActivity.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/ui/ToDoApp.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/navigation/ToDoNavHost.kt`

---

## 3) Модел и база података

## 3.1 Модел

`Zadatak` ентитет:
- `id: String`
- `naslov: String`
- `opis: String`
- `dateMillis: Long`
- `daLiJeResen: Boolean`
- `photoUri: String? = null`

Фајл:
- `app/src/main/java/rs/ac/ftn/todotmp/Zadatak.kt`

## 3.2 Room

- `@Database(entities = [Zadatak::class], version = 1, exportSchema = false)`
- База се креира из `assets` фајла.
- Назив базе: `zadatak-bazapodataka`.

Фајлови:
- `app/src/main/java/rs/ac/ftn/todotmp/bazapodataka/ZadaciBazaPodataka.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/RepozitorijumZadataka.kt`
- `app/src/main/assets/zadatak-bazapodataka`

## 3.3 DAO API

- `getZadaci(): Flow<List<Zadatak>>`
- `getZadatak(id: String): Flow<Zadatak?>`
- `dodajZadatak(zadatak: Zadatak)`
- `azurirajZadatak(zadatak: Zadatak)`
- `obrisiZadatak(zadatak: Zadatak)`

Фајл:
- `app/src/main/java/rs/ac/ftn/todotmp/bazapodataka/ZadatakDao.kt`

---

## 4) Repository слој

`RepozitorijumZadataka`:
- читање је реактивно (`Flow`),
- write операције су `suspend`,
- иницијализација иде преко `ToDoAplikacija`.

Фајлови:
- `app/src/main/java/rs/ac/ftn/todotmp/RepozitorijumZadataka.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/ToDoAplikacija.kt`
- `app/src/main/AndroidManifest.xml`

---

## 5) Навигација

Руте:
- `task_list`
- `task_new`
- `task_details/{taskId}`

Навигациони ток:
- Листа -> Детаљи (клик на ставку)
- Листа -> Нови задатак (FAB)
- Детаљи/Нови -> назад (`navigateUp`)

Фајлови:
- `app/src/main/java/rs/ac/ftn/todotmp/navigation/ToDoRoutes.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/navigation/ToDoNavHost.kt`

---

## 6) UI слој (Compose)

## 6.1 Листа задатака

Имплементирано:
- `Scaffold` + `TopAppBar`
- `LazyColumn` са `key = id`
- Празно стање („Нема задатака“)
- FAB за додавање
- Checkbox за toggle статуса
- UX корекција: клик за детаље је на текстуалном делу ставке, checkbox мења статус

Фајлови:
- `app/src/main/java/rs/ac/ftn/todotmp/ui/tasklist/TaskListScreen.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/ui/tasklist/TaskListViewModel.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/ui/tasklist/TaskListUiState.kt`

## 6.2 Детаљи задатка

Имплементирано:
- Наслов, опис
- Датум (приказ + избор преко дијалога)
- Статус завршености
- Save
- Delete (за постојећи задатак)
- Share action (implicit intent)

Фајлови:
- `app/src/main/java/rs/ac/ftn/todotmp/ui/taskdetails/TaskDetailsScreen.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/ui/taskdetails/TaskDetailsViewModel.kt`
- `app/src/main/java/rs/ac/ftn/todotmp/ui/taskdetails/TaskDetailsUiState.kt`

---

## 7) ViewModel и стање

## 7.1 TaskListViewModel

- Слуша `repository.getZadaci()`.
- Пакује податке у `TaskListUiState`.
- Имплементира `toggleCompleted`.

## 7.2 TaskDetailsViewModel

- Ако постоји `taskId`, учитава један задатак из Flow-а.
- Држи `TaskDetailsUiState`.
- Имплементира измене поља:
  - `updateTitle`
  - `updateOpis`
  - `updateCompleted`
  - `updateDate`
- `save()` ради insert/update.
- `delete()` ради брисање.
- `shareText()` генерише текст за дељење.

---

## 8) Већ имплементиране функционалности (са становишта корисника)

- Учитавање почетних података из базе.
- Приказ листе задатака.
- Отварање детаља задатка.
- Креирање новог задатка.
- Измена постојећег задатка.
- Брисање постојећег задатка.
- Toggle „завршен“ са листе.
- Избор датума.
- Дељење задатка (`ACTION_SEND`).

---

## 9) Позната ограничења/дуг

- Date picker је тренутно Android `DatePickerDialog` позван из Compose екрана, није Material 3 Compose `DatePicker`.
- Share action тренутно користи текстуалну иконицу (`↗`) уместо стандардне Material иконе.
- Нема претраге/филтера у листи.
- `photoUri` постоји у моделу, али фото функционалност није имплементирана.
- Нема DataStore подешавања (сортирање, филтери, show completed preference).

---

## 10) План за наредну имплементацију

Ово је план који је био предвиђен као следећи корак после тренутне верзије:

### 10.1 Фото из галерије (први следећи корак)

Циљ:
- Омогућити одабир фотографије за задатак и упис `photoUri` у базу.

Кораци:
- Додати `rememberLauncherForActivityResult` + `PickVisualMedia`.
- У `TaskDetailsScreen` додати action/дугме за избор слике.
- Додати у `TaskDetailsViewModel` метод за ажурирање `photoUri`.
- Save/update треба да чува `photoUri`.
- Приказ слике на details екрану (прво минимално; по потреби касније Coil `AsyncImage`).

### 10.2 Претрага/филтер листе

Циљ:
- Додати једноставан механизам за проналажење и/или филтрирање задатака.

Кораци:
- Проширити `TaskListUiState` са `query` и/или филтером.
- У `TaskListViewModel` применити филтрирање над текућом листом.
- Додати search поље у `TaskListScreen`.
- Опционо касније пренети филтрирање у DAO (ако буде потребно за веће листе).

### 10.3 Полирање UX-а

Циљ:
- Довести UI у стабилније наставно стање.

Кораци:
- Material иконе у app bar-у (back/share/delete).
- Боља валидација за празан наслов (визуелни feedback).
- Мала дорада текстова/форматирања датума.

### 10.4 DataStore (опционо)

Циљ:
- Подешавања која не припадају Room-у.

Кораци:
- `show completed`
- `sort order`
- (опционо) theme preference

---

## 11) Кратак статус

Тренутна верзија је стабилна основа модерне Compose-first апликације са Room базом и core CRUD функционалностима. Следећи приоритет је фото функционалност, затим претрага/филтер, па UX полирање и опционално DataStore.
