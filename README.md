# OMDB Movies Android App
An Android app that allows users to search for movies using the Open Movie Database Search API.

#### App Features
* User can search for any movie of their choice
* User can view basic details (title, year, poster) of each movie in the search results 
(Note: full details are visible once the user has selected/viewed a movie to avoid making too many network requests)
* User can select a search result to view the full details of the movie
* User can favorite a movie from the Details view
* User can view & select favorite movies from the search results
* Favorite status is shown in the search results if movie has been added to favorites

#### App Architecture 
Based on MVVM architecture and repository pattern.
 
#### Components:

* The UI (Tabs with fragments + activities), which shows a visual representation of the data in the ViewModel.
* A ViewModel that provides data specific for the UI.
* A repository that works with the database and the api service, providing a unified data interface.
* A local Room database that servers as a single source of truth for data presented to the user. 
* A web API service that connects to OMDB server.
* Unit Test cases for API service, Database, Repository and ViewModel.


#### App Packages
* <b>data</b> - contains 
    * <b>remote</b> - contains the api classes to make api calls to OMDBAPI server, using Retrofit. 
    * <b>local</b> - contains the db classes to cache network data.
    * <b>repository</b> - contains the repository classes, responsible for triggering api requests and saving the response in the database.
* <b>di</b> - contains dependency injection classes, using Dagger2.   
* <b>ui</b> - contains classes needed to display Activity and Fragment.
* <b>utils</b> - contains classes needed for activity/fragment redirection, ui/ux animations.


#### App Specs
* Minimum SDK 21
* [Java8](https://java.com/en/download/faq/java8.xml) (in master branch)
* MVVM Architecture
* Android Architecture Components (LiveData, Lifecycle, ViewModel, Room Persistence Library, Navigation Component, ConstraintLayout)
* [RxJava2](https://github.com/ReactiveX/RxJava) for implementing Observable pattern.
* [Dagger 2](https://google.github.io/dagger/) for dependency injection.
* [Retrofit 2](https://square.github.io/retrofit/) for API integration.
* [Gson](https://github.com/google/gson) for serialization.
* [Okhttp3](https://github.com/square/okhttp) for implementing interceptor, logging and mocking web server.
* [Mockito](https://site.mockito.org/) for implementing unit test cases
* [Picasso](http://square.github.io/picasso/) for image loading.

#### Build Instructions
* Build via command line : <b>./gradlew assembleDebug </b>. Resulting apk can then be found in <b>app/build/outputs/apk/app-debug.apk</b>
* Or Android Studio!
