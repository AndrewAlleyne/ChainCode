import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;



public class CC {

    Point startP = new Point();

    Scanner inputFile;

    public int label;
    public int numPixels;
    public int minRow;
    public int minCol;
    public int maxRow;
    public int maxCol;

    int numRows;
    int numCols;
    int minVal;
    int maxVal;
    Point currentP = new Point();
    Point nextP = new Point();
    Point[] neighborCoord = new Point[8];
    int zeroTable[] = {6, 0, 0, 2, 2, 4, 4, 6};


    int PchainDir;
    int nextDir;
    int lastQ;
    int nextQ;
    int numConnectedComponents;


    CC(Scanner inputFile) {
        this.inputFile = inputFile;

        //Reads the header information
        if (inputFile.hasNextInt()) numRows = inputFile.nextInt();
        if (inputFile.hasNextInt()) numCols = inputFile.nextInt();
        if (inputFile.hasNextInt()) minVal = inputFile.nextInt();
        if (inputFile.hasNextInt()) maxVal = inputFile.nextInt();

        //Reads the number of connected components
        if (inputFile.hasNextInt()) numConnectedComponents = inputFile.nextInt();

        //CC label
        if (inputFile.hasNextInt()) label = inputFile.nextInt();

        //numPixels
        if (inputFile.hasNextInt()) numPixels = inputFile.nextInt();


        //Reads the minRow, minCol of greyscale pixel values
        if (inputFile.hasNextInt()) minRow = inputFile.nextInt();
        if (inputFile.hasNextInt()) minCol = inputFile.nextInt();

        //Reads the maxRow, maxCol of greyscale pixel values
        if (inputFile.hasNextInt()) maxRow = inputFile.nextInt();
        if (inputFile.hasNextInt()) maxCol = inputFile.nextInt();

    }

    public void getChainCode(FileWriter fileWriter, int[][] cc_ary) throws IOException {

        boolean isThere = false;

        for (int i = 0; i < numRows + 2; i++) {
            for (int j = 0; j < numCols + 2; j++) {

                //get the first pixel
                if (cc_ary[i][j] == label) {
                    fileWriter.write(label + " " + minRow + " " + minCol + " ");
                    startP.row = i;
                    startP.col = j;
                    currentP.row = i;
                    currentP.col = j;
                    lastQ = 4;

                    while (currentP != startP) {
                        nextQ = (lastQ + 1) % 8;
                        System.out.println(lastQ);

                        PchainDir = findNextP(currentP, nextQ, cc_ary);
                        nextP.row = neighborCoord[PchainDir].row;
                        nextP.col = neighborCoord[PchainDir].col;

                        fileWriter.write(PchainDir + " ");

                        if (PchainDir == 0) {
                            lastQ = zeroTable[7];
                        } else {
                            lastQ = zeroTable[PchainDir - 1];
                        }

                        //change currentP to nextP
                        currentP.row = nextP.row;
                        currentP.col = nextP.col;

                        //check if currentP == nextP, if so break

                        if (currentP.row == startP.row && currentP.col == startP.col) {
                            isThere = true;
                            break;
                        }
                    }
                    fileWriter.flush();
                }
                if (isThere) {
                    break;
                }
            }

            //check condition before moving to the next row
            if (isThere) {
                break;
            }
        }
    }

    //Zeroes out the CC_Ary
    public void clearCC_Ary(int[][] cc_ary) {
        for (int i = 0; i < numRows + 2; i++) {
            for (int j = 0; j < numCols + 2; j++) {
                cc_ary[i][j] = 0;
            }
        }
    }

    //Extracts image where label is > 0
    public void loadCC(int label, int[][] cc_ary, int[][] imageAry) {
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (imageAry[i][j] > 0) {
                    cc_ary[i][j] = imageAry[i][j];
                    //System.out.print(cc_ary[i][j] + " ");
                } else {
                  //  System.out.print("0 ");
                }
            }
            //System.out.println();
        }
    }

    //We must know the current P and then obtain the next
    public int findNextP(Point currentP, int nextQ, int[][] cc_ary) {
        int chainDir;
        int direction = nextQ;


        //we must first load the neighbor coordinates.
        loadNeighborCoord(currentP);

        while(true){
            if(cc_ary[neighborCoord[direction].row][neighborCoord[direction].col] == label){
                chainDir = direction;
                break;
            }
            direction = (direction+1)%8;
        }

        //next chain direction
        return chainDir;
    }

    private int chainDir(int nextQ, int[][] cc_ary) {

        //checks neighbors starting from nextQ counter clockwise
        int currentPNeigh = neighCurrent(nextQ, cc_ary);
        return currentPNeigh;
    }

    //Scans currentP's neighbors and returns the first nonzero position]
    private int neighCurrent(int nextQ, int[][] cc_ary) {

        int i = currentP.row;
        int j = currentP.col;


        int loop = 0;
        while (loop < 8) {
            //direction
            nextQ = (++nextQ) % 8;


            switch (nextQ) {
                case 0:
                    if (cc_ary[i][j + 1] > 0) return 0;
                    break;
                case 1:
                    if (cc_ary[i - 1][j + 1] > 0) return 1;
                    break;
                case 2:
                    if (cc_ary[i - 1][j] > 0) return 2;
                    break;
                case 3:
                    if (cc_ary[i - 1][j - 1] > 0) return 3;
                    break;
                case 4:
                    if (cc_ary[i][j - 1] > 0) return 4;
                    break;
                case 5:
                    if (cc_ary[i + 1][j - 1] > 0) return 5;
                    break;
                case 6:
                    if (cc_ary[i + 1][j] > 0) return 6;
                    break;
                case 7:
                    if (cc_ary[i + 1][j + 1] > 0) return 7;
                    break;
                default:
                    break;
            }
            loop++;
        }
        return 0;
    }

    private void loadNeighborCoord(Point currentP) {

        //array of objects to store x-y
        for (int i = 0; i < 8; i++) {
            neighborCoord[i] = new Point();
        }
        neighborCoord[0].row = currentP.row;
        neighborCoord[0].col = currentP.col + 1;
        neighborCoord[1].row = currentP.row - 1;
        neighborCoord[1].col = currentP.col + 1;
        neighborCoord[2].row = currentP.row - 1;
        neighborCoord[2].col = currentP.col;
        neighborCoord[3].row = currentP.row - 1;
        neighborCoord[3].col = currentP.col - 1;
        neighborCoord[4].row = currentP.row;
        neighborCoord[4].col = currentP.col - 1;
        neighborCoord[5].row = currentP.row + 1;
        neighborCoord[5].col = currentP.col - 1;
        neighborCoord[6].row = currentP.row + 1;
        neighborCoord[6].col = currentP.col;
        neighborCoord[7].row = currentP.row + 1;
        neighborCoord[7].col = currentP.col + 1;

    }

    //Point class
    class Point {

        int row;
        int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Point() {

        }
    }


}
