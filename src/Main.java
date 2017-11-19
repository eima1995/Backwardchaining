import java.io.*;
import java.util.*;

public class Main {
    public static String fileName = "test1.txt"; // default
    public static String end;
    public static String[] facts;
    public static ArrayList<Production> pr = new ArrayList<Production>();
    public static Stack<String> goals = new Stack<String>();
    public static Stack<String> newFacts = new Stack<String>();
    public static String tab = "   ";
    public static String tab1 = "  ";
    public static ArrayList<String> gdb = new ArrayList<String>();
    public static ArrayList<String> path = new ArrayList<String>();
    public static int iteration = 0;
    public static boolean goal = false;
    public static boolean foundGoal = false;
    public static String output = "";
    public static int deep = 0;
    public static boolean ruleCanBeApplied;

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

    //facts = gdb
    public static boolean backwardChaining(String goal){
        goals.push(goal);// 1.Tikslas išsaugomas į tikslų steką.
        if(gdb.contains(goal)){//2.Jei tikslas yra tarp pradnių faktų, pašalinamas paskutinis tikslas iš tikslų steko, grąžinama, kad tikslas išvedamas (true).
            output += (++iteration) + ") "  + printDeep(deep) + "Tikslas " + goals.peek() + ". Faktas (duotas), nes faktai " + gdb.get(0) + printGdb() + ". Grįžtame, sėkmė." + "\n";
            goals.pop();
            deep--;
            return true;
        }else if(newFacts.contains(goal)){//3.Jei tikslas yra tarp naujų faktų, pašalinamas paskutinis tikslas iš tikslų steko, grąžinama, kad tikslas išvedamas (true).
            output += (++iteration) + ") "  + printDeep(deep) + "Tikslas " + goals.peek() + ". Faktas (buvo gautas). Faktai " + gdb.get(0) + printGdb() + "." + "\n";
            goals.pop();
            deep--;
            return true;
        }else if (isCycle(goal)){//4.Jei tikslų steke susidarė ciklas, pašalinamas paskutinis tikslas iš tikslų steko, grąžinama, kad tikslas neišvedamas (false).
            output += (++iteration) + ") " + printDeep(deep) + "Tikslas " + goals.peek() +". Ciklas. Grįžtame, FAIL" + "\n";
            goals.pop();
            deep--;
            return false;
        }else{
            //path
            //Stack<String> newFactsSaved = newFacts; //saugom isvestus faktus, jei negalesim isvest grazinsim
            for(int i = 0; i < pr.size(); i++){//7.Iteruojamos visos taisyklės.
                ruleCanBeApplied = true;       //8.Pažymima, kad einama taisyklė gali būti pritaikyta.
                if(pr.get(i).getKonsekventas().equals(goal)){ //9.Jei taisyklės konsekventas lygus ieškomam tikslui:
                    output += (++iteration) + ") " + printDeep(deep) + "Tikslas " + goals.peek() +". Randame R" + (i + 1) + ":" + getStProduction(i) + ". Nauji tikslai " + pr.get(i).getAntecedentaiSt() + "." + "\n";
                    for(int j = 0; j < pr.get(i).getAntecedentai().size(); j++){  //10.Iteruojami visi taisyklės faktai, einamą faktą vadiname nauju tikslu.
                        deep++;
                        String newGoal  = pr.get(i).getAntecedentai().get(j);
                        if(!backwardChaining(newGoal)){ //11.Rekursiškai kviečiama backwardChaining funkcija, kuriai, kaip tikslas paduodamas naujas tikslo parametras, tikrinama ar naujas tikslas yra išvedamas.
                            ruleCanBeApplied = false; //12.Jei naujas tikslas neišvedamas, taisyklė pažymima, kaip negalima pritaikyti.
                           // newFacts = newFactsSaved;
                            break; //15.Jei naujas tikslas neišvedamas, baigiami iteruoti taisyklės faktai.
                        }
                       // newFacts.add(pr.get(i).getAntecedentai().get(j));
                    }

                    if(ruleCanBeApplied){ //16.Tikrinama, ar taisyklė gali būti pritaikyta (ar visi taisyklės faktai galėjo būti rekursiškai išvesti).
                        newFacts.push(goal); //17.	Jei taisyklė gali būti pritaikyta, pridedamas naujas tikslas į naujų faktų aibę.
                        //gdb.add(goal);
                        //Prideti cia dar!!!!
                        output += (++iteration) + ") "  + printDeep(deep) + "Tikslas " + goals.peek() + ". Faktas (dabar gautas). Faktai " + gdb.get(0) +  " ir " + printNewFacts() + "." + "\n";
                        deep--;
                        goals.pop();
                        return  true;
                    }
                }
            }
            //Truksta cia dar!!!!
            output += (++iteration) + ") "  + printDeep(deep) + "Tikslas " + goals.peek() + ". Nėra taisyklių jo išvedimui. Grįžtame, FAIL." + "\n";
            goals.pop();
            newFacts.pop();
            deep--;

        }
        return false;
    }

    public static boolean isCycle(String goal){
        int counter = 0;
        for(int i = 0; i < goals.size(); i++){
            if(goals.get(i).equals(goal)){
                counter++;
            }
            if(counter > 1){
                return true;
            }
        }
        return false;
    }

    public static String  printNewFacts(){
        String temp = "";
        for(int j = 0; j < newFacts.size(); j++){
            if(newFacts.size() - 1 == j){
                temp += newFacts.get(j);
            }else{
                temp += newFacts.get(j) + ",";
            }
        }
        return temp;
    }

    /*
    public static void backwardChaining(){
        for(int i = 0; i < pr.size(); i++){
            if(pr.get(i).getKonsekventas().equals(goals.peek())){
                output += (++iteration) + ") " + printDeep() + "Tikslas " + goals.peek() +". Randame R" + (i + 1) + ":" + getStProduction(i) + ". Nauji tikslai " + pr.get(i).getAntecedentaiSt() + "." + "\n";
                //goals = pr.get(i).getAntecedentai();
                goals2.add(goals.peek());
                addGoals(i);

                if(goals2.contains(goals.peek())){
                    output += (++iteration) + ") " + printDeep() + "Tikslas " + goals.peek() +". Ciklas. Grįžtame, FAIL" + "\n";
                    goals2.remove(goals.peek());
                    goals.pop();
                    //deep--;
                }else {
                    deep++;
                    backwardChaining();
                }
            }else if(gdb.contains(goals.peek())){
                output += (++iteration) + ") "  + printDeep() + "Tikslas " + goals.peek() + ". Faktas (duotas), nes faktai " + gdb.get(0) + printGdb() + ". Grįžtame, sėkmė." + "\n";
                goals.pop();
                //perkelti tikriausiai reikes.
                gdb.add(goals.peek());
                deep--;
                output += (++iteration) + ") "  + printDeep() + "Tikslas " + goals.peek() + ". Faktas (dabar gautas). Faktai " + gdb.get(0) + printGdb() + "." + "\n";
                goals.pop();
                break;
            }else if(i == pr.size() - 1){
                output += (++iteration) + ") "  + printDeep() + "Tikslas " + goals.peek() + ". Nėra taisyklių jo išvedimui. Grįžtame, FAIL." + "\n";
                goals.pop();
                deep--;
            }
        }
    }*/

    public static String printDeep(int deep){
        return String.join("", Collections.nCopies(deep, "-"));
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
        backwardChaining(end);
        System.out.println(output);
    }
}
