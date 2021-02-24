# Retour sur la version du manuel de référence LUO du 24 février matin

## Type de base, valeurs et opérations

La description est assez complète, avec des exemples dans la plupart des cas.
Il est nécessaire de rajouter:
- les priorités de **toutes** les opérations
- dans la section instructions, il est fait mention des opérateurs ++, -- _etc._ 
  Ces opérateurs ne peuvent être considérés que comme donnant une instruction. 
  Il faut donc les rajouter aux expressions, en précisant bien leur sémantique.
- un exemple pour les chaînes de caractères 
```LUO 
char array message = "Hello World !"
```  

## Types composites 

Cette section manque de détails et surtout est imprécise,
et parfois incorrecte sur certains éléments. 

Pour tous les types composites, il manque la description de 
la représentation en mémoire de ces structures. Il faut l'ajouter 
et être précis, notamment quant à l'alignement mémoire. 

Il faut préciser explicitement pour chaque type composite 
ce qu'est une *valeur* de ce type composite, c'est-à-dire 
ce qui est affecté à une variable quand la partie droite 
d'une affectation est d'un type composite. 

Il n'y a pas lieu de parler de référence : on n'a pas de
notion de pointeur dans LUO, et il n'y a pas moyen de
passer les paramètes par référence.

Lors des discussions, nous avions décidé que : 
- pour les enregistrements, la valeur est l'ensemble des 
  valeurs des champs de l'enregistrement. Ceci veut 
  dire, en reprenant les exemples donnés, que si on 
  a la déclaration ```type1 x```, la mémoire pour stocker
  ```x``` est allouer tout de suite. 
  Ceci veux aussi dire que l'affectation est effectivement
  la copie.
- pour les tableaux, la valeur est une référence vers 
  le début du tableau. Donc:
  - ```int array t``` n'alloue de l'espace que pour stocker
    l'adresse du début du tableau
  - ```monTableau2 = monTableau1``` n'est **pas** la copie.
    Après cette affectation ```monTableau2```est juste un alias
    de ```monTableau1```.
  - la comparaison de deux tableaux est donc la comparaison
    de cette adresse, c'est-à-dire que ça renvoie ```true```
    uniquement si c'est le même tableau. Donc si on a :
    ```LUO
    int array t1 = {1, 2, 3}
    int array t2 = {1, 2, 3}
    int array t3 = t1
    ```
    alors ```t1 == t2``` est ```false``` et ```t3 == t1``` est ```true```  
- pour les dictionnaires, même chose que pour les tableaux en
  ce qui concerne les valeurs.

### Les tableaux

L'allocation ne marche évidemment pas qu'avec des types
primitifs : elle doit fonctionner pour **tous** les types.

Pour faire plus simple, la longueur d'un tableau ```t``` sera notée
```length(t)```. 

La représentation en mémoire est incorrecte : 
- la longueur est tout d'abord présente sur 1 mot mémoire
- les éléments sont contigus en mémoire 
  (dans un tableau de ```char``` par exemple, les éléments
  ne sont pas alignés sur des mots mémoires)

Il manque la syntaxe pour accéder à un élément du tableau,
et le fait que la cellule d'un tableau à gauche d'une 
affectation est valide (on dit que c'est une *l-value* 
ou valeur gauche). 
 
### Les dictionnaires

Je préfère le mot-clé `map` pour les dictionnaires,
`dico` est trop français. 

Le type ne peut pas être écrit `char * int map`, 
car cela laisse supposer que l'on a les types tuples
dans le langage (mais on ne les a pas). Ca pose d'ailleurs
un problème pour l'instruction `foreach`. 

Comme `length` pour les tableaux sera notée comme 
une application de fonction, `keys` et `values` seront
notées comme des fonctions. Elles retournent toutes 
les deux des tableaux, étant donné qu'on n'a pas de 
listes en LUO. 

Il manque de la syntaxe pour ajouter/supprimer une 
paire dans un dictionnaire. 
La modification est à supprimer, mais il faut indiquer
tout ce qui peut-être une *l-value* dans un dictionnaire. 

## Expressions

L'ordre d'évaluation : ok il est fixé, mais il faut 
alors l'expliciter !

Application d'un sous-programme : lors des discussions 
nous avions exclus les étiquettes lors des appels. 
L'appel d'un sous-programme est donc classique 
et positionnel. Il faut juste préciser le comportement
lorsqu'il y a des valeurs par défaut. 
Le principe : les valeurs par défaut ne peuvent 
être utilisés qu'à partir d'une certaine position. 

Exemple:
```LUO
int f(int x : 1, int y: 2, int z : 3){
  ...
}
```
Les appels suivants sont possibles:
- `f()` et dans le corps de la fonction `x` vaudra `1`,
  `y` vaudra `2` et `z` vaudra `3`
- `f(4)` et dans le corps de la fonction `x` vaudra `4`,
  `y` vaudra `2` et `z` vaudra `3`
- `f(4, 5)` et dans le corps de la fonction `x` vaudra `4`,
  `y` vaudra `5` et `z` vaudra `3`
- `f(4, 5, 6)` et dans le corps de la fonction `x` vaudra `4`,
  `y` vaudra `5` et `z` vaudra `6`
  
Les définitions locales dans les expressions ont 
été exclues. 

## Instructions et blocs

`lv++`, `++lv`, `lv--` et `--lv` sont des expressions.
Toute expression est aussi une instruction.
Par exemple en C ou Java il est possible d'écrire :
```Java
{
    int x = 0;
    42;
    x = 42;
}
```

Oui il faut un bloc pour le corps d'une fonction, mais 
c'est tout. Les blocs ne sont pas obligatoires pour 
les conditionnelles ou les boucles. Encore un exemple
Java:
```Java
int [] t = new int[10];
for(int i = 0; i <10; i++)
    t[i] = i;
```

Pour les boucles : il faut détailler chaque boucle,
préciser leur sémantique, et pour chacune donner 
un exemple.

## Fonctions

Ok, sauf:
- comment faire si on ne veut pas retourner de 
  valeur. Pas de type de retour, ou un type 
  de retour `void` ? C'est à préciser.
- appel de fonction à la fin, voir mes commentaires
  dans la section "Expressions".

Il faut préciser également que la surchage 
est autorisée, et en précisant bien les conditions
qui la permettent. 

## Fonctions prédéfinies

Les discussions ont été en partie ignorées pour la rédaction 
de cette section.

- `input` : la version proposée ne peut pas fonctionner, 
  la surcharge n'est autorisée que si le nombre ou 
  les types des argument sont différents, ce qui n'est 
  pas le cas ici.
  
- `print` : la version variable n'est donc pas une 
  fonction prédéfinie, mais un autre genre d'instruction.
  La version variable est donc à déplacer dans la 
  section instructions. 

- pour les fonctions de conversion, je propose de limiter 
  le nombre de fonctions prédéfinies. Entre `char` et `int`,
  et entre `bool` et `int`. Les autres fonctions de conversion
  pourront être fournies par une bibliothèque standard, 
  écrite en LUO (et qui pourra être importé ou pas).

## Programmes

L'essentiel est présent. Il faudrait juste préciser 
quelle est la visibilité quand rien n'est indiqué
(par défaut `public`). Pour les imports, préciser ce 
qui se passe en cas de collision de noms
(par exemple deux variables globales du même nom dans 
deux fichiers importés différents, ou deux types 
enregistrements).

Rappeler que les définitions de type enregistrement 
sont au niveau global. 