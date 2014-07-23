package LatticeDiseaseModel;

public class Individual {

	public final static int infectiousPeriod = 30;
	public char state;
	public int timeStepsSpentInfected;
	public boolean isPrimaryCase;
	
	public Individual(){
		state = 'S'; 
		timeStepsSpentInfected = 0;
		isPrimaryCase = false;
	}
	
	public void infect()
	{
		state = 'I';
	}
	
	public void initialInfection()
	{
		state = 'I';
		isPrimaryCase = true;
	}
	
	public void updateDiseaseState()
	{
		if(timeStepsSpentInfected > infectiousPeriod)
		{
			state = 'R';
		}
		timeStepsSpentInfected++;
	}
	
	public char getDiseaseState(){
		return state;
	}
}
