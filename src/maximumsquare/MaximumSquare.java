package maximumsquare;

import java.util.Arrays;

/**
 * Have the function MaximalSquare take a string array parameter being
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

    /**
     * Determines the squared size of the largest submatrix of 1's in a matrix.
     * @param matrix An array of strings forming a matrix of 1's and 0's 
     * @return The squared size of the largest submatrix of 1's
     */
    public static String MaximalSquare(String[] matrix) {
        matrix = ensureSquare(matrix); 
        int max = MaxSquare(matrix);
        return Integer.toString(max*max);
    }
    
    /**
     * Ensures the matrix is square of n by n. Adding extra 0's where blanks are detected. Assumes the input is at least a rectangle.
     * @param matrix The rectangle matrix to modify.
     * @return The square matrix.
     */
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

    /**
     * Makes a string with repeating characters.
     * @param c The character to repeat.
     * @param repeatNum The number of times to repeat the character c in a string.
     * @return The string of c repeated repeatNum times.
     */
    public static String repeatString(char c, int repeatNum) {
        char[] repeat = new char[repeatNum];
        Arrays.fill(repeat, c);
        return new String(repeat);
    }
    
    /**
     * Assumes matrix is a square of 1's and 0's. Outputs the maximum possible submatrix defined by bounds xMin, xMax, yMin, yMax in the passed matrix.
     * @param matrix An array of strings forming a matrix of 1's and 0's
     * @param xMin The minimum x bound of the submatrix to consider.
     * @param xMax The maximum x bound of the submatrix to consider.
     * @param yMin The minimum y bound of the submatrix to consider.
     * @param yMax The maximum y bound of the submatrix to consider.
     * @return The size of the largest submatrix of 1's
     */
    public static int MaxSquare(String[] matrix, int xMin, int xMax, int yMin, int yMax) {

        int width = xMax - xMin; //Width of square we are checking
        int maxPossible = width; //The maximum possible square of all 1's that may currently exist in this matrix

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
            //Check 4 possible subsquares to see if one contains all 1's
            int reduceDimension = width - maxPossible;
            maxPossible = MaxSquareSub(matrix,xMin+reduceDimension,xMax,yMin+reduceDimension,yMax,0); //First assignment gets a free pass, because maxPossible is hypothetical in the first call (it doesn't concretely find a square of 1's)
            if(maxPossible == width-1){ //If width-1 is the answer, this is the max sized square we can have for this matrix
                return maxPossible;
            }
            maxPossible = MaxSquareSub(matrix,xMin,xMax-reduceDimension,yMin,yMax-reduceDimension,maxPossible); 
            if(maxPossible == width-1){
                return maxPossible;
            }
            maxPossible = MaxSquareSub(matrix,xMin+reduceDimension,xMax,yMin,yMax-reduceDimension,maxPossible); 
            if(maxPossible == width-1){
                return maxPossible;
            }
            maxPossible = MaxSquareSub(matrix,xMin,xMax-reduceDimension,yMin+reduceDimension,yMax,maxPossible); 
        }
        
        return maxPossible;
    }
    
    /**
     * Returns the max possible square of 1's taking into consideration the current maxPossible value.
     * @param matrix An array of strings forming a matrix of 1's and 0's
     * @param xMin The minimum x bound of the submatrix to consider.
     * @param xMax The maximum x bound of the submatrix to consider.
     * @param yMin The minimum y bound of the submatrix to consider.
     * @param yMax The maximum y bound of the submatrix to consider.
     * @param maxPossible The maximum possible currently under consideration
     * @return The size of the largest submatrix of 1's
     */
    public static int MaxSquareSub(String[] matrix, int xMin, int xMax, int yMin, int yMax, int maxPossible){
        int subMax = MaxSquare(matrix,xMin,xMax,yMin,yMax); 
        if(subMax > maxPossible){ //Only return the largest of the two values
            return subMax;
        } else {
            return maxPossible;
        }
    }
    
    /**
     * Handles the special case where the bounds determine a submatrix of size 2x2
     * @param matrix An array of strings forming a matrix of 1's and 0's
     * @param xMin The minimum x bound of the submatrix to consider.
     * @param xMax The maximum x bound of the submatrix to consider.
     * @param yMin The minimum y bound of the submatrix to consider.
     * @param yMax The maximum y bound of the submatrix to consider.
     * @return The size of the largest submatrix of 1's
     */
    public static boolean MaxSquareTwoByTwo(String[] matrix,int xMin, int xMax,int yMin, int yMax){
        boolean num1 = Integer.valueOf(matrix[yMin].charAt(xMin)) != 48; //48 is the ASCII code for 0
        boolean num2 = Integer.valueOf(matrix[yMin].charAt(xMax-1)) != 48;
        boolean num3 = Integer.valueOf(matrix[yMax-1].charAt(xMin)) != 48;
        boolean num4 = Integer.valueOf(matrix[yMax-1].charAt(xMax-1)) != 48;
        return num1 || num2 || num3 || num4; //Returns true if any number is  1
    }
    
    /**
     * Short-hand for starting the recursive function in a neat way.
     * @param matrix An array of strings forming a matrix of 1's and 0's 
     * @return The size of the largest submatrix of 1's
     */
    public static int MaxSquare(String[] matrix){
        return MaxSquare(matrix,0,matrix[0].length(),0,matrix.length);
    }

    public static void main(String[] args) {
        System.out.println(MaximalSquare(new String[]{"01001", "11111", "01011", "11111", "01111", "11111"})); //3
        System.out.println(MaximalSquare(new String[]{"10100", "10111", "11111", "10010"})); //2
        System.out.println(MaximalSquare(new String[]{"1111", "1111"})); //2
        System.out.println(MaximalSquare(new String[]{"01001", "11111", "01011", "11011"})); //2
        System.out.println(MaximalSquare(new String[]{"101101", "111111", "010111", "111111"})); //3
        System.out.println(MaximalSquare(new String[]{"101101", "111111", "011111", "111111", "001111", "011111"})); //4
        System.out.println(MaximalSquare(new String[]{"1100", "0101", "0101","0101"})); //1
        System.out.println(MaximalSquare(new String[]{"1111", "1111", "1111","1111"})); //4
        System.out.println(MaximalSquare(new String[]{"1111", "1111", "1111","1110"})); //3
        System.out.println(MaximalSquare(new String[]{"1111", "1111", "1101","1111"})); //2
        System.out.println(MaximalSquare(new String[]{"0111", "1111", "1111", "1111"}));//3
        System.out.println(MaximalSquare(new String[]{"0111", "1101", "0111"})); //1
    }

}
