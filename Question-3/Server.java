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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
public class Server
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
    public static void main(String[] args)
    {
        try
        {
 
            int port = 7777;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 7777");
			socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String number = br.readLine();
            System.out.println("Key received from Bob is "+number);
			String returnMessage;
			int p = 1223,g = 5;
			int a = bbsKey();
			try
            {
				 int returnValue;
				if(a<10)
					returnValue=((int) (((long) Math.pow(g, a))%p))%p;
				else
				{ 	
					returnValue=(int) (((long) Math.pow(g, 10))%p);
					a=a-10;
				}
				for(;a>10;a=a-10)
					returnValue=(returnValue*(int) (((long) Math.pow(g, 10))%p))%p;
				if(a>0)
					returnValue=(returnValue*(int) (((long) Math.pow(g, a))%p))%p;
                 returnMessage = String.valueOf(returnValue) + "\n";
            }
             catch(NumberFormatException e)
             {
                 returnMessage = "Please send a proper number\n";
             }
			OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(returnMessage);
            System.out.println("Key sent to the Bob is "+returnMessage);
            bw.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){}
        }
    }
}