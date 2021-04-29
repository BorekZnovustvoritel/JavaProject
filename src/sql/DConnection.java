package sql;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import projekt.Databaze;
import projekt.Osoba;
import projekt.Student;
import projekt.Ucitel;
/**
 * Tøída slouží pro spojení projektu s lokální SQL databází
 * Obsahuje promìnné:
 * Connection conn - promìnná øídící spojení
 * 
 * Konstruktor je implicitní
 * @author Marek Szymutko
 */
public class DConnection {

	private Connection conn;
	/**
	 * Spojení s databázovým souborem
	 * @param path - název souboru nebo cesta + název (String)
	 * @param user - pøihlašovací login do databáze (String)
	 * @param password - heslo pro pøihlášení do databáze (String)
	 * @return boolean connected - podaøilo se spojení?
	 */
	public boolean connect(String path, String user, String password) 
	{ 
	      conn = null; 
	      try 
	      {
	    	  conn = DriverManager.getConnection("jdbc:sqlite:"+path, user, password);                       
	      } 
	      catch (SQLException e)
	      { 
	    	  System.out.println(e.getMessage());
	    	  return false;
	      }
	      return true;
	}
	/**
	 * Odpojení z databáze, øádné uzavøení
	 */
	public void disconnect()
	{
		if (conn != null)
		{
			try 
			{
				conn.close();
			} 
			catch (SQLException ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
	/**
	 * Vytvoøení databázové tabulky Osoby,  která má následující promìnné:
	 * int id
	 * bit student - je student (1) nebo uèitel (0)?
	 * varchar(50) jmeno
	 * varchar(50) prijmeni
	 * date datum
	 * @return boolean podariloSe
	 */
	public boolean createTableOsoby()
	{
	     if (conn==null)
	           return false;
	    String sql = "CREATE TABLE IF NOT EXISTS osoby (id int, student bit, jmeno varchar(50), prijmeni varchar(50), datum date, PRIMARY KEY (id));";
	    try
	    {
	            Statement stmt = conn.createStatement(); 
	            stmt.execute(sql);
	            return true;
	    } 
	    catch (SQLException e) {
	    System.out.println(e.getMessage());
	    }
	    return false;
	}
	/**
	 * Zresetování databázové tabulky Osoby (zahození a nové vytvoøení)
	 * @return boolean podariloSe
	 */
	public boolean resetTableOsoby()
	{
		if (conn==null)
			return false;
		String sql = "DROP TABLE IF EXISTS osoby;";
		try
	    {
	            Statement stmt = conn.createStatement(); 
	            stmt.execute(sql);
	            this.createTableOsoby();
	            return true;
	    } 
	    catch (SQLException e) {
	    System.out.println(e.getMessage());
	    }
	    return false;
	}
	/**
	 * Vytvoøení databázové tabulky Vztahy,  která má následující promìnné:
	 * int key (jen pro úèely SQL)
	 * int idU - ID uètele
	 * int idS - ID jeho žáka
	 * @return boolean podariloSe
	 */
	public boolean createTableVztahy()
	{
	     if (conn==null)
	           return false;
	    String sql = "CREATE TABLE IF NOT EXISTS vztahy (key int AUTO_INCREMENT, idU int, idS int, PRIMARY KEY (key));";
	    try
	    {
	            Statement stmt = conn.createStatement(); 
	            stmt.execute(sql);
	            return true;
	    } 
	    catch (SQLException e) {
	    System.out.println(e.getMessage());
	    }
	    return false;
	}
	/**
	 * Zresetování databázové tabulky Vztahy (zahození a nové vytvoøení)
	 * @return boolean podariloSe
	 */
	public boolean resetTableVztahy()
	{
		if (conn==null)
			return false;
		String sql = "DROP TABLE IF EXISTS vztahy;";
		try
	    {
	            Statement stmt = conn.createStatement(); 
	            stmt.execute(sql);
	            this.createTableVztahy();
	            return true;
	    } 
	    catch (SQLException e) {
	    System.out.println(e.getMessage());
	    }
	    return false;
	}
	/**
	 * Vytvoøení databázové tabulky Znamky,  která má následující promìnné:
	 * int key (jen pro úèely SQL)
	 * int idS - ID jeho žáka
	 * float znamka - známka žáka
	 * @return boolean podariloSe
	 */
	public boolean createTableZnamky()
	{
	     if (conn==null)
	           return false;
	    String sql = "CREATE TABLE IF NOT EXISTS znamky (key int AUTO_INCREMENT, idS int, znamka float, PRIMARY KEY (key));";
	    try
	    {
	            Statement stmt = conn.createStatement(); 
	            stmt.execute(sql);
	            return true;
	    } 
	    catch (SQLException e) {
	    System.out.println(e.getMessage());
	    }
	    return false;
	}
	/**
	 * Zresetování databázové tabulky Znamky (zahození a nové vytvoøení)
	 * @return boolean podariloSe
	 */
	public boolean resetTableZnamky()
	{
		if (conn==null)
			return false;
		String sql = "DROP TABLE IF EXISTS znamky;";
		try
	    {
	            Statement stmt = conn.createStatement(); 
	            stmt.execute(sql);
	            this.createTableZnamky();
	            return true;
	    } 
	    catch (SQLException e) {
	    System.out.println(e.getMessage());
	    }
	    return false;
	}
	/**
	 * Naèítání celé SQL databáze do Java databáze
	 * Nejprve naèteme osoby, pak vztahy, pak známky
	 * Pøed naètením smažeme Java databázi, aby nedocházelo ke kolizím
	 * @param skola - Java databáze školy (Databaze)
	 * @return boolean podariloSe
	 */
	public boolean nactiVse(Databaze skola)
	{
		if (conn==null)
	           return false;
		this.createTableOsoby();
		this.createTableVztahy();
		this.createTableZnamky();
		skola.reset();
		String sql = "SELECT * FROM osoby;";
		try
		{
            Statement stmt1 = conn.createStatement(); 
            ResultSet rs1 = stmt1.executeQuery(sql);
            while (rs1.next()) 
            {
            	boolean jeStudent = rs1.getBoolean("student");
            	int id = rs1.getInt("id");
            	String jmeno = rs1.getString("jmeno");
            	String prijmeni = rs1.getString("prijmeni");
            	LocalDate datum = (rs1.getDate("datum")).toLocalDate();
            	if (jeStudent)
            		skola.addStudentSQL(id, jmeno, prijmeni, datum);
            	else
            		skola.addUcitelSQL(id, jmeno, prijmeni, datum);
            }
            sql = "SELECT * FROM vztahy;";
            Statement stmt2 = conn.createStatement(); 
            ResultSet rs2 = stmt2.executeQuery(sql);
            while (rs2.next()) 
            {
            	int idU = rs2.getInt("idU");
            	int idS = rs2.getInt("idS");
            	Osoba U = skola.getOsoba(idU);
            	Osoba S = skola.getOsoba(idS);
            	if (U instanceof Ucitel && S instanceof Student)
            	{
            		((Ucitel)U).studentDoSeznamu((Student)S);
            	}
            }
            sql = "SELECT * FROM znamky;";
            Statement stmt3 = conn.createStatement();
            ResultSet rs3 = stmt3.executeQuery(sql);
            while (rs3.next())
            {
            	int idS = rs3.getInt("idS");
            	double znamka = rs3.getDouble("znamka");
            	Osoba o = skola.getOsoba(idS);
            	if (o instanceof Student)
            	{
            		((Student)o).addZnamka(znamka);
            	}
            }
            skola.getPocetLidi(); //nastavime dalsi indexovani
            return true;
	    } 
	    catch (SQLException e) {
	    System.out.println(e.getMessage());
	    }
		return false;
	}
	/**
	 * Uložení celé Java databáze do SQL databáze
	 * V cyklu projdeme všechny lidi a uložíme je buï jako uèitele nebo jako studenta, naèítání známek a vztahù je ve vnoøeném cyklu
	 * @param skola - Java databáze školy (Databaze)
	 * @return boolean podariloSe
	 */
	public boolean ulozVse(Databaze skola)
	{
		if (conn==null)
	           return false;
		this.resetTableOsoby();
		this.resetTableVztahy();
		this.resetTableZnamky();
		HashSet<Osoba> lidi = skola.getLidi();
		String sql = "INSERT INTO osoby (id, student, jmeno, prijmeni, datum) VALUES (?,?,?,?,?)";
		try
		{
			for (Osoba o: lidi)
			{
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, o.getID());
				if (o instanceof Student)
				{
					pstmt.setBoolean(2, true);
					
					ArrayList<Double> znamky = ((Student)o).getZnamky();
					for (double znamka: znamky)
					{
						String sql2 = "INSERT INTO znamky (idS, znamka) VALUES (?,?)";
						PreparedStatement pstmt2 = conn.prepareStatement(sql2);
						pstmt2.setInt(1, o.getID());
						pstmt2.setDouble(2, znamka);
						pstmt2.executeUpdate();
					}
				}
				else
				{
					pstmt.setBoolean(2, false);
					
					HashSet<Student> studenti = ((Ucitel)o).getStudenti();
					for (Student s: studenti)
					{
						String sql2 = "INSERT INTO vztahy (idU, idS) VALUES (?,?)";
						PreparedStatement pstmt2 = conn.prepareStatement(sql2);
						pstmt2.setInt(1, o.getID());
						pstmt2.setInt(2, s.getID());
						pstmt2.executeUpdate();
					}
				}
				pstmt.setString(3, o.getJmeno());
				pstmt.setString(4, o.getPrijmeni());
				pstmt.setDate(5, Date.valueOf(o.getDatumNar()));
				pstmt.executeUpdate();	
			}
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	/**
	 * Smaže osobu jen z SQL databáze, v Java databázi èlovìk zùstane
	 * @param skola - Java databáze, ze které èlovìka vybereme (Databaze)
	 * @param id - id èlovìka (int)
	 * @return boolean podariloSe
	 */
	public boolean vymazOsobuSQL(Databaze skola, int id)
	{
		if (conn==null)
	           return false;
		boolean jeStudent = skola.getOsoba(id) instanceof Student;
		String sql = "DELETE FROM osoby WHERE id=(?)";
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			if (jeStudent)
			{
				sql = "DELETE FROM znamky WHERE idS=(?)";
				PreparedStatement pstmt2 = conn.prepareStatement(sql);
				pstmt2.setInt(1, id);
				pstmt2.executeUpdate();
				sql = "DELETE FROM vztahy WHERE idS=(?)";
				PreparedStatement pstmt3 = conn.prepareStatement(sql);
				pstmt3.setInt(1, id);
				pstmt3.executeUpdate();
			}
			else
			{
				sql = "DELETE FROM vztahy WHERE idU=(?)";
				PreparedStatement pstmt2 = conn.prepareStatement(sql);
				pstmt2.setInt(1, id);
				pstmt2.executeUpdate();
			}
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	/**
	 * Naète jedinou osobu z SQL podle ID
	 * @param skola - Java databáze, kam nahráváme (Databaze)
	 * @param id - ID osoby (int)
	 * @return boolean podariloSe
	 */
	public boolean nactiOsobuSQL(Databaze skola, int id)
	{
		if (conn==null)
	           return false;
		
		this.createTableOsoby();
		this.createTableVztahy();
		this.createTableZnamky();
		String sql = "SELECT * FROM osoby WHERE id=(?);";
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next() != false)
			{
				boolean jeStudent = rs.getBoolean("student");
				String jmeno = rs.getString("jmeno");
				String prijmeni = rs.getString("prijmeni");
				LocalDate datum = (rs.getDate("datum")).toLocalDate();
				if (jeStudent)
				{
					skola.addStudentSQL(id, jmeno, prijmeni, datum);
					Student stu = ((Student)skola.getOsoba(id));
					sql = "SELECT znamka FROM znamky WHERE idS=(?)";
					PreparedStatement pstmt2 = conn.prepareStatement(sql);
					pstmt2.setInt(1, id);
					ResultSet rs2 = pstmt2.executeQuery();
					while (rs2.next())
					{
						Double znamka = rs2.getDouble("znamka");
						stu.addZnamka(znamka);
					}
					sql = "SELECT idU FROM vztahy WHERE idS=(?)";
					PreparedStatement pstmt3 = conn.prepareStatement(sql);
					pstmt3.setInt(1, id);
					ResultSet rs3 = pstmt3.executeQuery();
					while (rs3.next())
					{
						int idU = rs3.getInt("idU");
						Ucitel uci = ((Ucitel)(skola.getOsoba(idU)));
						if (uci != null)
							uci.studentDoSeznamu(stu);
					}
					
				}
				else
				{
					skola.addUcitelSQL(id, jmeno, prijmeni, datum);
					Ucitel uci = ((Ucitel)(skola.getOsoba(id)));
					sql = "SELECT idS FROM vztahy WHERE idU=(?)";
					PreparedStatement pstmt2 = conn.prepareStatement(sql);
					pstmt2.setInt(1, id);
					ResultSet rs2 = pstmt2.executeQuery();
					while (rs2.next())
					{
						int idS = rs2.getInt("idS");
						Student stu = ((Student)(skola.getOsoba(idS))); 
						if (stu != null)
							uci.studentDoSeznamu(stu);
					}
				}
				System.out.println("Úspìšné naètení");
				skola.getPocetLidi(); //kdyby pribyl dal, nez je momentalni kurzor, at neni premazan
				return true;
			}
			else
				System.out.println("Osoba nenalezena");
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
}
