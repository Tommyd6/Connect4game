import java.util.*;
/**
 * Tom Dorian 19496896
 * 02/01/2021
 */
public class game
{
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        Queue last5Games=new Queue();
        Queue Best5=new Queue();
        Queue Worst5=new Queue();
        // this while loops lets you to repeatily play the game
        while(true){
            char[][] board=new char[7][6];// array of the board
            System.out.println("Please Enter your name");
            String Name=sc.nextLine();// gets user name
            int moves=0;
            boolean winner=false;
            boolean win=false;
            int level=gameSetUp();// gets level user wants to play against
            // this loop is for the gameplay
            while(winner==false){
                winner=userInput(board);//gets users slot choice
                moves++;// increase moves
                // checks if that move won the game
                if(winner){// checks if that move won the game
                    System.out.println("Well Done you win");
                    win=true; // changes win to true for gamedetails opject
                    break;// breaks from gameplay loop
                }
                displayBoard(board); // shows the board to user
                // gets the move of the ai
                switch(level){
                    case 1:
                    winner=easyAi(board);
                    break;
                    case 2:
                    winner=medAi(board);
                    break;
                    case 3:
                    winner=hardAi(board);
                    break;
                }
                moves++;
                // checks if the ai made a winning move
                if(winner){
                    System.out.println("Hard Luck you lose");
                    break;
                }
                // checks if board is full
                if(isFull(board)==true){
                    System.out.println("Board Full Nobody wins");
                    break; // breaks if the game is a draw
                }
            }
            // makes an object that holds details of game
            GameDetails game= new GameDetails(level,moves,Name,win);
            last5Games.insert(game);// adds the gamedetails to q
            // if user won it see if it can add the best 5 wins
            if(win){
                Best5.InsertHigh(game);
            } else{// else it see if it can add to worst 5 loses q
                Worst5.InsertHigh(game);
            }
            displayBoard(board); // shows the board for user to look at 
            char EndGame=EndGame();// sees if user wants to play again
            if(EndGame=='n'){//if user says no we break from loop
                break;
            }
        }
        // printing out game log
        System.out.println("Last 5 games results");
        printQueues(last5Games);
        System.out.println("Quickest 5 wins");
        printQueues(Best5);
        System.out.println("Quickest 5 lose");
        printQueues(Worst5);
    }
    
    // choices what level of ai to use and returns it
    public static int gameSetUp()
    {
        System.out.println("please enter the level of difficultiy you want to play against from 1-3");
        Scanner sc=new Scanner(System.in);
        int level=sc.nextInt();
        if ((level<=3)&&(level>=1)){ // if user picked valid level we return it
            System.out.println("Level "+ level+" difficulty was picked");
            return level;
        }
        System.out.println("invalid choice pick a number between 1 and 3");
        return gameSetUp();// if choice isnt valid it does the method again
    }

    // gets input off user and udpates the game accordinly
    // return if user input is winning move
    public static boolean userInput(char board[][]){
        char player='r';
        displayBoard(board);// shows board
        System.out.println("please enter the slot you want to add to from 0 to 6");
        Scanner sc=new Scanner(System.in);
        int slot=sc.nextInt();// chosen slot off user

        if((slot>=7)||(slot<0)){// if use picks too high a number
            System.out.println("invalid slot number");
            return userInput(board);
        }
        if (board[slot][5] != 0){// if slot is full gts new input
            System.out.println("invalid move, slot full");
            return userInput(board);
        }
        int row=addToBoard(board,slot,player);// adds to board and get the row it was added to
        return winningMove(board,player,slot,row);// checks if this was a winning move
    }
    // goes from bottom of board to find first empty space in the parameter slot of the given array
    // changes the space with the player char
    // it returns the row thus move was in
    public static int addToBoard(char board[][],int slot,char player){
        for(int i=0;i<6;i++){ // goes from bottom of board to find first empty slot
            if(board[slot][i]==0){
                board[slot][i]=player;// swaps the space with the player char
                return i;// returns the row
            }
        }
        return 0;
    }
    // prints out the arrray
    public static void displayBoard(char board[][]){
        for(int j=5;j>=0;j--){// starts top row first
            for(int i=0;i<7;i++){// starts left slot first
                System.out.print("(");
                switch(board[i][j]){// used to make sure a space is printed if the position is empty
                    case 'r':
                    System.out.print('r');
                    break;
                    case 'g':
                    System.out.print('g');
                    break;
                    default:
                    System.out.print(" ");
                }
                System.out.print(")");
            }
            System.out.println();// new row
        }
        System.out.println(" 0  1  2  3  4  5  6");
    }

    // checks to see if the array is full
    public static boolean isFull(char board[][]){
        for(int i=0;i<7;i++){// goes through every slot
            if(board[i][5]==0){// if the top row in the slot is empty the array is empty
                return false; // returns false if empty
            }
        }
        return true;//returns true is not top rows are empty
    }
    // this method checks to see if its got a winning move
    // it takes in the board the player and the postion of their move
    // if it was a winning move the method returns a true else a false
    public static boolean winningMove(char board[][],char player,int slot,int row){
        // first we check vertical
        int tempX=slot;// used to store x postion we check from
        int tempY=row;// used to store y position we check from
        if(row>=3){// checks to see if we are atleast four up
            for(int i=row;i>=0;i--){// goes down from row to 0
                if(board[slot][i]!=player){// if the position ain't = to player char we break 
                    break;
                }
                if(i==row-3){
                    return true;// we got a winner
                }
            }    
        }
        // gets position of furthest left connected dot
        for(int i=slot;i>=0;i--){
            if(board[i][row]!=player){// if the position ain't = to player char we break 
                break;
            }
            if(i==slot-3){// we got a winner
                return true;
            }
            tempX=i;// gets the furthest left connected dot as current i
        }
        // check to see if there is four in a row
        for(int i=tempX;i<7;i++){
            if(board[i][row]!=player){// if the position ain't = to player char we break 
                break;
            }
            if(i==tempX+3){// we got a winner
                return true;
            }
        }
        // gets positive slope diagonal bottom left connected
        for(int i=slot,j=row;((i>=0)&&(j>=0));i--,j--){
            if(board[i][j]!=player){// if the position ain't = to player char we break 
                break;
            }
            if(i==slot-3){// we got a winner
                return true;
            }
            tempX=i;
            tempY=j;
        }
        // goes from bottom left up and right
        for(int i=tempX,j=tempY;((i<7)&&(j<6));i++,j++){
            if(board[i][j]!=player){// if the position ain't = to player char we break 
                break;
            }
            if(i==tempX+3){// we got a winner
                return true;
            }
        }
        // gets negative slope diagonal bottom left connected
        for(int i=slot,j=row;((i<7)&&(j>=0));i++,j--){
            if(board[i][j]!=player){// if the position ain't = to player char we break 
                break;
            }
            if(i==slot+3){// we got a winner
                return true;
            }
            tempX=i;
            tempY=j;
        }
        // goes from bottom right, left and up
        for(int i=tempX,j=tempY;((i>=0)&&(j<6));i--,j++){
            if(board[i][j]!=player){// if the position ain't = to player char we break 
                break;
            }
            if(i==tempX-3){// we got a winner
                return true;
            }
        }
        return false;
    }
    // this makes the level 1 move
    // it takes in the board and returns the slot
    public static boolean easyAi(char board[][]){
        char player='g';
        int slot=(int)(Math.random() * ((6) + 1));// chosen slot off rng from 0-6
        if (board[slot][5] != 0){// if slot is full gts new input
            return userInput(board);
        }
        int row=addToBoard(board,slot,player);// adds to board and get the row it was added to
        return winningMove(board,player,slot,row);// checks if this was a winning move
    }
    // this makes the level 2 move
    // it takes in the board and returns the slot
    public static boolean medAi(char board[][]){
        char player1='g';
        char player2='r';
        int slot=0;
        int row=0;
        int score=0;
        int tempRow=0;
        for(int i=0;i<7;i++){
            tempRow=7;
            for(int j=0;j<6;j++){ // goes from bottom of board to find first empty slot
                if(board[i][j]==0){
                    tempRow=j;// swaps row with j
                    break;
                }
            }
            int tempScore=weightPostion(board,i,tempRow,player1,player2);// gets weight of move to this postion
            tempScore=tempScore+(int)(Math.random() * ((3) + 1));// makes easier and adds something random to it
            if(tempScore>score){// if tempScore is greater than score we swap themand set i as slot
                score=tempScore;
                slot=i;
            }
        }
        row=addToBoard(board,slot,player1);// adds to board and get the row it was added to
        return winningMove(board,player1,slot,row);// checks if this was a winning move
    }
    // this makes the level 2 move
    // it takes in the board and returns the slot
    public static boolean hardAi(char board[][]){
        char player1='g';
        char player2='r';
        int slot=0;
        int row=0;
        int score=-1000;
        int tempRow=0;
        int tempRow2=0;
        int tempScore=0;
        int tempScore2=0;
        int negScore=0;
        // goes through every slot for max move
        for(int i=0;i<7;i++){
            tempRow=7;// sets tempRow to high to avoid array out of bounds
            for(int j=0;j<6;j++){ // goes from bottom of board to find first empty slot
                if(board[i][j]==0){
                    tempRow=j;// swaps row with j
                    break;
                }
            }
            tempScore=weightPostion(board,i,tempRow,player1,player2);// gets weight postion for ai move
             if(tempRow<7){
                board[i][tempRow]=player1;// adds move to board
                // goes through every slot for the best move after this
                for(int k=0;k<7;k++){
                    tempRow2=7;
                    for(int j=0;j<6;j++){ // goes from bottom of board to find first empty slot
                        if(board[k][j]==0){
                            tempRow2=j;// swaps row with j
                            break;
                        }
                    }
                    // this is used to swap tempScore2 if the new weight is bigger than tempScore 2
                    tempScore2=weightPostion(board,k,tempRow2,player2,player1)>tempScore2?weightPostion(board,k,tempRow2,player2,player1):tempScore2;
                }
                board[i][tempRow]=0;// removes ai move from board
            }
            tempScore=tempScore-tempScore2/2;// takes 1/2 of players best move from ai move
            tempScore2=0;//resets tempScore 2
            if(tempScore>score){// if tempScore is bigger than score this is now the move ai make
                score=tempScore;
                slot=i;
                row=tempRow;
            }
        }
        row=addToBoard(board,slot,player1);// adds to board and get the row it was added to
        return winningMove(board,player1,slot,row);// checks if this was a winning move
    }

    public static int weightPostion(char board[][],int slot,int row,char player1,char player2){
        int tempX=0;
        int tempY=0;
        // these are the negative and positives foreach move.
        // they are all declared how so it is easy to change them to optimise ai
        int own2W=2;
        int own3W=4;
        int opp2W=1;
        int opp3W=3;
        int centreW=5;
        int winW=1000;
        int oppW=100;
        int score=0;
        if(row==7){// slot is full so we return a high negative
            return -1000;
        }
        if(slot==3){// add score for centre slot
            score=score+centreW;
        }
        // this section adds postive weights to score 
        score=score+verticalWeight(board,slot,row,own2W,own3W,winW,player1);
        score=score+horizontalWeight(board,slot,row,own2W,own3W,winW,player1,player2);
        score=score+postiveSlopeWeight(board,slot,row,own2W,own3W,winW,player1,player2);
        score=score+negativeSlopeWeight(board,slot,row,own2W,own3W,winW,player1,player2);
        //this section takes the negative consequence of move
        score=score+verticalWeight(board,slot,row,opp2W,opp3W,oppW,player2);
        score=score+horizontalWeight(board,slot,row,opp2W,opp3W,oppW,player2,player1);
        score=score+postiveSlopeWeight(board,slot,row,opp2W,opp3W,oppW,player2,player1);
        score=score+negativeSlopeWeight(board,slot,row,opp2W,opp3W,oppW,player2,player1);

        return score;
    }
    // this gets the weight of how many disc it will connect vertical
    public static int verticalWeight(char board[][],int slot,int row,int W2,int W3, int winW,char player){
        int score=0;
        if((row-1>=0)&&(board[slot][row-1]==player)){// checks to see the char under it is matching
            score=row<4?score+W2:score;
            if((row-2>=0)&&(board[slot][row-2]==player)){// this adds a higher score to the weight
                score=row<3?score+W3:score;
                if((row-3>=0)&&(board[slot][row-3]==player)){// if it gets to here than we gone down 4 so we gotta winner ifwe place here
                    score=score+winW;
                }
            }

        }
        return score;
    }
    // this gets the weight of how many disc it will connect horizontally
    public static int horizontalWeight(char board[][],int slot,int row,int W2,int W3, int winW,char player1,char player2){
        int score=0;
        // this loop goes left 4 times
        for(int i=slot;((i>slot-4)&&(i>=0));i--){
            int tempCount=0;
            if(board[i][row]==player2){ // if the space = player 2 then we stop moving left
                break;
            }
            // we now check the section of 3 to the right of the space and itself
            // we do this to check how many of the matching colour it can makes
            for(int j=i;((j<slot+4)&&(j<7));j++){
                if(board[j][row]==player2){// if it meets player 2 colour in this four then this four is useless to us
                    break;
                }
                if(board[j][row]==player1){//if it encounters player 1 it adds to the count
                    tempCount++;
                }
                // depending on how many of player 1 colour it encounters is how much weight is added
                if(j==i+3){
                    switch(tempCount){
                        case 1:
                        score=score+W2;
                        break;
                        case 2:
                        score=score+W3+W2;
                        break;
                        case 3:
                        return winW;
                    }
                }

            }
        }
        return score;
    }
    // this gets the weight of how many disc it will connect diagonally up and right
    public static int postiveSlopeWeight(char board[][],int slot,int row,int W2,int W3, int winW,char player1,char player2){
        int score=0;
        // this loop moves down and left
        for(int i=slot,j=row;((i>slot-4)&&(i>=0)&&(j>=0));i--,j--){
            int tempCount=0;
            if(board[i][j]==player2){// if the space = player 2 then we stop moving left and down
                break;
            }
            // we now check the section of 3 to the right and up of the space and itself
            // we do this to check how many of the matching colour it can makes
            for(int k=i,l=j;((k<i+4)&&(k<7)&&(l<6));k++,l++){
                if(board[k][l]==player2){// if it meets player 2 colour in this four then this four is useless to us
                    break;
                }
                if(board[k][l]==player1){//if it encounters player 1 it adds to the count
                    tempCount++;
                }
                // depending on how many of player 1 colour it encounters is how much weight is added
                if(k==i+3){
                    switch(tempCount){
                        case 1:
                        score=score+W2;
                        break;
                        case 2:
                        score=score+W3+W2;
                        break;
                        case 3:
                        return winW;
                    }
                }
            }
        }
        return score;
    }

    public static int negativeSlopeWeight(char board[][],int slot,int row,int W2,int W3, int winW,char player1,char player2){
        int score=0;
        // this loop moves up and left
        for(int i=slot,j=row;((j>row-4)&&(i>=0)&&(j<6));i--,j++){
            int tempCount=0;// resets count for next loop
            if(board[i][j]==player2){// if the space = player 2 then we stop moving left and up
                break;
            }
            for(int k=i,l=j;((k<i+4)&&(k<7)&&(l>=0));k++,l--){
                if(board[k][l]==player2){// if it meets player 2 colour in this four then this four is useless to us
                    break;
                }
                if(board[k][l]==player1){//if it encounters player 1 it adds to the count
                    tempCount++;
                }
                // depending on how many of player 1 colour it encounters is how much weight is added
                if(k==i+3){
                    switch(tempCount){
                        case 1:
                        score=score+W2;
                        break;
                        case 2:
                        score=score+W3+W2;
                        break;
                        case 3:
                        return winW;
                    }
                }
            }
        }
        return score;
    }
    // this methods sends back y or n depending on if the user enters y or n
    public static char EndGame(){
        Scanner sc=new Scanner(System.in);
        System.out.println("If you want to play again enter yes else no");
        char end=sc.next().charAt(0); // gets first char of yes or no
        if(end=='y'||end=='n'){
            return end; //retruns char
        }
        System.out.println("Invalid input please try again");
        return EndGame();// invalid word was enter so method called again
    }
    // this method takesa gameDetails queue and prints out the data
    public static void printQueues(Queue item){
        System.out.println("");
        while(item.isEmpty()==false){// repeats this while queue isn't empty
            GameDetails temp=item.remove();// gets the first in queue
            String win=temp.getResult()?"win":"lose"; //gets string or win or lose depending on result
            // long print statement of informatiom
            System.out.println(temp.getName()+" "+win+" Amount of moves in the game "+temp.getMoves()+", difficulty level was "+temp.getLevel());
        }
        System.out.println("");
    }
}
//queue data structure that can work as prioity queue
class Queue{
    int maxSize=5;
    GameDetails[] queArray;
    int front;
    int rear;
    int nItems;
    public Queue() { // constructor
        queArray = new GameDetails[maxSize];
        front = 0;
        rear = -1;
        nItems = 0;
    }
    public boolean insert(GameDetails j) { // put item at rear of queue
        if(rear == maxSize-1) // deal with wraparound
            rear = -1;   
        rear++;
        queArray[rear] = j; // increment rear and insert
        nItems++; // one more item
        return true; //successfully inserted
    }
    public void InsertHigh(GameDetails item) { // insert item
        int j = nItems; // start at end
        if(nItems==0){ // if no items,
            queArray[0] = item; // insert at 0
            nItems++; // increase items
        }else{ 
            if(nItems==5&&item.getMoves()<queArray[j-1].getMoves()){// if full and new item is smaller we lower j to avoid array index out of bounds
                j--;
                nItems--;
            }
            while(j > 0 && queArray[j-1].getMoves() < item.getMoves()){ // while new item smaller
                queArray[j] = queArray[j-1]; // shift upward
                j--; // decrement j
            }
            queArray[j] = item; // insert it
            nItems++;
        } 
    }
    public GameDetails remove() { // take item from front of queue
        if(isEmpty())
            return null; //don’t remove if empty
        GameDetails temp = queArray[front];// get value and increase front
        front++;
        if(front == maxSize) // deal with wraparound
            front = 0;
        nItems--; // one less item
        return temp;
    }

    public GameDetails peekFront(){ // peek at front of queue
        return queArray[front];
    }

    public boolean isEmpty() { // true if queue is empty
        return (nItems==0);
    }

    public boolean isFull() { // true if queue is full
        return (nItems==maxSize);
    }

    public int size() { // number of items in queue
        return nItems;
    } 
}
// this is an object that holds details of game
// the details are player name, if the won, the amount of moves played in the game and the level it was played at
class GameDetails{
    int level;
    String Name;
    int moves;
    boolean win;
    public GameDetails(int nlevel,int nmoves,String nName,boolean nwin){
        level=nlevel;
        Name=nName;
        moves=nmoves;
        win=nwin;
    }
    // these methods just get the data
    public String getName(){
        return Name;
    }

    public int getLevel(){
        return level;
    }

    public int getMoves(){
        return moves;
    }

    public boolean getResult(){
        return win;
    }
}