package net.avateambuilder.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import net.avateambuilder.model.Battle;
import net.avateambuilder.model.Player;
import net.dv8tion.jda.core.utils.IOUtil;

public class FileMng {
	
    static int posLvl = 2;
	static int posUserId = 3;
	static String folderName = "AvaEvents";

	static String Path(String avaName) {
		String path = DirectoryPath() + File.separator + avaName + ".txt";
		return path;
	}
	static String DirectoryPath() {
		String path = System.getProperty("user.dir") + File.separator + folderName;
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
				FileWriter fw = new FileWriter(file, false);
				PrintWriter pw = new PrintWriter(fw);
				
				pw.print(battle.ToJson());
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static List<Battle> GetLastState() {
		List<Battle> battles = new ArrayList<Battle>();
		File folder = new File(DirectoryPath());
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
	            try {
					FileInputStream is = new FileInputStream(folderName + File.separator + listOfFiles[i].getName());
		            DataInputStream in = new DataInputStream(is);
		            BufferedReader br = new BufferedReader(new InputStreamReader(in));
		            String jsonTxt = "";
		            String tmp = "";
		            while((tmp = br.readLine()) != null){
		            	jsonTxt = jsonTxt + tmp;
		            }
		            in.close();
		            JSONObject objectJson = new JSONObject(jsonTxt);     
		            battles.add(new Battle(objectJson));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return battles;
    }

}
