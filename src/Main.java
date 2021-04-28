import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        //check arguments
        if(args.length < 2){

            System.out.println("You need more arguments!");
        }

        int numRows=0;
        int numCols=0;
        int minVal=0;
        int maxVal=0;

        int imageAry[][]; // needs to dynamically allocate at run time (numRows+2 by numCols+2)
        int CC_Ary[][]; // needs to dynamically allocate at run time (numRows+2 by numCols+2)
        int boundary_Ary[][]; // needs to dynamically allocate at run time (numRows+2 by numCols+2)

        //Provided input files for processing
        String labelFile =  args[0];
        String propFile = args[1];

        //Files generated at runtime
        String chainCode = args[0].replace(".txt","") + "_ChainCodeFile.txt";
        String boundaryFile = args[0].replace(".txt","") + "_BoundaryFile.txt";



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
        if(input.hasNextInt()) numRows = input.nextInt();
        if(input.hasNextInt()) numCols = input.nextInt();
        if(input.hasNextInt()) minVal = input.nextInt();
        if(input.hasNextInt()) maxVal = input.nextInt();

        //Dynamically allocate arrays

        imageAry = new int[numRows + 2][numCols + 2];
        CC_Ary = new int[numRows + 2][numCols + 2];
        boundary_Ary = new int[numRows + 2][numCols + 2];

        //Zero-frame arrays from imageAry
        for(int i = 0; i < numRows + 2; i++){
            for(int j = 0; j < numCols + 2; j++){
                imageAry[i][j] = 0;
            }
        }

        //Load image from input image
        for(int i = 1; i <= numRows; i++){
            for(int j = 1; j <= numCols; j++){
                if(input.hasNextInt()) imageAry[i][j] = input.nextInt();
            }
        }

        //Write Headers to files.
        FileWriter fileWriter = new FileWriter(chainCode);

        //Write header to Chain code file
        fileWriter.write(String.valueOf(new StringBuilder().append(numRows).append(" ")
                .append(numCols).append(" ")
                .append(minVal).append(" ")
                .append(maxVal)));

        FileWriter fileWriter_2 = new FileWriter(chainCode);

        //Write header to Boundary file
        fileWriter_2.write(String.valueOf(new StringBuilder().append(numRows).append(" ")
                .append(numCols).append(" ")
                .append(minVal).append(" ")
                .append(maxVal)));

        //CC_property class
        CC cc = new CC(input_2);

        //Zero out CC_Ary
        cc.clearCC_Ary(CC_Ary);

        //Extract CC.label from the image array into CC_Ary
        cc.loadCC(cc.label,CC_Ary,imageAry);

        //Get chain code
        cc.getChainCode(fileWriter,  CC_Ary);



        //Close outputs
        fileWriter.close();
        fileWriter_2.close();
    }
}
