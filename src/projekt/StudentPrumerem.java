package projekt;

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
		if (this.getPrumer()<s.getPrumer())
			return -1;
		else if (this.getPrumer()>s.getPrumer())
			return 1;
		else
			return 0;
	}
}
