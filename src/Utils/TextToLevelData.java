package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector4f;

import Graphics.SpriteBatch;
import Graphics.Models.Renderable2D;

public class TextToLevelData {
	
	public static ArrayList<ArrayList<Character>> load(String levelloc) {
		ArrayList<ArrayList<Character>> data = new ArrayList<>();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(new File(levelloc)));
			
			String line;
			while((line = in.readLine()) != null) {
				ArrayList<Character> rows = new ArrayList<>();
				char[] text = line.toCharArray();
				for(int i = 0; i < text.length; i++) {
					rows.add(text[i]);
				}
				data.add(rows);
			}
		}catch(IOException e) {
			System.out.println("Could not load level");
			System.exit(-1);
		}
		return data;
	}
}
