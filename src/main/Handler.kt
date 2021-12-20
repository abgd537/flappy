package main

import java.awt.Color
import java.awt.Graphics
import kotlin.properties.Delegates
import kotlin.random.Random

class Handler(private val startPos : Int, private val availableSpace : Int, private val bird : Bird)
{
	private val pipeGap = 200
	private val deltaX = 500
	
	private val pipeWidth = 100
	private val headWidth = pipeWidth * 6 / 5
	private val headHeight = 30
	
	private var flag = true
	private var pivot by Delegates.observable(0) {
		_, _, _ -> flag = true
	}
	
	val pipes = arrayOf(Pipe(startPos, availableSpace/4),
						Pipe(startPos + deltaX),
						Pipe(startPos + 2*deltaX))
	
	var collided = false
	var score = 0
	
	fun replace()
	{
		for(i in pipes.indices)
		{
			val temp = pipes[(pivot + i) % pipes.size]
			
			temp.x = startPos + deltaX * i
			temp.gapY = when(i) {
				0 -> availableSpace / 4
				else -> Random.nextInt(availableSpace / 8, availableSpace * 7 / 8 - pipeGap)
			}
		}
		
		bird.y = availableSpace/2
	}
	
	fun countScore()
	{
		if(flag && bird.left > pipes[pivot].getHeadRight())
		{
			score++
			flag = false
		}
	}
	
	fun checkForCollision()
	{
		//바닥
		if(bird.getBottom() > availableSpace)
		{
			collided = true
			bird.setBottom(availableSpace)
		}
		
		else
			pipes[pivot].checkForCollision(bird)
		
		if(collided)
			bird.velY = 0
	}
	
	fun tick()
	{
		pipes.forEach { it.x -= 6 }
		
		if(pipes[pivot].getHeadRight() < 0)
		{
			val temp = pipes[pivot]
			
			temp.x += deltaX * pipes.size
			temp.gapY = Random.nextInt(availableSpace/8, availableSpace*7/8 - pipeGap)
			pivot++
			
			if(pivot >= pipes.size)
				pivot -= pipes.size
		}
	}
	
	inner class Pipe(var x : Int, var gapY : Int = Random.nextInt(availableSpace/8, availableSpace*7/8 - pipeGap))
	{
		private fun getUpperHeadTop() = gapY - headHeight
		private fun getLowerHeadTop() = gapY + pipeGap
		private fun getLowerHeadBottom() = getLowerHeadTop() + headHeight
		private fun getHeadLeft() = x + pipeWidth / 2 - headWidth / 2
		
		fun getHeadRight() = getHeadLeft() + headWidth
		
		fun checkForCollision(bird : Bird)
		{
			//파이프 벽면
			if(bird.right - x in (0..pipeWidth)
			   && (bird.getBottom() < getUpperHeadTop() || bird.getTop() > getLowerHeadBottom()))
				collided = true
			
			//파이프 머리 옆
			else if(bird.right in (getHeadLeft()..getHeadRight())
					&& (bird.y in (getLowerHeadTop()..getLowerHeadBottom()) || bird.y in (getUpperHeadTop()..gapY)))
				collided = true
			
			//파이프 단면
			else if(bird.x in (getHeadLeft()..getHeadRight())
					&& (bird.getTop() < gapY || bird.getBottom() > getLowerHeadTop()))
				collided = true
			
			//각 꼭짓점
			else if(bird.includes(getHeadLeft(), getUpperHeadTop())
					|| bird.includes(getHeadLeft(), gapY)
					|| bird.includes(getHeadRight(), gapY)
					|| bird.includes(getHeadLeft(), getLowerHeadTop())
					|| bird.includes(getHeadLeft(), getLowerHeadBottom())
					|| bird.includes(getHeadRight(), getLowerHeadTop()))
				collided = true
		}
		
		fun render(g : Graphics)
		{
			g.color = Color.green.darker()
			g.fillRect(x, 0, pipeWidth, getUpperHeadTop())
			g.fillRect(getHeadLeft(), getUpperHeadTop(), headWidth, headHeight)
			g.fillRect(getHeadLeft(), getLowerHeadTop(), headWidth, headHeight)
			g.fillRect(x, getLowerHeadBottom(), pipeWidth, availableSpace - getLowerHeadBottom())
			
			g.color = Color(80, 225, 80)
			g.fillRect(x + 3, 0, pipeWidth - 6, getUpperHeadTop())
			g.fillRect(getHeadLeft() + 3, getUpperHeadTop() + 3, headWidth - 6, headHeight - 6)
			g.fillRect(getHeadLeft() + 3, getLowerHeadTop() + 3, headWidth - 6, headHeight - 6)
			g.fillRect(x + 3, getLowerHeadBottom(), pipeWidth - 6, availableSpace - getLowerHeadBottom())
		}
	}
}