import java.io.*;
import java.util.*;

public class Main {
    public static String fileName = "test1.txt"; // default
    public static String end;
    public static String[] facts;
    public static ArrayList<Production> pr = new ArrayList<Production>();
    public static Stack<String> goals = new Stack<String>();
    public static String tab = "   ";
    public static String tab1 = "  ";
    public static ArrayList<String> gdb = new ArrayList<String>();
    public static ArrayList<String> path = new ArrayList<String>();
    public static int iteration = 0;
    public static boolean goal = false;
    public static boolean foundGoal = false;
    public static String output = "";
    public static int deep = 0;


    public static void readFromFile() throws Exception {
        FileReader fr = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fr);
        String line;
        bufferedReader.readLine(); // vardas pavarde etc.
        bufferedReader.readLine(); // testo nr.
        bufferedReader.readLine(); // taisykles
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("2) Faktai")) {
                facts = bufferedReader.readLine().split(" ");
            } else if (line.equals("3) Tikslas")) {
                end = bufferedReader.readLine();
            } else {
                String[] temp = line.split(" ");
                ArrayList<String> antecedentai = new ArrayList<String>();
                String kosekventas = temp[0];
                for (int i = 1; i < temp.length; i++) {
                    if (temp[i].equals("//")) {
                        break;
                    }
                    antecedentai.add(temp[i]);
                }
                pr.add(new Production(kosekventas, antecedentai));
            }
        }
        fr.close();
        bufferedReader.close();

        for (int i = 0; i < facts.length; i++) {
            gdb.add(facts[i]);
        }

        goals.add(end);
    }

    public static void dataOutput() {
        System.out.println("1 DALIS. Duomenys \n");
        System.out.println("1) Taisyklės");
        for (int i = 0; i < pr.size(); i++) {
            String antecedentai = "";
            for (int j = 0; j < pr.get(i).getAntecedentai().size(); j++) {
                antecedentai = antecedentai + pr.get(i).getAntecedentai().get(j);
                if (j < pr.get(i).getAntecedentai().size() - 1) {
                    antecedentai = antecedentai + ", ";
                }
            }
            System.out.println(tab + "R" + (i + 1) + ": " + antecedentai + " -> " + pr.get(i).getKonsekventas());
        }
        System.out.println("\n2) Faktai");
        String temp = "";
        for (int i = 0; i < facts.length; i++) {
            temp = temp + facts[i];
            if (i < facts.length - 1) {
                temp = temp + ", ";
            }
        }
        System.out.println(tab + temp);
        System.out.println("\n3) Tikslas");
        System.out.println(tab + end);
    }

    public static void backwardChaining(){
        for(int i = 0; i < pr.size(); i++){
            if(pr.get(i).getKonsekventas().equals(goals.peek())){
                output += (++iteration) + ") " + recurssionDeep() + "Tikslas " + goals.peek() +". Randame R" + iteration + ":" + getStProduction(i) + ". Nauji tikslai " + pr.get(i).getAntecedentaiSt() + "." + "\n";
                //goals = pr.get(i).getAntecedentai();
                addGoals(i);
                deep++;
                backwardChaining();
            }else if(gdb.contains(goals.peek())){
                output += (++iteration) + ") "  + recurssionDeep() + "Tikslas " + goals.peek() + ". Faktas (duotas), nes faktai " + gdb.get(0) + printGdb() + ". Grįžtame, sėkmė." + "\n";
                goals.pop();
                //perkelti tikriausiai reikes.
                gdb.add(goals.peek());
                deep--;
                output += (++iteration) + ") "  + recurssionDeep() + "Tikslas " + goals.peek() + ". Faktas (dabar gautas). Faktai " + gdb.get(0) + printGdb() + "." + "\n";
                goals.pop();

                break;
            }else if(i == pr.size() - 1){
                output += (++iteration) + ") "  + recurssionDeep() + "Tikslas " + goals.peek() + ". Nėra taisyklių jo išvedimui. Grįžtame, FAIL." + "\n";
                goals.pop();
                deep--;
                gdb.remove(gdb.size()-1);
            }
        }
    }

    public static String printGdb(){
        String temp = "";
        if(gdb.size() > 1){
            temp += " ir ";
            for(int i = 1; i < gdb.size(); i++){
                if(gdb.size() - 1 == i){
                    temp += gdb.get(i);
                }else{
                    temp += gdb.get(i) + ",";
                }
            }
        }
        return temp;
    }

    public static void addGoals(int index){
        int temp =  pr.get(index).getAntecedentai().size();
        for(int i = 0; i < pr.get(index).getAntecedentai().size(); i++){
            goals.add(pr.get(index).getAntecedentai().get(temp - 1 - i));
        }
    }

    public static String recurssionDeep(){
        return String.join("", Collections.nCopies(deep, "-"));
    }

    public static void ui() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Įveskite testo nr.");
        int testNr = reader.nextInt();
        switch (testNr) {
            case 1:
                fileName = "test1.txt";
                break;
            case 2:
                fileName = "test2.txt";
                break;
            case 3:
                fileName = "test3.txt";
                break;
            case 4:
                fileName = "test4.txt";
                break;
            case 5:
                fileName = "test5.txt";
                break;
            case 6:
                fileName = "test6.txt";
                break;
            default:
                break;
        }
        reader.close();
    }

    public static String getStProduction(int i){
        String st = "";
        for (int j = 0; j < pr.get(i).getAntecedentai().size(); j++){
            if (j ==  pr.get(i).getAntecedentai().size() - 1){
                st += pr.get(i).getAntecedentai().get(j) + "->" + pr.get(i).getKonsekventas();
            }else {
                st += pr.get(i).getAntecedentai().get(j) + ",";

            }
        }
        return st;
    }

    public static void main(String args[]) throws Exception {
        ui();
        readFromFile();
        dataOutput();

        System.out.println("\n2 DALIS. Vykdymas");
        backwardChaining();
        System.out.println(output);

    }
}
