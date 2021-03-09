# Fonctions

## Corps de la fonction 

On déclare une fonction en indiquant tout d'abord le type de retour, le nom de 
la fonction, ensuite entre parenthèses se trouve les paramètres listés.
On met ensuite, délimité par des crochets, le code de la fonction. Pour les fonctions
ne devant pas retourner de valeur, on utilise le type de retour **void**. 

Pour indiquer l'expression retournée, on utilise le mot-clé **return**.

**Syntaxe déclaration d'une fonction :**
```
visibilité type_de_retour nom_de_la_fonction(...){
    ...
    return expression;
}
```
**Exemple :**
```
public int fonctionAddition(...){
    ...
    return 5+5;
}
```


&nbsp;
## Où définir une fonction 

Une fonction peut être définit dans le corps du programme principal. 
Il n'est pas possible de définir une fonction à l'intérieur d'une autre fonction.


&nbsp;
## Généricité

La généricité n'est pas supportée.


&nbsp;
## Modes de passage de paramètres

On liste les paramètres à l'intérieur des parenthèses de la déclaration de la fonction.
On sépare les paramètres avec une parenthèse.
Le nombre de paramètres est fixe.

**Syntaxe :**
```
(type nom,type nom) 

```

**Exemple :**
```
public int addition(int a, int b){

    return a+b;
}
```

###

Pour définir des paramètres avec des valeurs par défaut à la fin de la liste des paramètres. On définit la valeur par défaut en ajoutant **:** suivi de la valeur.

Pour l'appel des fonctions il faut respecter l'ordre des paramètres.
Si les paramètres ayant une valeur par défaut ne sont pas appelés, on leur affecte leur valeur par défaut.
C'est pour ça que les paramètres ayant recours à une valeur par
défaut sont placés à la fin de la liste des paramètres.


**Syntaxe :**
```
(type nom, type nom:valeur_par_defaut)
```
**Exemple de déclaration :**
```
public int addition(int a:4, int b:5){

    return a+b;
}
```

**Exemple d'appel :**
```
fonction();  //dans le corps de la fonction a vaudra 4 et b vaudra 5
fonction(7);  //dans le corps de la fonction a vaudra 7 et b vaudra 5
fonction(7,9);  //dans le corps de la fonction a vaudra 7 et b vaudra 9
```



