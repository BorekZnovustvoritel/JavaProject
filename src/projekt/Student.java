package projekt;

import java.util.ArrayList;
import java.time.LocalDate;
/**
 * Tøída objektù Student - žáci školy, odvozeno od tøídy Osoba
 * Obsahuje promìnné:
 * static double dobryPrumer - Prùmìr, od kterého bude studentùm vypláceno stipendium
 * static double stipendium - Odmìna za dobrý prùmìr studenta
 * ArrayList<Double> znamky - Známky, z nichž se vypoèítává prùmìr studenta
 * 
 * Konstruktor Student() využívá konstruktor rodièovské tøídy
 * @author Marek Szymutko
 */
public class Student extends Osoba
{
	private static double dobryPrumer = 1.5;
	private static double stipendium = 2000;
	private ArrayList<Double> znamky = new ArrayList<>();
	/**
	 * Konstruktor
	 * @param jmeno - køestní jméno studenta (String)
	 * @param prijmeni - pøíjmení studenta (String)
	 * @param datumNarozeni - datum narození studenta (java.time.LocalDate)
	 * @param ID - unikátní ID osoby (int)
	 */
	public Student(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		super(jmeno, prijmeni, datumNarozeni, ID);
	}
	/**
	 * Vrací finanèní odmìny studenta v závislosti na prùmìru
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
	 * Získání výpisu známek
	 * @return ArrayList<Double> znamky
	 */
	public ArrayList<Double> getZnamky()
	{
		return this.znamky;
	}
	/**
	 * Získání prùmìru ze známek
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
	 * Vrací true, pokud má student pøidìleno stipendium
	 * @return boolean maStipendium
	 */
	public boolean maStipendium()
	{
		if (this.getOdmena() != 0)
			return true;
		return false;
	}
	/**
	 * Pøidá známku do seznamu studenta, pokud je známka ve vhodném rozsahu
	 * @param znamka (double)
	 */
	public void addZnamka(double znamka)
	{
		if (znamka <= 5 && znamka >= 1)
			this.znamky.add(znamka);
	}
}
