package com.minogames.entities;

import java.awt.Color;
//import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.minogames.main.Game;
import com.minogames.main.Sound;
import com.minogames.world.AStar;
import com.minogames.world.Camera;
import com.minogames.world.Vector2i;


public class Enemy extends Entity{
//private double speed = 1;          //velocidade do inimigo
private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;

private BufferedImage[] sprites;

private int life = 3;

private boolean isDamage = false;
private int damageFrames = 10, damageCurrent = 0;
public int mx,my;


	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(112 + 16, 16, 16, 16);
		
	}

	public void tick () {
		depth = 0;
		maskx = 5;
		masky = 3;
		mwidth =8;
		mheight = 8;
	if (!isColiddingWhitPlayer( )) {
		if(path == null || path.size() == 0) {
				Vector2i start = new Vector2i ((int) (x/16), (int) (y/16));
				Vector2i end = new Vector2i ((int) (Game.player.x/16), (int) (Game.player.y/16));
				path = AStar.findPath(Game.world, start, end);
				
		}
	}else {
		if(new Random().nextInt(100) < 5 ) {
			Game.player.life-=Game.random.nextInt(3);
			Game.player.isDamaged = true;
			if(Game.player.life <= 0) {					//ADICIONEI
				
			}
		}
	}
			if (new Random().nextInt(100)<60 )
			followPath(path);
			if (new Random().nextInt(100)<5) {
				Vector2i start = new Vector2i ((int) (x/16), (int) (y/16));
			Vector2i end = new Vector2i ((int) (Game.player.x/16), (int) (Game.player.y/16));
			path = AStar.findPath(Game.world, start, end);
			
			}
			
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;			
			    }
	        }   
			
			
			collidingBullet(); 	
			
			if(life <= 0) {
				destroySelf();
				return;
			}
			
				if(isDamage) {
					this.damageCurrent++;
					if(this.damageCurrent == this.damageFrames) {
						this.damageCurrent = 0;
						this.isDamage = false;					
					}
				}
					
				
	      }

		  	public void destroySelf() {
		  	Game.enemies.remove(this);
		  	Game.entities.remove(this);
		    }
			
			public void collidingBullet () {
				for(int i = 0; i < Game.bulletShoot.size(); i++) {
					Entity e = Game.bulletShoot.get(i);
					if(e instanceof BulletShoot) {
						if(Entity.isCollidding(this, e)){
							isDamage = true;
							life --;
							Sound.tiroEffect.play();      //SOM DO TIRO
							Game.bulletShoot.remove(i);
							
							return;
						}
					}
				}
			 }
			

	

	
	public void render(Graphics g) {
		if(!isDamage) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y,null);
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y,null);
			
			Graphics2D g2= (Graphics2D) g;
			double angleMouse = Math.atan2(200 +25 -my,200 +25 - mx);
			g2.rotate(angleMouse, 200+25,200+25);
			//System.out.println(Math.toDegrees(angleMouse));
			g.setColor(Color.red);																	//giro na tela
			g.fillRect(200, 200, 50, 50);
			
				
		/*super.render(g);
		g.setColor(Color.blue);
		g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,maskW, maskH);		*/	
	   }	
		
		
	}
}

