# Fiche rendu projet

> Ce document est un bilan destiné au client. Présentez ce qui a été livré, ce qui fonctionne, et tournez habilement ce qui manque. Pas de jargon technique — on parle de fonctionnalités et de valeur perçue.

## Rappel du projet

<!-- Reprenez brièvement le pitch initial. Qu'aviez-vous promis ? -->

ToiletteMonLyon est une plateforme lyonnaise permettant de réserver en ligne des installations sanitaires publiques et privées à Lyon : cabines de toilettes, urinoirs et douches. Avec la possibilité de mettre des avis de manière anonyme.

## Ce qui a été livré

<!-- Présentez les fonctionnalités livrées. Captures d'écran / GIFs animés bienvenus. -->
<!-- Placez vos images dans docs/images/ et référencez-les avec : ![description](images/nom-du-fichier.png) -->

#### *Réservation d'une installation à Lyon*

La réservation d'une installation à Lyon est possible :

![images/1.png](images/1.png)

#### *Choix de l'ambiance et thème de la cabine*

Le choix de l'ambiance et du thème de la cabine sont possibles :

![images/2.png](images/2.png)

#### *Paiement flexible*

Pour les PMR la réservation de l'installation est gratuite mais les thèmes sont payants.

Ici lorsqu'on choisit PMR c'est gratuit :

![images/3.png](images/3.png)

#### *Gestion intelligente du stock avec alertes*

Le seuil d'alerte est déjà défini mais pas véritablement utilisé, pour nous il faut d'abord implémenter la création de compte pour lier le passage aux toilettes à une personne distincte et ensuite afficher l'alerte.

#### *Notifications et rappels intelligents*

Du coup cela pas implémenter car pas d'implémentation de users:
L'utilisateur est accompagné tout au long de sa réservation :

- Rappel 5 minutes avant le créneau (notification)
- Invitation à noter son expérience après la visite

#### *Timer*

Le timer est bien implémenté, lorsqu'un utilisateur réserve il bloque la réservation pour un autre utilisateur pendant une durée définie.

## Ce qui n'a pas été livré (et pourquoi)

- La `création de compte et profil client`, le `signalement de propreté et alerte nettoyage`, l'`interface d'administration` ainsi que les `partenariats lyonnais` n'ont pas été livrés car on souhaitait d'abord répondre au use case, les utilisateurs d'installations souhaitent avoir une application qui les référencent, veulent pouvoir y mettre des avis sans divulguer leurs noms et prénoms (comme sur Google) et une possibilité de paiement flexible.

- Le pass journée (offrant un accès illimité aux installations) n'a pas été implémenté car il est jugé trop "dangereux" par nos équipes. En effet, nous sommes encore en plain brainstorming sur les limites de ce pass pour en éviter tout abus.

## Perspectives

<!-- Quelles évolutions proposez-vous pour la suite ? -->

On a commencé à créer une classe User, pour par la suite proposer aux utilisateurs la possibilité de se créer un compte, de renseigner leurs avis, leurs favoris, etc...

En effet, la mise en place de compte utilisateurs constitue un atout majeur elle ouvrirait de nombreuses portes pour notre application, améliorant la prise en main de nos Lyonnais.