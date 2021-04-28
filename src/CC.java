import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//Point class
 class Point{

     int row;
     int col;

     int neighCoord[] = new int [8];
     int zeroTable[] = {6, 0, 0, 2, 2, 4, 4, 6};


}

public class CC {
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

    Point startP;
    Point currentP;
    Point nextP;
    int PchainDir;
    int nextDir;
    int lastQ;
    int nextQ;

    int numConnectedComponents;



    CC(Scanner inputFile){
        this.inputFile = inputFile;

        //Reads the header information
        if(inputFile.hasNextInt()) numRows = inputFile.nextInt();
        if(inputFile.hasNextInt()) numCols = inputFile.nextInt();
        if(inputFile.hasNextInt()) minVal = inputFile.nextInt();
        if(inputFile.hasNextInt()) maxVal = inputFile.nextInt();

        //Reads the number of connected components
        if(inputFile.hasNextInt()) numConnectedComponents = inputFile.nextInt();

        //CC label
        if(inputFile.hasNextInt()) label = inputFile.nextInt();

        //Reads the minRow, minCol of greyscale pixel values
        if(inputFile.hasNextInt()) minVal = inputFile.nextInt();
        if(inputFile.hasNextInt()) maxVal = inputFile.nextInt();

        //Reads the maxRow, maxCol of greyscale pixel values
        if(inputFile.hasNextInt()) maxVal = inputFile.nextInt();
        if(inputFile.hasNextInt()) maxCol = inputFile.nextInt();
    }

    //Zeroes out the CC_Ary
    public void clearCC_Ary(int[][] cc_ary) {
        for(int i = 0; i < numRows + 2; i++){
            for(int j = 0; j < numCols + 2; j++){
                cc_ary[i][j] = 0;
            }
        }
    }

    //Extracts image where label is > 0
    public void loadCC(int label, int[][] cc_ary, int[][] imageAry) {
        for(int i = 1; i <=numRows; i++){
            for(int j = 1; j <= numCols; j++){
                if(imageAry[i][j] > 0){
                    cc_ary[i][j] = imageAry[i][j];
                    System.out.print (cc_ary[i][j]+ " ");
                }else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    public void getChainCode(FileWriter fileWriter, int[][] cc_ary) throws IOException {

        for(int i = 0; i < numRows + 2; i++){
            for(int j = 0; j < numCols + 2; j++){
                if(cc_ary[i][j] == label){
                    fileWriter.write(label + " " + i + " " + j);

                    // I dont understand this code.
                    startP.row = i;
                    startP.col = j;
                    currentP.row = i;
                    currentP.col = j;
                    lastQ = 4;

                    while(currentP == startP){
                        nextQ = (lastQ + 1) % 8;
                        PchainDir = findNextP(currentP, nextQ, nextP);
                    }
                    fileWriter.flush();
                }
            }
        }
    }

    private int findNextP(Point currentP, int nextQ, Point nextP) {
        int value = 0;

        loadNeighborCoord(currentP);
        
        
        return value;
    }

    private void loadNeighborCoord(Point currentP) {

    }
}
