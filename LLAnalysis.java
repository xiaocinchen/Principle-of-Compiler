/*
 * @Descripttion: 
 * @version: 
 * @Editor: VScode
 * @Author: Spade Xiao
 * @Date: 2020-06-14 22:40:47
 * @LastEditors: Spade Xiao
 * @LastEditTime: 2020-06-15 01:54:19
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
public class LLAnalysis {

    
    public static void main(String[] args){
        Analysis_Process analysis = new Analysis_Process("result1.txt");
        if (analysis.LL1())
            System.out.println("true");
        else    
            System.out.print("false");  
    }

}

class Analysis_Process{

    private static Map<String, String> m;

    private BufferedReader br = null; 

    private static List<String> Input = new ArrayList<String>(); 

    private int index = 0; 

    public Analysis_Process(String fileName){
        File fp = new File(fileName);
        if (!fp.getName().endsWith(".txt")){
            System.out.println("Fail to open");
            return;
        }
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fp)));
            String twoElementFormula = "";
            while(((twoElementFormula=br.readLine())!=null))
                Input.add(twoElementFormula.substring(twoElementFormula.indexOf(",")+1,twoElementFormula.lastIndexOf(")")));
            Input.add("#");
            System.out.println("Two Element Formula File is "+fileName+".");
            Input.forEach(System.out::print);
            System.out.println();
        }catch(FileNotFoundException e){
            System.out.println(fileName+"There is no file.");
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(Input);
    }
    
    public void Createtable(){
        m = new HashMap<String,String>();
        m.put("S", "V=E");
        m.put("E", "Te");
        m.put("E(", "Te");
        m.put("e+", "ATe");
        m.put("e-", "ATe");
        m.put("e)", "");
        m.put("e", "");
        m.put("Ti", "Ft");
        m.put("T(", "Ft");
        m.put("t+", "");
        m.put("t-", "");
        m.put("t*", "MFt");
        m.put("t/", "MFt");
        m.put("t)", "");
        m.put("t", "");
        m.put("Fi", "i");
        m.put("F(", "(E)");
        m.put("A+", "+");
        m.put("A-", "-");
        m.put("M*", "*");
        m.put("M/", "/");
        m.put("V", "i");
    }

    public boolean LL1(){
        Stack<String> sta = new Stack<String>();
        Createtable();
        sta.push("#");
        sta.push("S");
        while(!sta.empty()){
            String a = sta.peek();
            String b = Input.get(0);
            //System.out.println("a="+a+" b="+b);
            // System.out.println(sta);
            // System.out.println(Input);
            if (a.equals(b)){
                sta.pop();
                Input.remove(0);
            }
            else if (m.get(a+b) != null){
                sta.pop();
                for (int i=0;i<m.get(a+b).length();i++)
                    sta.push(m.get(a+b).charAt(m.get(a+b).length()-i-1)+"");
            }
            else if (m.get(a) != null){
                sta.pop();
                for (int i=0;i<m.get(a).length();i++)
                    sta.push(m.get(a).charAt(m.get(a).length()-i-1)+"");
            }
            else {
                return false;
            }
        }
        if (Input.isEmpty()){
            return true;
        }
        else    
            return false;
    }
}