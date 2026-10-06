package com.biel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

// Classe Serializable con los atributos necesarios para crear un Videojuego.
class Videojoc implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String genre;
    private int launchYear;
    private String platform;
    private double price;

    // Constructor vacio
    public Videojoc() {
    }

    // Constructor completo
    public Videojoc(String name, String genre, int launchYear, String platform, double price) {
        this.name = name;
        this.genre = genre;
        this.launchYear = launchYear;
        this.platform = platform;
        this.price = price;
    }

    // Metodos auxiliares
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public int getLaunchYear() {
        return launchYear;
    }
    public void setLaunchYear(int launchYear) {
        this.launchYear = launchYear;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "Videojoc [name=" + name + ", genre=" + genre + ", launchYear=" + launchYear + ", platform=" + platform
                + ", price=" + price + "]";
    }
}

// Classe Main, logica principal del proyecto y programa con flujo de ejecución.
public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Lista de videojuegos cargada con el archivo .dat
        ArrayList<Videojoc> arrayGames = loadVideogames();

        int opcio = 0;

        try {

            // MENU
            do {
                System.out.println("\n--- Menú ---");
                System.out.println("1. Afegir videojoc");
                System.out.println("2. Veure tots els videojocs");
                System.out.println("3. Cercar videojoc");
                System.out.println("4. Actualizar videojoc");
                System.out.println("5. Eliminar videojoc");
                System.out.println("");
                System.out.println("6. Sortir");
                System.out.print("Tria una opció: ");
                opcio = sc.nextInt();
                sc.nextLine();

                // CRUD
                switch (opcio) {
                    
                    // C: CREATE - Creació i insercció de videojocs.
                    case 1:
                        System.out.print("Nom: ");
                        String name = sc.nextLine();
                        System.out.print("Gènere: ");
                        String genre = sc.nextLine();
                        System.out.print("Any de sortida: ");
                        int launchYear = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Plataforma: ");
                        String platform = sc.nextLine();
                        System.out.print("Preu: ");
                        double price = sc.nextDouble();
                        sc.nextLine();

                        Videojoc game = new Videojoc(name, genre, launchYear, platform, price);
                        arrayGames.add(game);
                        saveVideojocs(arrayGames);
                        System.out.println("Videojoc desada correctament.");
                        break;

                    // R.1: READ - Lectura de tots els videojocs guardats.
                    case 2:
                        if (arrayGames.isEmpty()) {
                            System.out.println("No hi ha videojocs desats.");
                        } else {
                            System.out.println("--- Llista de videojocs ---");
                            for (Videojoc g : arrayGames) {
                                System.out.println(g);
                            }
                        }
                        break;

                    // R.2: READ - Lectura de un videojoc específic.
                    case 3:
                        System.out.println("Nom del videojoc: ");
                        String gameToSearch = sc.nextLine();
                        System.out.println("");
                        searchVideogame(gameToSearch);
                        break;

                    // U: UPDATE - Actualització de les variables de un videojoc ja creat.
                    case 4:
                        System.out.println("Nom del videojoc a modificar: ");
                        gameToSearch = sc.nextLine();
                        arrayGames = updateVideogame(gameToSearch, loadVideogames());
                        break;

                    // D: DELETE - Eliminació de un videojoc a la llista i al fitxer de persistència.
                    case 5:
                        System.out.println("Nom del videojoc a eliminar: ");
                        gameToSearch = sc.nextLine();
                        arrayGames = deleteVideogame(gameToSearch, loadVideogames());
                        break;

                    default:
                        System.out.println("Opció incorrecta. Torna-ho a provar.");
                        break;
                }
            } while (opcio != 6);

        } catch (Exception e) {
            System.out.println("Error: Error a l'execucio... ");
        }
        sc.close();
    }

    // Mètode que escriu la llista de videojocs al fitxer de persistència.
    private static void saveVideojocs(ArrayList<Videojoc> videojocs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("fitxer.dat"))) {
            oos.writeObject(videojocs);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                System.out.println("Error: Cerca o creació de l'arxiu incorrecte: " + e.getMessage());
            } else {
                System.out.println("Error desant el videojoc: " + e.getMessage());
            }
        } catch (NullPointerException e) {
            System.out.println("Error: Cap videojoc creat: " + e.getMessage());
        }
    }

    // Mètode que descarrega les dades del fitxer de persistència i actualitza la llista de videojocs del programa.
    @SuppressWarnings("unchecked")
    private static ArrayList<Videojoc> loadVideogames() {
        ArrayList<Videojoc> arrayGames = new ArrayList<>();
        File fitxer = new File("fitxer.dat");
        if (fitxer.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("fitxer.dat"))) {
                arrayGames = (ArrayList<Videojoc>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error carregant persones: " + e.getMessage());
            }
        }
        return arrayGames;
    }

    // Mètode per buscar un videojoc en específic a la llista de videojocs.
    private static void searchVideogame(String name) {
        ArrayList<Videojoc> arrayGames = loadVideogames();
        boolean finded = false;

        for (int i = 0; i < arrayGames.size(); i++) {
            if (name.toLowerCase().equals(arrayGames.get(i).getName().toLowerCase())) {
                System.out.println(arrayGames.get(i).toString());
                finded = true;
            }
        }

        if (!finded) {
            System.out.println("Videojoc no trobat");
        }

    }

    // Mètode que permet la modificació de les variables del videojoc i actualitza la llista i fitxer de persistència.
    private static ArrayList<Videojoc> updateVideogame(String name, ArrayList<Videojoc> arrayGames) {
        boolean finded = false;

        for (Videojoc game : arrayGames) {
            if (name.toLowerCase().equals(game.getName().toLowerCase())) {
                finded = true;

                System.out.println("Informació actual: " + game.toString());
                System.out.println("");

                System.out.print("Introdueix el nou nom: ");
                game.setName(sc.nextLine());
                System.out.println("");

                System.out.print("Introdueix el genere nou: ");
                game.setGenre(sc.nextLine());
                System.out.println("");

                System.out.print("Introdueix l'any de llançament: ");
                game.setLaunchYear(sc.nextInt());
                sc.nextLine();
                System.out.println("");

                System.out.print("Introdueix la plataforma: ");
                game.setPlatform(sc.nextLine());
                System.out.println("");

                System.out.print("Introdueix el preu: ");
                game.setPrice(sc.nextDouble());
                sc.nextLine();
            }
        }

        if (finded) {
            System.out.println("Videojoc actualitzat correctament.");
            saveVideojocs(arrayGames);
        } else {
            System.out.println("No s'ha trobat cap videojoc amb aquest nom.");
        }

        return arrayGames;
    }

    // Mètode que permet la eliminació de un videojoc de la llista de videojocs i de el fitxer de persistència.
    private static ArrayList<Videojoc> deleteVideogame(String name, ArrayList<Videojoc> arrayGames) {
        boolean deleted = false;

        for (int i = 0; i < arrayGames.size(); i++) {
            if (name.toLowerCase().equals(arrayGames.get(i).getName().toLowerCase())) {
                arrayGames.remove(i);
                saveVideojocs(arrayGames);
                System.out.println("Videojoc eliminat correctament.");
                deleted = true;
            }
        }

        if (!deleted) {
            System.out.println("No s'ha trobat el videojoc a esborrar");
        }

        return arrayGames;
    }
}