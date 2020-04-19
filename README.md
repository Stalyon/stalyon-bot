### Fonctionnalités

- Alarme si sous attaque (check toutes les minutes) :
  - alarme sonore
  - envoi par mail possible
- Sauvetage de flotte si sous attaque (check toutes les minutes) :
  - si la flotte d'attaque est composée d'au moins 100 vaisseaux d'attaques, et a lieu dans moins de 4 minutes, la flotte et les ressources de la planète / lune attaquée sont envoyées vers le CDR à 20%
- Envoi automatique des flottes d'expédition (check toutes les minutes) :
  - paramétrage des vaisseaux à envoyer et de la destination
- Recyclage du CDR en position 16 (check toutes les 5 minutes)
- Génération d'informations sur le compte (toutes les 2 heures) :
  - classement et nombre de points
  - ressources disponibles par planète / lune
  - niveau des mines et centrales sur les planètes
  - envoi par mail possible
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
- Vol automatique des CDR en position 16 (check toutes les 15 minutes) :


### Pré-requis

- Java JDK 8
- Apache Maven
- Ogamed API (https://github.com/alaingilbert/ogame/releases)


### Paramétrages

1. Créer le fichier `application-UNIVERSE.properties` dans src/main/resources (en remplaçant UNIVERSE par le nom de l'univers en minuscule)
2. Copier / coller dans ce fichier le contenu de src/main/resources/application.properties
3. Configurer :
   1. TODO : à détailler (prévoir assez de GT pour les transports)


### Configuration Discord

Il est possible d'envoyer directement les logs sur un serveur Discord.
Voici les étapes à reproduire :
1. Créer votre serveur Discord
2. Création d'un bot Discord :
   - Aller sur https://discordapp.com/developers/applications/
   - Créer une application
   - Choisir un nom puis enregistrer
   - Onglet "Bot" : Ajouter un bot / copier le token ("Click to reveal token")
   - Coller le token dans le fichier de configuration sous `discord.bot.token`
3. Ajouter le bot au serveur Discord :
   - Onglet "OAuth2"
   - Cocher dans les scopes "bot"
   - En-dessous, cliquer sur Copy
   - L'url est du type `https://discordapp.com/api/oauth2/authorize?client_id=XXXX&permissions=0&scope=bot`
   - Ouvrir l'url dans un navigateur, et sélectionner votre serveur
4. Passer en mode développeur pour Discord (nécessaire pour étapes 5. et 6.)
   - Dans vos paramètres utilisateur, cliquer sur l'onglet "Apparence"
   - Cocher "Mode développeur"
5. Retrouver l'id du serveur :
   - Faîtes un clique droit sur la bulle correspondant à votre serveur dans Discord
   - Copier l'identifiant
   - Coller dans le fichier de configuration sous `discord.bot.guild.id`
6. Retrouver l'id du channel :
      - Faîtes un clique droit sur votre channel dans votre serveur Discord
      - Copier l'identifiant
      - Coller dans le fichier de configuration sous `discord.bot.channel.id`

Maintenant, mettre `discord.bot.enable` à true. Les logs devraient arriver automatiquement sur le serveur Discord (message "Démarrage du bot" au lancement de l'application).  


### Lancement

- Lancement d'Ogamed API : `./ogamed --universe=UNIVERSE --username=MAIL --password=PASSWORD --language=fr`
  - Remplacer UNIVERSE par le nom de l'univers (exemple : Quasar)
  - Remplacer MAIL et PASSWORD par vos identifiants de connexion

- Lancement du bot : `mvn spring-boot:run -Dspring-boot.run.profiles=UNIVERSE`
  - Remplacer UNIVERSE par le nom de l'univers en minuscule (exemple : quasar)

