# Fonctions

## Corps de la fonction 

On déclare une fonction en indiquant tout d'abord le type de retour, le nom de 
la fonction, ensuite entre parenthèses se trouve les paramètres listés.
On met ensuite, délimité par des crochets, le code de la fonction.

**Syntaxe déclaration d'une fonction:**
```
visibilité type_de_retour nom_de_la_fonction(...){
...

}
```

**Syntaxe permettant de retourner une valeur :**
```
return expression;

```


&nbsp;
## Où définir une fonction 

Une fonction peut être définit dans le corps du programme principal.
Il n'est pas possible de définir une fonction à l'intérieur d'une autre fonction.


&nbsp;
## Modes de passage de paramètres

On liste les paramètres à l'intérieur des parenthèses de la déclaration de la fonction.
On sépare les paramètres avec une parenthèse.
Le nombre de paramètres est fixe.

**Syntaxe :**
```
type nom ,type nom ...

```

&nbsp;
## Généricité

La généricité n'est pas supportée.


&nbsp;
## Définir des valeurs par défaut aux paramètres

tout d'abord on place les paramètres avec des valeurs par défaut à la fin de la liste des paramètres. On définit la valeur par défaut en ajoutant **:** suivi de la valeur.

Pour l'appel des fonctions il faut préciser le nom de l'argument suivit de **:** suivit de sa valeur pour chaque paramètres. Si les paramètres ayant une valeur par défaut ne sont pas appelés, on leur affecte leur valeur par défaut.

Avec la mention des noms des arguments dans l'appel, l'ordre des paramètres n'a pas à être respecté


**Syntaxe :**
```
type fonction(type a, type b:valeur_par_defaut) {...};
```
**Syntaxe appel de fonctions:**
```
fonction(a:5)
```



