package projekt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
/**
 * Tøída slouží jako objekt databáze školy
 * @author Marek Szymutko
 * Obsahuje promìnné:
 * int pocetLidi - urèuje, jaké je nejvìtší ID v databázi, další èlovìk dostane ID o pozici dál
 * Map<Integer, Osoba> databaze - do ní se osoby ukládají
 * 
 * Konstruktor tøídy není potøeba, dva atributy tøídy se iniciují implicitním konstruktorem
 */
public class Databaze
{
	private int pocetLidi = 0;
	private Map<Integer, Osoba> databaze = new HashMap<>();
	/**
	 * Vrátí poèet lidí a zároveò tento poèet lidí aktualizuje ve vlastní promìnné
	 * @return int pocetLidi
	 */
	public int getPocetLidi()
	{
		int pocet = 0;
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int temp = iter.next();
			if (temp > pocet)
			pocet = temp;
		}
		this.pocetLidi = pocet;
		return pocet;
	}
	/**
	 * Výpis osob z HashMapy databáze v podobì HashSetu
	 * @return HashSet<Osoba> lidi
	 */
	public HashSet<Osoba> getLidi()
	{
		HashSet<Osoba> result = new HashSet<>();
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			result.add(temp);
		}
		return result;
	}
	/**
	 * Pøidání nového studenta do databáze
	 * @param jmeno (String)
	 * @param prijmeni (String)
	 * @param datumNarozeni (java.time.LocalDate)
	 * @param ucit - uèitelé tohoto studenta (HashSet<Ucitel>)
	 * @return int pocetLidi - zároveò ID novì pøidané osoby
	 */
	public int addStudent(String jmeno, String prijmeni, LocalDate datumNarozeni, HashSet<Ucitel> ucit)
	{
		Student stud = new Student(jmeno, prijmeni, datumNarozeni, ++pocetLidi);
		this.databaze.put(pocetLidi, stud);
		for (Ucitel u: ucit)
			u.studentDoSeznamu(stud);
		return pocetLidi;
	}
	/**
	 * Naètení studenta z SQL - liší se již existujícím ID, které negenerujeme
	 * @param id (int)
	 * @param jmeno (String)
	 * @param prijmeni (String)
	 * @param datumNarozeni (java.time.LocalDate)
	 */
	public void addStudentSQL(int id, String jmeno, String prijmeni, LocalDate datumNarozeni)
	{
		Student stud = new Student(jmeno, prijmeni, datumNarozeni, id);
		this.databaze.put(id, stud);
	}
	/**
	 * Pøidání nového uèitele do databáze
	 * @param jmeno (String)
	 * @param prijmeni (String)
	 * @param datumNarozeni (java.time.LocalDate)
	 * @return int pocetLidi - zároveò ID novì pøidané osoby
	 */
	public int addUcitel(String jmeno, String prijmeni, LocalDate datumNarozeni)
	{
		Ucitel ucit = new Ucitel(jmeno, prijmeni, datumNarozeni, ++pocetLidi);
		databaze.put(pocetLidi, ucit);
		return pocetLidi;
	}
	/**
	 * Naètení uèitele z SQL - liší se již existujícím ID, které negenerujeme
	 * @param id (int)
	 * @param jmeno (String)
	 * @param prijmeni (String)
	 * @param datumNarozeni (java.time.LocalDate)
	 */
	public void addUcitelSQL(int id, String jmeno, String prijmeni, LocalDate datumNarozeni)
	{
		Ucitel ucit = new Ucitel(jmeno, prijmeni, datumNarozeni, id);
		this.databaze.put(id, ucit);
	}
	/**
	 * Vrátí objekt osoby z databáze podle ID
	 * Vrací null, pokud osoba není v databázi
	 * @param id (int)
	 * @return Osoba osoba
	 */
	public Osoba getOsoba(int id)
	{
		if (databaze.containsKey(id))
			return databaze.get(id);
		return null;
	}
	/**
	 * Smaže osobu z databáze, vrátí true, pokud se operace povedla
	 * @param id - id osoby ke smazání (int)
	 * @return boolean osobaSmazana
	 */
	public boolean remove(int id)
	{
		if (databaze.containsKey(id))
		{
			databaze.remove(id);
			return true;
		}
		return false;
	}
	/**
	 * Vytiskne celou databázi podle ID
	 */
	public void tiskDatabaze()
	{
		Iterator<Integer> iter = databaze.keySet().iterator();
		int i = 0;
		while (iter.hasNext())
		{
			i++;
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Student) 
				System.out.print("Student ");
			else
				System.out.print("Ucitel ");
			System.out.println("ID: "+temp.getID()+", jméno a pøíjmení: "+temp.getJmeno()+" "+temp.getPrijmeni());
		}
		if (i==0)
			System.out.println("Databáze je prázdná.");
	}
	/**
	 * Vrátí uèitele studenta
	 * @param student (Student)
	 * @return HashSet<Ucitel> uciteleStudenta
	 */
	public HashSet<Ucitel> getUciteleZaka(Student stu)
	{
		HashSet<Ucitel> ucitele = new HashSet<>();
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Ucitel)
			{
				if (((Ucitel) temp).getStudenti().contains(stu))
					ucitele.add((Ucitel)temp);
			}
		}
		return ucitele;
	}
	/**
	 * Nelze pøidávat žáky, pokud nejsou v databázi uèitelé, øešíme to kontrolou pomocí této metody
	 * @return boolean existujiUcitele
	 */
	public boolean existujiUcitele()
	{
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int klic = iter.next();
			if (databaze.get(klic) instanceof Ucitel)
				return true;
		}
		return false;
	}
	/**
	 * Vytiskne pouze uèitele seøazené podle poètu studentù
	 * @see UcitelPoctemStudentu
	 */
	public void tiskUcitelu()
	{
		
		ArrayList<UcitelPoctemStudentu> ucitele = new ArrayList<>();
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Ucitel)
			{
				ucitele.add(new UcitelPoctemStudentu((Ucitel)temp));
			}
		}
		Collections.sort(ucitele);
		if (ucitele.isEmpty())
			System.out.println("Žádní uèitelé k zobrazení");
		for (Ucitel u: ucitele)
		{
			System.out.println("ID: "+u.getID()+", "+u.getJmeno()+" "+u.getPrijmeni()+", poèet studentù: "+u.getStudenti().size());
		}
	}
	/**
	 * Vytiskne všechny osoby abecednì
	 * @see UciteleAbecedne
	 * @see StudentiAbecedne
	 */
	public void tiskVsichniAbecedne()
	{
		ArrayList<UcitelAbecedne> ucitele = new ArrayList<>();
		ArrayList<StudentAbecedne> studenti = new ArrayList<>();
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Ucitel)
			{
				ucitele.add(new UcitelAbecedne((Ucitel)temp));
			}
			else if (temp instanceof Student)
			{
				studenti.add(new StudentAbecedne((Student)temp));
			}
		}
		Collections.sort(ucitele);
		Collections.sort(studenti);
		System.out.println("Uèitelé:");
		if (ucitele.isEmpty())
			System.out.println("Žádní uèitelé k zobrazení");
		for (Ucitel u: ucitele)
		{
			System.out.println("ID: "+u.getID()+", "+u.getJmeno()+" "+u.getPrijmeni()+", nar.: "+u.getDatumNar()+", èistá odmìna: "+u.getCistaOdmena());
		}
		System.out.println("Studenti:");
		if (studenti.isEmpty())
			System.out.println("Žádní studenti k zobrazení");
		for (Student s: studenti)
		{
			System.out.println("ID: "+s.getID()+", "+s.getJmeno()+" "+s.getPrijmeni()+", nar.: "+s.getDatumNar()+", stipendia"
					+ ": "+s.getOdmena());
		}
	}
	/**
	 * Tiskne jen studenty, které daný uèitel vyuèuje
	 * @param ucitel (Ucitel)
	 */
	public void tiskStudentuUcitele(Ucitel u)
	{
		ArrayList<StudentPrumerem> studentiRazeni = new ArrayList<>();
		for (Student s: (u).getStudenti())
		{	
			StudentPrumerem tempS = new StudentPrumerem(s);
			studentiRazeni.add(tempS);
		}
		Collections.sort(studentiRazeni);
		if (studentiRazeni.isEmpty())
			System.out.println("Žádní studenti k zobrazení");
		for (StudentPrumerem s: studentiRazeni)
		{
			System.out.format("ID: "+s.getID()+", "+s.getJmeno()+" "+s.getPrijmeni()+", prumer: %.2f\n", s.getPrumer());	
		}
	}
	/**
	 * Vrátí hodnotu souètu všech hrubých odmìn uèitelù
	 * @return double soucet
	 */
	public double vydajeZaUcitele()
	{
		Iterator<Integer> iter = databaze.keySet().iterator();
		double soucet = 0;
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Ucitel)
				soucet += temp.getOdmena();
		}
		return soucet;
	}
	/**
	 * Vrátí hodnotu souètu všech stipendií studentù
	 * @return double soucet
	 */
	public double vydajeZaStudenty()
	{
		Iterator<Integer> iter = databaze.keySet().iterator();
		double soucet = 0;
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Student)
				soucet += temp.getOdmena();
		}
		return soucet;
	}
	/**
	 * Smazání databáze
	 */
	public void reset()
	{
		this.databaze = new HashMap<Integer, Osoba>();
		this.pocetLidi = 0;
	}
}
