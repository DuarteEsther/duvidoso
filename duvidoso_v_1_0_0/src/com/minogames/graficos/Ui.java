package com.minogames.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

//import com.minogames.entities.Player;
import com.minogames.main.Game;

public class Ui {

	public void render (Graphics g) {                 //tudo da barra de vida
		g.setColor(Color.red);
		g.fillRect(5, 8,50,8);
		g.setColor(Color.green);
		g.fillRect(5, 8,(int)((Game.player.life/Game.player.maxlife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font ("arial",Font.BOLD,8));
		g.drawString((int) Game.player.life+"/" + (int)Game.player.maxlife, 30, 15);
	}
}
