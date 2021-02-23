### Types primitifs

## Syntaxe de typage

Pour attribuer un type à une variable, la syntaxe est la suivante :
`**type** nomvar [ = **expr** ];`

par exemple, afin de déclarer et attribuer 5 à une variable nommée *dromadaire*, de type entier, l'on procède comme suit :  
`int dromadaire = 5;`

## Types

Le langage Luo possède plusieurs types primitifs, tels que les entiers, les booléens, les caractères ...

# Entiers
Les entiers sont désignés par le mot clef `int` au sein du langage Luo. Ces entiers sont représentés en mémoire sur 4 octets (ils sont bornés).
Le type entier permet de représenter les entiers positifs et négatifs (bit de poid fort représentant le signe), les bornes sont donc [ - 2^(32 / 2); + 2 ^ (32) - 1].

Les différents opérateurs applicables sur les types *int* sont :
- Opérateur unaire :
  - Opposé (-) : permet d'inverser le signe d'un entier. 
- Opérateurs binaires :
  - Addition (+).
  - Division (/) : Division entière (arrondie à l'entier inférieur).
  - Soustraction (-).
  - Multiplication (*).
  - Modulo (%).
  - Comparaisons (==, !=, <, <=, >, >=) : Les comparaisons sont faites par rapport à l'ordre numérique.

Exemples:  
```LUO
int i;                          // Déclaration
int j = 10 + 10;                // Déclaration avec initialisation
j = -j;                         // Opposé
int x = 10 % 10;                // Opérateurs binaires
bool b = 10 >= 100;             // Comparaisons
```

# Caractères
Les caractères sont désignés par le mot clef `char` au sein du langage Luo. Ces caractères sont représentés en mémoire sur 1 octet.
Les caractères sont représentés grâce à des simples quotes `'`.

Les différents opérateurs applicables sur les types *char* sont :
- Opérateurs binaires :
  - Comparaisons (==, !=, <, <=, >, >=) : Les comparaisons sont faites par rapport à l'ordre alphanumérique.

Exemples:
```LUO
char x;                 // Déclaration
char y = 'y';           // Déclaration avec initialisation
bool a = 'a' > 'b';     // Comparaisons
bool b = 'a' == 'b';    // Comparaisons
bool c = 'a' != 'b';    // Comparaisons
```

De plus, le caractère `\` est le caractère d'échappement.

# Chaines de caractères
Le langage Luo ne possède pas de type primitif `string`, ceux-ci sont représentés par un tableau de caractères. Cependant, l'utilisation de doubles quotes `"` permet la création "à la volée" de chaines de caractères.

# Booléens
Les booléens sont désignés par le mot clef `bool` au sein du langage Luo. Ces booléens sont représentés en mémoire sur 1 octet.
Lors de l'évaluation d'une condition chainée, les expressions sont évaluées de gauche à droite, avec des court-circuits.

Les différents opérateurs applicables sur les types *bool* sont :
- Opérateur unaire :
  - Inverse (!) 
- Opérateurs binaires :
  - Comparaisons (==, !=)
  - Opérations logiques (&&,  ||) : AND et OR

Exemples:
```LUO
bool a = true;              // Déclaration avec initialisation
bool b = false;             // Déclaration avec initialisation
bool c;                     // Déclaration
bool d = true == (a && b);  // Déclaration avec initialisation par comparaison
```
