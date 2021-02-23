## 2. Types composites


### 2.1 Enregistrements
*   *Spécificités :*  
    > 2 enregistrements sont différents même s'ils ont le même type
    ```
    rec type1 { int a; int b; }
    rec type2 { int a; int b; }
    => type1 et type2 sont différents
    ```
  
*   *Comment écrire le type et écrire les valeurs de ce type ?*
    ```
    rec monEnregistrement{
        int a;
        int b;
    };
    ```
    ```
    monEnregistrement.a = 2;
    monEnregistrement = {1,2}
    ```

*   *Les opérations sur les enregistrements*

    *   Copie d’un enregistrement :  
        `secondEnregistrement = monEnregistrement;`

    *   Notion d’équivalence :  
        Uniquement si c’est le même type d’enregistrement.

    *   Référence a une valeur de type enregistrement :  
        Pointeur vers le premier emplacement mémoire du premier élément de la valeur.
      
    *   Représentation en mémoire :  
        Les attributs sont alignés en mémoire.


### 2.2 Tableaux
*   *Comment écrire le type et écrire les valeurs de ce type ?*
    ```
    int [] monTableau = {1,2,3,4,5,6,7,...,100};  
    monTableau[0] = 1;
    ```
    ```
    int [][] monTableauDeuxDimension;
    ```
  
*   *Les opérations sur les tableaux*

    `int [] monTableau = {1,2,3,4,5,6,7,...,100};`
    * Récupérer la taille d'un tableau : `monTableau.length();`
    * Copie : `monTableau2 = monTableau1;`
    * Comparaison : Renvoi `true` si les tableaux sont de même taille
    * Représentation en mémoire : Éléments alignés en mémoire
    * Allocation mémoire :`char [] monTableau = new char[n];`  
        (*seulement avec des types primitifs*)  
    * Libération mémoire : `free(monTableau)`

### 2.4 Dictionnaires
*   *Spécificités :*  
**Représentation en arbres linéaires de recherche**


*   *Comment écrire le type et écrire les valeurs de ce type ?*
    ```
    char*int dico monDico = {(“maClé1”, 1),(“maClé2”,2)};
    monDico[maCle]
    monDico.put(“uneClé”, 1);
    monDico.pop(“uneClé”);
    ```

*   *Les opérations sur les dictionnaires*

    `char*int dico monDico = {(“maClé1”, 1),(“maClé2”,2)};`  
    *   Copie : `monDico2 = monDico1 ;`
    *   Obtenir les clés ou les valeurs : 
        *   `monDico.keys();` ⇒ Renvoie la liste des clés
        *   `monDico.values();` ⇒ Renvoie la liste des valeurs
    *   Modifier un couple clé/valeur : `monDico[maCle] = mavaleur`
    *   Représentation en mémoire :  
    Chaque chaînon du dictionnaire sont représentés en mémoire 
    de manière indépendante, ils pointent vers le prochain.
    *   Référence à une valeur de type dico :  
    Pointeur vers le premier emplacement mémoire 
    du premier élément de la valeur.


