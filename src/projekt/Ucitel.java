package projekt;

import java.time.LocalDate;
import java.util.HashSet;

public class Ucitel extends Osoba
{
	private static double zakladniOdmena = 20000;
	private static double odmenaZaStudenta = 100;
	private static double odmenaZaStudentaSeStipendiem = 500;
	private static double danZPrijmu = 0.15;
	private HashSet<Student> studenti = new HashSet<>();
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
	public Ucitel(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		super(jmeno, prijmeni, datumNarozeni, ID);
	}
	@Override
	public double getOdmena()
	{
		return zakladniOdmena + studenti.size()*odmenaZaStudenta+odmenaZaStudentaSeStipendiem*getStudentiSeStipendiem();
	}
	public double getCistaOdmena()
	{
		return (1-danZPrijmu)*(zakladniOdmena + studenti.size()*odmenaZaStudenta+odmenaZaStudentaSeStipendiem*getStudentiSeStipendiem());
	}
	
	public void studentDoSeznamu(Student stu)
	{
		this.studenti.add(stu);
	}
	public boolean removeStudent(Student stu)
	{
		if (this.studenti.contains(stu))
		{
			this.studenti.remove(stu);
			return true;
		}
		return false;
	}
	public HashSet<Student> getStudenti()
	{
		return this.studenti;
	}
}