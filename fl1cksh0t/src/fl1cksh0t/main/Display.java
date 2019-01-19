//fl1cksh0t will be a first person arena shooter type game made by Evin Kierans
//testing from linux 1234 :D

package fl1cksh0t.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import fl1cksh0t.main.graphics.Screen;

public class Display extends Canvas implements Runnable
{
	private static final long serialVersionUID = -3316329364487365225L;

	public static final int WIDTH = 800, HEIGHT = 600;
	public static final String TITLE = "fl1cksh0t - Pre-Alpha v0.03";
	
	public int time;
	
	private Thread thread;
	private Screen screen;
	private Game game;
	private boolean running = false;
	private BufferedImage img;
	private int[] pixels;
	
	public Display()
	{
		new Window(WIDTH, HEIGHT, TITLE, this);	//creating game window in game class
		screen = new Screen(WIDTH, HEIGHT);
		game = new Game();
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	}
	
	public synchronized void start()
	{
		if (running) 
		{
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
		
		System.out.println("Operation Flickshot is a go");
	}
	
	private void stop()
	{
		System.out.println("Game finito");
		if (!running)
		{
			return;
		}
		running = false;
		try
		{
			thread.join();	//waits for thread to DIE
		}catch (Exception e)	//if it wont die, make it with programming bullshit!
		 {
			e.printStackTrace();	//will tell us why it wont quit in console
			System.exit(0);
		 }
		
	}
	
	public void run()
	{
		long lastTime = System.nanoTime();
		try
		{
		    Thread.sleep(100);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		double amountOfTicks = 100.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(running == true)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while(delta >= 1)
			{
				tick();
				delta--;
			}
			if(running == true)
			{
				render();
			}
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.println("FPS = " + frames);
				frames = 0;	//comment this out if you want 'total frames rendered'
			}
		}
		stop();
	}
	

	
	private void tick()	//ticks in display
	{
		game.tick();
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)	//only want bs to run once, as it's in the render function, which is called repeatedly
		{
			this.createBufferStrategy(3);	//might have to take this. out at some stage, not sure though
			return;
		}
		
		screen.render(game);
		
		for (int i = 0; i<WIDTH * HEIGHT; i++)
		{
			pixels[i] = screen.pixels[i];
		} 
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String args[])	//main function
	{
		new Display();
	}
	
}
