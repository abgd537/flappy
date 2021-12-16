package main

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class MouseInput(private val game : Game) : MouseAdapter()
{
	override fun mousePressed(e : MouseEvent?)
	{
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
	
	override fun mouseMoved(e : MouseEvent) =
		game.bird.lookAt(e.x, e.y)
}