package projekt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Databaze
{
	private int pocetLidi = 0;
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
	public void setPocetLidi(int pocet)
	{
		this.pocetLidi = pocet;
	}
	private Map<Integer, Osoba> databaze = new HashMap<>(); 
	
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
	public int addStudent(String jmeno, String prijmeni, LocalDate datumNarozeni, HashSet<Ucitel> ucit)
	{
		Student stud = new Student(jmeno, prijmeni, datumNarozeni, ++pocetLidi);
		this.databaze.put(pocetLidi, stud);
		for (Ucitel u: ucit)
			u.studentDoSeznamu(stud);
		return pocetLidi;
	}
	public void addStudentSQL(int id, String jmeno, String prijmeni, LocalDate datumNarozeni)
	{
		Student stud = new Student(jmeno, prijmeni, datumNarozeni, id);
		this.databaze.put(id, stud);
	}
	public int addUcitel(String jmeno, String prijmeni, LocalDate datumNarozeni)
	{
		Ucitel ucit = new Ucitel(jmeno, prijmeni, datumNarozeni, ++pocetLidi);
		databaze.put(pocetLidi, ucit);
		return pocetLidi;
	}
	public void addUcitelSQL(int id, String jmeno, String prijmeni, LocalDate datumNarozeni)
	{
		Ucitel ucit = new Ucitel(jmeno, prijmeni, datumNarozeni, id);
		this.databaze.put(id, ucit);
	}
	public Osoba getOsoba(int id)
	{
		if (databaze.containsKey(id))
			return databaze.get(id);
		return null;
	}
	public boolean remove(int id)
	{
		if (databaze.containsKey(id))
		{
			databaze.remove(id);
			return true;
		}
		return false;
	}
	public void tiskDatabaze()
	{
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Student) 
				System.out.print("Student ");
			else
				System.out.print("Ucitel ");
			System.out.println("ID: "+temp.getID()+", jmeno a prijmeni: "+temp.getJmeno()+" "+temp.getPrijmeni());
		}
	}
	HashSet<Ucitel> getUciteleZaka(Student stu)
	{
		HashSet<Ucitel> ucitele = new HashSet<>();
		Iterator<Integer> iter = databaze.keySet().iterator();
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			if (temp instanceof Ucitel)
			{
				if (((Ucitel) temp).studenti.contains(stu))
					ucitele.add((Ucitel)temp);
			}
		}
		return ucitele;
	}
	boolean existujiUcitele()
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
		for (Ucitel u: ucitele)
		{
			System.out.println("ID: "+u.getID()+", "+u.getJmeno()+" "+u.getPrijmeni()+", pocet studentu: "+u.studenti.size());
		}
	}

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
		System.out.println("Ucitele:");
		for (Ucitel u: ucitele)
		{
			System.out.println("ID: "+u.getID()+", "+u.getJmeno()+" "+u.getPrijmeni()+", nar.: "+u.getDatumNar()+", cista odmena: "+u.getCistaOdmena());
		}
		System.out.println("Studenti:");
		for (Student s: studenti)
		{
			System.out.println("ID: "+s.getID()+", "+s.getJmeno()+" "+s.getPrijmeni()+", nar.: "+s.getDatumNar()+", stipendia"
					+ ": "+s.getOdmena());
		}
	}
	public void tiskStudentuUcitele(Ucitel u)
	{
		ArrayList<StudentPrumerem> studentiRazeni = new ArrayList<>();
		for (Student s: (u).studenti)
		{	
			StudentPrumerem tempS = new StudentPrumerem(s);
			studentiRazeni.add(tempS);
		}
		Collections.sort(studentiRazeni);
		for (StudentPrumerem s: studentiRazeni)
		{
			System.out.format("ID: "+s.getID()+", "+s.getJmeno()+" "+s.getPrijmeni()+", prumer: %.2f\n", s.getPrumer());	
		}
	}
	public double vydaje()
	{
		Iterator<Integer> iter = databaze.keySet().iterator();
		double soucet = 0;
		while (iter.hasNext())
		{
			int klic = iter.next();
			Osoba temp = databaze.get(klic);
			soucet += temp.getOdmena();
		}
		return soucet;
	}
	public void reset()
	{
		this.databaze = new HashMap<Integer, Osoba>();
		this.pocetLidi = 0;
	}
}
