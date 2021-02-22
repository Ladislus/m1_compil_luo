# Fonctions prédéfinies

## Entrée utilisateur

La fonction **input** permet à l’utilisateur de saisir une valeur au programme. 
Le type de la valeur saisie peut être choisi lors de l'affectation : un caractère, une chaîne de caractères, un entier ou un booléen.

**Notations :**

```
char input()
char array input()
int input()
bool input()
```


&nbsp;
## Affichage sortie

La procédure **print** permet d’afficher une valeur dans la fenêtre de sortie. 
Elle prend en paramètre un caractère, une chaîne de caractères, un entier ou un booléen et l’affiche.


**Notations :**

```
void print(char msg)
void print(char array msg)
void print(int msg)
void print(bool msg)
```

Il y a aussi la possibilité d’afficher plusieurs messages dans un print 
avec un type T qui est de type char, char array, int ou bool.


**Notation :**

    void print(T msgs . . .)


&nbsp;
## Fonctions de conversion

Il y a possiblité de changer le type d’une valeur, plusieurs fonctions prédéfinies de conversion sont disponibles 
pour modifier les types char, char array, int et bool.

**Notations :**  

Convertir en char :
```Convertir en char :
char toChar(char array value)
char toChar(int value)
char toChar(bool value)
```
Convertir en char array :
```
char array toString(char value)
char array toString(int value)
char array toString(bool value)
```
Convertir en int :
```
int toInt(char value)
int toInt(char array value)
int toInt(bool value)
```
Convertir en bool :
```
bool toBool(char value)
bool toBool(char array value)
bool toBool(int value)
```


&nbsp;
## Assert

La fonction assert permet de stopper l’exécution du programme si la condition **cdt** n’est pas remplie 
alors le message **msg** s’affiche.

**Notation :**

    assert(bool cdt, char array msg); 
