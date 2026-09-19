package LowLevelDesign.ConnectFour.model;

import LowLevelDesign.ConnectFour.enums.DiscColor;

import java.util.LinkedList;
import java.util.Queue;

public class Board {
    private final int rows;
    private final int columns;
    private final DiscColor[][] grid;
    private final int target;

    public Board(int rows, int columns, int target){
        this.rows = rows;
        this.columns = columns;
        grid = new DiscColor[rows][columns];
        this.target = target;
    }

    public boolean canPlace(int column){
        for(int row=0;row<rows;row++){
            if(grid[row][column]==null){
                return true;
            }
        }
        return false;
    }

    public int placeDisc(int column, DiscColor color){
        for(int row=this.rows-1;row>=0;row--){
            if(grid[row][column]==null){
                grid[row][column] = color;
                return row;
            }
        }
        return -1;
    }

    public boolean isFull(){
        for(int row=0;row<rows;row++){
            for(int column=0;column<columns;column++){
                if(grid[row][column]==null){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWin(int row, int column, DiscColor color){

        int count = 0;
        for(int currRow = 0; currRow<this.rows; currRow++) {
            if (this.grid[currRow][column] == color) count++;
            else count = 0;
            if (count == this.target) {
                return true;
            }
        }
        count = 0;
        for(int currColumn = 0; currColumn<this.columns; currColumn++) {
            if (this.grid[row][currColumn] == color) count++;
            else count = 0;
            if (count == this.target) {
                return true;
            }
        }

        int newRow = row;
        int newColumn = column;
        while(newRow>0 && newColumn>0){
            newRow--;
            newColumn--;
        }
        count = 0;
        while(newRow<this.rows && newColumn<this.columns){
            if (this.grid[newRow][newColumn] == color) count++;
            else count = 0;
            if (count == this.target) {
                return true;
            }
            newRow++;
            newColumn++;
        }

        newRow = row;
        newColumn = column;

        while(newRow>0 && newColumn<this.columns-1){
            newRow--;
            newColumn++;
        }
        count = 0;
        while(newRow<this.rows && newColumn>=0){
            if (this.grid[newRow][newColumn] == color) count++;
            else count = 0;
            if (count == this.target) {
                return true;
            }
            newRow++;
            newColumn--;
        }

        return false;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public DiscColor[][] getGrid() {
        return grid;
    }
}
