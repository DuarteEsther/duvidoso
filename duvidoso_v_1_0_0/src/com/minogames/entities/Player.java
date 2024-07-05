package com.minogames.entities; 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.minogames.main.Game;
import com.minogames.main.Sound;
import com.minogames.world.Camera;
import com.minogames.world.World;

public class Player extends Entity{
     /*variaveis*/

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public int speed = 2;                     //velocidade do gamer
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage playerDamage;
	private int damageFrames = 0;
	public boolean shoot = false,mouseShoot = false;
	public boolean hasgun = false; 
	public int ammo = 0; 
	public boolean IsDamage = false;
	public  double life = 100,maxlife=100;
	public int mx,my;
	public boolean jump = false;
	public boolean isJumping = false;
	public int z = 0;
	public int jumpFrames = 50, jumpCur = 0; //ALTURA DE PULO 
	public boolean jumpUp = false,jumpDown = false;
	public int jumpSpd = 1; //jumpspeed
	public boolean isDamaged;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0,16,16,16);
		
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 0, 16, 16);
		}
		
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 16, 16, 16);
		}
    }
	
	public void revealMap() {
		int xx = (int) (x/16);
		int yy = (int) (y/16);
		
		
		World.tiles[xx-1+yy*World.WIDTH].show = true;
		World.tiles[xx+yy*World.WIDTH].show = true;
		World.tiles[xx+1+yy*World.WIDTH].show = true;
		
		
		World.tiles[xx+((yy+1)*World.WIDTH)].show = true;
		World.tiles[xx+((yy-1)*World.WIDTH)].show = true;
		
		World.tiles[xx-1+((yy-1)*World.WIDTH)].show = true;
		World.tiles[xx+1+((yy-1)*World.WIDTH)].show = true;
		
		World.tiles[xx-1+((yy+1)*World.WIDTH)].show = true;
		World.tiles[xx+1+((yy+1)*World.WIDTH)].show = true;
	}
	
	
	public void tick() {
		
		revealMap();
		
		depth = 1;
		if (jump) {
			if(isJumping == false) {
			jump = false;
			isJumping = true;
			jumpUp = true;
			
			}
		}
		
		if(isJumping == true) {
			
				if(jumpUp) {
					jumpCur+=2;
			    }else if (jumpDown) {
			    	jumpCur -=2;
			    	if(jumpCur <= 0) {
			    		isJumping = false;
			    		jumpDown = false;
			    		jumpUp = false;
			    	
			    	}
			    }
				z = jumpCur;
				if(jumpCur >= jumpFrames ) {
					jumpUp = false;
					jumpDown = true;
					Sound.jumpEffect.play();
					
				}
			}
		
		
		
		
		
		moved = false;
		
		if (right && World.isFree((int)(x+speed),this.getY(),z, width, height)) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int)(x-speed),this.getY(),z, width, height)) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}

		if (up && World.isFree(this.getX(),(int)(y-speed),z, width, height)) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(),(int)(y+speed),z, width, height)) {
			moved = true;
			y += speed;
		}
		
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				
				}
		    }
		}

		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
	
		if(IsDamage) {
			this.damageFrames++;
			if (this.damageFrames == 8) {
				this.damageFrames = 0;
				this.IsDamage = false;
				
			}
		}
		
		
		
		if(shoot) {
			//criar bala e atirar
			shoot = false;
			if(hasgun && ammo > 0) {
			   ammo --;
				
			int dx = 0; 
			int px = 0;
			int py = 6;
			
			if(dir == right_dir) {
				px= 18;
				dx= 1;
				
			} else {
				px = -8;
				dx = -1;
			}
		
			BulletShoot bulletShoot = new BulletShoot(this.getX()+px, this.getY()+py,3 , 3, null, dx, 0);
			Game.bulletShoot.add(bulletShoot);
	
		}
	}
			
		if(mouseShoot) {
				mouseShoot = false;
				if(hasgun && ammo > 0) {
				ammo --;                        
		
				int px = 0, py = 9;
				double angle =0;
				if(dir == right_dir) {
					px= 18;                      //altura da bala
					 angle = Math.atan2( my - (this.getY()+ py - Camera.y),  mx - (this.getX()+ px- Camera.x));
				} else {
					px= -8;                     //altura da bala
					angle = Math.atan2( my - (this.getY()+ py - Camera.y),  mx - (this.getX()+ px- Camera.x));
				}
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				BulletShoot bulletShoot = new BulletShoot(this.getX()+px, this.getY()+py,3 , 3, null, dx,dy);
				Game.bulletShoot.add(bulletShoot);
		       }
		  }	
	
		

		
		
		  if(life<=0) {
			 //gameover
			  life = 0;
			Game.gameState = "GAME_OVER";
			Sound.gameoverEffect.play();    ///SOM DE GAME OVER
		  }
		
		updateCamera();
	 }
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() -(Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() -(Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
		
	}
		
	 public void checkCollisionGun() {
			for(int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if(e instanceof Weapon) {
					if(Entity.isCollidding(this, e)) {
					    hasgun=true;   
						Sound.coletaEffect.play();
					     System.out.println("Pegou a arma" + ammo);
						Game.entities.remove(e);
					
			
					}
			     }
             }
			
      }
		

		public void checkCollisionAmmo() {
			for(int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if(e instanceof Bullet) {
					if(Entity.isCollidding(this, e)) {
					     ammo+=10;    //quantidade de muniçao
					     if (ammo >= 100) {
					    	 ammo = 100;
					     }
							Sound.coletaEffect.play();
					    // System.out.println("Munição atual" + Ammo);
						 Game.entities.remove(i);
						 return;
			
		                
	                 }
                 }
             }
		}
		
	//Coletar vida
		public void checkCollisionLifePack() {
			for(int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if(e instanceof Lifepack) {
					if(Entity.isCollidding(this, e)) {
						life+=8;
						if(life >= 100) {
							life = 100;
							Sound.coletaEffect.play();
						}
						Game.entities.remove(i);
						return;
					}
				}
			}
		}
	
		
		
	public void render(Graphics g) {
	if(!IsDamage) {
			if(dir == right_dir) {
			   g.drawImage(rightPlayer[index], this.getX()-	Camera.x,this.getY() - Camera.y - z ,null);
			   if (hasgun) {  
				   //desenhar arma para direita
				   g.drawImage(Entity.GUN_RIGHT, this.getX()+6 - Camera.x,this.getY()+5 - Camera.y -z ,null);  //altura e distancia 
			   
			   }
			   
			} else if(dir == left_dir) {
				 g.drawImage(leftPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y - z ,null);
			     if(hasgun) {   //desenhar arma pra esquerda
				   g.drawImage(Entity.GUN_LEFT, this.getX()-4 - Camera.x, this.getY()+5  - Camera.y - z ,null);  //altura e distancia
			  }
			
		}
	}else {
		g.drawImage(playerDamage, this.getX()-Camera.x, this.getY() - Camera.y- z,null);
		if(hasgun) {
			if(dir == left_dir) {
				g.drawImage(Entity.GUN_DAMAGE_LEFT, this.getX()-8 - Camera.x,this.getY() - Camera.y - z, null); 
			}else {
				g.drawImage(Entity.GUN_DAMAGE_RIGHT, this.getX()+1 - Camera.x,this.getY()+2 - Camera.y - z, null); //arminha renderizada dano
			}
			
		}
	}				
			if(isJumping) {
				g.setColor(Color.gray);        //tudo sobre a sombra
				g.fillOval(this.getX()-Camera.x + 8, this.getY()- Camera.y + 16, 8, 8);
				
			}
		}
	
  }

 





