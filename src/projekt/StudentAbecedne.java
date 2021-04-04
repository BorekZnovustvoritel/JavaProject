package projekt;

public class StudentAbecedne extends Student implements Comparable<StudentAbecedne>
{
	public StudentAbecedne(Student s)
	{
		super(s.getJmeno(), s.getPrijmeni(), s.getDatumNar(), s.getID());
		this.znamky = s.znamky;
	}

	@Override
	public int compareTo(StudentAbecedne o)
	{
		return this.getPrijmeni().compareTo(o.getPrijmeni());
	}
}
