//Incorporate the AES-128 encryption/decryption into your program.
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.PrintWriter;


public class ASCIIToText {
	public static void aSCIIToText() throws IOException {
	BufferedReader fin = new BufferedReader(new FileReader("DecryptFile.txt"));
	PrintWriter pw = new PrintWriter("outputMessage.txt");	  
	      String line="";
	      int c=0;
		  String out="";
	      while((line=fin.readLine())!=null)
	      {
			  out+=line;
		  }
		  StringBuilder output = new StringBuilder();
			for (int i = 0; i < out.length(); i+=3) {
	        String str = out.substring(i, i+2);
			if(str.equals("0a"))
				pw.println();
			else
			pw.print((char)Integer.parseInt(str, 16));
	        output.append((char)Integer.parseInt(str, 16));
			}
			//pw.println(output);
		fin.close();
		pw.close();
    }	
	
}

