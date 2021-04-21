package projekt;

import java.time.LocalDate;
/**
 * Tøída slouží jako obecná abstraktní forma pro objekty typu Student a Ucitel
 * @author Marek Szymutko
 * Obsahuje promìnné:
 * int id - pomocí nìj se osoba ukládá do Java Mapy a SQL databáze
 * String jmeno, String prijmeni
 * LocalDate datum - datum narození
 * 
 * Konstruktor Osoba() pro lepší implementaci dìdièných konstruktorù
 * abstract double getOdmena() musí být implementována dìdiènými tøídami
 */
public abstract class Osoba
{
	private int id;
	private String jmeno;
	private String prijmeni;
	LocalDate datum;
	/**
	 * Konstruktor pro implementaci konstruktorù dìdièných tøíd
	 * @param jmeno - jméno èlovìka (String)
	 * @param prijmeni - pøíjmení èlovìka (String)
	 * @param datumNarozeni - datum narození (java.time.LocalDate)
	 * @param ID - jedineèné ID (int)
	 */
	public Osoba(String jmeno, String prijmeni, LocalDate datumNarozeni, int ID)
	{
		this.jmeno = jmeno;
		this.prijmeni = prijmeni;
		this.datum = datumNarozeni;
		this.id = ID;
	}
	/**
	 * Nutno implementovat v dìdièných tøídách
	 * @return double odmena
	 */
	public abstract double getOdmena();
	/**
	 * Vrací pouze køestní jméno objektu typu Osoba
	 * @return String jmeno
	 */
	public String getJmeno()
	{
		return this.jmeno;
	}
	/**
	 * Metoda nebyla využita, ale pro pøípadné rozšíøení aplikace je vhodné ji mít (lidé se mohou pøejmenovat a nechat si ID)
	 * @param jmeno - jméno pro nahrání do objektu (String)
	 */
	public void setJmeno(String jmeno)
	{
		this.jmeno = jmeno;
	}
	/**
	 * Vrací pouze pøíjmení objektu typu Osoba
	 * @return String prijmeni
	 */
	public String getPrijmeni()
	{
		return this.prijmeni;
	}
	/**
	 * Metoda nebyla využita, ale pro pøípadné rozšíøení aplikace je vhodné ji mít (lidé se mohou pøejmenovat a nechat si ID)
	 * @param prijmeni - pøíjmení pro nahrání do objektu (String)
	 */
	public void setPrijmeni(String prijmeni)
	{
		this.prijmeni = prijmeni;
	}
	/**
	 * Metoda vrací identifikaèní èíslo objektu Osoba
	 * @return int ID
	 */
	public int getID()
	{
		return this.id;
	}
	/**
	 * Metoda vrací datum narození objektu
	 * @return LocalDate datum
	 */
	public LocalDate getDatumNar()
	{
		return this.datum;
	}
}
