/*
 * @Descripttion: 
 * @version: 
 * @Editor: VScode
 * @Author: Spade Xiao
 * @Date: 2020-05-26 19:46:55
 * @LastEditors: Spade Xiao
 * @LastEditTime: 2020-05-30 21:37:00
 */ 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

public class Recursive_Descent_Analysis {
    public static void main(String[] args){
        Analysis_Process analysis = new Analysis_Process("result.txt");
        if (analysis.S())
            System.out.println("true");
        else    
            System.out.print("false");        
    }
}

class Analysis_Process{

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
    } 

    public boolean S(){
        if (V())
            if (Input.get(index).equals("=")){
                index++;
                if (E())
                    return true;
            }
        return false;
    } 

    public boolean V(){
        if (Input.get(index).equals("i")){
            index++;
            return true;
        }
        return false;
    }

    public boolean E(){
        if (Input.get(index).equals("i") || Input.get(index).equals("(")){
            if (T())
                if (E1())
                    return true;
        }
        return false;
    }

    public boolean T(){
        if (Input.get(index).equals("i") || Input.get(index).equals("("))
            if(F())
                if (T1())
                    return true;
        return false;
    }

    public boolean E1(){
        while(true){
            if (Input.get(index).equals("+") || Input.get(index).equals("-"))
                if (A())
                    if (T())
                        continue;
                    else
                        return false;
                else
                    return false;
            else if (Input.get(index).equals(")") || Input.get(index).equals("#"))
                return true;
            else 
                return false;
        }
    }

    public boolean A(){
        if (Input.get(index).equals("+") || Input.get(index).equals("-")){
            index++;
            return true;
        }
        else 
            return false;
    }

    public boolean F(){
        if (Input.get(index).equals("(")){
            index++;
            if (E())
                if (Input.get(index).equals(")")){
                    index++;
                    return true;
                }
                else
                    return false;
            else
                return false;
        }
        else if (Input.get(index).equals("i")){
            index++;
            return true;
        }
        else 
            return false;
    }

    public boolean T1(){
        while (true){
            if (Input.get(index).equals("*") || Input.get(index).equals("/"))
                if (M())
                    if (F())
                        continue;
                    else
                        return false;
                else 
                    return false;
            else if (Input.get(index).equals(")") || Input.get(index).equals("#") || Input.get(index).equals("+") || Input.get(index).equals("-"))
                return true;
            else
                return false;
        }
    }

    public boolean M(){
        if (Input.get(index).equals("*") || Input.get(index).equals("/")){
            index++;
            return true;
        }
        else 
            return false;
    }
}

