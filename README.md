<br />

  <h3 align="center">Projet 7</h3>

  <p align="center">
    Library management system MSA
    <br />
    <br />
  </p>
</p>



<!-- SOMMAIRE -->
## Sommaire

1. [Prérequis](#prérequis)
2. [Configuration](#configuration)
    * [Connexion à votre base de données](#connexion-à-votre-base-de-données)
    * [Génération des fichiers jar avec votre configuration](#génération-des-fichiers-jar-avec-votre-configuration) 
    
3. [Déploiement](#déploiement)
    * [Base de données](#base-de-données)
    * [Keycloak](#keycloak)
    * [Lancement des fichiers jar](#lancement-des-fichiers-jar)
    
4. [Plus d'informations](#plus-dinformations)

<!-- PRÉREQUIS -->
## Prérequis

Dans ce tutoriel nous allons voir comment configurer et déployer l'application via les outils suivants.

* JDK 11
* PostgreSQL 11
* Apache Maven 3.6.3
* Keycloak 8.0.1

Pour commencer, clonez le projet sur votre ordinateur dans un repertoire facilement accessible.

Dans cet exemple nous allons utiliser le bureau. Ouvrez un terminal et tapez les commandes suivantes :

```sh
cd Desktop
```
```sh
git clone https://github.com/axelallain/library.git
```

<!-- CONFIGURATION -->
## Configuration

### Connexion à votre base de données

Pour connecter l'application à votre base de données, nous devons modifier le fichier de configuration "bootstrap.properties" du microservice "books".

Ouvrez le dossier du projet cloné et suivez ce chemin d'accès pour accéder à ce fichier de configuration :
```sh
books/src/main/resources/bootstrap.properties
```
Munissez-vous de votre éditeur de texte préféré pour éditer "bootstrap.properties".

Il ne vous reste plus qu'à modifier les propriétés suivantes pour connecter votre base de données PostgreSQL.
```properties
jdbc.url=jdbc:postgresql://localhost:5432/library
jdbc.username=postgres
jdbc.password=password
```

### Génération des fichiers .jar avec votre configuration

Une fois la configuration terminée, générons les fichiers .jar qui seront déployés.

Pour cela, ouvrez un terminal et rendez-vous à la racine du projet. Si le dossier du projet est sur le bureau :
```sh
cd Desktop/library
```

Pour plus de configuration concernant les fichiers .jar, vous pouvez éditer le fichier pom.xml situé dans les microservices.

Vous pouvez désormais utiliser la commande Maven suivante pour générer proprement les fichiers .jar :
```sh
mvn clean package
```

Si le build se passe correctement, vos fichiers .jar seront accessibles dans le dossier target situé dans les microservices. Exemple :
```sh
library/books/target/books.jar
```

<!-- DÉPLOIEMENT -->
## Déploiement

Vos fichiers .jar sont prêts ? Alors passons à l'étape finale de ce tutoriel : le déploiement !

### Base de données

En premier lieu, nous allons mettre en place notre base de données. Plusieurs choix s'offrent à vous :

* Les tables ET les données de démo
* Uniquement les tables SANS données de démo

Une troisième option consiste à prendre la deuxième option puis d'ajouter les données de démo via la sauvegarde fournie.

Les script SQL sont disponibles à la racine du projet, voici à quelle option ils correspondent :

Les tables ET les données de démo :
```sh
library.backup.sql
```

Uniquement les tables SANS données de démo :
```sh
create.sql
```

Uniquement les données de démo :
```sh
insert.sql
```

Dans cet exemple d'exécution des scripts, nous allons utiliser l'utilisateur par défault de PostgreSQL "postgres".

Accédons d'abord aux scripts présents à la racine du projet via un terminal :
```sh
cd Desktop/library
```

Pour exécuter le script "library.backup.sql", entrez la commande suivante dans votre terminal :

```sh
psql -U postgres -f library.backup.sql
```

La base de données est prête !

Pour exécuter le script "create.sql", nous devons d'abord créer la base de données via les commandes suivantes :

Se connecter en tant qu'utilisateur "postgres" :
```sh
psql -U postgres
```

Créer la base de données en lui attribuant un nom. Dans notre exemple, son nom est "library" :
```sh
CREATE DATABASE library;
```

Pour exécuter le script fermez votre terminal, ouvrez-en un nouveau et retournez à la racine du projet via cd..

Une fois de nouveau à la racine du projet, entrez cette commande :
```sh
psql -U postgres -d library -f create.sql
```

La base de données vide est prête !

Si vous souhaitez ajouter les données de démo, voici la commande :
```sh
psql -U postgres -d library -f insert.sql
```

### Keycloak

Keycloak va nous permettre de gérer les utilisateurs et de rediriger vers un formulaire d'authentification si nécessaire.

Il fonctionne en duo avec Spring Security qui lui va renseigner les restrictions et les URL concernées.

Il suffit de télécharger Keycloak à cette adresse :
```sh
https://www.keycloak.org/downloads.html
```

Une fois décompressé, entrez dans le dossier téléchargé via un terminal puis lancez Keycloak.

Voici les commandes selon votre environnement :

UNIX :
```sh
cd bin
./standalone.sh -Djboss.socket.binding.port-offset=100
```

MS-DOS :
```sh
...\bin\standalone.bat -Djboss.socket.binding.port-offset=100
```

### Lancement des fichiers .jar

L'étape finale de l'étape finale ! Nous allons déployer les fichiers .jar via leur serveur Tomcat embarqué.

Procédons au démarrage des fichiers .jar. 
Pour cela rendez-vous dans le dossier "target" du microservice via un terminal puis exécutez cette commande (Changez le nom du fichier .jar par le votre):
```sh
java -jar books.jar
```

Le microservice est déployé !

Faites de même pour tous les microservices.

L'URL pour y accéder varie selon le port du microservice, voici une liste de la configuration par défaut (ports et noms) :

L'ordre de cette liste définit aussi l'ordre de lancement optimal des microservices.

- configserver.jar (9101)
- eureka.jar (9102)
- springadmin.jar (9103)
- zuul.jar (9005)
- books.jar (9001)
- clientui.jar (9004)
- batch.jar (9003)

Pour accéder à l'interface graphique, l'URL est le suivant :
```sh
localhost:9004/
```

<!-- PLUS D'INFORMATIONS -->
## Plus d'informations

Pour plus d'informations un PDF est disponible à la racine du projet.

Ce tutoriel est terminé, merci et bonne journée :-)
