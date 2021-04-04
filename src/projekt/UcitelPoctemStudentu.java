package projekt;

public class UcitelPoctemStudentu extends Ucitel implements Comparable<UcitelPoctemStudentu>
{
	@Override
	public int compareTo(UcitelPoctemStudentu u)
	{
		if (this.studenti.size() < (u).studenti.size())
			return 1;
		else if (this.studenti.size() > (u).studenti.size())
			return -1;
		else return 0;
	}
	public UcitelPoctemStudentu(Ucitel u)
	{
		super(u.getJmeno(), u.getPrijmeni(), u.getDatumNar(), u.getID());
		this.studenti = u.studenti;
	}
}
