//Incorporate the AES-128 encryption/decryption into your program.
// Server Side
import java.net.*;
import java.io.*;
public class ServerSide { 
  public void readFromFileWriteToClient() {
	  FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		Socket server=null;
	try {
		int serverPort = 7777;//server is listening for connection request on port via TCP.
		ServerSocket s = new ServerSocket(serverPort);
		s.setSoTimeout(50000);//set Time out
		System.out.println("Waiting for client"); 
		server = s.accept();//to listen for incoming connection requests from clients.
		File myFile = new File ("encrypt.txt");
        byte [] byteArray  = new byte [(int)myFile.length()];
        bis = new BufferedInputStream(new FileInputStream(myFile));
        bis.read(byteArray,0,byteArray.length);
        os = server.getOutputStream();
        System.out.println("Sending File from Server to Client");
        os.write(byteArray,0,byteArray.length);
        os.flush();
        System.out.println("Done.");		
	}
	catch(UnknownHostException ex) {
		System.out.println("Host not found");
		ex.printStackTrace();
	}
	catch(IOException e){
		e.printStackTrace();//The Socket constructor throws an IOException if it cannot make a connection.
	}
	finally {
		try{
          if (bis != null) bis.close();
          if (os != null) os.close();
          if (server!=null) server.close();
		}
		catch(IOException e){
		e.printStackTrace();//The Socket constructor throws an IOException if it cannot make a connection.
	}
        }
  }
  public static void main(String[] args) {
		TextToASCII ta=new TextToASCII();
		ServerSide s = new ServerSide();
		Encryption e=new Encryption();
		try{
			ta.textToASCII();
			e.encryption();
		}
		catch(IOException ex){
		ex.printStackTrace();//The Socket constructor throws an IOException if it cannot make a connection.
	}
		s.readFromFileWriteToClient();
  }
}