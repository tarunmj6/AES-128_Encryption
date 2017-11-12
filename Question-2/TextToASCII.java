//Incorporate the AES-128 encryption/decryption into your program.
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class TextToASCII {
	public static void textToASCII() throws IOException {
        BufferedReader fin = new BufferedReader(new FileReader("inputMessage.txt"));
        PrintWriter pw = new PrintWriter("send.txt");
	      String line="";
	      int c=0;
	      while((line=fin.readLine())!=null)
	      {
			  char e[]=line.toCharArray();
			  for (int i = 0; i < e.length; i++)
			  {
				 pw.print(String.format("%02x", (int) e[i])+" ");
				 c++;
				 if(c%16==0)
				 {
					 c=0;
					 pw.println();
				 }
			  }
			  pw.print("0a ");
			  c++;
			  if(c%16==0)
				 {
					 c=0;
					 pw.println();
				 }
		  }
	      for (int i = c; i < 16; i++) {
	    	  pw.print("00 ");
		}
		fin.close();
     pw.close();   
    }	
}

