package com.minogames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.minogames.main.Game;
import com.minogames.main.Sound;
import com.minogames.world.Camera;
import com.minogames.world.World;

public class BulletShoot extends Entity {

	private double dx;
	private double dy;
	private double spd =4 ;
	
	private int life =30, curLife = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
	
		this.dx = dx;
		this.dy = dy;
	
	}
	
	
	public void tick() {
		if(World.isFree((int)(x+(dx*spd)),(int)( y+(dy*spd)),z, 3 , 3)) {
			x+=dx*spd;
			y+=dy*spd;
		}else {
			Game.bulletShoot.remove(this);
			World.generateParticles(100,(int) x,(int) y);
			System.out.println("Removido");
			return;
		}
		
		
		curLife++;
		if(curLife == life) {
			Game.bulletShoot.remove(this);
			Sound.balaEffect.play();
			return;
			
		}
		
	}
			
	
	
	////////////////////////////g.drawOval(this.getX() - Camera.x,this.getY() - Camera.y, width, height);
	
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.drawOval(this.getX() - Camera.x,this.getY() - Camera.y, width, height);
		
		
	}
	
}

