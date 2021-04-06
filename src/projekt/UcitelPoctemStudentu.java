package projekt;

public class UcitelPoctemStudentu extends Ucitel implements Comparable<UcitelPoctemStudentu>
{
	@Override
	public int compareTo(UcitelPoctemStudentu u)
	{
		if (this.getStudenti().size() < (u).getStudenti().size())
			return 1;
		else if (this.getStudenti().size() > (u).getStudenti().size())
			return -1;
		else return 0;
	}
	public UcitelPoctemStudentu(Ucitel u)
	{
		super(u.getJmeno(), u.getPrijmeni(), u.getDatumNar(), u.getID());
		for (Student s: u.getStudenti())
			this.studentDoSeznamu(s);
	}
}
