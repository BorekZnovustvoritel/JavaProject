package projekt;

import java.time.LocalDate;
/**
 * T��da slou�� jako obecn� abstraktn� forma pro objekty typu Student a Ucitel
 * @author Marek Szymutko
 * Obsahuje prom�nn�:
 * int id - pomoc� n�j se osoba ukl�d� do Java Mapy a SQL datab�ze
 * String jmeno, String prijmeni
 * LocalDate datum - datum narozen�
 * 
 * Konstruktor Osoba() pro lep�� implementaci d�di�n�ch konstruktor�
 * abstract double getOdmena() mus� b�t implementov�na d�di�n�mi t��dami
 */
public abstract class Osoba
{
	private int id;
	private String jmeno;
	private String prijmeni;
	LocalDate datum;
	/**
	 * Konstruktor pro implementaci konstruktor� d�di�n�ch t��d
	 * @param jmeno - jm�no �lov�ka (String)
	 * @param prijmeni - p��jmen� �lov�ka (String)
	 * @param datumNarozeni - datum narozen� (java.time.LocalDate)
	 * @param ID - jedine�n� ID (int)
	 */
	public Osoba(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		this.jmeno = jmeno;
		this.prijmeni = prijmeni;
		this.datum = datumNarozeni;
		this.id = ID;
	}
	/**
	 * Nutno implementovat v d�di�n�ch t��d�ch
	 * @return double odmena
	 */
	public abstract double getOdmena();
	/**
	 * Vrac� pouze k�estn� jm�no objektu typu Osoba
	 * @return String jmeno
	 */
	public String getJmeno()
	{
		return this.jmeno;
	}
	/**
	 * Metoda nebyla vyu�ita, ale pro p��padn� roz���en� aplikace je vhodn� ji m�t (lid� se mohou p�ejmenovat a nechat si ID)
	 * @param jmeno - jm�no pro nahr�n� do objektu (String)
	 */
	public void setJmeno(String jmeno)
	{
		this.jmeno = jmeno;
	}
	/**
	 * Vrac� pouze p��jmen� objektu typu Osoba
	 * @return String prijmeni
	 */
	public String getPrijmeni()
	{
		return this.prijmeni;
	}
	/**
	 * Metoda nebyla vyu�ita, ale pro p��padn� roz���en� aplikace je vhodn� ji m�t (lid� se mohou p�ejmenovat a nechat si ID)
	 * @param prijmeni - p��jmen� pro nahr�n� do objektu (String)
	 */
	public void setPrijmeni(String prijmeni)
	{
		this.prijmeni = prijmeni;
	}
	/**
	 * Metoda vrac� identifika�n� ��slo objektu Osoba
	 * @return int ID
	 */
	public int getID()
	{
		return this.id;
	}
	/**
	 * Metoda vrac� datum narozen� objektu
	 * @return LocalDate datum
	 */
	public LocalDate getDatumNar()
	{
		return this.datum;
	}
}
