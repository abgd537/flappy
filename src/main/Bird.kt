package main

import java.awt.Color
import java.awt.Graphics
import kotlin.math.*

class Bird(val x : Int, var y : Int)
{
	private val r = 30
	private val eyeSize = 16
	
	private var eyeCos = 1.0
	private var eyeSin = 0.0
	private var dist = 0
	
	val right = x + r
	val left = x - r
	
	var velY = 0
	
	fun getTop() = y - r
	fun getBottom() = y + r
	
	fun setBottom(pos : Int)
	{
		y = pos - r
	}
	
	fun includes(otherX : Int, otherY : Int) =
		sqrt((x - otherX).pow(2) + (y - otherY).pow(2)) < r
	
	fun lookAt(posX : Int, posY : Int)
	{
		val deltaX = posX - (x + 4)
		val deltaY = posY - (y - r/2 + 4)
		
		val d = sqrt(deltaX.pow(2) + deltaY.pow(2))
		
		dist = when
		{
			d > 150 -> 3
			d in 10.0..150.0 -> 2
			
			else -> 1
		}
		
		eyeCos = deltaX/d
		eyeSin = deltaY/d
	}
	
	private fun Int.pow(n : Int) = toDouble().pow(n)
	
	fun tick()
	{
		y += velY
		velY += 1
	}
	
	fun render(g : Graphics)
	{
		g.color = Color.orange.darker().darker()
		g.fillOval(x - r, y - r, 2 * r, 2 * r)
		
		g.color = Color.orange
		g.fillOval(x - r + 2, y - r + 2, 2 * r - 4, 2 * r - 4)
		
		g.color = Color.white
		g.fillOval(x, y - r / 2, eyeSize, eyeSize)
		
		g.color = Color.black
		g.fillOval(x + 4 + round(dist*eyeCos).toInt(), y - r / 2 + 4 + round(dist*eyeSin).toInt(),
				   eyeSize / 2, eyeSize / 2)
	}
}