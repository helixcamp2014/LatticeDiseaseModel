package LatticeDiseaseModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * should this class be immutable?
 * memory vs. time vs. parallelism considerations
 * assumes square array
 * 
 */

///TODO: add in timestep field
//TODO: add method to initialize by string
//TODO: add method to compute next state use conway rules

/*
 * right now interface is tied to internal representation -> how to generalize?
 * should this be a generic class?
 * 
 */
public class LatticeModel {
private Individual lattice[][]; //TODO: stores an int should this be an enum, or class?
private int rows;
private int columns;
private int currentStep;
private final double beta = 0.2;//this is a paramter that we will calibrate the model with


	public LatticeModel(int rows, int columns){
		currentStep = 0;
		if(rows <= 0 || columns <= 0)
		{
			throw new IllegalArgumentException("rows and columns must both be nonzero") ;
		}
		lattice = new Individual[rows][columns];//assume Java initializes to 0
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				lattice[row][column] = new Individual();
			}
		}	
		this.rows = rows;
		this.columns = columns;
	};
	
	public LatticeModel(Individual [][] initialState){
		//GameOfLife(initialState.length, initialState[0].length); - can't chain ctor calls apparently
		currentStep = 0;
		this.rows = initialState.length;
		this.columns = initialState[0].length;
		lattice = new Individual[rows][columns];
		
		if(rows <= 0 || columns <= 0 || rows != columns )
		{
			throw new IllegalArgumentException("rows and columns must both be nonzero and equal length. rows: "+rows+" , columns: "+columns);
		}
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				lattice[row][column] = new Individual();
			}
		}	
		
	};
	
	public void reset()
	{
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				lattice[row][column] = new Individual();
			}
		}	
		currentStep = 0;
	}
	
	public void loadStateFromFile(File cellsFile)
	{
		BufferedReader reader = null;
		
		reset();

		try {
		    reader = new BufferedReader(new FileReader(cellsFile));
		    String text = null;
		    int rowIdx = 0;

		    while ((text = reader.readLine()) != null) {
		        //parse text string
		    	if(text.charAt(0) != '!'){//else is a comment
		    		
		    		for(int columnIdx = 0; columnIdx < text.length(); columnIdx ++)
		    		{
		    			if(columnIdx < columns && rowIdx < rows) 
		    			{
		    				if(text.charAt(columnIdx) == 'S')
		    				{
		    					lattice[rowIdx][columnIdx] = new Individual();
		    				} 
		    				else if(text.charAt(columnIdx) == 'I')
		    				{
		    					lattice[rowIdx][columnIdx] = new Individual();
		    					lattice[rowIdx][columnIdx].infect();
		    				}
		    			}
		    		}
		    		rowIdx++;
		    	}
		    	
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
		
	}
	
	public Individual getStateAt(int row, int column)
	{
		return lattice[row][column];
	}
	
	
	/*
	 * return the size of 1st dimension (number of rows)
	 */
	public int getRows()
	{
		return rows;
	}
	/*
	 * return the size of 2nd dimension (number of columns)
	 */
	public int getColumns()
	{
		return columns;
	}
	/*
	 * since Java returns by reference here, this is leaking internals
	 * what can we do about it?
	 */
	
	public Individual[][] getCurrentState()
	{
		return lattice;
	}
	
	public int getCurrentStep()
	{
		return currentStep;
	}
	
	public void computeNextState(){
		//Individual nextState [][] = new Individual[rows][columns];
		
		//perform "concurrent" computation of next state
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				applyRule(row,column,lattice);
			}
		}		
		
		//replace lattice with next state
		//lattice = nextState;		
		currentStep++;
	}
	
	
	private void applyRule(int row, int column, Individual [][] a_lattice)
	{
		/*
		 * TODO: clever way to do this with iteration
		 * 
		 */
		
		if(a_lattice == null)
		{
			System.out.println("lattice was null in applyRule");
		}
		//placeholder
		Individual currentIndividual = a_lattice[row][column];
		if(currentIndividual == null)
		{
			System.out.println("Individual was null in applyRule");
		}
		if(currentIndividual.state == 'I')
		{
			//infect neighbours
			Individual neighbour = getLeft(row,column,a_lattice);
			if(Math.random() < beta)
			{
				//transmission happens
				neighbour.infect();
				
			}
			//update the time spent infected
			//if this time exceeds the infectious period then turn Removed ('R')
			currentIndividual.updateDiseaseState();
			
			neighbour = getRight(row,column,a_lattice);
			if(Math.random() < beta)
			{
				//transmission happens
				neighbour.infect();
				
			}
			//update the time spent infected
			//if this time exceeds the infectious period then turn Removed ('R')
			currentIndividual.updateDiseaseState();
			neighbour = getDown(row,column,a_lattice);
			if(Math.random() < beta)
			{
				//transmission happens
				neighbour.infect();
				
			}
			//update the time spent infected
			//if this time exceeds the infectious period then turn Removed ('R')
			currentIndividual.updateDiseaseState();
			neighbour = getUp(row,column,a_lattice);
			if(Math.random() < beta)
			{
				//transmission happens
				neighbour.infect();
				
			}
			//update the time spent infected
			//if this time exceeds the infectious period then turn Removed ('R')
			currentIndividual.updateDiseaseState();
		}		
	}
	
	//TODO: validate arguments
	private Individual getLeft(int row, int column, Individual [][] a_lattice)
	{
		if(column ==0)
		{
			return a_lattice[row][a_lattice[row].length - 1];
		}
		else
		{
			return a_lattice[row][column - 1];
		}
	}
	
	private Individual getRight(int row, int column, Individual [][] a_lattice)
	{
		if(column == a_lattice[row].length - 1)
		{
			return a_lattice[row][0];
		}
		else
		{
			return a_lattice[row][column + 1]; 
		}
		
	}
	
	private Individual getUp(int row, int column, Individual [][] a_lattice)
	{
		if(row == 0)
		{
			return a_lattice[a_lattice.length - 1][column];
		}
		else
		{
			return a_lattice[row-1][column]; 
		}
		
	}
	
	private Individual getDown(int row, int column, Individual [][] a_lattice)
	{
		if(row == a_lattice.length - 1)
		{
			return a_lattice[0][column];
		}
		else
		{
			return a_lattice[row+1][column]; 
		}
		
	}
	
	private Individual getUpRight(int row, int column, Individual [][] a_lattice)
	{
		if(row == 0)
		{
		
			if(column == a_lattice[row].length - 1)
			{
				return a_lattice[a_lattice.length - 1][0];
			}
			else
			{
				return a_lattice[a_lattice.length - 1][column + 1]; 
			}
			
		}
		else
		{
			
			if(column == a_lattice[row].length - 1)
			{
				return a_lattice[row-1][0];
			}
			else
			{
				return a_lattice[row-1][column + 1]; 
			}
		}
	
	}
	private Individual getUpLeft(int row, int column, Individual [][] a_lattice)
	{
		if(row == 0)
		{
		
			if(column ==0)
			{
				
				return a_lattice[a_lattice.length - 1][a_lattice[row].length - 1];
			}
			else
			{
				return a_lattice[a_lattice.length - 1][column - 1]; 
			}
			
		}
		else
		{
			
			if(column == 0)
			{
				return a_lattice[row-1][a_lattice[row].length - 1];
			}
			else
			{
				return a_lattice[row-1][column - 1]; 
			}
		}
	
	}
	private Individual getDownRight(int row, int column, Individual [][] a_lattice)
	{
		if(row == a_lattice.length - 1)
		{
		
			if(column == a_lattice[row].length - 1)
			{
				return a_lattice[0][0];
			}
			else
			{
				return a_lattice[0][column + 1]; 
			}
			
		}
		else
		{
			
			if(column == a_lattice[row].length - 1)
			{
				return a_lattice[row+1][0];
			}
			else
			{
				return a_lattice[row+1][column + 1]; 
			}
		}
	
	}
	private Individual getDownLeft(int row, int column, Individual [][] a_lattice)
	{
		if(row == a_lattice.length - 1)
		{
		
			if(column == 0)
			{
				return a_lattice[0][a_lattice[row].length - 1];
			}
			else
			{
				return a_lattice[0][column - 1]; 
			}
			
		}
		else
		{
			
			if(column == 0)
			{
				return a_lattice[row+1][a_lattice[row].length - 1];
			}
			else
			{
				return a_lattice[row+1][column - 1]; 
			}
		}
	
	}
	public String toString()
	{
		String result = "";
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				result += getStateAt(row, column);
			}
			result += "\n";
		}
		return result;
	}
	
	
}