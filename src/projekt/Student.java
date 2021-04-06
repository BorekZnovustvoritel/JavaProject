package projekt;

import java.util.ArrayList;
import java.time.LocalDate;

public class Student extends Osoba
{
	private static double dobryPrumer = 1.5;
	private static double stipendium = 2000;
	private ArrayList<Double> znamky = new ArrayList<>();
	public Student(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		super(jmeno, prijmeni, datumNarozeni, ID);
	}
	@Override
	public double getOdmena()
	{
		double prumer = this.getPrumer();
		if (prumer <= dobryPrumer && prumer != 0)
			return stipendium;
		return 0;
	}
	
	public ArrayList<Double> getZnamky()
	{
		return this.znamky;
	}
	
	public double getPrumer()
	{
		double soucet = 0;
		int pocet = 0;
		for (double znamka: this.znamky)
		{
			pocet++;
			soucet += znamka;
		}
		if (pocet == 0)
			return 0;
		return (soucet/pocet);
	}
	

	public boolean maStipendium()
	{
		if (this.getOdmena() != 0)
			return true;
		return false;
	}
	public void addZnamka(double znamka)
	{
		this.znamky.add(znamka);
	}
	
}
