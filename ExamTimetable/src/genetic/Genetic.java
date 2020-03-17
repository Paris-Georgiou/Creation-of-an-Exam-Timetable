package genetic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Genetic
{
	private ArrayList<Chromosome> population;
	private ArrayList<Integer> fitnessBounds;
	
	public Genetic()
	{
		this.population = null;
		this.fitnessBounds = null;
	}

    /* 
     * populationSize: The size of the population in every step
     * mutationPropability: The propability a mutation might occur in a chromosome
     * minimumFitness: The minimum fitness value of the solution we wish to find
     * maximumSteps: The maximum number of steps we will search for a solution
     */
	public Chromosome geneticAlgorithm(int populationSize, double mutationProbability, int minimumFitness, int maximumSteps)
	{
		initializePopulation(populationSize);
		Random r = new Random();
		for(int step=0; step < maximumSteps; step++)
		{
            //Initialize the new generated population
			ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
			for(int i=0; i < populationSize; i++)
			{
				int fitSize = fitnessBounds.size();
				int xIndex = fitnessBounds.get(r.nextInt(fitSize));
				Chromosome x = population.get(xIndex);
				int yIndex = fitnessBounds.get(r.nextInt(fitSize));
				while(yIndex == xIndex)
				{
					yIndex = fitnessBounds.get(r.nextInt(fitSize));
				}
				Chromosome y = population.get(yIndex);
                //We generate the "child" of the two chromosomes
				Chromosome child = x.reproduce(y);
                //We might then mutate the child
				if(r.nextDouble() < mutationProbability)
				{
					child.mutate();
				}
                //...and finally add it to the new population
				newPopulation.add(child);
			}
			population = new ArrayList<Chromosome>(newPopulation);

            //We sort the population so the one with the greater fitness is first
			Collections.sort(population, Collections.reverseOrder());
            
			if(population.get(0).getFitness() >= minimumFitness) {
                System.out.println("Finished after " + step + " steps...");
				return population.get(0);
			}
            
			updateFitnessBounds();
		}

        System.out.println("Finished after " + maximumSteps + " steps...");
		return population.get(0);
	}

	public void initializePopulation(int populationSize)
	{
		population = new ArrayList<Chromosome>();
		for(int i=0; i<populationSize; i++)	{
			population.add(new Chromosome());
		}
		updateFitnessBounds();
	}
  
	public void updateFitnessBounds()
	{
		fitnessBounds = new ArrayList<Integer>();
		for (int i=0; i<population.size(); i++) {
			for(int j=0; j<population.get(i).getFitness(); j++) {
				fitnessBounds.add(i);
			}
		}
	}

    
}
