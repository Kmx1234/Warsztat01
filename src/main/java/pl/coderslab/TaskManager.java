package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static final String FILE_NAME = "tasks.csv";
    static final String[] Opcje = {"dodaj zadanie", "usuń zadanie", "lista", "wyjście"};
    static String[][] zadania;


    public static void main(String[] args) {
        wyświetlOpcje(Opcje);
        zadania = wczytajDane(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            switch (input) {
                case "dodaj zadanie" -> dodajZadania();
                case "wyjście" -> {
                    zapiszDoPliku(FILE_NAME, zadania);
                    System.out.println(ConsoleColors.RED + "Koniec programu.");
                    System.exit(0);
                }
                case "usuń zadanie" -> {
                    usuńZadanie(zadania, podajLiczbe());
                    System.out.println("Value was successfully deleted.");
                }
                case "lista" -> tablica(zadania);
                default -> System.out.println("Proszę wybrać poprawną opcję.");
            }
            wyświetlOpcje(Opcje);
        }
    }

    public static void wyświetlOpcje(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Proszę wybrać opcję " + ConsoleColors.RESET);
        for (String option : Opcje) {
            System.out.println(option);
        }
    }

    public static String[][] wczytajDane(String fileName) {
        Path plik = Paths.get(fileName);
        if (!Files.exists(plik)) {
            System.out.println("Plik nie istnieje");
            System.exit(0);
        }
        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(plik);
            tab = new String[strings.size()][strings.get(0).split(",").length];
            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                System.arraycopy(split, 0, tab[i], 0, split.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            System.out.println("Brak zaplanowanych zadań. Dodaj nowe zadanie");
        }
        return tab;
    }

    public static void tablica(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void dodajZadania() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj opis zadania");
        String description = scanner.nextLine();
        System.out.println("Podaj datę zadanie");
        String dueDate = scanner.nextLine();
        System.out.println("Czy zadanie jest ważne: true/false");
        String isImportant = scanner.nextLine();

        zadania = Arrays.copyOf(zadania, zadania.length + 1);
        zadania[zadania.length - 1] = new String[3];
        zadania[zadania.length - 1][0] = description;
        zadania[zadania.length - 1][1] = dueDate;
        zadania[zadania.length - 1][2] = isImportant;
    }

    public static boolean liczba(String input) {

        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int podajLiczbe() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Proszę wybierz zadanie do usunięcia");

        String n = scanner.nextLine();
        while (!liczba(n)) {
            System.out.println("Błąd. Wprowadź liczbę większą lub zero");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void usuńZadanie(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                zadania = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element nie istnieje w tablicy");
        }
    }

    public static void zapiszDoPliku(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);
        String[] lines = new String[zadania.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
