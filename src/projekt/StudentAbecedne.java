package projekt;
/**
 * T��da objekt� StudentAbecedne - studenti �koly s pravidlem �azen�, odvozeno od t��dy Student, implementuje Comparable
 * @author Marek Szymutko
 * 
 * Konstruktor StudentAbecedne() kop�ruje atributy vlo�en�ho studenta
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
