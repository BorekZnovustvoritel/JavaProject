package projekt;

import java.time.LocalDate;
import java.util.HashSet;
/**
 * T��da objekt� Ucitel - u�itel� �koly, odvozeno od t��dy Osoba
 * Obsahuje prom�nn�:
 * static double zakladniOdmena - Z�kladn� plat ka�d�ho u�itele
 * static double odmenaZaStudenta - P��platek za ka�d�ho studenta zvl᚝
 * static double odmenaZaStudentaSeStipendiem - P��platek za ka�d�ho studenta se stipendiem
 * static double danZPrijmu - Desetinn� tvar relativn� dan� z p��jmu
 * HashSet<Student> studenti - Seznam student�, kter� u�itel u��
 * 
 * Konstruktor Ucitel() vyu��v� konstruktor rodi�ovsk� t��dy
 * @author Marek Szymutko
 */
public class Ucitel extends Osoba
{
	private static double zakladniOdmena = 20000;
	private static double odmenaZaStudenta = 100;
	private static double odmenaZaStudentaSeStipendiem = 500;
	private static double danZPrijmu = 0.15;
	private HashSet<Student> studenti = new HashSet<>();
	/**
	 * Konstruktor
	 * @param jmeno - k�estn� jm�no u�itele (String)
	 * @param prijmeni - p��jmen� u�itele (String)
	 * @param datumNarozeni - datum narozen� u�itele (java.time.LocalDate)
	 * @param ID - unik�tn� ID osoby (int)
	 */
	public Ucitel(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		super(jmeno, prijmeni, datumNarozeni, ID);
	}
	/**
	 * Vrac� po�et student� se stipendiem tohoto u�itele
	 * @return int pocetStudentuSeStipendiem
	 */
	private int getStudentiSeStipendiem() 
	{
		int pocet = 0;
		for (Student stu: studenti)
		{
			if (stu.maStipendium())
				pocet++;
		}
		return pocet;
	}
	/**
	 * V�po�et nezdan�n� odm�ny u�itele pro zji�t�n� v�daj� �koly
	 * @return double hrubaOdmena
	 */
	@Override
	public double getOdmena()
	{
		return zakladniOdmena + studenti.size()*odmenaZaStudenta+odmenaZaStudentaSeStipendiem*getStudentiSeStipendiem();
	}
	/**
	 * V�po�et zdan�n� odm�ny u�itele
	 * @return double cistaOdmena
	 */
	public double getCistaOdmena()
	{
		return (1-danZPrijmu)*(zakladniOdmena + studenti.size()*odmenaZaStudenta+odmenaZaStudentaSeStipendiem*getStudentiSeStipendiem());
	}
	/**
	 * P�id�n� studenta k u�iteli
	 * @param student - Student p�id�len� u�iteli (Student)
	 */
	public void studentDoSeznamu(Student stu)
	{
		this.studenti.add(stu);
	}
	/**
	 * Odebr�n� studenta od u�itele, vrac� true, pokud se operace zda�ila
	 * @param student - Student, kter� m� b�t odebr�n
	 * @return boolean operaceSeZdarila
	 */
	public boolean removeStudent(Student stu)
	{
		if (this.studenti.contains(stu))
		{
			this.studenti.remove(stu);
			return true;
		}
		return false;
	}
	/**
	 * Z�sk�n� v�ech student� tohoto u�itele
	 * @return HashSet<Student> studenti
	 */
	public HashSet<Student> getStudenti()
	{
		return this.studenti;
	}
}