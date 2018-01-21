package net.dofusteammaker.main;

import java.io.*;

public class FileMng {
	
	static int posUserId = 3;
	static String folderName = "AvaEvents";
	
	static String Path(String avaName) {
		String path = System.getProperty("user.dir") + File.separator + folderName + File.separator + avaName + ".txt";
		return path;
	}
	
	static void CreateFile(String fileName) {
		String path = Path(fileName);
		File f = new File(path);
		
		try {
			f.getParentFile().mkdirs(); 
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void AddPlayer(String fileName, Joueur joueur) {

		String path = Path(fileName);
		File file = new File(path);
		
		try {
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println(joueur.getPseudo() +" "+ joueur.getClasse() + " " + joueur.getLvl() + " " + joueur.getUserId());
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

		String path = Path(fileName);
		File file = new File(path);
		boolean result = false;
		String line;
		String[] args;
		try {
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
