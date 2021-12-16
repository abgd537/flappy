package main

import java.awt.Dimension
import javax.swing.JFrame

class Screen(scrSize : Dimension, game : Game) : JFrame()
{
	init
	{
		size = scrSize
		
		defaultCloseOperation = EXIT_ON_CLOSE
		isResizable = false
		setLocationRelativeTo(null)
		
		add(game)
		pack()
		isVisible = true
		
		game.start()
	}
}