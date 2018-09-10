# Chargement DSD

C'est un POC qui permet d'éditer des DSD du W3C Data Cube, et de charger et visualiser des données à l'intérieur.
Le projet n'a pas initialement été pensé pour être un outil unique et a dû être recomposé à la dernière minute à partir de plusieurs sources, donc n'hésitez pas à faire des remarques si jamais vous avez des soucis.

## Getting started

Chargement DSD est une application JAVA + [React](https://facebook.github.io/react/).
Pour lancer l'application, il faut :
  - aller dans le fichier ./datacube-core/src/main/resources/fr/insee/config/melodi.properties et mettre l'URL de la base RDF et le repository
  - aller dans le dossier ./datacube-ws-interne/src/main/webapp/static/react-melodi/react et lancer la commande "npm install"
  - aller dans le fichier ./datacube-ws-interne/src/main/webapp/static/react-melodi/react/_properties.js et renseigner le contextpath du web-service java
  
URL du web-service : http://localhost:8080/datacube-ws-interne/static/swagger-ui/index.html
  
URL du back-office : http://localhost:8080/datacube-ws-interne/los/react/melodi/code-liste/recherche

## W3C Data Cube DSDs Model

!["DSDs Model"](https://github.com/LOS-ESSnet/DSD-Editor/blob/master/docs/img/qb.png)
