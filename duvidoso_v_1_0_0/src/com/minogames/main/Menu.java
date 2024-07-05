package com.minogames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.minogames.world.World;

public class Menu {
	public String[] options = {"novo jogo", "carregar jogo", "sair"};
	public int currentOption = 0;
	public int maxOption = options.length -1;
	public boolean  up, down, enter;
	//public Object font;
	public static boolean pause = false;
	public static boolean saveExists = false;
	public static boolean SaveGame = false;
	@SuppressWarnings("unused")
	private static String[] trans;
	@SuppressWarnings("unused")
	private static int[] spl2;
	@SuppressWarnings("unused")
	private static Object[] val;
	@SuppressWarnings("unused")
	private static Object encode;
	@SuppressWarnings("unused")
	private static int i;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("fontnew.ttf");
	public Font newfont;	

public Menu() {
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(70f);
			
			
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
   }
	
	
	public void tick() {
		
		File file = new File("save.txt");
		if(file.exists()) {
			
		}
		if(up) {
			Sound.menuEffect.play();
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			Sound.menuEffect.play();
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0 ;
			
		}
		if (enter) {
			Sound.menuEffect.play();
			enter = false;
			if (options[currentOption] =="novo jogo"  || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File("save.txt");
				file.delete();
				
			}else if (options[currentOption] == "carregar jogo") 	{
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}

				
						
				
			}else if (options [currentOption] == "sair") {
				System.exit(1);
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[]  spl2 = spl[i].split(":");
			switch(spl2[0])
			{
				case "level":
				World.restartGame("level"+spl2[1]+".png");
				Game.gameState = "NORMAL";
				pause= false;
				break;
				
				case "vida":
					Game.player.life = Integer.parseInt(spl2[1]);
					break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File ("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");  //trans de transition 
						char[] val = trans[1].toCharArray();
						trans[1] ="";
						for (int i = 0; i < val.length; i++) {
							val[i] -=encode;
							trans[1]+= val[i];
					}	
					line+= trans[0];
					line+= ":";
					line+= trans[1];
					line+= "/";
					
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void SaveGame(String[] val1, int[] val2, int encode ) {      //SALVAR
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];  
			current+= ":";
			char[] value = Integer.toString(val2[i]).toCharArray();           //criptografia 
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i< val1.length -1)
					write.newLine();
			}catch(IOException e) {}
			
		} 
		 try {
			 write.flush();
			 write.close();
			
		}catch (IOException e) {}
		
	}
	
	
	
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width * Game.SCALE, Toolkit.getDefaultToolkit().getScreenSize().height * Game.SCALE);
		g.setColor(Color.red);
		//g.setFont(new Font("arial", Font.createFont(font., null),36));
	
		g.setFont(newfont);
		g.drawString("Duvidoso",(Game.WIDTH*Game.SCALE) / 2 - 90, Game.HEIGHT*(Game.SCALE) / 2 - 160); //nome do jogo
		
		//opçoes de menu
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,24));
		if(pause== false)
			g.drawString("Novo jogo", (Game.WIDTH*Game.SCALE) / 2 - 50, 160); 
		else 
			g.drawString("Resumir", (Game.WIDTH*Game.SCALE) / 2 - 50, 160); 
		g.drawString("carregar jogo", (Game.WIDTH*Game.SCALE) / 2 - 70, 190); 
		g.drawString("sair", (Game.WIDTH*Game.SCALE) / 2 - 10, 220); 
		
		if(options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 90, 160); 
		}else if(options[currentOption] == "carregar jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 90, 190); 
		}else if(options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 40, 220); 
		}
	}
}
