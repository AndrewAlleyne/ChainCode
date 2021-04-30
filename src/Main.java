import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        //check arguments
        if (args.length < 2) {

            System.out.println("You need more arguments!");
        }

        int numRows = 0;
        int numCols = 0;
        int minVal = 0;
        int maxVal = 0;

        int nextBorder = 0;

        //To reconstruct image
        int startingRow = 0;
        int startingCol = 0;
        int pixelValue = 0;

        int[][] imageAry; // needs to dynamically allocate at run time (numRows+2 by numCols+2)
        int[][] CC_Ary; // needs to dynamically allocate at run time (numRows+2 by numCols+2)
        int[][] boundary_Ary; // needs to dynamically allocate at run time (numRows+2 by numCols+2)

        //Provided input files for processing
        String labelFile = args[0];
        String propFile = args[1];

        //Files generated at runtime
        String chainCode = args[0].replace(".txt", "") + "_ChainCodeFile.txt";
        String boundaryFile = args[0].replace(".txt", "") + "_BoundaryFile.txt";


        //Open stream to input files
        File inputFile = new File(labelFile);
        File inputFile_2 = new File(propFile);

        Scanner input = new Scanner(inputFile);
        Scanner input_2 = new Scanner(inputFile_2);


        //Create new files to output to
        File outputFile = new File(chainCode);
        outputFile.createNewFile();

        File outputFile_2 = new File(boundaryFile);
        outputFile_2.createNewFile();

        //Initialize r,c,mv,Mv
        if (input.hasNextInt()) numRows = input.nextInt();
        if (input.hasNextInt()) numCols = input.nextInt();
        if (input.hasNextInt()) minVal = input.nextInt();
        if (input.hasNextInt()) maxVal = input.nextInt();

        //Dynamically allocate arrays

        imageAry = new int[numRows + 2][numCols + 2];
        CC_Ary = new int[numRows + 2][numCols + 2];
        boundary_Ary = new int[numRows + 2][numCols + 2];

        //Zero-frame arrays from imageAry
        for (int i = 0; i < numRows + 2; i++) {
            for (int j = 0; j < numCols + 2; j++) {
                imageAry[i][j] = 0;
            }
        }

        //Load image from input image
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (input.hasNextInt()) imageAry[i][j] = input.nextInt();
            }
        }

        //Write Headers to files.
        FileWriter fileWriter = new FileWriter(chainCode);

        //Write header to Chain code file
        fileWriter.write(numRows + " " + numCols + " " + minVal + " " + maxVal + "\n");

        FileWriter fileWriter_2 = new FileWriter(boundaryFile);

        //Write header to Boundary file
        fileWriter_2.write(numRows + " " +
                numCols + " " +
                minVal + " " +
                maxVal + "\n");

        //CC_property class
        CC cc = new CC(input_2);

        //Zero out CC_Ary
        cc.clearCC_Ary(CC_Ary);

        //Extract CC.label from the image array into CC_Ary
        cc.loadCC(cc.label, CC_Ary, imageAry);

        //Get chain code
        cc.getChainCode(fileWriter, CC_Ary);

        //Construct Boundary

        //Open stream to labelFile_ChainCodeFile.txt
        Scanner input_3 = new Scanner(outputFile);


        //Read next items from stream
        if (input_3.hasNextInt()) numRows = input_3.nextInt();
        if (input_3.hasNextInt()) numCols = input_3.nextInt();


        if (input_3.hasNextInt()) minVal = input_3.nextInt();
        if (input_3.hasNextInt()) maxVal = input_3.nextInt();


        if (input_3.hasNextInt()) pixelValue = input_3.nextInt();


        //get starting row and starting column
        if (input_3.hasNextInt()) startingRow = input_3.nextInt();
        if (input_3.hasNextInt()) startingCol = input_3.nextInt();


        //We can use the header to recreate the original image
        boundary_Ary[startingRow][startingCol] = pixelValue;

        int rOffset = startingRow;
        int cOffset = startingCol;


        nextBorder++;

        while (nextBorder <= cc.numConnectedComponents) {

            while (input_3.hasNextInt()) {
                int direction = input_3.nextInt();

                switch (direction) {

                    case 0:
                        cOffset++;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    case 1:
                        rOffset--;
                        cOffset++;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    case 2:
                        rOffset--;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    case 3:
                        rOffset--;
                        cOffset--;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    case 4:
                        cOffset--;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    case 5:
                        rOffset++;
                        cOffset--;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    case 6:
                        rOffset++;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    case 7:
                        rOffset++;
                        cOffset++;
                        boundary_Ary[rOffset][cOffset] = 1;
                        break;

                    default:
                        break;

                }

                if (direction > 7) {
                    nextBorder++;

                    break;
                }
            }


        }


        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {

                if (boundary_Ary[i][j] == 0) {
                    System.out.print(". ");
                    fileWriter_2.write(boundary_Ary[i][j] + " ");
                } else {
                    System.out.print("1 ");
                    fileWriter_2.write(boundary_Ary[i][j] + " ");
                }
            }
            System.out.println();
            fileWriter_2.write("\n");

        }


        //Close all outputs
        fileWriter.close();
        fileWriter_2.close();
    }
}