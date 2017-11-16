import java.util.ArrayList;

public class Production {
    private ArrayList<String> antecedentai = new ArrayList<String>();
    private String konsekventas;
    private boolean flag;
    private boolean flag2;

    Production(String konsekventas, ArrayList<String> antecedentai){
        this.antecedentai = antecedentai;
        this.konsekventas = konsekventas;
        flag = false; //default
        flag2 = false;
    }

    public ArrayList<String> getAntecedentai(){
        return antecedentai;
    }

    public String getKonsekventas(){
        return konsekventas;
    }

    public void setFlag(){
        flag = true;
    }

    public void setFlag2(){
        flag2 = true;
    }

    public boolean getFlag2(){
        return flag2;
    }

    public boolean getFlag(){
        return flag;
    }

    public String getAntecedentaiSt(){
        String temp = "";
        for (int i = 0; i < antecedentai.size(); i++){
            temp = temp + antecedentai.get(i);
            if (i < antecedentai.size() - 1){
                temp = temp + ", ";
            }
        }
        return temp;
    }

    public String getAntecedentaiSt2(){
        String temp = "";
        for (int i = 0; i < antecedentai.size(); i++){
            temp = temp + antecedentai.get(i);
            if (i < antecedentai.size() - 1){
                temp = temp + ",";
            }
        }
        return temp;
    }
}
