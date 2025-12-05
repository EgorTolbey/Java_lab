import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.sort;

public class Main {
    public static void main(String[] args) {
        List<Journal> j = new ArrayList<>();
        try {
            j = reading();
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        try {
            output(j);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public static void utilityFunction(List<Journal> j, int n, PrintWriter writer) {
        List<Journal> journas = new ArrayList<>();
        for (Journal k : j) {
            journas.add(new Journal(k));
        }
        sort(journas, new CompareUtility());
        writer.println("Все журналы:");
        writer.println(journas);
        writer.println();
        writer.println();
        writer.println("Журналы по издательству: ");
        List<List<Journal>> jByPublic = new ArrayList<>(journas.stream().collect(Collectors.groupingBy(Journal::getNameOfPublic)).values());
        for (List<Journal> p : jByPublic) {
            sort(p, new CompareUtility());
        }
        for (List<Journal> p : jByPublic) {
            for (int i = 0; i < n && i < p.size(); i++) {
                writer.println(p.get(i));
            }
        }
        writer.println();
        writer.println();
        writer.println("Журналы по тематике: ");
        jByPublic = new ArrayList<>(journas.stream().collect(Collectors.groupingBy(Journal::getCategory)).values());
        for (List<Journal> p : jByPublic) {
            sort(p, new CompareUtility());
        }
        for (List<Journal> p : jByPublic) {
            for (int i = 0; i < n && i < p.size(); i++) {
                writer.println(p.get(i));
            }
        }
    }

    public static void output(List<Journal> j) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter("rez.txt"));
        System.out.println("введите количество первых журналов: ");
        Scanner buf = new Scanner(System.in);
        String s = buf.nextLine();
        int n = 0;
        try {
            n = Integer.parseInt(s.trim());
            if (n <= 0) {
                System.out.println("значение должно быть натуральным");
                return;
            }
        } catch (NumberFormatException e) {
            throw new Exception("неправильное значение");
        }
        System.out.println("Хотите ли вы отсортировать по функции полезности?(ДА/НЕТ)");
        buf = new Scanner(System.in);
        s = buf.nextLine();
        if (s.contains("ДА")) {
            utilityFunction(j, n, writer);
            return;
        }
        System.out.println("выберите сортировку по издательству или по тематике: ");
        buf = new Scanner(System.in);
        s = buf.nextLine();
        if (!s.isEmpty()) {
            if (s.contains("по издательству")) {
                sortByFactor(j, n, writer);
            } else if (s.contains("по тематике")) {
                sortByCategory(j, n, writer);
            } else {
                System.out.println("такого критерия нет!");
            }
        }
    }

    public static void sortByCategory(List<Journal> j, int n, PrintWriter writer) {
        List<Journal> journas = new ArrayList<>();
        for (Journal k : j) {
            journas.add(new Journal(k));
        }
        List<List<Journal>> jByPublic = new ArrayList<>(journas.stream().collect(Collectors.groupingBy(Journal::getCategory)).values());
        for (List<Journal> p : jByPublic) {
            sort(p, new CompareAVGCitations());
        }
        for (List<Journal> p : jByPublic) {
            for (int i = 0; i < n && i < p.size(); i++) {
                writer.println(p.get(i));
            }
        }
    }

    public static void sortByFactor(List<Journal> j, int n, PrintWriter writer) {
        List<Journal> journas = new ArrayList<>();
        for (Journal k : j) {
            journas.add(new Journal(k));
        }
        List<List<Journal>> jByPublic = new ArrayList<>(journas.stream().collect(Collectors.groupingBy(Journal::getNameOfPublic)).values());
        System.out.println("Выберите по какому критерию вы хотите отсортировать списки журналов каждого издательства: " +
                "по импакт-фактору, по общему количеству цитирований или коэффициент цитирования в рейтинговых журналах");
        Scanner buf = new Scanner(System.in);
        String s = buf.nextLine();
        if (!s.isEmpty()) {
            if (s.contains("по импакт-фактору")) {
                for (List<Journal> p : jByPublic) {
                    p.sort(Comparator.comparing(Journal::getImpactFactor).reversed());
                }
            } else if (s.contains("по общему количеству цитирований")) {
                for (List<Journal> p : jByPublic) {
                    p.sort(Comparator.comparing(Journal::getCoefCitations).reversed());
                }
            } else if (s.contains("коэффициент цитирования в рейтинговых журналах")) {
                for (List<Journal> p : jByPublic) {
                    p.sort(Comparator.comparing(Journal::getCoefCitationsInRateMags).reversed());
                }
            } else {
                System.out.println("Такого параметра нет");
                return;
            }
        }
        for (List<Journal> p : jByPublic) {
            for (int i = 0; i < n && i < p.size(); i++) {
                writer.println(p.get(i));
            }
        }
    }

    public static List<Journal> reading() throws Exception {
        List<Journal> j = new ArrayList<>();
        try {
            BufferedReader buf = new BufferedReader(new FileReader("journalist.txt"));
            String s;
            int cnt = 0;
            double k1 = 0.0;
            double k2 = 0.0;
            double k3 = 0.0;
            int n1 = 0;
            int n2 = 0;
            int n3 = 0;
            String s1 = "";
            String s2 = "";
            String s3 = "";
            while ((s = buf.readLine()) != null) {
                if (!s.trim().isEmpty()) {
                    try {
                        switch (cnt % 9) {
                            case 0:
                                s1 = s;
                                break;
                            case 1:
                                k1 = Double.parseDouble(s.trim());
                                break;
                            case 2:
                                if (s.contains("Chemistry")) {
                                    s2 = "Chemistry";
                                } else if (s.contains("Physics")) {
                                    s2 = "Physics";
                                } else if (s.contains("Biology")) {
                                    s2 = "Biology";
                                } else if (s.contains("Medicine")) {
                                    s2 = "Medicine";
                                } else {
                                    List<String> k = new ArrayList<>(Arrays.asList(s.split(" ", 3)));
                                    if (k.size() < 3) {
                                        k.add(" ");
                                    }
                                    s2 = k.get(2);
                                }
                                break;
                            case 3:
                                n1 = Integer.parseInt(s.trim());
                                break;
                            case 4:
                                n2 = Integer.parseInt(s.trim());
                                break;
                            case 5:
                                n3 = Integer.parseInt(s.trim());
                                break;
                            case 6:
                                k2 = Double.parseDouble(s.trim());
                                break;
                            case 7:
                                k3 = Double.parseDouble(s.trim());
                                break;
                            case 8:
                                s3 = s;
                                Journal journal = new Journal(s1, k1, s2, n1, n2, n3, k2, k3, s3);
                                j.add(journal);
                                break;
                        }
                        cnt++;
                    } catch (NumberFormatException e) {
                        throw new Exception("Неправильный формат числа");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
        return j;
    }
}
