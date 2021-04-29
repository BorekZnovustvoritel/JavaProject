package projekt;

import java.util.ArrayList;
import java.time.LocalDate;
/**
 * T��da objekt� Student - ��ci �koly, odvozeno od t��dy Osoba
 * Obsahuje prom�nn�:
 * static double dobryPrumer - Pr�m�r, od kter�ho bude student�m vypl�ceno stipendium
 * static double stipendium - Odm�na za dobr� pr�m�r studenta
 * ArrayList<Double> znamky - Zn�mky, z nich� se vypo��t�v� pr�m�r studenta
 * 
 * Konstruktor Student() vyu��v� konstruktor rodi�ovsk� t��dy
 * @author Marek Szymutko
 */
public class Student extends Osoba
{
	private static double dobryPrumer = 1.5;
	private static double stipendium = 2000;
	private ArrayList<Double> znamky = new ArrayList<>();
	/**
	 * Konstruktor
	 * @param jmeno - k�estn� jm�no studenta (String)
	 * @param prijmeni - p��jmen� studenta (String)
	 * @param datumNarozeni - datum narozen� studenta (java.time.LocalDate)
	 * @param ID - unik�tn� ID osoby (int)
	 */
	public Student(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		super(jmeno, prijmeni, datumNarozeni, ID);
	}
	/**
	 * Vrac� finan�n� odm�ny studenta v z�vislosti na pr�m�ru
	 * @return double odmena
	 */
	@Override
	public double getOdmena()
	{
		double prumer = this.getPrumer();
		if (prumer <= dobryPrumer && prumer != 0)
			return stipendium;
		return 0;
	}
	/**
	 * Z�sk�n� v�pisu zn�mek
	 * @return ArrayList<Double> znamky
	 */
	public ArrayList<Double> getZnamky()
	{
		return this.znamky;
	}
	/**
	 * Z�sk�n� pr�m�ru ze zn�mek
	 * @return double prumer
	 */
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
	/**
	 * Vrac� true, pokud m� student p�id�leno stipendium
	 * @return boolean maStipendium
	 */
	public boolean maStipendium()
	{
		if (this.getOdmena() != 0)
			return true;
		return false;
	}
	/**
	 * P�id� zn�mku do seznamu studenta, pokud je zn�mka ve vhodn�m rozsahu
	 * @param znamka (double)
	 */
	public void addZnamka(double znamka)
	{
		if (znamka <= 5 && znamka >= 1)
			this.znamky.add(znamka);
	}
}
