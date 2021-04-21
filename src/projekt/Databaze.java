package projekt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
/**
 * T��da slou�� jako objekt datab�ze �koly
 * @author Marek Szymutko
 * Obsahuje prom�nn�:
 * int pocetLidi - ur�uje, jak� je nejv�t�� ID v datab�zi, dal�� �lov�k dostane ID o pozici d�l
 * Map<Integer, Osoba> databaze - do n� se osoby ukl�daj�
 * 
 * Konstruktor t��dy nen� pot�eba, dva atributy t��dy se iniciuj� implicitn�m konstruktorem
 */
public class Databaze
{
	private int pocetLidi = 0;
	private Map<Integer, Osoba> databaze = new HashMap<>();
	/**
	 * Vr�t� po�et lid� a z�rove� tento po�et lid� aktualizuje ve vlastn� prom�nn�
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
	 * V�pis osob z HashMapy datab�ze v podob� HashSetu
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
	 * P�id�n� nov�ho studenta do datab�ze
	 * @param jmeno (String)
	 * @param prijmeni (String)
	 * @param datumNarozeni (java.time.LocalDate)
	 * @param ucit - u�itel� tohoto studenta (HashSet<Ucitel>)
	 * @return int pocetLidi - z�rove� ID nov� p�idan� osoby
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
	 * Na�ten� studenta z SQL - li�� se ji� existuj�c�m ID, kter� negenerujeme
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
	 * P�id�n� nov�ho u�itele do datab�ze
	 * @param jmeno (String)
	 * @param prijmeni (String)
	 * @param datumNarozeni (java.time.LocalDate)
	 * @return int pocetLidi - z�rove� ID nov� p�idan� osoby
	 */
	public int addUcitel(String jmeno, String prijmeni, LocalDate datumNarozeni)
	{
		Ucitel ucit = new Ucitel(jmeno, prijmeni, datumNarozeni, ++pocetLidi);
		databaze.put(pocetLidi, ucit);
		return pocetLidi;
	}
	/**
	 * Na�ten� u�itele z SQL - li�� se ji� existuj�c�m ID, kter� negenerujeme
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
	 * Vr�t� objekt osoby z datab�ze podle ID
	 * Vrac� null, pokud osoba nen� v datab�zi
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
	 * Sma�e osobu z datab�ze, vr�t� true, pokud se operace povedla
	 * @param id - id osoby ke smaz�n� (int)
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
	 * Vytiskne celou datab�zi podle ID
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
			System.out.println("ID: "+temp.getID()+", jm�no a p��jmen�: "+temp.getJmeno()+" "+temp.getPrijmeni());
		}
		if (i==0)
			System.out.println("Datab�ze je pr�zdn�.");
	}
	/**
	 * Vr�t� u�itele studenta
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
	 * Nelze p�id�vat ��ky, pokud nejsou v datab�zi u�itel�, �e��me to kontrolou pomoc� t�to metody
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
	 * Vytiskne pouze u�itele se�azen� podle po�tu student�
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
			System.out.println("��dn� u�itel� k zobrazen�");
		for (Ucitel u: ucitele)
		{
			System.out.println("ID: "+u.getID()+", "+u.getJmeno()+" "+u.getPrijmeni()+", po�et student�: "+u.getStudenti().size());
		}
	}
	/**
	 * Vytiskne v�echny osoby abecedn�
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
		System.out.println("U�itel�:");
		if (ucitele.isEmpty())
			System.out.println("��dn� u�itel� k zobrazen�");
		for (Ucitel u: ucitele)
		{
			System.out.println("ID: "+u.getID()+", "+u.getJmeno()+" "+u.getPrijmeni()+", nar.: "+u.getDatumNar()+", �ist� odm�na: "+u.getCistaOdmena());
		}
		System.out.println("Studenti:");
		if (studenti.isEmpty())
			System.out.println("��dn� studenti k zobrazen�");
		for (Student s: studenti)
		{
			System.out.println("ID: "+s.getID()+", "+s.getJmeno()+" "+s.getPrijmeni()+", nar.: "+s.getDatumNar()+", stipendia"
					+ ": "+s.getOdmena());
		}
	}
	/**
	 * Tiskne jen studenty, kter� dan� u�itel vyu�uje
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
			System.out.println("��dn� studenti k zobrazen�");
		for (StudentPrumerem s: studentiRazeni)
		{
			System.out.format("ID: "+s.getID()+", "+s.getJmeno()+" "+s.getPrijmeni()+", prumer: %.2f\n", s.getPrumer());	
		}
	}
	/**
	 * Vr�t� hodnotu sou�tu v�ech hrub�ch odm�n u�itel�
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
	 * Vr�t� hodnotu sou�tu v�ech stipendi� student�
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
	 * Smaz�n� datab�ze
	 */
	public void reset()
	{
		this.databaze = new HashMap<Integer, Osoba>();
		this.pocetLidi = 0;
	}
}
