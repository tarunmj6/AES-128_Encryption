//Incorporate the AES-128 encryption/decryption into your program.
// Client Side
import java.io.*;
import java.net.*;
public class ClientSide {
  public void run() {
	FileOutputStream fos = null;
	BufferedOutputStream bos = null;
	Socket socket = null;
	int read;
    int c = 0;
	try {
		int serverPort = 7777;
		InetAddress host = InetAddress.getByName("localhost"); //
		System.out.println("Connecting to server on port " + serverPort); 
		socket = new Socket(host,serverPort); //Connected with Server with host and port like 127.0.0.1:7777
		System.out.println("Connected to " + socket.getRemoteSocketAddress());
		byte [] mybytearray  = new byte [5555555];
        InputStream is = socket.getInputStream();
        fos = new FileOutputStream("receive.txt");
        bos = new BufferedOutputStream(fos);
        read = is.read(mybytearray,0,mybytearray.length);
      c = read;
	  System.out.println("Receving File from Server");
		do {
         read =
            is.read(mybytearray, c, (mybytearray.length-c));
         if(read >= 0) c += read;
      } while(read > -1);
	
      bos.write(mybytearray, 0 , c);
      bos.flush();
	  System.out.println("Done");
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
      if (fos != null) fos.close();
      if (bos != null) bos.close();
      if (socket != null) socket.close();
	  }
		catch(IOException e){
		e.printStackTrace();//The Socket constructor throws an IOException if it cannot make a connection.
	}
    }
	}
	public static void main(String[] args) {
		ClientSide c = new ClientSide();
		c.run();
		Decryption d=new Decryption();
		try{
			d.decryption();
			ASCIIToText at= new ASCIIToText();
			at.aSCIIToText();
		}
		catch(IOException ex){
		ex.printStackTrace();//The Socket constructor throws an IOException if it cannot make a connection.
	}
  }
}