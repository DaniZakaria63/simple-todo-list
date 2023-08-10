# Simple ToDo List
<img src="https://github.com/DaniZakaria63/simple-todo-list/assets/43902417/fed7ab46-f2b5-4ae3-b233-ac03ea5da23b" width="666" height="666">

## Dependencies

**UI:** Jetpack Compose, Lifecycle-Compose, Material Design 3

**Data:** Room Database Persistance, ViewModel, Repository

**Unit Test:** JUnit4, Mockito, Coroutine Test, Robolectric, Compose Test

## Documentation

Based on **MVVM** architecture of android application which use Compose as the UI Element, packages divided by **UI** and **Data** folder. Although, in same level has file, it is application-level file for distribute global dependencies. This project implement **Clean Architecture** for distributing data and state, further explanation given below.

### UI

The UI package focusing on appearance management *without any include data functionality*. Contains **activities**, **theme**, and **widget** as the top level.

Each activities needs to create their own folder, in this case has **main** and **splash**. Inside main has **Activity**, **Screen**, and **ViewModel**.

For theme folder, it focused on how globally appearance looks like. For **widget** folder just to save all compose that used in whole application. As example in this app has **task** and **common**.

Each widget's folder has their own functionality as composable component and another component can't bother or join it's functionality.

### Data

Otherwise, Data packages contains **repository**, **source**, **model**, and **local**. As it name, it has its own functionality to manage data. Dividing between repository and source (which source just full of interface) is big deal, because helps a lot during automation test. Model folder just common data based file, and local is to save room file.

During implementation, I used **domain** splitting to make sure integrity of each entity data. So inside local package, has *RoomDatabase* object instance (**`TodoDatabase`**) and folder of every entity. Inside that folder contains *Dao* and *Entity*

Entity file is different from Model file, because their functionality already different. So, model domain used as UI driven data, and entity domain to handle local data. They can communicate each other by mapping domain.

## Architecture

As I was mentioning about, local data communication into app is asynchronous using Flow. Every value changed, the flow already know and provided to screen in asynchronous way. This transaction happened inside coroutines. 

Before value given to receiver, they need to converted and be wrapped using Result to safely going through around repository-viewmodel-compose. This is really nice because it just not making sure the data safe with *single-source of truth* but also make is *stateful* with its own method.


<p align="center">
  
<img src="https://github.com/DaniZakaria63/simple-todo-list/assets/43902417/827f2fa5-09c5-43fa-baa7-59865e64f586" width="369" height="369">

<img src="https://github.com/DaniZakaria63/simple-todo-list/assets/43902417/0ee1b493-b593-426f-b11a-0c7c3717ebc0" width="369" height="369">

</p>

<p align="center">
Base data communication diagram
.. Event communication diagram
</p>

That image is not completely strange, although it’s pretty boring. But this is what happened. Inside of event communication image, **`MainScreen`** has hot data with sealed object with many event triggered into it. So the events are hoisted into Screen-Level component, and for me it’s really good because each component cannot be process data for its own. They need to bring it up until met their parent. Centered processing event is best way to being used in this application.

## Running Tests

Test using JUnit4 with Coroutine Test. All the file have inside **`testUnit`** section. This project using Flow with Sealed data, and it makes challenging to test. Testing repository using fake data method. Creating fake task data source that has behavior as **`TaskDao`** and **`TypeDao`** as well. This way make test looks more realistic. Then **`ViewModel`** has tested with **`Mockito`** for mock behavior of repository. Every method in repository that contribute in **`ViewModel`** got stabbed and provide what it should return.

## Single Author

[DaniZakaria63](https://www.github.com/DaniZakaria63)
