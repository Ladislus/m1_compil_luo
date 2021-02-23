## 4. Instructions et blocs

### 4.1 Séquences

* LUO dispose d'un séquençage d’instruction similaire à Java ou C. On préfère utiliser une instruction de type *expr;* que d’utiliser le format d’Ocaml *expr1;expr2;...;exprN*.

* Le point-virgule « ; » est le symbole de terminaison pour toute instruction.

### 4.2 Expressions et affectations

* Les affectations seront de la forme NomVar = Valeur.
* Sur les variables de type entier on peut utiliser les opérateurs d’affectation composée:
    * lv += expression;
    * lv  -= expression;
    * lv /= expression;
    * lv  *= expression;
    * lv++;
    * lv--;
* La concaténation des chaines de caractères sera fournie par la libraire standard LUO.

### 4.3 Blocs et d ́eclarations

* LUO est un langage fortement typé. La déclaration de variable se fait de la façon suivante : **Type NomVar;**  
A noter qu'une valeur peut-être affectée lors de la déclaration d'une variable en utilisant l'écriture :
**Type NomVar = Valeur;**  
* Les variables doivent être déclarées en début de bloc.
* L'ouverture et la fermeture de bloc se fait par l'utilisation d'accolades ouvrante et fermante.
* Les blocs définissent le corps d'une fonction, d'une instruction conditionnelle ou d'une boucle.
* Les variables ont des noms unique à leur bloc et pour tous les sous-blocs suivants. C'est-à-dire que contrairement à C, une variable ne peut pas partager son nom dans des sous-blocs.  
Exemple :  
```
{
    int var;
    {
        int var; // interdit !
    }
}
```
### 4.4 Instructions conditionnelles

LUO utilisera la syntaxe Java sauf pour le sinon si (elseif au lieu de else if ).  
Exemple:
```
if ( expression_booléenne ) {
    print("Résultat if");
}
elseif ( expression_booléenne ) {
    print("Résultat elseif");
}
else {
    print("Résultat else");
}
```

### 4.5 Boucle

Pour la syntaxe des boucles, la forme sera celle de Java.  
Nous aurons 4 boucles : **foreach**, **for**, **while**, **do...while**.  
Exemple:  
```
foreach( Type var : Collection_Var){...}
```
