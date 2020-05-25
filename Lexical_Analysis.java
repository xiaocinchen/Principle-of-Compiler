/*
 * @Descripttion:
 * @version:
 * @Author: Spade Xiao
 * @Date: 2020-05-25 10:35:05
 * @LastEditors: Spade Xiao
 * @LastEditTime: 2020-05-25 15:58:50
 */
//package Compilation_principle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lexical_Analysis{
    public static void main(String[] args) {
        FileScanner scanner = new FileScanner("C:\\Users\\spade\\Desktop\\workspace\\Compilation_principle\\test.c");
        scanner.AnalysisProcess();
        return;
    }
}

class FileScanner{
    private String input = null;
    private FileReader fr = null;
    private FileWriter fw = null;
    private PushbackReader pr = null;
    private String outputFileName = null;
    private String output = null;
    private String content = null;
    private SymbolTable symbolTable = new SymbolTable();

    public FileScanner(String fileName){
        File fp = new File(fileName);

        if(!fp.getName().endsWith(".c")) {
            System.out.println("文件格式不正确...");
            return;
        }

        //构造文件阅读器
        try {
            fr = new FileReader(fp);
            pr = new PushbackReader(fr,2); //用可以回退的回退流封装，并设置两个字符大小的缓存
        } catch (FileNotFoundException e) {
            System.out.println(fileName+"文件不存在...");
            e.printStackTrace();
        }

        // 构造输出文件
        outputFileName = fileName.substring(0, fileName.indexOf('.')) + ".re";
        fp = new File(outputFileName);

        try {
            if (!fp.exists()) {
                fp.createNewFile(); // 创建输出的中间文件
            }
            fw = new FileWriter(fp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AnalysisProcess(){
        if (pr == null){
            System.out.println("文件错误...");
            return;
        }

        char ch;
        try {
            Loop:while(true){
                output = null;
                ch = (char)pr.read();
                if(ch==(char)-1) { //读取文件尾，说明文件分析完成，退出
                    break;
                }
                //System.out.println(ch);
                switch(ch){
                    case ';':
                    case '(':
                    case ')':
                    case '{':
                    case '}':{
                        output = "Single character separator";
                        break;
                    }
                    case '+':
                        ch = (char)pr.read();
                        if (ch == '=')
                            output = "+=";
                        else if (ch == '+')
                            output = "++";
                        else {
                            pr.unread(ch);
                            output = "=";
                        }
                        break;
                    case '-':
                        ch = (char)pr.read();
                        if (ch == '=')
                            output = "-=";
                        else if(ch == '-')
                            output = "--";
                        else{
                            pr.unread(ch);
                            output = "-";
                        }
                        break;
                    case '*':
                        ch = (char)pr.read();
                        if (ch == '=')
                            output = "*=";
                        else if (ch == '/')
                            output = "*/";
                        else{
                            pr.unread(ch);
                            output = "*";
                        }
                        break;
                    case '/':
                        ch = (char)pr.read();
                        if (ch == '=')
                            output = "/=";
                        else if (ch == '*'){
                            output = "/*";
                            while(true){
                                ch = (char)pr.read();
                                if (ch == '*'){
                                    if ((char)pr.read() =='/'){
                                        pr.unread(ch);
                                        pr.unread('/');
                                        break;
                                    }
                                }
                                if(ch==(char)-1) { //读取文件尾，说明文件分析完成，退出
                                    break Loop;
                                }
                            }
                        }
                        else if (ch == '/'){
                            output = "//";
                            while(true){
                                ch = (char)pr.read();
                                if (ch == '\r'){
                                    pr.read();
                                    break;
                                }
                            }
                        }
                        else{
                            pr.unread(ch);
                            output = "/";
                        }
                        break;
                    case '&':
                        ch = (char)pr.read();
                        if (ch == '&')
                            output = "&&";
                        else {
                            pr.unread(ch);
                            output = "&";
                        }
                        break;
                    case '|':
                        ch = (char)pr.read();
                        if (ch == '|')
                            output = "||";
                        else{
                            pr.unread(ch);
                            output = "|";
                        }
                        break;
                    case '!':
                        ch = (char)pr.read();
                        if (ch == '=')
                            output = "!=";
                        else{
                            pr.unread(ch);
                            output = "!";
                        }
                        break;
                    case '>':
                        ch = (char)pr.read();
                        if (ch == '>')
                            output = ">>";
                        else if (ch == '=')
                            output = ">=";
                        else{
                            pr.unread(ch);
                            output = ">";
                        }
                        break;
                    case '<':
                        ch =  (char)pr.read();
                        if (ch == '<')
                            output = "<<";
                        else if (ch == '=')
                            output = "<=";
                        else{
                            pr.unread(ch);
                            output = "<";
                        }
                        break;
                    default:
                        if (Character.isLetter(ch)){
                            switch(ch){
                                case 'v':{
                                    content = String.valueOf(ch);
                                    while (true){
                                        ch = (char)pr.read();
                                        if (ch != ' ')
                                            content += ch;
                                        else
                                            break;
                                    }
                                    if (content.equals("void"))
                                        output = "void";
                                    else
                                        output = "Identifier";
                                    break;
                                }
                                case 'i':{
                                    content = String.valueOf(ch);
                                    while (true){
                                        ch = (char)pr.read();
                                        if (ch != ' ')
                                            content += ch;
                                        else
                                            break;
                                    }
                                    if (content.equals("int"))
                                        output = "int";
                                    else if (content.equals("if"))
                                        output = "if";
                                    else
                                        output = "Identifier";
                                    break;
                                }
                                case 'd':{
                                    content = String.valueOf(ch);
                                    while (true){
                                        ch = (char)pr.read();
                                        if (ch != ' ')
                                            content += ch;
                                        else
                                            break;
                                    }
                                    if (content.equals("double"))
                                        output = "double";
                                    else if (content.equals("do"))
                                        output = "do";
                                    else
                                        output = "Identifier";
                                    break;
                                }
                                case 'f':{
                                    content = String.valueOf(ch);
                                    while (true){
                                        ch = (char)pr.read();
                                        if (ch != ' ')
                                            content += ch;
                                        else
                                            break;
                                    }
                                    if (content.equals("float"))
                                        output = "float";
                                    else if (content.equals("for"))
                                        output = "for";
                                    else
                                        output = "Identifier";
                                    break;
                                }
                                case 'e':{
                                    content = String.valueOf(ch);
                                    while (true){
                                        ch = (char)pr.read();
                                        if (ch != ' ')
                                            content += ch;
                                        else
                                            break;
                                    }
                                    if (content.equals("else"))
                                        output = "else";
                                    else
                                        output = "Identifier";
                                    break;
                                }
                                case 'm':{
                                    content = String.valueOf(ch);
                                    while (true){
                                        ch = (char)pr.read();
                                        if (ch != ' ')
                                            content += ch;
                                        else
                                            break;
                                    }
                                    if (content.equals("main"))
                                        output = "main";
                                    else
                                        output = "Identifier";
                                    break;
                                }
                            }
                        }
                        else if (Character.isDigit(ch)){
                            content = String.valueOf(ch);
                            while (true){
                                ch = (char)pr.read();
                                if (ch != ' ')
                                    content += ch;
                                else
                                    break;
                            }
                            output = "Unsigned Integer";
                            break;
                        }
                        else {
                            output = "Undefined identifier";
                            break;
                        }
                }
                WhereisKey(output, content, ch);
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void WhereisKey(String output,String content,char ch){
        System.out.println(output);
        symbolTable.createSymbleTable();
        for (String key:symbolTable.getSymHashMap().keySet()){
            if (key.equals(output)){
                if (key.equals("Single character separator")||key.equals("Unsigned Integer")||key.equals("Identifier")||key.equals("Undefined identifier")){
                    try {
                        fw.write("("+content+","+symbolTable.getSymHashMap().get(key)+")\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        fw.write("("+output+","+symbolTable.getSymHashMap().get(key)+")\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}

class SymbolTable{

    //private SymbolTable symtable;
    private static HashMap<String,Integer> symbleTableMap = new HashMap<String,Integer>();

    public SymbolTable (){}

    public SymbolTable createSymbleTable(){
        SymbolTable symbolTable = new SymbolTable();
        symbleTableMap.put("void", 1);
        symbleTableMap.put("int", 2);
        symbleTableMap.put("float", 3);
        symbleTableMap.put("double", 4);
        symbleTableMap.put("if", 5);
        symbleTableMap.put("else", 6);
        symbleTableMap.put("for", 7);
        symbleTableMap.put("do", 8);
        symbleTableMap.put("while", 9);
        symbleTableMap.put("main", 10);
        symbleTableMap.put("+", 11);
        symbleTableMap.put("-", 12);
        symbleTableMap.put("*", 13);
        symbleTableMap.put("/", 14);
        symbleTableMap.put(">>", 15);
        symbleTableMap.put("<<", 16);
        symbleTableMap.put("=", 17);
        symbleTableMap.put("==", 18);
        symbleTableMap.put("!=", 19);
        symbleTableMap.put(">",20);
        symbleTableMap.put("<", 21);
        symbleTableMap.put(">=",22);
        symbleTableMap.put("<=",23);
        symbleTableMap.put("//", 24);
        symbleTableMap.put("/*", 25);
        symbleTableMap.put("*/", 26);
        symbleTableMap.put("+=", 27);
        symbleTableMap.put("++", 28);
        symbleTableMap.put("-=", 29);
        symbleTableMap.put("--", 30);
        symbleTableMap.put("*=", 31);
        symbleTableMap.put("/=", 32);
        symbleTableMap.put("&", 33);
        symbleTableMap.put("&&", 34);
        symbleTableMap.put("|", 35);
        symbleTableMap.put("||", 36);
        symbleTableMap.put("Single character separator",37);
        symbleTableMap.put("Unsigned Integer", 38);
        symbleTableMap.put("Identifier", 39);
        symbleTableMap.put("Undefined identifier",40);

        return symbolTable;
    }

    public HashMap<String,Integer> getSymHashMap(){
        return symbleTableMap;
    }

}