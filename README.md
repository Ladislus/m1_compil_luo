# LUO: Language de l'Université d'Orléans

Ce dépôt contient du code et des documents développés conjointement
par toutes les équipes du cours de compilation (année 2021), ainsi que
du code fourni par l'enseignant.

Il n'est pas autorisé de committer et pusher sur ce dépôt en dehors
des séances de travaux dirigés, en particulier d'y déposer du code
spécifique à une équipe. Les contrevenants seront pénalisés sur la
note du projet.

Le dépôt est organisé selon la structure par défaut des projets Java
avec IntelliJ. Il contient des fichiers de préférences Gradle qui sont
pré-configurés pour prendre en compte les dépendances nécessaires.

Comme le dépôt ne contient que les fichiers de configuration est les 
sources, il sera nécessaire pour vous de créer un nouveau projet 
(New > Project from Version Control) que l'IDE génère ce qui faut 
d'après les fichiers de configuration.

## Version du 15 mars

Il y a maintenant une classe Main executable qui prend en argument
un nom de fichier LUO en argument et qui effectue l'analyse syntaxique
puis qui affiche le fichier. Cet affiche n'est pas un "pretty printer"
mais plutôt un "ugly printer". Toutefois la sortie du printer est 
syntaxiquement correcte et peut-être redonnée au compilateur.

Depuis un terminal IntelliJ:

`./gradlew --console=plain --quiet run --args "src/main/luo/helloworld.luo"`

doit donner quelque chose comme :

```
public static version Version = { major = 1, minor = 0 }
rec version{
int major
int minor
}
public void printVersion(Version v)
{
print(v.major, ".", v.minor)
}
public void main(char array array arguments)
{
print("Hello World !\n", "Version: ")
printVersion(Version)
print("/n")
}
```
