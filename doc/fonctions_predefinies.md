# Fonctions prédéfinies

## Entrée utilisateur

La fonction **input** permet à l’utilisateur de saisir une valeur au programme. 
Le type de la valeur saisie peut être choisi lors de l'affectation : un caractère, une chaîne de caractères, un entier ou un booléen.

**Notations :**

```
char inputChar()
char array inputString()
int inputInt()
bool inputBool()
```

**Exemples :**  

```
int number = input(); // 51
char array string = input(); // "une chaine"
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

**Exemples :**

```
print("Hello world!");
print(35);
```


&nbsp;
## Fonctions de conversion

Il y a possiblité de changer le type d’une valeur, plusieurs fonctions prédéfinies de conversion sont disponibles 
pour modifier les types char, int et bool.

**Notations :**  

Convertir en char :
```Convertir en char :
char toChar(int value)
```
Convertir en int :
```
int toInt(char value)
int toInt(bool value)
```
Convertir en bool :
```
bool toBool(int value)
```

**Exemples :**

```
char A = toChar(41); // A = 'A'
int number = toInt('1'); // number = 49
bool boolean = toBool(1); // boolean = true
```  


&nbsp;
## Assert

La fonction assert permet de stopper l’exécution du programme si la condition **cdt** n’est pas remplie 
alors le message **msg** s’affiche.

**Notation :**

    assert(bool cdt, char array msg); 

**Exemples :**

```
assert(1 + 1 == 2, "error computing"); // no error
assert(true == false, "error true not false"); // error
```