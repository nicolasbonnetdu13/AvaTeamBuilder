package net.dofusteammaker.main;

import java.io.*;

public class FileMng {
	
	static int posUserId = 3;
	
	static void CreateFile(String fileName) {
		fileName = fileName + ".txt";
		
		try {
			PrintWriter outputStream = new PrintWriter(fileName);
			//outputStream.println("Banan");
			outputStream.close(); //flush
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void AddPlayer(String fileName, Joueur joueur) {
		
		fileName = fileName + ".txt";
		
		try {
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println(joueur.getPseudo() +" "+ joueur.getClasse() + " " + joueur.getLvl() + " " + joueur.getUserId());
			
			//PrintWriter outputStream = new PrintWriter(Name);
			//outputStream.append(joueur.getPseudo() +" "+ joueur.getClasse() + " " + joueur.getLvl() + " " + joueur.getUserId())
			//outputStream.close(); //flush
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static boolean IsRegistered(String fileName, String userId) {
		
		fileName = fileName + ".txt";
		boolean result = false;
		String line;
		String[] args;
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			System.out.println("NewUserId : " + userId);
			while (((line = br.readLine()) != null) && (result == false)) {
				args = line.split(" ");
				System.out.println("UserId : " + args[3]);
				
				if (args[3] == userId) {
					result = true;
				}
			}
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
