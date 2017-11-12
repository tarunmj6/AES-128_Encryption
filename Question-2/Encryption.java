//Incorporate the AES-128 encryption/decryption into your program.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
public class Encryption {
	public static int Lb=4,Lr=0,Lk=0;
	static int encrypt_in[]=new int[16];
	static int encrypt_out[]=new int[16];
	static int encrypt_state[][]=new int[4][4];
	static int encrypt_RoundKey[]=new int[240];
	static int Key[]=new int[32];
	static int sbox[];
	static int Rcon[] = {
	    0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a,
	    0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
	    0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a,
	    0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8,
	    0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef,
	    0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc,
	    0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b,
	    0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3,
	    0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94,
	    0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20,
	    0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35,
	    0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f,
	    0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04,
	    0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63,
	    0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd,
	    0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb  };
	public static int hexVal(char c) {
		    if (c >= '0' && c <= '9')
		      return (int)(c-'0');
		    else
		      return (int)(c-'a'+10);
	}
	private static void readBox() throws IOException {
	    BufferedReader fin = new BufferedReader(new FileReader("sbox.txt"));
	    sbox = new int[256];
		for (int i=0; i<16; i++) {
	      String line = fin.readLine();
	    StringTokenizer tok = new StringTokenizer(line);
		for (int j=0; j<16; j++) {
		String flag = tok.nextToken();
	        sbox[16*i+j] = 16*hexVal(flag.charAt(0))+hexVal(flag.charAt(1));
	      }
	    }
		fin.close();
	  }
	static int Sbox_Val(int n)
	{
	    if(n>=256)
	    	n=n%256;
	    return sbox[n];
	}
	private static BufferedReader file;
	static void encrypt_AddRoundKey(int rnd)
	{
	    int i,j;
	    for(i=0;i<4;i++)
	    {
	        for(j=0;j<4;j++)
	        {
	            encrypt_state[j][i] ^= encrypt_RoundKey[rnd * Lb * 4 + i * Lb + j];
	        }
	    }
	}
	static void encrypt_SubBytes()
	{
	    int i,j;
	    for(i=0;i<4;i++)
	    {
	        for(j=0;j<4;j++)
	        	encrypt_state[i][j] = Sbox_Val(encrypt_state[i][j]);
	    }
	}
	static void encrypt_ShiftRows()
	{
	    int flag;
	    flag=encrypt_state[1][0];
	    encrypt_state[1][0]=encrypt_state[1][1];
	    encrypt_state[1][1]=encrypt_state[1][2];
	    encrypt_state[1][2]=encrypt_state[1][3];
	    encrypt_state[1][3]=flag;
	
		flag=encrypt_state[2][0];
	    encrypt_state[2][0]=encrypt_state[2][2];
	    encrypt_state[2][2]=flag;
	    flag=encrypt_state[2][1];
	    encrypt_state[2][1]=encrypt_state[2][3];
	    encrypt_state[2][3]=flag;
	    
		flag=encrypt_state[3][0];
	    encrypt_state[3][0]=encrypt_state[3][3];
	    encrypt_state[3][3]=encrypt_state[3][2];
	    encrypt_state[3][2]=encrypt_state[3][1];
	    encrypt_state[3][1]=flag;
	}
	static int encrypt_time(int q){   return ((q<<1) ^ (((q>>7) & 1) * 0x1b));}
	static void MixColumns()
	{
	    int i;
	    int val,cr,p;
	    for(i=0;i<4;i++)
	    {
	        p=encrypt_state[0][i];
	        val = encrypt_state[0][i] ^ encrypt_state[1][i] ^ encrypt_state[2][i] ^ encrypt_state[3][i] ;
	        cr = encrypt_state[0][i] ^ encrypt_state[1][i] ; cr = encrypt_time(cr); encrypt_state[0][i] ^= cr ^ val ;
	        cr = encrypt_state[1][i] ^ encrypt_state[2][i] ; cr = encrypt_time(cr); encrypt_state[1][i] ^= cr ^ val ;
	        cr = encrypt_state[2][i] ^ encrypt_state[3][i] ; cr = encrypt_time(cr); encrypt_state[2][i] ^= cr ^ val ;
	        cr = encrypt_state[3][i] ^ p ; cr = encrypt_time(cr); encrypt_state[3][i] ^= cr ^ val ;
	    }
	}
	static void encrypt_KeyExpansion()
	{
	    int i,j;
	    int flag[]=new int[4];
	    int k;
	    for(i=0;i<Lk;i++)
	    {
	        encrypt_RoundKey[i*4]=Key[i*4];
	        encrypt_RoundKey[i*4+1]=Key[i*4+1];
	        encrypt_RoundKey[i*4+2]=Key[i*4+2];
	        encrypt_RoundKey[i*4+3]=Key[i*4+3];
	    }
	    while (i < (Lb * (Lr+1)))
	    {
	        for(j=0;j<4;j++)
	        {
	            flag[j]=encrypt_RoundKey[(i-1) * 4 + j];
	        }
	        if (i % Lk == 0)
	        {
	            {
	                k = flag[0];
	                flag[0] = flag[1];
	                flag[1] = flag[2];
	                flag[2] = flag[3];
	                flag[3] = k;
	            }
	            {
	                flag[0]=Sbox_Val(flag[0]);
	                flag[1]=Sbox_Val(flag[1]);
	                flag[2]=Sbox_Val(flag[2]);
	                flag[3]=Sbox_Val(flag[3]);
	            }
	            flag[0] =  flag[0] ^ Rcon[i/Lk];
	        }
	        else if (Lk > 6 && i % Lk == 4)
	        {
	            {
	                flag[0]=Sbox_Val(flag[0]);
	                flag[1]=Sbox_Val(flag[1]);
	                flag[2]=Sbox_Val(flag[2]);
	                flag[3]=Sbox_Val(flag[3]);
	            }
	        }
	        encrypt_RoundKey[i*4+0] = encrypt_RoundKey[(i-Lk)*4+0] ^ flag[0];
	        encrypt_RoundKey[i*4+1] = encrypt_RoundKey[(i-Lk)*4+1] ^ flag[1];
	        encrypt_RoundKey[i*4+2] = encrypt_RoundKey[(i-Lk)*4+2] ^ flag[2];
	        encrypt_RoundKey[i*4+3] = encrypt_RoundKey[(i-Lk)*4+3] ^ flag[3];
	        i++;
	    }
	}
	static void encrypt_Cipher()
	{
	    int i,j,rnd=0;
	    for(i=0;i<4;i++)
	    {
	        for(j=0;j<4;j++)
	            encrypt_state[j][i] = encrypt_in[i*4 + j];
	    }
	    encrypt_AddRoundKey(0);
	    for(rnd=1;rnd<Lr;rnd++)
	    {
	        encrypt_SubBytes();
	        encrypt_ShiftRows();
	        MixColumns();
	        encrypt_AddRoundKey(rnd);
	    }
	    encrypt_SubBytes();
	    encrypt_ShiftRows();
	    encrypt_AddRoundKey(Lr);
	    for(i=0;i<4;i++)
	    {
	        for(j=0;j<4;j++)
	            encrypt_out[i*4+j]=encrypt_state[j][i];
	    }
	}
	public static void readKey(String file,int sbox[]) throws IOException {
    BufferedReader fin = new BufferedReader(new FileReader(file));
	String line = fin.readLine();
    StringTokenizer tok = new StringTokenizer(line);
	for (int j=0; j<16; j++) {
		String flag = tok.nextToken();
        sbox[j] = 16*hexVal(flag.charAt(0))+hexVal(flag.charAt(1));
      }
    fin.close();
  }
	public static void encryption() throws IOException
	{
	    int i;
	    Lr=128;
	    Lk = Lr / 32;
	    Lr = Lk + 6;
		int flag[]=new int[16];
		readKey("key.txt",flag);
		file = new BufferedReader(new FileReader("send.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter("encrypt.txt"));
		char temp2[]=new char[16];
		readBox();
		String line="";
		while ((line=file.readLine())!=null){
		StringTokenizer tok = new StringTokenizer(line);
		for (int j=0; j<16; j++) {
		String p = tok.nextToken();
	        temp2[j] =(char) (16*hexVal(p.charAt(0))+hexVal(p.charAt(1)));
		}
		for(i=0;i<Lk*4;i++)
	    {
	        Key[i]=flag[i];
	        encrypt_in[i]=temp2[i];
	    }
		encrypt_KeyExpansion();
	    encrypt_Cipher();
	    for(i=0;i<Lk*4;i++)
			pw.printf("%02x ",encrypt_out[i]);
	    pw.println();
		}
		pw.close();
	 }	 
}