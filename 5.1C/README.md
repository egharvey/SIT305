# 5.2C
This folder contains 2 apps developed for Task 5.2C, SportsNews and iStream.

# SportsNews
This is a news app. It uses multiple recycler views and fragments to do everything in a single activity. It also allows for articles to be searched for and bookmarked. One problem with it at the moment is that it is using dummy data, not real data. As such, while it was initially designed with Room in mind, it was not implemented, resulting in poorer performance and data handeling.

## Important Files
- **SportsNews\app\src\main\java\com\example\sportsnews** contains all Java files running the app, including classes, activities and fragments
- **SportsNews\app\src\main\res\layout** contains all xml files defining layouts for items such as recycler view cards, fragments and activities.
- **SportsNews\app\src\main\res\drawable** contains images used in the user interface.
- **SportsNews\app\src\main\res\values** contains all defined colours for the application.

# iStream
This is an app that allows users to sign in (account details stored via Room persistence), and view YouTube videos via a link they have (using YouTube's API in a Web View). In addition, they can save videos to their playlist, which they can view whenever they want (in the form of a recycler view).

## Important Files
- **iStream\app\src\main\java\com\example\sportsnews** contains all Java files running the app, including classes, activities and fragments
- **iStream\app\src\main\res\layout** contains all xml files defining layouts for items such as recycler view cards, fragments and activities.
- **iStream\app\src\main\res\drawable** contains images used in the user interface.
- **iStream\app\src\main\res\values** contains all defined colours for the application.