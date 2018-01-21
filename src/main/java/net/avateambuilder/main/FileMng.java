package net.avateambuilder.main;

import java.io.*;
import java.util.List;

import net.avateambuilder.model.Battle;
import net.avateambuilder.model.Player;

public class FileMng {
	
    static int posLvl = 2;
	static int posUserId = 3;
	static String folderName = "AvaEvents";
	
	static String Path(String avaName) {
		String path = System.getProperty("user.dir") + File.separator + folderName + File.separator + avaName + ".txt";
		return path;
	}
	
	static void CreateFileIfNeeded(File file) {
		if (file.exists()) {
			return;
		}
		try {
			file.getParentFile().mkdirs(); 
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void SaveCurrentState(List<Battle> battles) {
		for (Battle battle : battles) {

			String path = Path(battle.getName());
			File file = new File(path);
			CreateFileIfNeeded(file);
			try {
				FileWriter fw = new FileWriter(file, true);
				PrintWriter pw = new PrintWriter(fw);
				
				pw.print(battle.ToJson());
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static void AddPlayer(String fileName, Player joueur) {

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

    
    static void AddToTeam(String fileName, Player joueur) {
        
        String path = Path(fileName);
        File file = new File(path);
        FileReader fr;
        int linecounter = 1;
        int linetoread = 1;
        int lvl;
        String line;
        String[] args;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            
            while ((line = br.readLine()) != "EOF") {
                // A REVOIR
            if (linecounter == linetoread) {
                if (line == null) {
                    // TODO AJOUTER LE JOUEUR LINE A CETTE LIGNE                    
                }
                else {
                    args = line.split(" ");
                    lvl = Integer.parseInt(args[posLvl]);
                    if ((((joueur.getLvl() >= 190) && (lvl < 190)) || ((joueur.getLvl() < 190) && (lvl >= 190))) 
                        || (joueur.getClasse().name().equals(args[1]))) {
                        linetoread = (linetoread/5+1)*5+1;
                    }
                    else {
                        linetoread++;
                    }
                }
            }
            linecounter++;
        }        
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
