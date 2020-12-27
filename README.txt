This app was created for a class assignment Spring 2020.

Herpetology is a branch of zoology concerning reptiles (snakes, lizards, turtles, crocodilians, etc) and amphibians (frogs, toads, salamanders ect). On the less academic side, there is a hobby known as "herping" that I enjoy, which is the act of searching for reptiles and amphibians.

Herpalooza is an app for herpers (those who like finding reptiles and amphibians) to organize photos and information about the herps (said reptiles and amphibians) they find or are looking for. They can enter a name for their herp (nickname or common name), type, species, age, sex, location found (town, park, etc), habitat found (under a rock, pond, etc), notes, and a picture.

There are settings for hiding found herps, only showing one type of herp, and changing how the herps are sorted in the list. Settings and info accessible by menu in detail and main.

The backbone of the app is a recycler view of all the herps, with name, picture, type (lizard, snake, etc),
and a found checkbox. There is a menu to add new, as well as delete all. Details include all that info plus species, age, sex, location found, and notes.

There is also ability to edit an entry (found in detail fragment). This allows users to fix typos or add new information to an entry without having to complelty start over. And yes, it even works with pictures! The only pitfall is you have to "refresh" the details page after your edits for them to appear (or simply go back to the main list and back). There is a refresh button built into the menu for this purpose, which is only availible on the detail fragment.

Other minor features include a toast for when an entry is deleted and pictures on the main list.

Photos are intended to be taken in landscape, though the app is portrait locked.