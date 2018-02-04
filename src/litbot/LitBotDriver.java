package litbot;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author adam
 */
public class LitBotDriver {
    
    public static void main(String[] args){
        WordBank b = new WordBank();
        BufferedReader br;
        String line;
        String[] words;
        try {
            
            br = new BufferedReader(new FileReader(new File("input.txt")));
            
            while( (line = br.readLine()) != null){
                words = line.split(" ");
                for(int i = 0; i < words.length; i++){
                    b.addWord(words[i]);
                    
                }
            }
            
        } catch(FileNotFoundException e){
            System.out.print(e.getMessage());
        } catch(IOException ioe){
            
        }
        b.write(100);
    }
}
