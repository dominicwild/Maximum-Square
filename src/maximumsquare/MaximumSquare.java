package maximumsquare;

import java.util.Arrays;

/**
 * Have the function MaximalSquare(strArr) take the strArr parameter being
 * passed which will be a 2D matrix of 0 and 1's, and determine the area of the
 * largest square submatrix that contains all 1's. A square submatrix is one of
 * equal width and height, and your program should return the area of the
 * largest submatrix that contains only 1's. For example: if strArr is ["10100",
 * "10111", "11111", "10010"] then this looks like the following matrix:
 *
 * 1 0 1 0 0
 * 1 0 1 *1 *1 1 1 1 *1 *1 1 0 0 1 0
 *
 * For the input above, you can see the *1's create the largest square submatrix
 * of size 2x2, so your program should return the area which is 4. You can
 * assume the input will not be empty.
 *
 * @author Dominic
 */
public class MaximumSquare {

    public static String MaximalSquare(String[] matrix) {
        
        matrix = ensureSquare(matrix);
        return MaxSquare(matrix) + "";

    }
    
    public static String[] ensureSquare(String[] matrix){
        //Make the matrix a square, if it isn't already
        if (matrix.length != matrix[0].length()) {
            if (matrix.length > matrix[0].length()) { //If the height is greater than the width of the square

                int diff = matrix.length - matrix[0].length(); //How much larger the length is

                for (String s : matrix) { //Append 0's until the same length
                    for (int i = 0; i < diff; i++) {
                        s += "0";
                    }
                }
            } else { //Width is greater than height, add rows to compensate

                int diff = -matrix.length + matrix[0].length(); //How much larger the width is
                String[] newMatrix = new String[matrix[0].length()];

                for (int i = 0; i < matrix.length; i++) { //Append 0's until the same length
                    newMatrix[i] = matrix[i];
                }

                for (int i = matrix.length; i < newMatrix.length; i++) {
                    String s = repeatString('0', matrix[0].length());
                    newMatrix[i] = s;
                }
                
                return newMatrix;
            }
        }
        return matrix;
    }

    public static String repeatString(char c, int repeatNum) {
        char[] repeat = new char[repeatNum];
        Arrays.fill(repeat, c);
        return new String(repeat);
    }
    
    //Assumes matrix is a square
    public static int MaxSquare(String[] matrix, int xMin, int xMax, int yMin, int yMax) {

        int width = xMax - xMin; //Width of square we are checking
        int maxPossible = width;

        matrixLoop:
        for (int i = yMin; i < yMax; i++) {//Iterate all rows
            String row = matrix[i];
            for (int col = xMin; col < xMax; col++) { //Iterate all characters in rows
                int c = Integer.valueOf(row.charAt(col)) == 48 ? 0 : 1; //48 is the ASCII code for 0
                if (c == 0) {
                    if (col == xMin || col == xMax - 1 || i == yMin || i == yMax - 1) { //if 0 is on any edge
                        maxPossible -= 1; //Otherwise we know the one this size is impossible
                    } else {
                        maxPossible -= 2; //We know a square one lower is impossible
                    }
                    break matrixLoop; //Breaks out of all loops
                }
            }
        }
        
        if(maxPossible == width){ //If MaxPossible remains unchanged, means everything was a 1
            return maxPossible;
        } else if(width == 2){ //Something was a 0, handle special case of 2
            if(MaxSquareTwoByTwo(matrix,xMin,xMax,yMin,yMax)){
                return 1;
            }
        } else {
            int reduceDimension = width - maxPossible;
            maxPossible = MaxSquare(matrix,xMin+reduceDimension,xMax,yMin+reduceDimension,yMax); 
            if(maxPossible == width-1){
                return maxPossible;
            }
            maxPossible = MaxSquare(matrix,xMin,xMax-reduceDimension,yMin,yMax-reduceDimension); 
            if(maxPossible == width-1){
                return maxPossible;
            }
            maxPossible = MaxSquare(matrix,xMin+reduceDimension,xMax,yMin,yMax-reduceDimension); 
            if(maxPossible == width-1){
                return maxPossible;
            }
            maxPossible = MaxSquare(matrix,xMin,xMax-reduceDimension,yMin+reduceDimension,yMax); 
        }
        
        return maxPossible;
    }
    
    public static boolean MaxSquareTwoByTwo(String[] matrix,int xMin, int xMax,int yMin, int yMax){
        boolean num1 = Integer.valueOf(matrix[yMin].charAt(xMin)) != 48; //48 is the ASCII code for 0
        boolean num2 = Integer.valueOf(matrix[yMin].charAt(xMax-1)) != 48;
        boolean num3 = Integer.valueOf(matrix[yMax-1].charAt(xMin)) != 48;
        boolean num4 = Integer.valueOf(matrix[yMax-1].charAt(xMax-1)) != 48;
        return num1 || num2 || num3 || num4; //Returns true if any number is  1
    }
    
    public static int MaxSquare(String[] matrix){
        return MaxSquare(matrix,0,matrix[0].length(),0,matrix.length);
    }

    public static void main(String[] args) {
        System.out.println(MaximalSquare(new String[]{"1100", "0101", "0101","0101"}));
        System.out.println(MaximalSquare(new String[]{"1111", "1111", "1111","1111"}));
        System.out.println(MaximalSquare(new String[]{"1111", "1111", "1111","1110"}));
        System.out.println(MaximalSquare(new String[]{"1111", "1111", "1101","1111"}));
        System.out.println(MaximalSquare(new String[]{"0111", "1111", "1111", "1111"}));
        System.out.println(MaximalSquare(new String[]{"0111", "1101", "0111"}));
    }

}
