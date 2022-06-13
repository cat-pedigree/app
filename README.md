<div align="center">

  ![Cat Pedigree](https://github.com/cat-pedigree/.github/blob/main/assets/images/cover.jpg)

  <h1>Cat Pedigree App</h1>
  
  <p>
    CatPedigree is an application created using the Kotlin programming language, this application has the following features: social media to connect cat lovers in one community, classification to classify cat types, pedigree to predict the offspring of crossed cats, veterinary to find a veterinary clinic that available, and cat dating, which provides a platform for cat owners to find their cat mate online. This app uses a self-built API and uses a tensorflow lite model built by machine learning
  </p>
  
<!-- Badges -->
<p>
  <a href="https://github.com/cat-pedigree/app/graphs/contributors">
    <img src="https://img.shields.io/github/contributors/cat-pedigree/app" alt="contributors" />
  </a>
  <a href="">
    <img src="https://img.shields.io/github/last-commit/cat-pedigree/app" alt="last update" />
  </a>
  <a href="https://github.com/cat-pedigree/app/network/members">
    <img src="https://img.shields.io/github/forks/cat-pedigree/app" alt="forks" />
  </a>
  <a href="https://github.com/cat-pedigree/app/stargazers">
    <img src="https://img.shields.io/github/stars/cat-pedigree/app" alt="stars" />
  </a>
  <a href="https://github.com/cat-pedigree/app/issues/">
    <img src="https://img.shields.io/github/issues/cat-pedigree/app" alt="open issues" />
  </a>
</p>
   
<h4>
    <a href="https://github.com/cat-pedigree/app/">Download APK</a>
  <span> · </span>
    <a href="https://github.com/cat-pedigree/app/">Documentation</a>
  <span> · </span>
    <a href="https://www.behance.net/gallery/145755683/Cat-Pedigree-App">User Interface and User Experience</a>
  </h4>
</div>

<br />

<!-- Table of Contents -->
# :notebook_with_decorative_cover: Table of Contents

- [About the Project](#about)
  * [User Interface](#camera-user-interface)
  * [Tech Stack](#space_invader-tech-stack)
  * [How To Clone This Project](#how-to-clone-this-project)
  * [How to Get Google Maps API key](#how-to-get-google-maps-api-key)
- [Getting Started](#getting-started)
  * [Prerequisites](#bangbang-prerequisites)
  * [Installation](#gear-installation)
- [Usage](#cat-pedigree-usage)
- [Contributing](#wave-contributing)
- [Contact](#handshake-contact)
- [Acknowledgements](#gem-acknowledgements)

<!-- About the Project -->
## :star2: About the Project


<!-- User Interface -->
### :camera: User Interface
You can see all the user interface and user experience at the following link

|  ![User Interface](https://github.com/cat-pedigree/.github/blob/main/assets/images/ui/1.png) | ![User Interface](https://github.com/cat-pedigree/.github/blob/main/assets/images/ui/2.png)  |
|---|---|
| ![User Interface](https://github.com/cat-pedigree/.github/blob/main/assets/images/ui/3.png)  | ![User Interface](https://github.com/cat-pedigree/.github/blob/main/assets/images/ui/4.png)  |

### How to clone this project### Clone Manual
- You can clone the `Cat Pedigree App` repository or download the zip file from the app repository by:
```bash
git clone https://github.com/cat-pedigree/app.git
```
- Open In `Android studio`
- Please wait gradle project synchronization
- End

### Clone project in android studio

- Open the android studio
- Click menu `file` -> `new` -> `project from version control`
- Enter the cat pedigree app repository link `https://github.com/cat-pedigree/app.git`
- Please wait gradle project synchronization
- End

### How to Get Google Maps API key
how to get Google Maps API key? Don't worry, Android Studio will provide a link that you can directly use to create a project in Google Console and get an API Key for Google Maps.
- First, sign in to the Google Cloud Console and go to the Credentials tab.
- Check and click AGREE AND CONTINUE to agree to the Terms of Service.
- Then go to the side menu and select API & Services → Credentials.
- Create a new project by clicking CREATE PROJECT and changing the project name to your liking. Click CREATE to continue.
- Next, activate the Google Maps feature by selecting Enabled APIs & Services on the side menu and clicking the + ENABLED APIS AND SERVICES button.
- Various features will appear that you can use in the Google Cloud Console. Select Maps SDK for Android and click Enable.
- Then select the Credentials menu on the side menu and click the CREATE CREDENTIALS → API key button to create a new credential.
- At this point, you have got a key that usually starts with the word “AIza…”. Actually you can already use this API key, but this key is still not secure because any project can use it. To be more secure, click the Edit API key link.
- Select the Android apps checkbox under Application restrictions and add new data by clicking ADD AN ITEM. Then fill in the package name of your application and SHA-1 of the device used.
Notes:
Each device has a different SHA certificate. To get SHA-1, you can run the command gradlew signingreport on Terminal
- When finished, click SAVE and copy the key with the prefix "AIza ..." that has been obtained to the meta-data in the following AndroidManifest.
```xml
  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.catpedigree.capstone.catpedigreebase">
    <application
      ...
        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="YOUR_API_KEY" />
    </application>
    ...
</manifest>
```

<!-- Tech Stack -->
### :space_invader: Tech Stack

 - [Kotlin](#)
 - [Tensorflow Lite](#)
 - [Retrofit](#)
 - [Room](#)
 - [Room Paging](#)
 - [Camerax](#)
 - [Datastore](#)
 - [Gson](#)
 - [Glide](#)
 - [Navigation Dynamic](#)
 - [Viewmodel](#)
 - [Live Data](#)
 - [Navigation UI](#)
 - [Constraint Layout](#)
 - [Circle Image](#)

<!-- Getting Started -->
## 	:toolbox: Getting Started

<!-- Prerequisites -->
### :bangbang: Prerequisites

- Android 9.0 (Pie) API 28
- Internet Connection
- Camera
- GPS

<!-- Installation -->
### :gear: Installation

- Download the APK
- Install the APK
- Create a new account

<!-- Contributing -->
## :wave: Contributing

<a href="https://github.com/cat-pedigree/app/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=cat-pedigree/app" />
</a>

<!-- Contact -->
## :handshake: Contact

| Team ID | C22-PS083                         |
|---------|-----------------------------------|
| Theme   | Human Healthcare & Animal Welfare |


 ![Cat Pedigree Teams](https://github.com/cat-pedigree/.github/blob/main/assets/images/team/team.png)

| ID         | NAME                    | PATH               | University                          | CONTACT |
|------------|-------------------------|--------------------|-------------------------------------|---------|
| M7269J2321 | Bugi Sulistiyo          | Machine Learning   | Universitas Mulawarman              |  <a href="https://www.linkedin.com/in/bugi-sulistiyo/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a> <a href="https://github.com/Bugi-Sulistiyo"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>        |
| M2272J2364 | Zahrizhal Ali           | Machine Learning   | Universitas Muslim Indonesia        |  <a href="https://www.linkedin.com/in/zahrizhal-ali/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a> <a href="https://github.com/ZahrizhalAli"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>       |
| M2208K1922 | Wardatun Sayyidah       | Machine Learning   | Universitas Hasanuddin              | <a href="https://www.linkedin.com/in/wardatun-sayyidah/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a> <a href="https://github.com/wardatunsayyidah"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>          |
| A2152G1665 | Robby Ramadhan Anshory  | Mobile Development | Sekolah Tinggi Teknologi Bandung    | <a href="https://www.linkedin.com/in/robby-ramadhan-anshory-99b338228/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a> <a href="https://github.com/RobbyRamadhanAsnhory"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>        |
| A7314F2733 | Budi Setiawan           | Mobile Development | Universitas Singaperbangsa Karawang | <a href="https://www.linkedin.com/in/budi-setiawan15/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a><a href="https://github.com/budistwn15"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>         |
| C7312F2682 | Fina Enno Rizki Oktavia | Cloud Computing    | Universitas Sebelas Maret           |  <a href="https://www.linkedin.com/in/finaenno/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a><a href="https://github.com/finaenno"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>        |


<!-- Acknowledgments -->
## :gem: Acknowledgements

This section mentions useful resources and libraries that have been used in the project.

 - [Figma](https://www.figma.com/)
 - [Android Studio](https://developer.android.com/studiohl=id&gclid=CjwKCAjwtIaVBhBkEiwAsr7c0muNv5iZXQPkbdA4XrY5ocVx01Vn6jRX4bPPYCkP7DKGSJcbYpecBoCtqYQAvD_BwE&gclsrc=aw.ds)
 - [Tensorflow Lite](https://www.tensorflow.org/lite/android/quickstart)
 - [Github](https://github.com/)
 - [Google Fonts](https://fonts.google.com/)
 - [Undraw](https://undraw.co/)
 - [Story Set](https://storyset.com/)
 - [Material IO](https://material.io/)
 - [Flaticon](https://www.flaticon.com/)
 - [Maze](https://maze.co/)

