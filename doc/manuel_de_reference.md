# Manuel de référence du langage LUO

## Syntaxe du langage

Les programmes du langage LUO sont écrits à l'aide de caractères 
encodés en UTF-8. La grammaire formelle du langage est donnée 
dans le fichier [Luo.g4](../src/main/antlr/Luo.g4).

L'exemple suivant est un programme LUO valide:
```
// Est-ce vraiment un programme valide ?
public void main((char array) array arguments){
  if (length(arguments) == 0)
    print("Il n'y a pas d'arguments passés à ce programme.\n");
  else {
    print("Il y a ", length(argument), " passé à ce programme.\n");
    print("Ces arguments sont : \n");
    foreach(char array argument : arguments){
      print("- ", argument, "\n");
    } 
  }
} 
```

## Sémantique du langage

* [Types de base, valeurs, opérations](types_base.md)
* [Types composites, valeurs, opérations](types_composites.md)
* [Expressions et leur évaluation](expressions.md)
* [Instructions](instructions.md)
* [Fonctions définies par l'utilisateur](fonctions.md)
* [Fonctions prédéfinies du langage LUO](fonctions_predefinies.md)
* [Programmes LUO](programmes.md)
