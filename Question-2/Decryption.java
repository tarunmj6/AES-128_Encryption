//Incorporate the AES-128 encryption/decryption into your program.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
public class Decryption {
	static int Lb= 4,Lr=0,Lk=0;
	static int Key[]=new int[32];
	static int decrypt_invbox[]=new int[256];
	static int decrypt_RoundKey[]=new int[240];
	static int decrypt_state[][]=new int[4][4];
	static int decrypt_in[]=new int[16];
	static int decrypt_out[]=new int[16];
	static int sbox[]=new int[256];
	private static void readBox(int box[],String file) throws IOException {
	    BufferedReader fin = new BufferedReader(new FileReader(file));
		for (int i=0; i<16; i++) {
	      String line = fin.readLine();
	    StringTokenizer tok = new StringTokenizer(line);
		for (int j=0; j<16; j++) {
		String flag = tok.nextToken();
	        box[16*i+j] = 16*hexVal(flag.charAt(0))+hexVal(flag.charAt(1));
			
	      }
	    }
		fin.close();
	  }
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
	static int decrypt_getinvSbox(int n)
	{
		if(n>=256)
			n=n%256;
		return decrypt_invbox[n];
	}
	static int invSbox_val(int n)
	{
		if(n>=256)
			n=n%256;
		return sbox[n];
	}
	private static PrintWriter pw;
	private static BufferedReader file;
	static void decrypt_ExpandKey()
	{
		int i,j;
		int flag[]=new int[4];
		int m;
		for(i=0;i<Lk;i++)
		{
			decrypt_RoundKey[i*4]=Key[i*4];
			decrypt_RoundKey[i*4+1]=Key[i*4+1];
			decrypt_RoundKey[i*4+2]=Key[i*4+2];
			decrypt_RoundKey[i*4+3]=Key[i*4+3];
		}
		while (i < (Lb * (Lr+1)))
		{
			for(j=0;j<4;j++)
			{
				flag[j]=decrypt_RoundKey[(i-1) * 4 + j];
			}
			if (i % Lk == 0)
			{
				{
					m = flag[0];
					flag[0] = flag[1];
					flag[1] = flag[2];
					flag[2] = flag[3];
					flag[3] = m;
				}
				{
					flag[0]=invSbox_val(flag[0]);
					flag[1]=invSbox_val(flag[1]);
					flag[2]=invSbox_val(flag[2]);
					flag[3]=invSbox_val(flag[3]);
				}
				flag[0] =  flag[0] ^ Rcon[i/Lk];
			}
			else if (Lk > 6 && i % Lk == 4)
			{
				{
					flag[0]=invSbox_val(flag[0]);
					flag[1]=invSbox_val(flag[1]);
					flag[2]=invSbox_val(flag[2]);
					flag[3]=invSbox_val(flag[3]);
				}
			}
			decrypt_RoundKey[i*4+0] = decrypt_RoundKey[(i-Lk)*4+0] ^ flag[0];
			decrypt_RoundKey[i*4+1] = decrypt_RoundKey[(i-Lk)*4+1] ^ flag[1];
			decrypt_RoundKey[i*4+2] = decrypt_RoundKey[(i-Lk)*4+2] ^ flag[2];
			decrypt_RoundKey[i*4+3] = decrypt_RoundKey[(i-Lk)*4+3] ^ flag[3];
			i++;
		}
	}
	static void decrypt_AddRoundKey(int rnd) 
	{
		int i,j;
		for(i=0;i<4;i++)
		{
			for(j=0;j<4;j++)
			{
				decrypt_state[j][i] ^= decrypt_RoundKey[rnd * Lb * 4 + i * Lb + j];
			}
		}
	}
	static void decrypt_InvSubBytes()
	{
		int i,j;
		for(i=0;i<4;i++)
		{
			for(j=0;j<4;j++)
			{
				decrypt_state[i][j] = decrypt_getinvSbox(decrypt_state[i][j]);

			}
		}
	}
	static void decrypt_InvShiftRows()
	{
		int flag;
		flag=decrypt_state[1][3];
		decrypt_state[1][3]=decrypt_state[1][2];
		decrypt_state[1][2]=decrypt_state[1][1];
		decrypt_state[1][1]=decrypt_state[1][0];
		decrypt_state[1][0]=flag;

		flag=decrypt_state[2][0];
		decrypt_state[2][0]=decrypt_state[2][2];
		decrypt_state[2][2]=flag;

		flag=decrypt_state[2][1];
		decrypt_state[2][1]=decrypt_state[2][3];
		decrypt_state[2][3]=flag;

		flag=decrypt_state[3][0];
		decrypt_state[3][0]=decrypt_state[3][1];
		decrypt_state[3][1]=decrypt_state[3][2];
		decrypt_state[3][2]=decrypt_state[3][3];
		decrypt_state[3][3]=flag;
	}
	static int decrypt_time(int q){   return ((q<<1) ^ (((q>>7) & 1) * 0x1b));}
	static int GF(int q,int u){
		return ((u & 1) * q) ^ ((u>>1 & 1) * decrypt_time(q)) ^ ((u>>2 & 1) * decrypt_time(decrypt_time(q))) ^ ((u>>3 & 1) * decrypt_time(decrypt_time(decrypt_time(q)))) ^ ((u>>4 & 1) * decrypt_time(decrypt_time(decrypt_time(decrypt_time(q)))));
	}
	static void InvMixColumns()
	{
		int i;
		int f,g,h,z;
		for(i=0;i<4;i++)
		{	
		
			f = decrypt_state[0][i];
			g = decrypt_state[1][i];
			h = decrypt_state[2][i];
			z = decrypt_state[3][i];
		
			decrypt_state[0][i] = GF(f, 0x0e) ^ GF(g, 0x0b) ^ GF(h, 0x0d) ^ GF(z, 0x09);
			decrypt_state[1][i] = GF(f, 0x09) ^ GF(g, 0x0e) ^ GF(h, 0x0b) ^ GF(z, 0x0d);
			decrypt_state[2][i] = GF(f, 0x0d) ^ GF(g, 0x09) ^ GF(h, 0x0e) ^ GF(z, 0x0b);
			decrypt_state[3][i] = GF(f, 0x0b) ^ GF(g, 0x0d) ^ GF(h, 0x09) ^ GF(z, 0x0e);
		}
	}
	static void decrypt_InvCipher()
	{
		int i,j,rnd=0;
		for(i=0;i<4;i++)
		{
			for(j=0;j<4;j++)
			{
				decrypt_state[j][i] = decrypt_in[i*4 + j];
			}
		}
		decrypt_AddRoundKey(Lr); 
		for(rnd=Lr-1;rnd>0;rnd--)
		{
			decrypt_InvShiftRows();
			decrypt_InvSubBytes();
			decrypt_AddRoundKey(rnd);
			InvMixColumns();
		}
		decrypt_InvShiftRows();
		decrypt_InvSubBytes();
		decrypt_AddRoundKey(0);
		for(i=0;i<4;i++)
		{
			for(j=0;j<4;j++)
			{
				decrypt_out[i*4+j]=decrypt_state[j][i];
			}
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
	public static void decryption() throws IOException
	{
		int i;
		Lr=128;
		Lk = Lr / 32;
		Lr = Lk + 6;
		readBox(sbox,"sbox.txt");
		readBox(decrypt_invbox,"invsbox.txt");
		int flag[]=new int[16];
		readKey("key.txt",flag);
		file = new BufferedReader(new FileReader("receive.txt"));
		pw = new PrintWriter(new FileWriter("DecryptFile.txt"));
		int temp2[] =new int[16];
		String line="";
		while ((line=file.readLine())!=null){
	    StringTokenizer tok = new StringTokenizer(line);
		for (int j=0; j<16; j++) {
		String t = tok.nextToken();
	        temp2[j] = 16*hexVal(t.charAt(0))+hexVal(t.charAt(1));
		}
		for(i=0;i<Lk*4;i++)
		{
			Key[i]=flag[i];
			decrypt_in[i]=temp2[i];
		}
		decrypt_ExpandKey();
		decrypt_InvCipher();
		for(i=0;i<Lb*4;i++)
			pw.printf("%02x ",decrypt_out[i]);
	    pw.println();
		}
		pw.close();
	}
	public static int hexVal(char h){
	    if (h >= '0' && h <= '9')
	      return (int)(h-'0');
	    else
	      return (int)(h-'f'+10);
	}
}
