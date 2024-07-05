package com.minogames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.minogames.entities.BulletShoot;
import com.minogames.entities.Enemy;
import com.minogames.entities.Entity;
import com.minogames.entities.Npc;
import com.minogames.entities.Player;
import com.minogames.graficos.Spritesheet;
import com.minogames.graficos.Ui;
import com.minogames.world.World;

public class Game extends Canvas implements Runnable, KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public final static int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bulletShoot;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random random;


	public Ui ui;
	
	public static String gameState = "MENU";
	public static Object bullets; 								//////////////////
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	
	//sistema curt 
	public static int entrada =1;
	public static int comecar =2;
	public static int jogando = 3;
	public static int estado_cena = entrada;
	
	public int timeCena =0, maxTimeCena = 60*3;   // 60 vezes 3 é tres segundos
	
//	 npc = new Npc(32, 32, 16, 16, spritesheet.getSprite(32, 32, 16, 16));
	
	public Menu menu;
	public boolean saveGame = false;
	//public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
//	public Font newFont;
	//private Font newfont;
	//public Font newfont;
	public int mx,my;
	//private Object newfont;
//	private Component g;
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("gameova.ttf");
	public Font newfont;
	public int[] pixels;  
	//public int xx, yy;
	public BufferedImage lightmap;
	public int [] lightMapPixels;
	public static int[] minimapaPixels;
	public static BufferedImage minimapa;
	
	public BufferedImage sprite1;
	public BufferedImage sprite2;
	public int[] pixels1;
	public int[] pixels2;
	public Npc npc;
	
	
	public Game() {
		
		Sound.musicBackground.loop();
		random = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
	//	this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();		
		ui = new Ui ();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//luz
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e) {
		e.printStackTrace();
		}
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bulletShoot = new ArrayList<BulletShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		minimapa = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapaPixels = ((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
	
		 npc = new Npc (31, 31, 16, 16, spritesheet.getSprite(32, 32, 16, 16));
		 entities.add(npc);
	//	try {
	//		newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(70f);   //f
	///	} catch (FontFormatException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	} catch (IOException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels,0, lightmap.getWidth());
		menu = new Menu();
	}
	

	public void initFrame() {
		
		frame = new JFrame("Game"); 
		frame.add(this);
		frame.setUndecorated(true);		//tirar minimizaçao
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		 
		// icone da janela
		
		Image Imagem = null;
	 	try {
			Imagem = ImageIO.read(getClass().getResource("/icon.png"));
		}catch (IOException e) {
			e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(getClass().getResource("/icon.png"));
		Cursor c = toolkit.createCustomCursor(image, new Point (0, 0), "img");
		frame.setCursor(c);
		frame.setIconImage(Imagem);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	

	
	public synchronized void start() {
		
		thread = new Thread(this);
		isRunning = true;
		thread.start();
		
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	@SuppressWarnings("static-access")
	public void tick() {
		if(gameState == "NORMAL") {
				this.saveGame = false;
			
			if(Game.estado_cena == Game.jogando) {
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
		
			
			for(int i = 0; i < bulletShoot.size(); i++) {
				bulletShoot.get(i).tick();
				
			}
			}else {
				if(Game.estado_cena == Game.entrada) {
					if(player.getX() < 100) {
						player.x++;
					
					}else {
						System.out.println("Game entrada concluido");
						Game.estado_cena = Game.comecar;
					}
				}else if (Game.estado_cena == Game.comecar) {
					timeCena++;
					if(timeCena == maxTimeCena) {
						Game.estado_cena = Game.jogando;
						
					}
					
				}
			}
		
		
		if(enemies.size() == 0) {
			//avançar para o proximo nivel
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL){
				CUR_LEVEL =1;
			}
			String newWorld = "level"+CUR_LEVEL+".png";
			World.restartGame(newWorld);
		}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {  //velocidade do pisco na tela
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}			
	
			if(restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL =1;
				String newWorld = "level"+CUR_LEVEL+".png";
			    World.restartGame(newWorld);
			} 
		}else if(gameState == "MENU") {
			//
			player.updateCamera();
			this.menu.tick();
			
		}
	
	}
	
	
	
//	public void drawRectangleExample(int xOff, int yOff) {
//		for(int xx = 0; xx < 32; xx++) {
	//		for(int yy = 0; yy < 32; yy++) {
	//			int xOff = xx + xoff;				//posiçao
	//			int yOff = yy + yoff;				//posico
//				if (xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff>= HEIGHT)			//VERIFICACAO
	//				continue;
	//			pixels[xOff+(yOff*WIDTH)] = 0xff0000;   //posicao pra baixo
	//			
	//			
//			}
//		}
		
	
	public void applyLight() {
		for(int xx =0; xx < Game.WIDTH; xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				if(lightMapPixels[xx+(yy*Game.WIDTH)]== 0xffffffff) {						//0xffffffff
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0xFF636363, 0);		// 282828, 0
					pixels[xx+(yy*Game.WIDTH)] = pixel;
				}
			}
			
		}
	}
	
	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
		    return;
		}  
		
		Graphics g = image.getGraphics();
		g.setColor(Color.gray);                                                         //cpr fundo
		g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		world.render(g);
		Collections.sort(entities, Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bulletShoot.size(); i++) {
			bulletShoot.get(i).render(g);
			
		}
		applyLight();											
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();						//APARTIR DAQUI É SOBRE O MENU DO JOGO
		//drawRectangleExample(xx,yy);					//rectangle
		g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height,null);	
		g.setFont(new Font("fira mono", Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo, 580, 20);
		g.setColor(Color.red);
		g.drawString("duvidoso", 15, 15);
		World.renderMiniMap();
		g.drawImage(minimapa,900,90,World.WIDTH*5, World.HEIGHT*5 ,null);
		
		if(gameState == "GAME_OVER") {
			Graphics2D g2= (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100 ));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE );
			//g.setFont(new Font("arial", Font.ITALIC,36));
			g.setFont(newfont);
			g.setColor(Color.red);
			g.drawString("Game Over",(WIDTH*SCALE) /2 -90 , (HEIGHT*SCALE) /2 -20);
			
			g.setFont(new Font("arial", Font.ITALIC,32));
			g.setColor(Color.white);
			if(showMessageGameOver) 
				g.drawString("> Pressione Enter para reiniciar <" , ( WIDTH*SCALE)/2 -230 , (HEIGHT*SCALE) /2 +40);
		}else if(gameState == "MENU") {
			menu.render(g);
			
		}	
		
		if(Game.estado_cena == Game.comecar) {
			g.drawString("o jogo vai começar",( WIDTH*SCALE)/2 -230 , (HEIGHT*SCALE) /2 +40);
		}
	
	
	
		
		
												
		bs.show();
	}
		
	//private void initFrame() {
		// TODO Auto-generated method stub
		
	//}
	


	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning == true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
		
	}


	public void keyTyped(KeyEvent e) {
	
		}
	

	
	@SuppressWarnings("static-access")
	public void keyPressed(KeyEvent e) {
		
		//ENTER DO DIALOGO
		Sound.dialogEffect.play();
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			npc.ShowMessage = false;
			System.out.println("fechado");
		}
		
		if(e.getKeyCode() == KeyEvent.VK_Z) { //PULAR
			player.jump = true;
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
				
				player.right = true;
					
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
					e.getKeyCode() == KeyEvent.VK_A) {
					
				player.left = true;
		}
			
		if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W) {
					
				player.up = true;
				if(gameState == "MENU") {
					this.menu.up = true;
				}
				
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
						e.getKeyCode() == KeyEvent.VK_S) {
					
				player.down = true;
				if (gameState == "MENU") {
					this.menu.down = true;
				}
		}	  
		if(e.getKeyCode() ==  KeyEvent.VK_X) {
			player.shoot = true;
			
		}
			
			
		if(e.getKeyCode() == KeyEvent.VK_P) {
				gameState = "MENU";
				menu.pause = true;
				//System.out.println("PAUSE");
			}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(Game.gameState == "NORMAL")	
			   this.saveGame = true;
				
		}
		
	}
   


	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
				
				player.right = false;
					
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
					e.getKeyCode() == KeyEvent.VK_A) {
					
				player.left = false;
		}
			
		if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W) {
					
				player.up = false;
				
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
						e.getKeyCode() == KeyEvent.VK_S) {
					
				player.down = false;
		}

		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if (gameState == "MENU") {
				this.menu.enter = true;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		player.mouseShoot = true;
		player.mx = (e.getX() /3);
		player.my = (e.getY() /3);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
		
	}
}
