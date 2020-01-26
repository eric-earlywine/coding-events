# coding-events
The purpose of this app is to create events, give information about the events, allow users to sign-up and follow the events and create their own calendar of events that they will be attending.

Currently, the app only allows creation of events and categories for the events. TODO: User sign-up and the ability to add events to a personal calendar.

To achieve that:

A Person class must be created with fields of username, password, email, listOfEventsAttending, and perhaps interests (which would contain eventCategories or maybe tags of some of their personal interests in order to potentially recommend certain events or help them find events they'd be interested in.) Methods would include getters and setters, as well as an addEven and delEvent method.

A Tag class would need to be created if going the route of tags for interests instead of eventCategories.

Person would have a ManyToMany relationship with Events as well ManyToMany with tags or eventCategories depending on which is decided upon.
