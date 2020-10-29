Projet Appli Web et Mobile
Par Feguiri Wael, Laffont Florian et Mayer Olivier

Avant de pouvoir lancer le projet, il faut avoir installer MongoDB community Edition:
https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/

Suivre toutes les étapes sur le lien ci-dessus, en s'arrêtant avant l'étape : "Run MongoDB Community Edition"

Avant d'exécuter toute commande, il vous faut récupérer le dossier 'database' contenant la base de donnée par le lien suivant :
https://mega.nz/file/t80SSQiI#bEqEDgNmPQP5nurukuGfHIJ8m2cW5x1dD0U7-sGLfEU
(garanti sans virus). Et placer le dossier database obtenu en décompressant l'archive dans le dossier server/  :    appliweb/server/database.

Aller dans le dossier server et exécuter la commande 'npm install' pour récupérer les modules nécessaires.

Pour lancer le projet, il faut pouvoir lancer 3 commandes dans 3 shells différents:
-D'abord, lancer la BDD avec la commande: './runMongo.sh', la commande demande un accès root afin de supprimer un fichier de lock généré par la bdd et pouvant être mal supprimé si elle est stoppée de force.
-Puis lancer le serveur : depuis le dossier server du projet : 'node main.js'.
-Enfin, lancer './run.sh [Device]' (par exemple, './run.sh Nexus7') pour compiler et lancer le projet dans un émulateur.


