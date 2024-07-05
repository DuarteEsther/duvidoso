package com.minogames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.minogames.entities.Bullet;
import com.minogames.entities.Enemy;
import com.minogames.entities.Entity;
import com.minogames.entities.Flor;
import com.minogames.entities.Lifepack;
import com.minogames.entities.Particle;
import com.minogames.entities.Player;
import com.minogames.entities.Weapon;
import com.minogames.graficos.Spritesheet;
import com.minogames.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
//	private static int zplayer;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			//Teste de posicao cor
			
			/*for(int i = 0; i < pixels.length; i++) {
				if(pixels[i] == 0xff7F3300) {									//TESTE DE COR
					System.out.println("Estou no Vermelho!");
				}
			}*/
			
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {

					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

					if (pixelAtual == 0xFF000000) {
						// Floor chao
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					}
					
					
					else if (pixelAtual == 0xFF606060) {
						// Wall muro
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					
					
					} else if (pixelAtual == 0xFF320096) {
						// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					}

					// Objs Game
					else if (pixelAtual == 0xFFF70000) {
						// Enemy
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
						
						
					} else if (pixelAtual == 0xFF0094FF) {
						// Weapon
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
						
						
					} else if (pixelAtual == 0xFFFF00DC) {
						// Life Pack
						Game.entities.add(new Lifepack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN));

						
						
					} else if (pixelAtual == 0xFF267F00) {
						// Bullet
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					
						
						
					} else if (pixelAtual == 0xFFFF00DC) {
						Flor flor = new Flor(xx * 16, yy * 16, 16, 16, Entity.Flor);
						Game.entities.add(flor);
					}
					}
				}
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateParticles(int amount,int x, int y) {
		for(int i =0; i< amount; i++) {
			Game.entities.add(new  Particle(x, y, 1 , 1, null));
		}
		
	}
	
	public static boolean isFree(int xnext, int ynext, int zplayer, int width, int height) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+width-1) / TILE_SIZE;
		int y2 = ynext /TILE_SIZE;
		
		
		int x3 = xnext /TILE_SIZE;
		int y3 = (ynext+height-1) / TILE_SIZE;
	
		int x4 = (xnext+width-1) / TILE_SIZE;
		int y4 = (ynext+height-1) / TILE_SIZE;

		if(!((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile))) {
			return true;
		}
		if(zplayer>0) {  //se o player e maior que zero ele ta pulando
			return true;
		}
		
		return false;
	}
	
	

	
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.bulletShoot.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32,  0,  16,  16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return; 
	}
	
public void render(Graphics g) {
		
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);  
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}

			public static void renderMiniMap() {
				for (int i = 0; i < Game.minimapaPixels.length; i++) {
					Game.minimapaPixels[i] = 0;
		
				}
				for (int xx = 0; xx < WIDTH; xx++) {
					for (int  yy =0; yy < HEIGHT; yy++) {
						if(tiles[xx + (yy * WIDTH)] instanceof WallTile)   {
							Game.minimapaPixels [xx + (yy * WIDTH)] = 0xff0000;
							
						}			
						
					}
					
				}
					
				int xPlayer = Game.player.getX()/16; 
				int yPlayer = Game.player.getY()/16;							//	Game.minimapaPixels[ xPlayer + (yPlayer*WIDTH)] = 0x000ff;
				
						
				Game.minimapaPixels[xPlayer + (yPlayer*WIDTH)] = 0x0000ff;
				
			}
	
}





