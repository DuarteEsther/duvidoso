package com.minogames.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.minogames.main.Game;

public class Npc extends Entity {

	public String[] frases = new  String[2];
	
	public boolean ShowMessage = false;
	public boolean  Show = false;
	
	public int curIndexMsg = 0;
	
	public int fraseIndex = 0;
	
	public int time = 0;
	
	public int maxTime =8;
	
	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Aoba";
		frases[1] = "Destrua os inimigos";
	}

	
	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		if(Math.abs(xPlayer-xNpc) < 20 &&
				Math.abs(yPlayer - yNpc) < 20) {
			
			if(Show == false) {
			ShowMessage = true;
			Show = true;
			}
			
		}else {
			//ShowMessage = false;
		}
		
	this.time++;
		
		if(ShowMessage) {
			
			if(this.time >= this.maxTime) {
				this.time= 0;
			if(curIndexMsg < frases[fraseIndex].length() -1) {				
					curIndexMsg++;
			}else {	
				if(fraseIndex < frases.length -1) {
				fraseIndex++;
				curIndexMsg = 0;
				}
				
			}
		}
	}
}
	
	
	
	public void render(Graphics g) {
		super.render(g);
		if(ShowMessage) {
			
			//BORDA
			g.setColor(Color.white);
			g.fillRect(9, 9, Game.WIDTH -49, Game.HEIGHT-49);
			
			
			//CAIXINHA 
			g.setColor(Color.blue);
			g.fillRect(10, 10, Game.WIDTH -50, Game.HEIGHT-50);
	
			//LETRA
			g.setFont(new Font("Arial", Font.BOLD,8));
			g.setColor(Color.white);
			g.drawString(frases[fraseIndex].substring(0 , curIndexMsg), (int)x , (int)y);
			
			//FECHAR CAIXINHA
			g.drawString("> Precisone Enter para fechar", (int)x+10 , (int)y+11);
			
		}
		
	}
}
