# Minesweeper
A CLI version of the classic Minesweeper game implemented in Java.


## Compiling The Game

To compile this project:<br>
- Install **[Java Development Kit] (https://www.oracle.com/java/technologies/downloads/#java11-windows)** for your OS.
- Add the **JDK** to your system's PATH variable.
	- On Windows: 
		- Add the Java Runtime Environment to your system's PATH variable:
		- `> setx PATH "%PATH%;C:\Program/ Files\Java\jdk-22\bin" /m`
	- On OSX:
		- The Java Runtime Environment files are usually added to the `/usr/bin` directory which should already be added to the system's $PATH variable.
		- To verify, run `echo $PATH`
		- If it's missing, append the `/usr/bin` path to the `/etc/paths` file with a text editor.
- Change directories into the `src` folder:
	- `> cd src`
- Compile the main file:
	- `> javac MineSweeperGame.java`
	- If a series of `.class` files is created without error, the game compiled successfully.

## Running The Game

To run this game in a terminal:<br>
- Change directories into the `src` folder:
	- `> cd src`
- Run the game with the following command:
	- `> java MineSweeperGame`

```
---------------------------------
* * * M I N E S W E E P E R * * *
---------------------------------

new 8 10    - Starts a new game of size 8x8 with 10 bombs. Max size is 20x20
reveal 3 5  - Reveals the tile at column 3, row 5.
mark 5 6    - Marks a mine at column 5, row 6.
exit        - Exits the game.


   0  1  2  3  4  5  6  7
 0[■][■][■][■][■][■][■][■]
 1[■][■][■][■][■][■][■][■]
 2[■][■][■][■][■][■][■][■]
 3[■][■][■][■][■][■][■][■]
 4[■][■][■][■][■][■][■][■]
 5[■][■][■][■][■][■][■][■]
 6[■][■][■][■][■][■][■][■]
 7[■][■][■][■][■][■][■][■]

---------------------------------

---------------------------------
Enter a command:

```

**Enjoy!**