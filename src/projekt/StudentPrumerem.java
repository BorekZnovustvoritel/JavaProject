package projekt;
/**
 * T��da objekt� StudentPrumerem - studenti �koly s pravidlem �azen�, odvozeno od t��dy Student, implementuje Comparable
 * @author Marek Szymutko
 * 
 * Konstruktor StudentPrumerem() kop�ruje atributy vlo�en�ho studenta
 */
public class StudentPrumerem extends Student implements Comparable<StudentPrumerem>
{
	public StudentPrumerem(Student s)
	{
		super(s.getJmeno(), s.getPrijmeni(), s.getDatumNar(), s.getID());
		for (double znamka: s.getZnamky())
			this.addZnamka(znamka);
	}
	@Override
	public int compareTo(StudentPrumerem s) 
	{
		double thisTemp = this.getPrumer();
		double thatTemp = s.getPrumer();
		if (thisTemp<thatTemp && thisTemp != 0)
			return -1;
		else if (thisTemp>thatTemp || thisTemp == 0)
			return 1;
		else
			return 0;
	}
}
