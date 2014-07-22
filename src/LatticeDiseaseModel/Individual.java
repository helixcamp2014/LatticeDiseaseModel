package LatticeDiseaseModel;

public class Individual {

	public final static int infectiousPeriod = 20;
	public char state;
	public int timeStepsSpentInfected;
	
	public Individual(){
		state = 'S'; 
		timeStepsSpentInfected = 0;
	}
	
	public void infect()
	{
		state = 'I';
	}
	
	public void updateDiseaseState()
	{
		if(timeStepsSpentInfected > infectiousPeriod)
		{
			state = 'R';
		}
		timeStepsSpentInfected++;
	}
}
