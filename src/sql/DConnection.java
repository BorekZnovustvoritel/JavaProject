package sql;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import projekt.Databaze;
import projekt.Osoba;
import projekt.Student;
import projekt.Ucitel;

public class DConnection {
	public DConnection() {
	}

	private Connection conn;

	public boolean connect(String user, String password) 
	{ 
	      conn = null; 
	      try 
	      {
	    	  conn = DriverManager.getConnection("jdbc:sqlite:myDB.db", user, password);                       
	      } 
	      catch (SQLException e)
	      { 
	    	  System.out.println(e.getMessage());
	    	  return false;
	      }
	      return true;
	}

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
				System.out.println("Uspesne nacteni");
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
