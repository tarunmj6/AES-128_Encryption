/* Use Diffie-Hellman Key Exchange Algorithm to exchange keys between
Alice and Bob. When the client first makes a request for connection, each side will use
BBS to generate its own pseudorandom numbers, and then use Diffie-Hellman to obtain a
common secrete key between the client and the server.*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random; 
public class Client
{
 
    private static Socket socket;
	public static int bbsKey()
	{
		int p=383,q=503;
		int n=p*q;
		Random t = new Random();
		long s=t.nextInt(500)*7777;
		String bbs;
		while(true)
		{
			bbs=KeyUsingBBS(p,q,n,s);
			if(bbs.length()>=7)
				break;
		}
		if(bbs.length()>7)
			bbs=bbs.substring(0, 7);
		if(bbs.charAt(5)=='0' && bbs.charAt(6)=='0')
			bbs='1'+bbs.substring(0,6);
		return Integer.parseInt(bbs,2);
	}

	private static String KeyUsingBBS(int p,int q,int n,long s) {
		long x[]=new long[128];//declaration and instantiation  
		x[0]=(s*s)%n;
		for(int i=1;i<128;i++)
			x[i]=(x[i-1]*x[i-1])%n;
		long[] b = new long[128];
		for(int j=0;j<128;j++)
		{	b[j]=x[j]%2;
		}
		System.out.println();
		
		long w=0;
		for(int j=127;j>=0;j--)
		{
			if(w>=1111111)
				break;
			w=w*10+b[j];
		}
		String bbs=w+"";
		return bbs;
	}
    public static void main(String args[])
    {
        try
        {
            String host = "localhost";
            int port = 7777;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
 
            //Send the message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
			int p = 1223,g = 5;
			int b = bbsKey();
			int returnValue;
			if(b<10)
				returnValue=((int) (((long) Math.pow(g, b))%p))%p;
			else
			{ 	
				returnValue=(int) (((long) Math.pow(g, 10))%p);
				b=b-10;
			}
			for(;b>10;b=b-10)
				returnValue=(returnValue*(int) (((long) Math.pow(g, 10))%p))%p;
			if(b>0)
				returnValue=(returnValue*(int) (((long) Math.pow(g, b))%p))%p;
            String number = returnValue+"";
 
            String sendMessage = number + "\n";
            bw.write(sendMessage);
            bw.flush();
            System.out.println("Key sent to Alice from Bob: "+sendMessage);
 
            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            System.out.println("Key received from the Alice : " +message);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}