# Pràctica: Gestió de Videojocs amb Persistència Binària

Aquest projecte és una aplicació en Java desenvolupada per gestionar un catàleg de videojocs. Permet realitzar operacions CRUD (Crear, Llegir, Actualitzar i Eliminar) i manté les dades de forma persistent utilitzant fitxers binaris (`ObjectOutputStream` i `ObjectInputStream`).

## Objectius de la Pràctica
* Consolidar el concepte de **CRUD** (Create, Read, Update, Delete).
* Practicar l’ús de la interfície `Serializable` i les classes d'entrada/sortida `ObjectOutputStream` i `ObjectInputStream`.
* Treballar amb col·leccions d'objectes (`ArrayList`).
* Gestionar menús interactius i entrades de l’usuari via `Scanner`.

---

## Apunts Teòrics: Fitxers Binaris i Serialització

Un **fitxer binari** és un fitxer que conté dades codificades en format binari (0s i 1s). A diferència dels fitxers de text pla, no és directament llegible per humans.

### Característiques Principals
* **Eficiència:** Són més compactes i eficients que els fitxers de text, ja que no contenen caràcters extra de formatació.
* **Emmagatzematge Complex:** Permeten guardar estructures de dades i objectes complexos complets de Java (no només cadenes de text).
* **Il·legibilitat Directa:** No es poden obrir i entendre directament amb editors de text (com Notepad o VSCode).

### Problemàtiques i Limitacions
La serialització i l'ús de fitxers binaris tenen certes contrapartides:
1. **Dependència de la plataforma i versió:** Si la classe Java original (ex: `Videojoc`) canvia la seva estructura (s'afegeixen o treuen atributs), el fitxer binari creat prèviament pot no ser llegible, llançant errors com `InvalidClassException`. (Per això s'utilitza el `serialVersionUID`).
2. **Inseguretat:** Deserialitzar fitxers provinents de fonts desconegudes pot ser perillós, ja que pot executar codi maliciós a l'instanciar objectes manipulats.
3. **Manca d'Interoperabilitat:** Un fitxer `.dat` creat amb la serialització nativa de Java no pot ser entès fàcilment per altres llenguatges de programació (com Python o C++).

### Exemple senzill de persistència binària
```java
// Escriure objecte al fitxer
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("persona.dat"));
oos.writeObject(new Persona("Anna", 22, "anna@mail.com"));
oos.close();

// Llegir objecte del fitxer
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("persona.dat"));
Persona p = (Persona) ois.readObject();
ois.close();
System.out.println("Llegit: " + p);

Classe / Mètode,Descripció,Exemple d'ús
ObjectOutputStream,Escriu objectes en un flux binari. Les classes dels objectes han d'implementar Serializable.,"new ObjectOutputStream(new FileOutputStream(""dades.dat""))"
writeObject(Object o),Escriu un objecte serialitzat al fitxer.,oos.writeObject(persona);
flush(),Força a escriure les dades pendents al fitxer immediatament.,oos.flush();
close(),Tanca l’stream i allibera els recursos associats.,oos.close();
ObjectInputStream,Llegeix objectes d’un flux binari prèviament serialitzat.,"new ObjectInputStream(new FileInputStream(""dades.dat""))"
readObject(),Llegeix un objecte del fitxer i retorna un Object. Cal fer un cast explícit.,Persona p = (Persona) ois.readObject();
available(),Indica els bytes restants. Nota: No és fiable per saber si queden objectes. És millor fer bucles controlats amb EOFException.,
close(),Tanca l’stream i allibera recursos.,ois.close();
```

Preguntes Freqüents
Què és i per a què serveix @SuppressWarnings("unchecked")?

És una anotació de Java que serveix per evitar que el compilador mostri advertiments (warnings) concrets.
Context del codi

Quan fem la següent lectura d'un fitxer binari:
```java
ArrayList<Videojoc> arrayGames = (ArrayList<Videojoc>) ois.readObject();
```
El mètode readObject() de la classe ObjectInputStream retorna un Object genèric. Com que nosaltres sabem positivament que allà dins hi hem guardat un ArrayList<Videojoc>, fem un cast (conversió explícita).

En aquest punt, el compilador ens avisa amb el següent missatge:
```java
Unchecked cast: 'java.lang.Object' to 'java.util.ArrayList<Videojoc>'
```
Això passa perquè el compilador no pot comprovar en temps de compilació que l’objecte realment és d'aquest tipus (només ho sabrà en temps d’execució, quan es llegeixi el fitxer).
Què fa exactament aquesta anotació?

    Indica al compilador: "Sé el que estic fent, no m’avisis per aquest cast no comprovat".

    Evita que apareguin missatges molestos i subratllats grocs cada vegada que compiles el projecte.

    Important: No canvia el funcionament intern del programa. Si el cast fos incorrecte (per exemple, si el fitxer tingués una altra cosa que no fos l'ArrayList), en temps d’execució el programa fallaria igualment llançant una ClassCastException.

Recomanació didàctica

No és bona idea abusar d’aquesta anotació de forma genèrica, perquè pots amagar problemes reals en el teu codi.
No obstant això, en aquest cas concret és totalment acceptable perquè controlem el 100% de la persistència i sabem amb certesa que només escrivim i llegim un ArrayList de la classe indicada.

## Casos de Prova (Exemples d'execució)

A continuació es detalla una seqüència de proves (simulant la interacció per consola) per verificar que totes les funcionalitats del CRUD i la persistència del fitxer `fitxer.dat` funcionen correctament.

### 1. Afegir Videojocs (Create)
**Objectiu:** Comprovar que el programa permet introduir dades i crea els objectes correctament.
* **Acció:** Triar l'opció `1`.
* **Entrades de prova (Joc 1):**
  * Nom: `The Legend of Zelda`
  * Gènere: `Aventura`
  * Any de sortida: `2017`
  * Plataforma: `Switch`
  * Preu: `59.99`

<img width="1417" height="270" alt="image" src="https://github.com/user-attachments/assets/9b95210c-c6fa-4f17-99c3-7fb5fdbde9e1" />

* **Entrades de prova (Joc 2):**
  * Nom: `God of War`
  * Gènere: `Accio`
  * Any de sortida: `2018`
  * Plataforma: `PS4`
  * Preu: `39.99`

<img width="1415" height="259" alt="image" src="https://github.com/user-attachments/assets/54940600-0385-4c10-8027-c51ade31b1b3" />

* **Resultat esperat:** El programa ha de mostrar el missatge `"Persona desada correctament."` (o "Videojoc desat correctament") després de cada inserció i tornar al menú.

### 2. Veure tots els videojocs (Read)
**Objectiu:** Comprovar que les dades s'han desat a l'ArrayList i es mostren per pantalla.
* **Acció:** Triar l'opció `2`.
* **Resultat esperat:** 
  ```text
  --- Llista de videojocs ---
  Videojoc [name=The Legend of Zelda, genre=Aventura, launchYear=2017, platform=Switch, price=59.99]
  Videojoc [name=God of War, genre=Accio, launchYear=2018, platform=PS4, price=39.99]
  ```
* **Resultat obtingut:**
<img width="1418" height="214" alt="image" src="https://github.com/user-attachments/assets/bf3c944d-3c9d-4cdb-b1be-947bc412d027" />


### 3. Cercar videojoc (Search)
**Objectiu:** Comporvar que el programa pot localitzar un joc exacte pel seu títol.
* **Acció:** Triar l'opció `3`.
* **Entrada:** God of War
* **Resultat esperat:**
  ```text
  Videojoc [name=God Of War, genre=Acció, launchYear=2018, platform=PS4, price=39.99]
  ```
* **Resultat obtingut:**
<img width="1418" height="234" alt="image" src="https://github.com/user-attachments/assets/dc8d4ad5-1e43-4911-973d-db4c8bd790b9" />

### 4. Actualitzar videojoc (Update)
**Objectiu:** Modificar les dades d'un joc existent i verificar que es guarden els canvis.
* **Acció:** Triar l'opció `4`.
* **Entrada:** God of War
* **Noves dades a introduir:**
  
    **Nom:** `God of War`
  
    **Gènere:** `Accio`
  
    **Any:** `2018`
  
    **Plataforma:** `PC` (Canviem de PS4 a PC)
  
    **Preu:** `49.99` (Disminució del preu)
  
* **Resultat esperat:**
  ```text
  El programa mostra "Videojoc actualitzat correctament." i sobreescriu el fitxer. Si triem l'opció 2, veurem el joc amb la nova plataforma i preu.
  ```
* **Resultat obtingut:**
<img width="1414" height="834" alt="image" src="https://github.com/user-attachments/assets/6cc53db8-0846-4c66-bebd-dd97dcd48656" />

### 5. Eliminar videojoc (Delete)
**Objectiu:** Esborrar un joc de la memòria i del fitxer binari.
* **Acció:** Triar l'opció `2`.
* **Entrada:** The Legend Of Zelda
* **Resultat esperat:**
  ```text
  A la llista ara només hi hauria d'aparèixer el joc de God of War.
  ```
* **Resultat obtingut:**
<img width="1418" height="639" alt="image" src="https://github.com/user-attachments/assets/032e6c44-a1a9-4567-b8b7-db24de343863" />

### 6. Comprovació de persistència (Shut on/off)
**Objectiu:** Garantir que les dades sobreviuen a la finalització del programa.
* **Acció:** Triar l'opció `6`.
* **Procediment i resultat:**
  ```text
  S'hauria de crear el fitxer .dat i al apagar i executar haurien de poder veure's els jocs creats anteriorment.
  ```
* **Resultat obtingut:**
<img width="620" height="37" alt="image" src="https://github.com/user-attachments/assets/976c2d46-74b7-4994-a72c-fc3a9985f1a0" />
<img width="1415" height="267" alt="image" src="https://github.com/user-attachments/assets/4c2b9bde-8bbd-4bfc-b3f5-5ebe22eb481d" />
