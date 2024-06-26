# Weather App with Database Integration

This project implements a weather app using a free weather API to download weather data based on user input. Additionally, it incorporates functionality to store this data in a local database, enabling access even without network connectivity. The app is implemented in Kotlin/Java and utilizes Gradle for project management

## Features

### Q1: Weather App with API

1. Utilizes a free weather API to download weather data.
2. Accepts date and year from the user as input.
3. Displays the maximum and minimum temperature for the given date.
4. Parses JSON files received from the API to extract relevant data.
5. Provides a user-friendly interface for input and output.
6. Validates user input and handles errors gracefully.

### Q2: Database Integration

1. Creates a database and schema to store weather data.
2. Inserts downloaded weather data into the database.
3. Supports querying the database to retrieve maximum and minimum temperatures for a given date.
4. Calculates temperatures for future dates by averaging the last 10 available years' data.
5. Checks for network connectivity and adapts functionality accordingly.
6. Provides informative error messages and ensures correct app behavior.

## Submission Contents

1. **Source Code**: Kotlin/Java files along with Gradle configuration. Composition and XML files are used where appropriate. XML files contain identifiers used for necessary changes.
2. **README**: This document explaining the implementation details.
3. **GitHub Repository**: Submission must be uploaded to a private GitHub repository, with access granted to the evaluator and submitted to both Google Classroom and GitHub as instructed.

## Implementation Details

### Q1: Weather App with API

- The app uses Retrofit library for network requests to interact with the weather API.
- User input is validated to ensure proper date and year format.
- JSON response from the API is parsed to extract relevant weather data.
- A simple UI is designed to facilitate user interaction and display weather information.

### Q2: Database Integration

- Room Persistence Library is utilized to handle database operations.
- Upon receiving weather data from the API, it is stored in the local database.
- Queries are implemented to retrieve temperature data from the database based on user input.
- Logic is implemented to handle future dates by averaging past data.
- Network connectivity is checked to determine whether to fetch data from the API or the local database.

## 

# Mars Photos ViewModel

This ViewModel is designed to manage data for the Mars Photos application. It fetches Mars photos information from the Mars API, allows users to select a date and location, and handles the UI state accordingly. This ViewModel is written in Kotlin and is compatible with Android development.

## Features

- Retrieves Mars photos information from the Mars API Retrofit service.
- Allows users to select a date and location for retrieving photos.
- Handles UI state such as loading, success, and error states.
- Validates user input for item details.
- Inserts item details into the database using the provided repository.
- Provides conversion functions between item details and item objects.

## Usage

### MarsViewModel

### Properties

- `itemUiState`: Manages the status of the most recent request for item details.
- `marsUiState`: Manages the status of the most recent request for Mars photos.
- `selectedDate`: Represents the selected date for retrieving Mars photos.
- `plocation`: Represents the selected location for retrieving Mars photos.
- `listResult`: Stores the retrieved Mars photos information.

### Methods

- `updateUiState(itemDetails: ItemDetails)`: Updates the item UI state with the provided item details and validates input.
- `saveItem()`: Saves the item if input validation succeeds.
- `updateSelectedDate(calendar: Calendar)`: Updates the selected date for retrieving Mars photos.
- `updatelocation(loc: String)`: Updates the selected location for retrieving Mars photos.
- `getlocation(): String`: Retrieves the selected location for retrieving Mars photos.
- `getMarsPhotos()`: Fetches Mars photos information from the Mars API based on selected date and location.

### Extensions

- `toItem()`: Converts `ItemDetails` to `Item`.
- `toItemUiState(isEntryValid: Boolean)`: Converts `Item` to `ItemUiState`.
- `toItemDetails()`: Converts `Item` to `ItemDetails`.

## ViewModel Factory

The ViewModel is created using the provided `viewModelFactory` which initializes the ViewModel with the necessary repository.

![Untitled](Weather%20App%20with%20Database%20Integration%20e4ae787f5df94741b9d381524e460c68/Untitled.png)

# Mars Photos Network Service

This Kotlin file contains the definition of the Retrofit service interface `MarsApiService`, which is responsible for making network requests to retrieve Mars photos information from a remote server. Additionally, it provides constants and configuration for setting up the Retrofit client.

## Features

- Defines a Retrofit service interface `MarsApiService` with methods for fetching Mars photos data.
- Provides constants for the base URL, API key, and JSON serialization.
- Configures and builds the Retrofit client with appropriate converters and base URL.

## Usage

### MarsApiService Interface

### Methods

- `getPhotos(location: String, startDateTime: String, aggregateHours: Int, unitGroup: String, contentType: String, dayStartTime: String, dayEndTime: String, apiKey: String)`: Makes a GET request to retrieve Mars photos data from the specified location and start date-time.

### Constants

- `BASE_URL`: Base URL for the Mars photos API.
- `API_KEY`: API key required for accessing the Mars photos API.
- `json`: JSON serializer/deserializer configuration for Kotlin serialization library.

### Retrofit Configuration

- Configures Retrofit client with appropriate JSON converter factory and base URL.

## Retrofit Setup

To use this service, ensure you have Retrofit and Kotlin serialization dependencies added to your project. Then, create a Retrofit instance with the provided configuration and use it to create an instance of `MarsApiService`.

![Untitled](Weather%20App%20with%20Database%20Integration%20e4ae787f5df94741b9d381524e460c68/Untitled%201.png)

![Untitled](Weather%20App%20with%20Database%20Integration%20e4ae787f5df94741b9d381524e460c68/Untitled%202.png)

![Untitled](Weather%20App%20with%20Database%20Integration%20e4ae787f5df94741b9d381524e460c68/Untitled%203.png)

![Untitled](Weather%20App%20with%20Database%20Integration%20e4ae787f5df94741b9d381524e460c68/Untitled%204.png)

![Untitled](Weather%20App%20with%20Database%20Integration%20e4ae787f5df94741b9d381524e460c68/Untitled%205.png)