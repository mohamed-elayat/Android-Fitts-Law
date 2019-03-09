# Fitts' Law Test Android Game

Game that tests how closely the user's reaction time follows Fitts' Law.

&nbsp;
&nbsp;

## About

One of the projects for our User Interface class. The goal of the game is to click on the button as fast as possible.
After each click, the button randomly changes its size and position. After a series of 20 clicks,
a results page is shown. The user can see the details of each individual trial
as well as the how closely his trials form a linear graph. Additionally, clicking 
on the visualize button will display that graph for the user.


## Running the app

Android Studio needs to be installed to run the app. To run the app from Android Studio, do the following: 

1. Download a ZIP of the GitHub project.
2. Unzip the GitHub project to a folder.
2. Open Android Studio.
3. Go to File -> New -> Import Project. Then choose the unzipped folder and click Next -> Finish.
4. Go to Build -> Rebuild project.
6. Go to Run -> Run (or press Shift + F10).
7. Either connect an Android phone to run it on, or download an emulator. (Might take a few minutes to build).

P.S: In some versions of Android Studio a certain error might occur: 
&nbsp;     
"error:package android.support.v4.app does not exist"
&nbsp;    
&nbsp;   
To fix it, go to Gradle Scripts -> build.gradle(Module:app) and add the following dependencies:

dependencies {      
    compile fileTree(dir: 'libs', include: ['*.jar'])  
    compile 'com.android.support:appcompat-v7:21.0.3'  
}


## Demo

Click on the following image to be redirected to the youtube video.

[![Demo of the app. Click on this to be redirected](https://i.imgur.com/QNHAO9B.jpg)](https://www.youtube.com/watch?v=ARpZUwPSY4A)

&nbsp;  
&nbsp;  
&nbsp;    

#### Authors

Mohamed Elayat  
Pierre Luc Munger  
Arnaud L'heureux
