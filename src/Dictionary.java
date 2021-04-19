import java.io.*;
import java.util.*;

public class Dictionary {


    public static void importDic(Map<String, ArrayList<String>> dic, String path) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(path));
        String tmp;
        int count = 0;
        try {
            while(true) {
                tmp = file.readLine();
                count++;
                if(tmp == null) {
                    System.out.println(count);
                    break;
                }
                //System.out.println(tmp);
                String[] spl = tmp.split("`");
                if(spl.length != 2)
                    continue;
                String[] def = spl[1].split("\\|");
                Arrays.parallelSetAll(def, (i) -> def[i].trim());
                ArrayList<String> defList = new ArrayList<>(Arrays.asList(def));
                dic.put(spl[0], defList);
            }
        } catch (EOFException e) {
            System.out.println("File read");
        }
        file.close();
    }

    public static void exportDic(Map<String, ArrayList<String>> dic, String path) {
        try {
            PrintWriter file = new PrintWriter(path);
            for(Map.Entry<String, ArrayList<String>> entry : dic.entrySet()) {
                file.write(entry.getKey() + "`");
                String val = String.join("| ", entry.getValue());
                file.write(val);
                file.write('\n');
            }
            file.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot create file");
        }
    }

    public static void printASlang(Map<String, ArrayList<String>> dic, String key) {
        System.out.print(key + ": ");
        System.out.println(String.join("; ", dic.get(key)));
    }

    public static void findBySlang(Map<String, ArrayList<String>> dic, String slang) {
        for(Map.Entry<String, ArrayList<String>> entry : dic.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(slang)) {
                printASlang(dic, entry.getKey());
            }
        }
    }

    public static void findByDef(Map<String, ArrayList<String>> dic, String def) {
        for(Map.Entry<String, ArrayList<String>> entry : dic.entrySet()) {
            for(String str : entry.getValue()) {
                if(str.toLowerCase().contains(def.toLowerCase())) {
                    printASlang(dic, entry.getKey());
                }
            }
        }
    }

    public static void historyShow(List<String> history) {
        System.out.println("Cac slang word da tim kiem: ");
        for(String str : history) {
            System.out.println(str);
        }
    }

    public static void mainMenu() {
        System.out.println("1. Tim kiem theo slang word");
        System.out.println("2. Tim kiem theo dinh nghia");
        System.out.println("3. History");
        System.out.println("4. Them slang word moi");
        System.out.println("5. Edit slang word");
        System.out.println("6. Delete slang word");
        System.out.println("7. Reset dictionary");
        System.out.println("8. Slang word ngau nhien");
        System.out.println("9. Do vui slang word");
        System.out.println("10. Do vui definition");
        System.out.println("11. Thoat");
        System.out.print("Vui long chon: ");
    }


    public static void addNewSlang(Map<String, ArrayList<String>> dic) {
        Scanner scanner = new Scanner(System.in);
        String key = scanner.next();
        if(dic.containsKey(key)) {
            System.out.println("Slang word da ton tai, ban co muon them definition vao slang word co san (Y,n): ");
            String con = scanner.next();
            if(con.equalsIgnoreCase("y")) {
                System.out.println("Nhap definition: ");
                scanner.nextLine();
                String value = scanner.nextLine();
                dic.get(key).add(value);
                System.out.println("Them thanh cong");
            }
        } else {
            ArrayList<String> def = new ArrayList<>();
            while (true) {
                System.out.println("Nhap definition: ");
                scanner.nextLine();
                String value = scanner.nextLine();
                def.add(value);
                System.out.println("Ban co muon nhap them definition (Y,n): ");
                String con = scanner.next();
                if(!con.equalsIgnoreCase("y")) {
                    break;
                }
            }
            dic.put(key, def);
        }
    }

    public static void editSlang(Map<String, ArrayList<String>> dic) {
        System.out.println("Nhap slang can edit: ");
        Scanner scanner = new Scanner(System.in);
        String key = scanner.next();
        if(!dic.containsKey(key)) {
            System.out.println("Slang nay khong ton tai");
            return;
        }
        printASlang(dic, key);
        System.out.println("Ban muon edit slang hay definition:");
        System.out.println("1. Slang");
        System.out.println("2. Definition");
        String tmp;
        int choice = scanner.nextInt();
        if (choice == 2) {
            System.out.println("1. Them");
            System.out.println("2. Xoa");
            System.out.println("3. Sua");
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("Nhap definition can them: ");
                    scanner.nextLine();
                    tmp = scanner.nextLine();
                    dic.get(key).add(tmp);
                }
                case 2 -> {
                    System.out.println("Nhap definition can xoa: ");
                    scanner.nextLine();
                    tmp = scanner.nextLine();
                    dic.get(key).remove(tmp);
                }
                case 3 -> {
                    System.out.println("Nhap definition can sua: ");
                    scanner.nextLine();
                    tmp = scanner.nextLine();
                    System.out.println("Nhap definition moi: ");
                    String tmp1 = scanner.nextLine();
                    dic.get(key).remove(tmp);
                    dic.get(key).add(tmp1);
                }
                default -> System.out.println("invalid");
            }
            printASlang(dic, key);
        } else if (choice == 1) {
            System.out.println("Nhap slang word can sua: ");
            tmp = scanner.next();
            if (dic.containsKey(tmp)) {
                System.out.println("Nhap slang moi: ");
                String tmp1 = scanner.next();
                if(dic.containsKey(tmp1)) {
                    System.out.println("Slang word da ton tai");
                    return;
                }
                ArrayList<String> dup = dic.get(tmp);
                dic.remove(key);
                dic.put(tmp1, dup);
                printASlang(dic, tmp1);
            } else {
                System.out.println("Khong ton tai slang");
            }
        }
    }

    public static void removeSlang(Map<String, ArrayList<String>> dic, String slang) {
        if (dic.containsKey(slang)) {
            System.out.println("Ban co muon xoa slang word " + slang + "(Y,n): ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("y")) {
                dic.remove(slang);
            }
        } else {
            System.out.println("Slang khong ton tai");
        }
    }

    public static void restoreBackup(Map<String, ArrayList<String>> dic, String path, String backupPath) {
        try {
            File file = new File(backupPath);
            if(!file.exists()) {
                System.out.println("Khong ton tai file backup");
                return;
            }
            dic.clear();
            importDic(dic, backupPath);
            exportDic(dic, path);
        } catch (IOException e) {
            System.out.println("Khong the khoi phuc");
        }
    }

    public static String randomSlang(Map<String, ArrayList<String>> dic) {
        Object[] key =  dic.keySet().toArray();
        Random random = new Random();
        return key[random.nextInt(key.length)].toString();
    }

    public static String randomDefinition(Map<String, ArrayList<String>> dic, String slang) {
        Random random = new Random();
        List<String> def = dic.get(slang);
        return def.get(random.nextInt(def.size()));
    }

    public static void quizBySlang(Map<String, ArrayList<String>> dic) {
        if(dic.size() < 5) {
            System.out.println("Khong du so tu de do vui");
            return;
        }
        String slang = randomSlang(dic);
        List<String> ans = new ArrayList<>();
        ans.add(randomDefinition(dic, slang));
        while(ans.size() < 4) {
            String randSlang = randomSlang(dic);
            if(randSlang.equals(slang))
                continue;
            String randDef = randomDefinition(dic, randSlang);
            if(ans.contains(randDef))
                continue;
            ans.add(randDef);
        }
        Collections.shuffle(ans);
        System.out.println("Definition nao phu hop voi slang \"" + slang + "\":");
        for(int i = 0; i < ans.size(); i++) {
            System.out.println((i+1) + ". " + ans.get(i));
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Chon dap an: ");
        int choice = scanner.nextInt();
        if(choice > ans.size() || choice == 0) {
            System.out.println("Khong hop le");
            return;
        }
        if(dic.get(slang).contains(ans.get(choice - 1))) {
            System.out.println("Ban da tra loi dung");
        } else {
            System.out.println("Ban da tra loi sai");
        }
    }

    public static void quizByDefinition(Map<String, ArrayList<String>> dic) {
        if(dic.size() < 5) {
            System.out.println("Khong du so tu de do vui");
            return;
        }
        String slang = randomSlang(dic);
        List<String> ans = new ArrayList<>();
        String def = randomDefinition(dic, slang);
        ans.add(slang);
        while(ans.size() < 4) {
            String randSlang = randomSlang(dic);
            if(randSlang.equals(slang))
                continue;
            ans.add(randSlang);
        }
        Collections.shuffle(ans);
        System.out.println("Slang nao phu hop voi definition \"" + def + "\":");
        for(int i = 0; i < ans.size(); i++) {
            System.out.println((i+1) + ". " + ans.get(i));
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Chon dap an: ");
        int choice = scanner.nextInt();
        if(choice > ans.size() || choice == 0) {
            System.out.println("Khong hop le");
            return;
        }
        if(slang.equals(ans.get(choice - 1))) {
            System.out.println("Ban da tra loi dung");
        } else {
            System.out.println("Ban da tra loi sai");
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, ArrayList<String>> dic = new HashMap<>();
        List<String> history = new ArrayList<>();
        String fileName = "slang.txt";
        importDic(dic, fileName);
        String tmp;
        while(true) {
            System.out.println("--------------------------");
            mainMenu();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter: ");
                    tmp = scanner.next();
                    findBySlang(dic, tmp);
                    history.add(tmp);
                    break;
                case 2:
                    System.out.print("Enter: ");
                    scanner.nextLine();
                    tmp = scanner.nextLine();
                    findByDef(dic, tmp);
                    break;
                case 3:
                    historyShow(history);
                    break;
                case 4:
                    addNewSlang(dic);
                    exportDic(dic, fileName);
                    break;
                case 5:
                    editSlang(dic);
                    exportDic(dic, fileName);
                    break;
                case 6:
                    System.out.print("Enter: ");
                    tmp = scanner.next();
                    removeSlang(dic, tmp);
                    exportDic(dic, fileName);
                    break;
                case 7:
                    restoreBackup(dic, fileName, "slang.txt.backup");
                    break;
                case 8:
                    System.out.println("On this day slang word");
                    printASlang(dic, randomSlang(dic));
                    break;
                case 9:
                    quizBySlang(dic);
                    break;
                case 10:
                    quizByDefinition(dic);
                    break;
                case 11:
                    return;
            }
        }
    }
}
