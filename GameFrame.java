import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.*;
public class GameFrame extends ClosingFrame implements Runnable, KeyListener{
    private BufferedImage wall, floor, endPoint, coin, question, cherry, gem; //images for sprites
    private BufferedImage mainMenu, helpMenu, questionScreen, correctAnswer, wrongAnswer, endScreen; //images for menus and screens
    private BufferedImage offScreen; // drawn before shown to prevent screen tearing
    private Tile loadedTiles[][]; //to store tiles
    private boolean quit; //see if the user wants to quit or not
    private int levelNo = 1, score, gameState = 1;
    private Thread animation; //thread for animation
    private Player player;
    private Level level;
    private Question currQuestion; //current random question
    private boolean questionsUsed[]; //array for questions that are already used

    // constants for sprites images
    private final String WALL_SPRITE = "Sprites/wall.png";
    private final String FLOOR_SPRITE = "Sprites/floor.png";
    private final String ENDPOINT_SPRITE = "Sprites/end-point.png";
    private final String COIN_SPRITE = "Sprites/coin.png";
    private final String QUESTION_SPRITE = "Sprites/question.png";
    private final String CHERRY_SPRITE = "Sprites/cherry.png";
    private final String GEM_SPRITE = "Sprites/gem.png";
    private final String FACING_DOWN = "Sprites/Facing-Down.png";

    // constants for menu images
    private final String MAIN_MENU_SCREEN = "ScreensAndMenus/MainMenu.jpg";
    private final String HELP_MENU_SCREEN = "ScreensAndMenus/HelpMenu.jpg";
    private final String QUESTION_TEMPLATE_SCREEN = "ScreensAndMenus/QuestionTemplate.jpg";
    private final String CORRECT_SCREEN = "ScreensAndMenus/CorrectScreen.jpg";
    private final String WRONG_SCREEN = "ScreensAndMenus/WrongScreen.jpg";
    private final String END_MENU_SCREEN = "ScreensAndMenus/EndScreen.jpg";

    // constants for different tiles
    private final char WALL = '*';
    private final char FLOOR = '#';
    private final char ENDPOINT = '^';
    private final char COIN = '%';
    private final char QUESTION = '?';
    private final char CHERRY = '"';
    private final char GEM = '~';

    // constants for the game states
    private final int MAIN_MENU = 1;
    private final int PLAYING_GAME = 2;
    private final int HELP_MENU = 3;
    private final int END_SCREEN = 4;
    private final int QUESTION_SCREEN = 5;
    private final int ANSWER_SCREEN = 6;
    private final int CORRECT_ANSWER = 7;
    private final int WRONG_ANSWER = 8;

    // arraylist for questions
    ArrayList<Question> questions = new ArrayList<Question>();

    // constructor
    public GameFrame(String t, int w, int h){
        super(t, w, h);
        // loading sprites and blocks
        try {
            wall = ImageIO.read(new File(WALL_SPRITE));
            floor = ImageIO.read(new File(FLOOR_SPRITE));
            endPoint = ImageIO.read(new File(ENDPOINT_SPRITE));
            coin = ImageIO.read(new File(COIN_SPRITE));
            question = ImageIO.read(new File(QUESTION_SPRITE));
            cherry = ImageIO.read(new File(CHERRY_SPRITE));
            gem = ImageIO.read(new File(GEM_SPRITE));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading sprites of blocks and pickups\nError Message: " + e.getMessage(), "Blocks and Pickups Sprites Loading Error", JOptionPane.ERROR_MESSAGE);
        }

        // loading menus and screens
        try {
            mainMenu = ImageIO.read(new File(MAIN_MENU_SCREEN));
            helpMenu = ImageIO.read(new File(HELP_MENU_SCREEN));
            questionScreen = ImageIO.read(new File(QUESTION_TEMPLATE_SCREEN));
            correctAnswer = ImageIO.read(new File(CORRECT_SCREEN));
            wrongAnswer = ImageIO.read(new File(WRONG_SCREEN));
            endScreen = ImageIO.read(new File(END_MENU_SCREEN));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading menus\nError Message: " + e.getMessage(), "Menus Loading Error", JOptionPane.ERROR_MESSAGE);
        }

        quit = false;

        offScreen = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);

        level = new Level();
        level.loadLevel("maze1.txt");

        loadQuestions();

        // starting the animation thread
        animation = new Thread(this);
        animation.start();

        player = new Player(FACING_DOWN, 32, 20, 32, 32);
        player.setVisible(true);

        addKeyListener(this);
    }

    public void drawLevel(){
        int x = 0;
        int y = 25;
        Graphics g = offScreen.getGraphics();

        for(int i = 0; i < 15; i++){ // loop for x
            for(int j = 0; j < 20; j++){ //loop for y
                switch(level.getTile(j, i)){ //using character from get tile to decide which block
                    case WALL:   g.drawImage(wall, x, y, null);         
                    break;
                    case FLOOR:   g.drawImage(floor, x, y, null);       
                    break;
                    case ENDPOINT:   g.drawImage(endPoint, x, y, null); 
                    break;
                    case COIN:   g.drawImage(coin, x, y, null);         
                    break;
                    case QUESTION: g.drawImage(question, x, y, null);   
                    break;
                    case CHERRY: g.drawImage(cherry, x, y, null);      
                    break;
                    case GEM: g.drawImage(gem, x, y, null);             
                    break;
                    default: JOptionPane.showMessageDialog(null, "Error loading tiles into level", "Tiles Loading Error", JOptionPane.ERROR_MESSAGE);
                }
                x = x + 32; //to move a block to the right 
            }

            y = y + 32; //to move a block down
            x = 0; //to start drawing from the top again
        }
    }

    @Override
    public void run(){
        while(quit == false){
            renderGame();
            repaint();

            try{
                Thread.sleep(30); // Make the tread sleep to give time for other threads to be processed
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error wtih Thread\nError Message: " + e.getMessage(), "Thread Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void renderGame(){
        Graphics g = offScreen.getGraphics();
        if(gameState == MAIN_MENU){
            g.drawImage(mainMenu, 0, 0, null);
        } else if(gameState == PLAYING_GAME){
            drawLevel();
            player.paint(g);

            g.setFont(new Font("Helvetica", Font.BOLD, 28));
            g.setColor(Color.WHITE);

            g.drawString("Score: " + score, 475, 60);
        } else if(gameState == HELP_MENU){
            g.drawImage(helpMenu, 0, 0, null);
        } else if(gameState == END_SCREEN){
            g.drawImage(endScreen, 0, 0, null);

            g.setFont(new Font("Helvetica", Font.BOLD, 37));
            g.setColor(Color.WHITE);

            g.drawString("" + score, 500, 250);
        } else if(gameState == QUESTION_SCREEN){
            g.drawImage(questionScreen, 0, 0, null);

            int ran;

            do {
                ran = (int)(Math.random() * questions.size()); // choosing random question
            } while(questionsUsed[ran] == true);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Century", Font.BOLD, 18));

            currQuestion = questions.get(ran);
            questionsUsed[ran] = true;

            g.drawString(currQuestion.getQuestion(), 55, 75);

            g.setFont(new Font("Century", Font.BOLD, 20));

            g.drawString(currQuestion.getOp1(), 150, 175);
            g.drawString(currQuestion.getOp2(), 150, 255);
            g.drawString(currQuestion.getOp3(), 150, 335);
            g.drawString(currQuestion.getOp4(), 150, 415);

            gameState = ANSWER_SCREEN;
        } else if(gameState == ANSWER_SCREEN){
            // nothing since its waiting for user to answer
        } else if(gameState == CORRECT_ANSWER){
            g.drawImage(correctAnswer, 0, 0, null);
        } else if(gameState == WRONG_ANSWER){
            g.drawImage(wrongAnswer, 0, 0, null);
        }

    }

    public void loadQuestions(){
        try{
            BufferedReader f = new BufferedReader(new FileReader("questions.txt"));

            String line; //for the BR to read from

            while((line = f.readLine()) != null){
                Question q = new Question();

                q.setQuestion(line);
                q.setOp1(f.readLine());
                q.setOp2(f.readLine());
                q.setOp3(f.readLine());
                q.setOp4(f.readLine());
                q.setAnswer(Integer.parseInt(f.readLine()));

                questions.add(q); //put all questions with variables in an arraylist
            }
            f.close();

            //created boolean array to avoid duplicate questions
            questionsUsed = new boolean [questions.size()];
            for(boolean question : questionsUsed){
                question = false;
            }

            //System.out.println(questions.size() + " questions loaded");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error loading questions\nError Message: " + e.getMessage(), "Questions Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Graphics g){
        paint(g);
    }

    @Override
    public void paint(Graphics g){
        if(offScreen != null){
            g.drawImage(offScreen, 1, 1, null);
            g.setColor(Color.BLACK);
            g.fillRect(0, 480, 640, 10);
        }
    }

    public void newGame(){
        score = 0;
        level.loadLevel("maze1.txt");
        player.setLocation(32, 20);
    }

    public void changeLevel(){
        if(levelNo == 1){
            level.loadLevel("maze2.txt");
            levelNo++;
            player.setLocation(32, 20);
        } else if(levelNo == 2){
            level.loadLevel("maze3.txt");
            levelNo++;
            player.setLocation(32, 20); 
        } else if(levelNo == 3){
            gameState = END_SCREEN;
        }
    }

    // checks the blocks adjacent to character
    public char tileToRight(int x, int y){
        int posX = (x/32) + 1;
        int posY = (y/32);
        if(posX < 20){
            return level.getTile(posX, posY);
        } else {
            return FLOOR;
        }
    }

    public char tileToLeft(int x, int y){
        int posX = (x/32) - 1;
        int posY = (y/32);
        if(posX < 20){
            return level.getTile(posX, posY);
        } else {
            return FLOOR;
        }
    }

    public char tileToDown(int x, int y){
        int posX = (x/32);
        int posY = (y/32) + 1;
        if(posY < 15){
            return level.getTile(posX, posY);
        } else {
            return FLOOR;
        }
    }

    public char tileToUp(int x, int y){
        int posX = (x/32);
        int posY = (y/32) - 1;
        if(posY < 15){
            return level.getTile(posX, posY);
        } else {
            return FLOOR;
        }
    }

    public void checkNextTile(){
        int px = player.getX(); 
        int py = player.getY();

        if(level.getTile(px/32, py/32) == COIN || level.getTile(px/32, py/32) == CHERRY || level.getTile(px/32, py/32) == GEM){
            level.setTile(px/32, py/32, FLOOR);
            score++;
        } else if (level.getTile(px/32, py/32) == ENDPOINT){
            changeLevel();
        } else if (level.getTile(px/32, py/32) == QUESTION){
            gameState = QUESTION_SCREEN;
            level.setTile(px/32, py/32, FLOOR);
        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        int px = player.getX(); 
        int py = player.getY();

        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            if(tileToLeft(px, py) != WALL){
                player.moveLeft();

                px = player.getX(); //getting updated x and y postitions
                py = player.getY();

                checkNextTile();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            if(tileToRight(px, py) != WALL){
                player.moveRight();

                px = player.getX(); 
                py = player.getY();

                checkNextTile();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            if(tileToDown(px, py) != WALL){
                player.moveDown();

                px = player.getX(); 
                py = player.getY();

                checkNextTile();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            if(tileToUp(px, py) != WALL){
                player.moveUp();

                px = player.getX(); 
                py = player.getY();

                checkNextTile();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_F1){
            if(gameState == MAIN_MENU){
                newGame();
                gameState = PLAYING_GAME;
            } else if(gameState == HELP_MENU){
                gameState = MAIN_MENU;
            } else if(gameState == END_SCREEN){
                gameState = MAIN_MENU;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_F2){
            if(gameState == MAIN_MENU){
                gameState = HELP_MENU;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_F3){
            if(gameState == MAIN_MENU){
                quit = true;
                System.exit(0);
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_1){
            if(gameState == ANSWER_SCREEN){          
                if(1 == currQuestion.getAnswer()){
                    score+=2;
                    gameState = CORRECT_ANSWER;
                } else {
                    gameState = WRONG_ANSWER;
                }
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_2){
            if(gameState == ANSWER_SCREEN){
                if(2 == currQuestion.getAnswer()){
                    score+=2;
                    gameState = CORRECT_ANSWER;
                } else {
                    gameState = WRONG_ANSWER;
                }
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_3){
            if(gameState == ANSWER_SCREEN){
                if(3 == currQuestion.getAnswer()){
                    score+=2;
                    gameState = CORRECT_ANSWER;
                } else {
                    gameState = WRONG_ANSWER;
                }
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_4){
            if(gameState == ANSWER_SCREEN){
                if(4 == currQuestion.getAnswer()){
                    score+=2;
                    gameState = CORRECT_ANSWER;
                } else {
                    gameState = WRONG_ANSWER;
                }
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(gameState == CORRECT_ANSWER || gameState == WRONG_ANSWER){
                gameState = PLAYING_GAME;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e){

    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void onClosing(){
        quit = true;
        System.exit(0);
    }
}