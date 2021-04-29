package projekt;
/**
 * Tøída objektù UcitelAbecedne - uèitelé školy s pravidlem øazení, odvozeno od tøídy Ucitel, implementuje Comparable
 * Konstruktor UcitelAbecedne() kopíruje atributy vloženého uèitele
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
