//Joshua Itagaki
//CS 380

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Ex3Client {
	public static void main(String[] args) throws Exception {
		try (Socket socket = new Socket("codebank.xyz", 38103)){
			System.out.println("Connected to server.");
			InputStream is = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			byte size = (byte) is.read();
			byte[] array = new byte[size];
			System.out.println("Reading " + size + " bytes");
			for(int i = 0; i < size; i++){
				array[i] = (byte) is.read();
			}
			System.out.println("Data received: ");
			for(int i = 0; i < array.length; i++){
				System.out.print(Integer.toHexString(array[i]).toUpperCase());
				if(i%20 == 0){
					System.out.println();
				}
			}
			System.out.println();
			short value = checksum(array,size);
			System.out.println("Checksum calculated: " + value);
			out.write(value>>8);
			out.write(value);
			int received = is.read();
			if(received == 1){
				System.out.println("Response is good.");
			}
			else {
				System.out.println("Response error.");
			}
		}
	}
	public static short checksum(byte[] array, int count){
		short sumU = 0;
		int index = 0;
		while(count-- != 0){
			sumU += array[index++];
			if((sumU & 0xFFFF0000) != 0){
				sumU &= 0xFFFF;
				sumU++;
			}
		}
		return (short) (sumU & 0xFFFF);
	}
}
