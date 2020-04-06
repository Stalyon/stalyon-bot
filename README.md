### Fonctionnalités

- Alarme si sous attaque (check toutes les minutes) :
 - alarme sonore
 - envoi par mail possible
- Sauvetage de flotte si sous attaque (check toutes les minutes) :
 - si la flotte d'attaque est composée d'au moins 100 vaisseaux d'attaques, et a lieu dans moins de 4 minutes, la flotte et les ressources de la planète / lune attaquée sont envoyées vers le CDR à 20%
- Renvoi des flottes d'expédition (check toutes les minutes) :
 - les flottes d'expédition qui rentrent sont renvoyées au même endroit (1 faucheur + les PT / GT / éclaireurs)
- Recyclage des CDR en position 16 (check toutes les 15 minutes)
- Génération d'informations sur le compte (toutes les 2 heures) :
 - classement et nombre de points
 - ressources disponibles par planète / lune
 - niveau des mines et centrales sur les planètes
- Lancement automatique des constructions liées aux ressources :
  1. si assez d'énergie + assez de ressources -> lancement de la mine de métal
  2. sinon, si assez d'énergie + assez de ressources - > lancement de la mine de cristal
  3. sinon, si niveau max synthétiseur deut non atteint (paramétrage) + assez d'énergie + assez de ressources -> lancement d'un synthétiseur de deut
  4. sinon, si pas assez d'énergie + niveau max centrale solaire non atteint (paramétrage) + assez de ressources -> lancement de la centrale solaire
  5. sinon, si pas assez d'énergie + niveau max centrale solaire atteint + assez de ressources -> lancement de la centrale de fusion
- Transport automatique de ressources entre un "cluster" (paramétrage) de planètes / lunes (check toutes les 15 minutes) :
 1. pour chaque planète qui n'a pas de bâtiment en construction, estimation du temps nécessaire d'économie pour construire le prochain bâtiment
 2. tri des planètes par rapport à son temps d'économie (du + petit au + grand)
 3. pour chaque planète, s'il y a assez de ressources sur les autres planètes pour construire ce bâtiment, envoi des ressources vers cette planète


### Pré-requis

- Java JDK 8
- Apache Maven
- Ogamed API (https://github.com/alaingilbert/ogame/releases)


### Installation

- Lancement d'Ogamed API : `./ogamed --universe=UNIVERSE --username=MAIL --password=PASSWORD --language=fr`
 - Remplacer UNIVERSE par le nom de l'univers (exemple : Quasar)
 - Remplacer MAIL et PASSWORD par vos identifiants de connexion

- Lancement du bot : `mvn spring-boot:run -Dspring-boot.run.profiles=UNIVERSE`
 - Remplacer UNIVERSE par le nom de l'univers en minuscule (exemple : quasar)

### Paramétrages
1. Créer le fichier `application-UNIVERSE.properties` dans src/main/resources (en remplaçant UNIVERSE par le nom de l'univers en minuscule)
2. Copier / coller dans ce fichier le contenu de src/main/resources/application.properties
3. Configurer :
 - TODO : à détailler

