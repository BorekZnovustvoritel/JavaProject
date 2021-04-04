package projekt;

import java.time.LocalDate;

public abstract class Osoba
{
	public Osoba(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		this.jmeno = jmeno;
		this.prijmeni = prijmeni;
		this.datum = datumNarozeni;
		this.id = ID;
	}
	private int id;
	private String jmeno;
	private String prijmeni;
	LocalDate datum;
	public abstract double getOdmena();
	public String getJmeno()
	{
		return this.jmeno;
	}
	public void setJmeno(String jmeno)
	{
		this.jmeno = jmeno;
	}
	public String getPrijmeni()
	{
		return this.prijmeni;
	}
	public void setPrijmeni(String prijmeni)
	{
		this.prijmeni = prijmeni;
	}
	public int getID()
	{
		return this.id;
	}
	public LocalDate getDatumNar()
	{
		return this.datum;
	}
}
