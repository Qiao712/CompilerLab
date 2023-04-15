package github.qiao712.lexical;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LexicalAnalyzer {
    public static void main(String[] args) {
        Set<String> keywords = new HashSet<>();
        String source = "";

        //读入
        try(FileInputStream sourceFile = new FileInputStream(args[0]);
            FileInputStream reslistFile = new FileInputStream(args[1]);
            BufferedInputStream sourceInput = new BufferedInputStream(sourceFile);
            Scanner reslistScanner = new Scanner(reslistFile)) {

            System.out.println("保留表:");
            while(reslistScanner.hasNextLine()){
                String keyword = reslistScanner.nextLine();
                keywords.add(keyword);
                System.out.println(keyword);
            }

            StringBuilder sb = new StringBuilder(sourceFile.available());
            int c;
            while((c = sourceInput.read()) != -1){
                sb.append((char)c);
            }
            source = sb.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //词法分析
        StateMachine stateMachine = new StateMachine(keywords);
        List<Token> tokens = stateMachine.analyze(source);

        //输出\打印
        try(FileWriter outputWriter = new FileWriter("output");
            FileWriter idlistWrite = new FileWriter("idlist");
            FileWriter uintlistWrite = new FileWriter("uintlist")){
            for (Token token : tokens) {
                System.out.println(token);
                outputWriter.write(token.toString() + "\n");

                if(token.type == TokenType.IDENTIFIER){
                    idlistWrite.write(token.value);
                    idlistWrite.write('\n');
                }

                if(token.type == TokenType.NUMBER){
                    uintlistWrite.write(token.value);
                    uintlistWrite.write('\n');
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
