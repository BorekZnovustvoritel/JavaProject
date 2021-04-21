package projekt;
/**
 * Tøída objektù StudentAbecedne - studenti školy s pravidlem øazení, odvozeno od tøídy Student, implementuje Comparable
 * @author Marek Szymutko
 * 
 * Konstruktor StudentAbecedne() kopíruje atributy vloženého studenta
 */
public class StudentAbecedne extends Student implements Comparable<StudentAbecedne>
{
	public StudentAbecedne(Student s)
	{
		super(s.getJmeno(), s.getPrijmeni(), s.getDatumNar(), s.getID());
		for (double znamka: s.getZnamky())
			this.addZnamka(znamka);
	}

	@Override
	public int compareTo(StudentAbecedne o)
	{
		return this.getPrijmeni().compareTo(o.getPrijmeni());
	}
}
