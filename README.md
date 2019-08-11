<h1 align="center">
  <br>
  <a href="http://www.amitmerchant.com/electron-markdownify"><img src="https://raw.githubusercontent.com/nedimf/maildroid/master/maildroid.png" alt="Markdownify" width="200"></a>
  <br>
  Maildroid
  <br>
</h1>

<h4 align="center"> :tada: Maildroid is a small robust android library for sending emails using SMTP server :tada:</h4>
<p align = "center">
<a href="https://android-arsenal.com/details/1/7818"><img src="https://img.shields.io/badge/Android%20Arsenal-Maildroid-green.svg?style=flat-square" border="0" alt="Android Arsenal"></a>	
</p>
<br>
<p align="center">

<img alt="JitPack" src="https://img.shields.io/jitpack/v/github/nedimf/maildroid?color=%230000&style=flat-square">
<a href="https://www.codacy.com/app/nedx45/maildroid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nedimf/maildroid&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/0e4b1eea545649cbbc459d0a54bf64af"/></a>
<img alt="JitPack - Downloads" src="https://img.shields.io/jitpack/dw/github/nedimf/maildroid?&style=flat-square">  
<img alt="Twitter Follow" src="https://img.shields.io/twitter/follow/_nedimf?color=%23&label=Follow&style=social">
	
	
</p>

<p align="center">
  <a href="#key-features">Key Features</a> •
  <a href="#add-to-your-project">Add to your project</a> •
  <a href="#documentation">Documentation</a> •
  <a href="#development">Development</a> •
  <a href="#faq">FAQ</a>
</p>

![screenshot](https://raw.githubusercontent.com/nedimf/maildroid/master/show.gif)
Library is using Oracle [Java Mail API](https://javaee.github.io/javamail/Android) to handle connections and sending emails.

### Key Features

- Sending emails using SMTP protocol :incoming_envelope:
- Compatible with all smtp providers :tada:
- Sending HTML/CSS styled emails :art:
- Library is using Java Mail API that is well known as best library for sending emails :telescope:

## Add to your project 
Maildroid is hosted on [JitPack](https://jitpack.io) and it's quite easy to integrate in to your project.
Maildroid requires at least Android API level 19 [Android KitKat](https://en.wikipedia.org/wiki/Android_version_history#Android_4.4_KitKat_.28API_19.29) 

How do you want to integrate Maildroid into your project

<details><summary><b>Gradle</b></summary>
<p>
Add this to your root.gradle file  

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add dependency
```gradle

dependencies {
	        implementation 'com.github.nedimf:maildroid:0.0.1'
          }
```

</p>
</details>

<details><summary><b>Maven</b></summary>
<p>

Add the JitPack repository to your build file
```
<repositories>
   <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Add the dependency

```maven

<dependency>
     <groupId>com.github.nedimf</groupId>
     <artifactId>simple-java-mail</artifactId>
     <version>Tag</version>
</dependency>
```
</p>
</details>

## Add to your app
Adding Maildroid to your app is straight forword process. Library is using Builder pattern to achieve flexebilty and easy to read wholesome implementation
```kotlin
  MaildroidX.Builder()
            .smtp("")
            .smtpUsername("")
            .smtpPassword("")
            .smtpAuthentication()
            .port("")
            .type("")
            .to("")
            .from("")
            .subject("")
            .body("")
            .attachment("")
            .onCompleteCallback(object : MaildroidX.onCompleteCallback{
                override fun onSuccess() {
                    //Place for your code when email is sent successfully!
                }
                override fun onFail() {
                   //Place for your code when email is not sent!
                }
            },3000)
            .mail()
```
### Documentation
***
#### Documentation for version v.0.0.1
- smtp
> Constructor that is used to declare SMTP server your will use **(String)** 
- smtpUsername
> Constructor that is used to declare SMTP username of your server  **(String)** 
- smtpPassword
> Constructor that is used to declare SMTP password of your server  **(String)** 
- smtpAuthentication
> Constructor that is used to declare if your server needs authentication **(Boolean)** 
- port
> Constructor that is used to declare port of your server **(String)**
- type
> Constructor that is used to declare type of your content  **(String)**
  >- HTML 
  >- PLAIN
- to 
> Constructor that is used to declare internet adress where email will be send  **(String)** 
- from
> Constructor that is used to declare internet adress witch email is sent from. It s fully supporting @no_replay or not existent email adresses **(String)** 
- subject 
>  Constructor that is used to declare subject of email your sending **(String)** 
- body
> Constructor that is used to declare body of email your sending **(String)** 
- attachment
> Constructor that is used to declare attachment of email in case that ones need to be added. **(String)** 
>- **Attachment in version 0.0.1 is single supported** in next version more attachments will be supported and they will  	   be type of an **ArrayString** 

- onCompleteCallback ()
> When sending email is done, call this constructor to handle further actions in your app. 
> Constructor is made out of two functions
>- onSuccess() that handles when email is succssfully sent
> - onFail() that handles any error in sending email
> - timeout value that is used to predict timout how long will it take for email to be sent defualt is 3 seconds **(Long)** 
- mail () 
> Function that is called when email is ready to be sent

#### Errors
***
Maildroid is handling small amount of exceptions.

**IllegalArgumentException** 
<br>
These exceptions are called after an error in checking if mandatory fields are not existent.
- ``` MaildroidX detected that you didn't pass [smtp] value in to the builder! ```
- ``` MaildroidX detected that you didn't pass [smtpAuthentication] value to the builder! ```
- ``` MaildroidX detected that you didn't pass [port] value to the builder! ```

**AuthenticationFailedException**
<br>
These exceptions are called when username or password on SMTP server is not correct, or address of SMTP server is not existent.
- ``` MaildroidX detected that you didn't pass [smtpUsername] or [smtpPassword] to the builder! ```
- ```MaildroidX detected that you didn't pass [smtpUsername] or [smtpPassword] to the builder!```

**Other**
- ``` SMTPAddressFailedException ```
> Thrown when mail can't be sent
- ``` MessagingException ```
> Thrown when there is problem with message 
- ``` IOException ```
> File in attachment not found or not existent

## Development
We love open source :hearts: <br>
Contributing to our project is really easy if you follow these steps.

- Add `` maildroidx `` to your machine
  - Download maildroid folder
  - Open it on your machine using your favourite IDE (Android Studio / InteliJ ) is recomended


- Contribution :tada:
  - Fork the repository
  - Create new branch ``` git checkout -b maildroidx-community-features ```
  - Add your feature or other changes to files
  - Commit your changes ``` git commit -m 'New feature' ```
  - Push to the branch ``` git push origin maildroidx-community-features ```
  - Create a pull request

### Bug :bug:
***
We are trying to make this library as bug free as possible ,but as you know some bugs can occure. If you find bug or typo in our library be free to open issue and report it.
- Open issue 
  - bug
   > **Explaining bug is most important thing** please use standard english language and don't forget to share your debug log
  - typo

### Feature Request
***
We strive to make `` maildroid `` best mailing library out there. We have ideas to add, but we would also like to hear from you.
- Open issue
  - fetaure

### Built with :muscle:
***
- **Kotlin** 
- **Java Mail API / Jakarta Mail API  by Eclipse foundation**

### TODO
***
- Add option to use more then one attachment

## Team
[![nedimf](https://avatars1.githubusercontent.com/u/24845593?v=3&s=144)](https://github.com/nedimf) 
<br>
[@nedimf](https://nedim.co) owner and maintainer of **maildroid**
<br>
Your name here :hearts:

### Motivation
Maildroid was born from the frustration of implementing a good emailing solution I had while developing a few client apps. I had to do many hours of unneeded work to make some emailing functions work using old libraries. Those libraries were limited to one SMTP server and because of that frustration ``Maildroid`` was born..

## FAQ 

[![HitCount](http://hits.dwyl.io/nedimf/maildroid.svg)](http://hits.dwyl.io/nedimf/maildroid)

- How to use Google `` gmail `` smtp server 
> As stated above every smtp server that uses SSL should be supported.<br>Tutorial how to use ``smtp.gmail.com``: <br>
  - If you choose this SMTP server we suggest making brand new account and enabling **less secure apps** in settings. [Google support link](https://support.google.com/accounts/answer/6010255?hl=en)
  - **``` smtp.("smtp.gmail.com")  ```**
  - **``` smtpUsername.("")  ```** your gmail email adress
  - **``` smtpPassword.("")  ```** your gmail password
  - **``` smtpPort.("465")  ```** gmail TLS port

- How to test sending emails,but not to affect client
> Maildroid works great with [mailtrap.io](https://mailtrap.io). They limit 50 message per inbox.Inbox can be erase at any time what it makes it one of the best solution for developers.

- Do this library save any of those data to external parties or servers?
> Maildroid is open source and it **DOES NOT** save ANY data to extarnal servers or parties.

This part will grow more over time as question are posted.

### Designer credits
Icon made by [Designer](https://www.flaticon.com/authors/kiranshastry) & [Freepik](https://www.flaticon.com/authors/freepik) from [flaticon](https://www.flaticon.com/authors/freepik)


