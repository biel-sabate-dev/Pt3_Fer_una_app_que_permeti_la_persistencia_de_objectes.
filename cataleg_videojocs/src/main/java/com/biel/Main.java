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

class Videojoc implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String genre;
    private int launchYear;
    private String platform;
    private double price;

    public Videojoc() {
    }

    public Videojoc(String name, String genre, int launchYear, String platform, double price) {
        this.name = name;
        this.genre = genre;
        this.launchYear = launchYear;
        this.platform = platform;
        this.price = price;
    }

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

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ArrayList<Videojoc> arrayGames = loadVideogames();

        int opcio = 0;

        try {

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

                switch (opcio) {
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
                        System.out.println("Persona desada correctament.");
                        break;

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

                    case 3:
                        System.out.println("Nom del videojoc: ");
                        String gameToSearch = sc.nextLine();
                        System.out.println("");
                        searchVideogame(gameToSearch);
                        break;

                    case 4:
                        System.out.println("Nom del videojoc a modificar: ");
                        gameToSearch = sc.nextLine();
                        arrayGames = updateVideogame(gameToSearch, loadVideogames());
                        break;

                    case 5:
                        System.out.println("Nom del videojoc a eliminar: ");
                        gameToSearch = sc.nextLine();
                        deleteVideogame(gameToSearch, loadVideogames());

                    default:
                        System.out.println("Opció incorrecta. Torna-ho a provar.");
                }
            } while (opcio != 0);

        } catch (Exception e) {
            System.out.println("Error: Error a l'execucio... ");
        }
        sc.close();
    }

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

    private static void searchVideogame(String name) {
        ArrayList<Videojoc> arrayGames = loadVideogames();
        for (int i = 0; i < arrayGames.size(); i++) {
            if (name.equals(arrayGames.get(i).getName())) {
                System.out.println(arrayGames.get(i).toString());
                //game = arrayGames.get(i);
            } else {
                System.out.println("Videojoc no trobat...");
            }
        }

    }

    private static ArrayList<Videojoc> updateVideogame(String name, ArrayList<Videojoc> arrayGames) {
        boolean finded = false;

        for (Videojoc game : arrayGames) {
            if (name.equals(game.getName())) {
                finded = true;

                System.out.println("Informació actual: " + game.toString());
                System.out.println("Introdueix el nou nom: ");
                game.setName(sc.nextLine());

                System.out.println("Introdueix el genere nou: ");
                game.setGenre(sc.nextLine());

                System.out.println("Introdueix l'any de llançament: ");
                game.setLaunchYear(sc.nextInt());
                sc.nextLine();

                System.out.println("Introdueix la plataforma: ");
                game.setPlatform(sc.nextLine());

                System.out.println("Introdueix el preu: ");
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

    private static ArrayList<Videojoc> deleteVideogame(String name, ArrayList<Videojoc> arrayGames) {
        for (int i = 0; i < arrayGames.size(); i++) {
            if (name.equals(arrayGames.get(i).getName())) {
                arrayGames.remove(i);
                saveVideojocs(arrayGames);
            }
        }

        return arrayGames;
    }
}