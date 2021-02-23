# Programme

## Corps du programme

Le corps du programme principal est une fonction avec un nom fixé. Cette fonction se nomme main. 
Elle va retourner un entier, cela permet d’indiquer le code de retour du programme. 
Les codes de retour sont 0 : succès, -1: erreur. Le paramètre est  un tableau de chaînes de caractères : **args**.  


**Notation :**
```
int main((char array) array args) {

	/*
	A compléter
	*/

	return 0;
}
```


&nbsp;
## Variables globales

On peut déclarer et utiliser des variables globales. Elles sont visibles dans tous les fichiers luo si la visibilité est en publique.
Sinon, elle seront visibles que dans le fichier si la visibilité est privée. 
Elles sont écrites juste avant le programme principal. La déclaration se fait comme un variable standard, 
sauf qu’on va ajouter le mot **static** devant. Les types sont int, bool, char, array, dico, rec. 


**Notations :**
```
visibility static type maVarGlabal = value ;
visibility static type maVarGlabal;  
```

**Exemples :**

```
public static int PI = 3.14; // Déclarer une variable statique en publique
private static USER = "LUO"; // Déclarer une variable statique en privée
public static STATE; // Déclarer une variable globale dans valeur
USER = "LUO2"; // Assigner une valeur à une variable globale
```

&nbsp;
## Découpage du programme

On peut découper le programme. Les fichiers importés peuvent  contenir  des fonctions,
des procédures et des variables globales. Les fichiers importés ne peuvent pas contenir la fonction main.   
Pour importer un fichier, il faut écrire **import** et le chemin du fichier au début du programme.
Le chemin du fichier est un chemin relatif ou absolu. Chaque répertoire est séparé par **/** . 
Le **./** signifie qu’on désigne  le chemin dans le répertoire courant.  
Le **../**  signifie qu’on désigne le chemin du répertoire parent.


**Notations :**
```
import  “./rep1/nomFichier”
import  “/rep1/nomFichier”
import  “../rep1/nomFichier”
import  “nomFichier”
```

**Exemples :**
```
import ./ast/type;
import /home/luo/ast/type;
import ../shape/square;
import math;
```

&nbsp;
## Commentaires

Pour commenter, il y a deux façons possibles. On peut commenter une ligne ou plusieurs lignes. Il n’y a pas d’emboîtement à l’intérieur des commentaires. Lorsque nous avons **/\***, tout le code entre **/\*** et le prochain ***/** seront mis en commentaire.


**Notations :**
```
// commentaire sur une ligne

/*
Commentaire 
sur plusieurs
lignes
*/
```
