package main

import java.awt.Canvas
import java.awt.Color
import java.awt.Dimension
import java.awt.Font

class Game : Canvas(), Runnable
{
	private val scrWidth = 960
	private val scrHeight = 640
	private val floorHeight = scrHeight / 8
	
	private var thread : Thread? = null
	private var running = false
	
	val bird = Bird(scrWidth / 6, (scrHeight - floorHeight)/2)
	val handler = Handler(scrWidth / 2, scrHeight - floorHeight, bird)
	
	var started = false
	var gameOver = false
	
	fun restart()
	{
		handler.replace()
		handler.collided = false
		handler.score = 0
		gameOver = false
	}
	
	@Synchronized
	fun start()
	{
		if(running) return
		
		thread = Thread(this)
		thread!!.start()
		
		running = true
	}
	
	@Synchronized
	private fun stop()
	{
		if(!running) return
		
		running = false
		
		try
		{
			thread!!.join()
		}
		
		catch(e : Exception)
		{
			e.printStackTrace()
			exitProcess(0)
		}
	}
	
	override fun run()
	{
		this.requestFocus()
		
		var lastTime = System.nanoTime()
		val amountOfTicks = 60.0
		val ns = 1000000000 / amountOfTicks
		var delta = 0.0
		
		while(running)
		{
			val now = System.nanoTime()
			delta += (now - lastTime) / ns
			lastTime = now
			
			while(delta >= 1)
			{
				if(started)
					tick()
				
				delta--
			}
			
			render()
		}
		
		stop()
	}
	
	private fun tick()
	{
		if(!handler.collided && !gameOver)
		{
			handler.tick()
			bird.tick()
			handler.checkForCollision()
			handler.countScore()
		}
		
		else
		{
			gameOver = true
			started = false
		}
	}
	
	private fun render()
	{
		val bs = bufferStrategy
		
		if(bs == null)
		{
			this.createBufferStrategy(3)
			return
		}
		
		val g = bs.drawGraphics
		
		g.color = Color(90, 150, 220)
		g.fillRect(0, 0, scrWidth, scrHeight - floorHeight)
		
		g.color = Color(100, 60, 48)
		g.fillRect(0, scrHeight - floorHeight, scrWidth, floorHeight)
		
		g.color = Color.green.darker()
		g.fillRect(0, scrHeight - floorHeight, scrWidth, floorHeight/4)
		
		handler.pipes.forEach { it.render(g) }
		bird.render(g)
		
		g.color = Color.black
		g.font = Font(null, Font.PLAIN, 30)
		g.drawString("Score : " + handler.score, 10, 35)
		
		g.dispose()
		bs.show()
	}
	
	init
	{
		val scrSize = Dimension(scrWidth, scrHeight)
		
		minimumSize = scrSize
		preferredSize = scrSize
		maximumSize = scrSize
		
		addKeyListener(KeyInput(this))
		addMouseListener(MouseInput(this))
		addMouseMotionListener(MouseInput(this))
		
		Screen(scrSize, this)
	}
}
