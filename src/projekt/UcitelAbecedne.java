package projekt;
/**
 * T��da objekt� UcitelAbecedne - u�itel� �koly s pravidlem �azen�, odvozeno od t��dy Ucitel, implementuje Comparable
 * Konstruktor UcitelAbecedne() kop�ruje atributy vlo�en�ho u�itele
 * @author Marek Szymutko
 */
public class UcitelAbecedne extends Ucitel implements Comparable<UcitelAbecedne>
{
	public UcitelAbecedne(Ucitel u)
	{
		super(u.getJmeno(), u.getPrijmeni(), u.getDatumNar(), u.getID());
		for (Student s: u.getStudenti())
			this.studentDoSeznamu(s);
	}
	@Override
	public int compareTo(UcitelAbecedne o)
	{
		return this.getPrijmeni().compareTo(o.getPrijmeni());
	}
}
