# GroopyApp

## Procedure de déploiement

1) Dans le fichier **build.gradle (Module: app)** : 


	defaultConfig {
        applicationId "adri.suys.un_mutescan"
        minSdkVersion 19
        targetSdkVersion 28
        versionCode 2
        versionName "2.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    
Changez le **versionCode** et le **versionName**


2) Dans le menu, **Build** --> **Generate Signed APK...**

3) Cliquez sur **Next**

4) Indiquez le lien vers le keystore dans **Key store path** et entrez son mot de passe, faites de même pour la clé (key) et cliquez sur **Next**

5) Choisissez un dossier de destination pour votre APK

6) Choisissez le mode **release** pour le build Type

7) Cochez les cases V1 **ET** V2 pour les Signature Versions 

8) Cliquez sur **Finish**

## Générer un certificat

Dans le cas où il faudrait générer un nouveau certificat, voici la commande à taper dans votre terminal (les paramètres sont à changer à votre guise) :

	**keytool -export -rfc -keystore keystore.jks -alias key0 -file upload_certificate.pem**

	- keystore.jks : chemin vers le keystore
	- key0 : nom de la clé
	- upload_certificate.pem : le nom du certificat (créé dans le dossier courant)
	


