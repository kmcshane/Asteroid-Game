package game;

import java.util.concurrent.*;
import jline.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Game {

	final static int HEIGHT = 22;
	final static int WIDTH = 30;
	

	static ArrayList<SpaceObj> rockList;
	static Painter gamePainter;
	static SpaceObj playerShip;
	static ConsoleReader reader;
	static int score;

	static double timePerTick = 0.5;
	static int ticksTillAdd = 5;
	static int tickCounter = 5; // adds rocks immediately
	static int numRocksToAdd = 6;
	
	static boolean leftMove = false;
	static boolean rightMove = false;
	

	static boolean running;
	static double timeOne = (double) System.nanoTime() / 1000000000; // convert to seconds
	static double timeTwo;
	
	static char[] allowed = {'a','d'};

	public static void main(String args[]) throws IOException {

		// initial setup and then calls the mainLoop();
		reader = new ConsoleReader(System.in, new PrintWriter(System.out));
		rockList = new ArrayList<SpaceObj>();
		gamePainter = new Painter(HEIGHT, WIDTH);
		score = 0;
		running = true;

		playerShip = new Ship(WIDTH);
		gamePainter.paint(playerShip, rockList);

		mainLoop();

	}

	public static void updateDynamicLogic(double passedTime){
		// Dynamic logic based on timePassed if ever needed
		try {
			int i = 0;
			i = reader.readCharacter(allowed);
			processChar(i);
		} 	
		catch (IOException e) {
			e.printStackTrace();
		}
    }

	public static void updateFixedLogic(){
		// All Fixed time logic
		
		score += 100;
		
		if(rightMove){
			playerShip.xcor += 1;
			rightMove = false;
		}
		
		if(leftMove){
			playerShip.xcor -= 1.3;
			System.out.println("did");
			leftMove = false;
		}
		

		if (tickCounter == ticksTillAdd) { // adds rocks each ticksTillAdd
			for (int x = 0; x < numRocksToAdd; x++) {
				rockList.add(new Rock(HEIGHT, WIDTH));
			}
			tickCounter = 0;
		}

		//moves rocks down
		for (SpaceObj rock : rockList) {
			if (rock.ycor >= 1){
				rock.ycor -= 1;
			}
			else{
				rock = null;
			}
		}
		
		//collision code
		for (SpaceObj rock : rockList){
			if (rock.xcor == playerShip.xcor && rock.ycor == playerShip.ycor){
				running = false;
			}
		}

	}

	public static void mainLoop() {
		
		double timePassed = 0;
		double deltaTime = 0;

		while (running) {

			updateDynamicLogic(deltaTime);

			while (timePassed >= timePerTick) {
				updateFixedLogic();
				tickCounter += 1;
				timePassed -= timePerTick;
				gamePainter.paint(playerShip, rockList);
			}


			deltaTime = getTimePassedAndResetTimer();
			timePassed += deltaTime;

		}

		System.out.println("Game Over");
		System.out.println("Your Score: " + score);

	}

	public static double getTimePassedAndResetTimer() {
		timeTwo = (double) System.nanoTime() / 1000000000;
		double deltaTime = timeTwo - timeOne;
		timeOne = timeTwo;
		return deltaTime;
	}
	
	public static void processChar(int i){
		switch(i){
			case 'a':
				leftMove = true;
			case 'd':
				rightMove = true;

		}
    }

}
