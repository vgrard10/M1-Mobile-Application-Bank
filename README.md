Using Android Studio (or any editor of your choice), we will have to create a mobile application.
The goal is to create a secure application to see our bank accounts.
This application must be available offline.
A refresh button allows the user to update its accounts.
Access to the application is restricted 
Exchanges with API must be secure ( with TLS)

Explain how you ensure user is the right one starting the app :

I think that with digital prints we could ensure that the user who uses the application is so the owner of the phone.
Also with this only the owner of the phone can access to his data
But I didn’t succeed to do it

I put screenshoots of my application in the pdf file


How do you securely save user's data on your phone ?

We can securely save user's data on the phone we use internal storage and the data stored are private to the app (also if there is and uninstallation of the app the data are removed
Other app also can't read the data.



How did you hide the API URL?

To hide the API URL, we use the fact of verifying certificates and hostnames. We hide the URL in the properties of gradle and a variable send the request in the code (all done with security_config.xml and cipher)
https://itnext.io/hide-and-keep-your-api-key-out-of-github-repository-7e89cc4b159d
