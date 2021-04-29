package projekt;

import java.time.LocalDate;
import java.util.HashSet;
/**
 * Tøída objektù Ucitel - uèitelé školy, odvozeno od tøídy Osoba
 * Obsahuje promìnné:
 * static double zakladniOdmena - Základní plat kadého uèitele
 * static double odmenaZaStudenta - Pøíplatek za kadého studenta zvláš
 * static double odmenaZaStudentaSeStipendiem - Pøíplatek za kadého studenta se stipendiem
 * static double danZPrijmu - Desetinnı tvar relativní danì z pøíjmu
 * HashSet<Student> studenti - Seznam studentù, které uèitel uèí
 * 
 * Konstruktor Ucitel() vyuívá konstruktor rodièovské tøídy
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
	 * @param jmeno - køestní jméno uèitele (String)
	 * @param prijmeni - pøíjmení uèitele (String)
	 * @param datumNarozeni - datum narození uèitele (java.time.LocalDate)
	 * @param ID - unikátní ID osoby (int)
	 */
	public Ucitel(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		super(jmeno, prijmeni, datumNarozeni, ID);
	}
	/**
	 * Vrací poèet studentù se stipendiem tohoto uèitele
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
	 * Vıpoèet nezdanìné odmìny uèitele pro zjištìní vıdajù školy
	 * @return double hrubaOdmena
	 */
	@Override
	public double getOdmena()
	{
		return zakladniOdmena + studenti.size()*odmenaZaStudenta+odmenaZaStudentaSeStipendiem*getStudentiSeStipendiem();
	}
	/**
	 * Vıpoèet zdanìné odmìny uèitele
	 * @return double cistaOdmena
	 */
	public double getCistaOdmena()
	{
		return (1-danZPrijmu)*(zakladniOdmena + studenti.size()*odmenaZaStudenta+odmenaZaStudentaSeStipendiem*getStudentiSeStipendiem());
	}
	/**
	 * Pøidání studenta k uèiteli
	 * @param student - Student pøidìlenı uèiteli (Student)
	 */
	public void studentDoSeznamu(Student stu)
	{
		this.studenti.add(stu);
	}
	/**
	 * Odebrání studenta od uèitele, vrací true, pokud se operace zdaøila
	 * @param student - Student, kterı má bıt odebrán
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
	 * Získání všech studentù tohoto uèitele
	 * @return HashSet<Student> studenti
	 */
	public HashSet<Student> getStudenti()
	{
		return this.studenti;
	}
}