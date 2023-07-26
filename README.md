# Simple ToDo List

## Dependencies

**UI:** Jetpack Compose, Lifecycle

**Data:** Room

**Unit Test:** JUnit4, Coroutine Test, Robolectric, ~~Expresso~~, ~~Compose Test~~

## Documentation

Based on **MVVM** architecture of android application which use Compose as the UI Element, packages divided by **UI** and **Data** folder. Although, in same level has file, it is application-level file for distribute global dependencies.

### UI

The UI package focusing on appearance management *without any include data functionality*. Just focuses for showing the best application can do. Contains **activities**, **theme**, and **widget** as the top level.

Each activities needs to create their own folder, in this case has **main** and **splash**. Inside main has **Activity**, **Screen**, and **ViewModel**.

For theme folder, it focused on how globally appearance looks like. For **widget** folder just to save all compose that used in whole application. As example in this app has **task** and **common**.

Each widget's folder has their own functionality as composable component and another component can't bother or join it's functionality.

### Data

Otherwise, Data packages really important. It contains **repository**, **source**, **model**, and **local**. As it name, it has its own functionality to manage data. Dividing between repository and source (which source just full of interface) is big deal, because helps a lot during automation test. Model folder just common data based file, and local is to save room file.

During implementation, I used **domain** splitting to make sure integrity of each entity data. So inside local package, has *RoomDatabase* object instance (**`TodoDatabase`**) and folder of every entity. Inside that folder contains *Dao* and *Entity*

Entity file is different from Model file, because their functionality already different. So, model domain used as UI driven data, and entity domain to handle local data. They can communicate each other by mapping domain.

*I have opinion, this UI-Data structure is the most completely effective in my references*

## Architecture

As I was mentioning about, local data communication into app is asynchronous using Flow. Every value changed, the flow already know and provided to screen in asynchronous way. This transaction happened inside coroutines. 

Before value given to receiver, they need to converted and be wrapped using Result to safely going through around repository-viewmodel-compose. This is really nice because it just not making sure the data safe with *single-source of truth* but also make is *stateful* with its own method.

**base data communication diagram**
![data communication](https://github.com/DaniZakaria63/simple-todo-list/assets/43902417/827f2fa5-09c5-43fa-baa7-59865e64f586)

**event communication diagram**
![event communication](https://github.com/DaniZakaria63/simple-todo-list/assets/43902417/0ee1b493-b593-426f-b11a-0c7c3717ebc0)


That image is not completely strange, although it’s pretty boring. But this is what happened. Inside of event communication image, **`MainScreen`** has hot data with sealed object with many event triggered into it. So the events are hoisted into Screen-Level component, and for me it’s really good because each component cannot be process data for its own. They need to bring it up until met their parent. Centered processing event is best way to being used in this application.

## Running Tests

Test using JUnit4 with Coroutine Test. All the file have inside **`testUnit`** section. This project using Flow with Sealed data, and it makes hard to test. The characteristic of Flow which more asynchronous than **`LiveData`** used perfectly fit with UI, and highly cost for testing. I am still looking for more information about that.

## Authors

[@DaniZakaria63](https://www.github.com/DaniZakaria63)

Notes:

```
Hello readers, I was knew composable at Monday, 24 July 2023. 
This project shared at July, 26, so in 2 days I was working really hard,
eventhough mostly I used View instead of Compose, but I want to learn.
This really good opportunity, and I really appreciate your feedback 
about layout or Composable structure. I will waiting for you, friends.
```
