package main

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.File
import kotlin.system.exitProcess

class KeyInput(private val game : Game) : KeyAdapter()
{
	override fun keyPressed(e : KeyEvent)
	{
		when(e.keyCode)
		{
			KeyEvent.VK_ESCAPE -> exitProcess(0)
			
			KeyEvent.VK_DELETE -> {
				File("src/data/high").writeText("0")
				game.handler.highScore = 0
			}
			
			else -> {
				if(!game.gameOver)
				{
					if(!game.started)
					{
						game.started = true
						game.handler.collided = false
					}
					
					game.bird.velY = -14
				}
				
				else
					game.restart()
			}
		}
	}
}