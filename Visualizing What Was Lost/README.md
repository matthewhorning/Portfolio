# Visualizing What Was Lost

Visualizing What Was Lost is a multiplatform, react based progressive web application. The application features an interactable map with Google Maps style pins which, when interacted with, pull up information on the location and what used to be there prior to construction of I-64. Users, if within 100 meters of a location, can take a picture of what is currently at the location and upload the picture to our database so other users can see and compare.

### Project Date: *Fall 2021 & Spring 2022*

## My Responsibilities

I worked primarily on the frontend and on Android app support. This included the creation of the pop-up card which shows the context for the application on each start-up as well as the location information pop-up which shows when tapping a pin. The frontend was created using Capacitor, Ionic by Capacitor, SwiperJS, and the Mapbox API for the map.

## Screenshots

[Album of screenshots](https://photos.app.goo.gl/NDx1QdgGzgyyRqQo6)

![Screenshot of onboarding card.](https://lh3.googleusercontent.com/pw/AM-JKLVQcQVwt2EwkJvfvF-X_eHHLtf3k6TiDS8sQleUPR9jLozkyJhDfzsuf6xZ3zPq_2PqiBiKwDB_xTM2q0RMUFTtvwq8CsSHy5GIU3cwNez8BSGhF9HXvovvbZmkPkkqelTN3x_UeAXdcROma9YXfbQ6=w684-h1368-no?authuser=2)
![Screenshot of information pop-up.](https://lh3.googleusercontent.com/pw/AM-JKLV2IWIhgSs1YXb_kiSYa8cL7XU2ghZZGomanmFrJVVkD6eV5uvDU2LX41GJJntLxPf5QnPAKAk2MEBSm7kOCetvT7MLTQ5VecZZZ3F7CpdzDsOwt9him_dRYZztwPzq4rLZ3ZHOHcOcI5eNzUBJcifB=w684-h1368-no?authuser=2)
![Screenshot of map view.](https://lh3.googleusercontent.com/pw/AM-JKLWTGEp-in9qjHEfzGnLuQkAZDJ6l0jajx2d-omwlYEtmRpNNFyghoS84N2g3_TMu2aJkdn5k_HRyl0-D14uWLe6vcPgRw-A6AzDY47afyVVUBnox5tQrzSfUSSZvviRMpxDAj9MrJzJsxtxkwaCnrM3=w684-h1368-no?authuser=2)

**Build Instructions**

```
npm install
npm run build
npx cap run android or npx cap run ios
```