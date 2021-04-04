package projekt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Scanner;

import sql.DConnection;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		Databaze skola = new Databaze();
		String userName = "", password = "";
		try
		{
			FileReader DBC = new FileReader(new File("DBConfig.txt"));
			BufferedReader DBConfig = new BufferedReader(DBC);
			userName = DBConfig.readLine();
			String[] pole = userName.split(":");
			if (pole.length == 2)
			{
				userName = pole[1];
				System.out.println("Jmeno nacteno uspesne.");
			}
			else
				System.out.println("Bylo nastaveno defaultni jmeno");
			password = DBConfig.readLine();
			pole = password.split(":");
			if (pole.length == 2)
			{
				password = pole[1];
				System.out.println("Heslo nacteno uspesne.");
			}
			else
				System.out.println("Bylo nastaveno defaultni heslo");
			DBConfig.close();
			DBC.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Udaje k pripojeni do SQL se nepodarilo nacist.");
		}
		boolean leave = false;
		while(!leave)
		{
			switch(Functionalities.menu(
					"1 .. Pridani noveho cloveka\n"+
					"2 .. Tisk databaze\n"+
					"3 .. Informace o osobe podle ID\n"+
					"4 .. Oznamkovani studenta podle ID\n"+
					"5 .. Propusteni z univerzity podle ID\n"+
					"6 .. Vypis vsech ucitelu zaka podle ID\n"+
					"7 .. Presouvani zaku\n"+
					"8 .. Vypis ucitelu podle poctu studentu\n"+
					"9 .. Tisk studentu ucitele podle prumeru\n"+
					"10.. Tisk vsech lidi v databazi podle abecedy\n"+
					"11.. Zobrazit celkove vydaje skoly\n"+
					"12.. Ulozit vse do SQL\n"+
					"13.. Nacist vse z SQL\n"+
					"14.. Vymazani osoby z SQL\n"+
					"15.. Nacteni osoby z SQL\n"+
					"16.. Exit\n", 16))
			{
			case 1:
				int option;
				if (skola.existujiUcitele())
				{
					option = Functionalities.menu(
							"1 .. Pridani studenta\n"+
							"2 .. Pridani ucitele\n"+
							"3 .. Zpet", 3);
				}
				else
					option = 2;
				switch(option)
				{
					case 1:
					{
						boolean viceUcitelu = true;
						HashSet<Ucitel> ucitele = new HashSet<>();
						do
						{
							System.out.println("Zadejte ID ucitele tohoto studenta");
							int id = Functionalities.scanInt();
							Osoba ucitel = skola.getOsoba(id);
							if (ucitel instanceof Ucitel)
							{
								ucitele.add((Ucitel)ucitel);
								System.out.println("Pokud chcete pridat dalsi ucitele, stisknete 1, jinak zadejte jine cele cislo");
								if (Functionalities.scanInt() != 1)
									viceUcitelu = false;
							}
							else
								System.out.println("Neexistujici ID ucitele. Ucitel nemohl byt studentovi pridan.");
						} while (viceUcitelu);
						System.out.println("Zadejte jmeno studenta");
						String jmeno = Functionalities.scanWord();
						System.out.println("Zadejte prijmeni");
						String prijmeni = Functionalities.scanWord();
						int studID = skola.addStudent(jmeno, prijmeni, Functionalities.scanLocalDate(), ucitele); //ma na starosti i spojeni s listem studentu ucitele
						System.out.println("Byl pridan student. ID: "+studID);
						break;
					}
					case 2:
					{
						System.out.println("Zadejte jmeno ucitele");
						String jmeno = Functionalities.scanWord();
						System.out.println("Zadejte prijmeni");
						String prijmeni = Functionalities.scanWord();
						int ucID = skola.addUcitel(jmeno, prijmeni, Functionalities.scanLocalDate());
						System.out.println("Byl pridan ucitel. ID: "+ucID);
						break;
					}
				}				
				break;
			case 2:
				skola.tiskDatabaze();
				break;
			case 3:
			{
				System.out.println("Zadejte ID pozadovane osoby");
				int id = Functionalities.scanInt();
				Osoba temp = skola.getOsoba(id);
				if (temp instanceof Student)
				{
					System.out.format("Student, ID: "+temp.getID()+", jmeno a prijmeni: "+temp.getJmeno()+" "+temp.getPrijmeni()+
							", datum narozeni: "+temp.getDatumNar()+"\nStudijni prumer: %.2f, financni odmeny: %.2f\n", ((Student) temp).getPrumer(), temp.getOdmena());
				}
				else if(temp instanceof Ucitel)
				{
					System.out.format("Ucitel, ID: "+temp.getID()+", jmeno a prijmeni: "+temp.getJmeno()+" "+temp.getPrijmeni()+
							", datum narozeni: "+temp.getDatumNar()+"\nFinancni odmeny : %.2f\n", ((Ucitel)temp).getCistaOdmena());
					System.out.println("Zaci tohoto ucitele:");
					for (Student stud: ((Ucitel) temp).studenti)
						System.out.println(stud.getJmeno()+" "+stud.getPrijmeni()+", ID: "+stud.getID());
				}
				else
					System.out.println("Osobu se v databazi nepodarilo nalezt.");
				break;
			}
			case 4:
			{
				System.out.println("Zadejte ID studenta k oznamkovani");
				int id = Functionalities.scanInt();
				Osoba temp = skola.getOsoba(id);
				if (temp instanceof Student)
				{
					System.out.println("Zadejte znamku");
					double znamka = Functionalities.scanDouble();
					if (znamka >= 1 && znamka <= 5)
						((Student)temp).addZnamka(znamka);
					else
						System.out.println("Znamka zvolena ze spatneho rozsahu!");
				}
				else
					System.out.println("Student s takovym ID nebyl nalezen");
				break;
			}
			case 5:
			{
				System.out.println("Zadejte ID cloveka k odstraneni");
				int id = Functionalities.scanInt();
				Osoba temp = skola.getOsoba(id);
				if (temp != null)
				{
					System.out.print("Opravdu chcete odstranit tuto osobu z databaze?\n"
							+temp.getJmeno()+" "+temp.getPrijmeni()+", "+temp.getClass().getSimpleName()
							+"\nPotvrdte zadanim cisla 1:\n");
					if ((Functionalities.scanInt()) == 1)
						if (skola.remove(id))
							System.out.println("Byl uspesne odstranen zaznam");
				}
				else
					System.out.println("Osoba nenalezena.");
				break;
			}
			case 6:
			{
				System.out.println("Zadejte ID studenta");
				int id = Functionalities.scanInt();
				Osoba student = skola.getOsoba(id);
				if (student instanceof Student)
				{
					System.out.println("Ucitele zaka: ");
					for (Ucitel u: (skola.getUciteleZaka((Student)student)))
						System.out.println("ID: "+u.getID()+", "+u.getJmeno()+" "+u.getPrijmeni());
				}
				else
					System.out.println("Student s takovym ID nebyl nalezen.");
				break;
			}
			case 7:
			{
				System.out.println("Zadejte ID studenta");
				int idS = Functionalities.scanInt();
				Osoba student = skola.getOsoba(idS);
				if (student instanceof Student)
				{
					System.out.println("Zadejte ID ucitele");
					int idU = Functionalities.scanInt();
					Osoba ucitel = skola.getOsoba(idU);
					if (ucitel instanceof Ucitel)
					{
						Student temp = (Student)student;
						switch(Functionalities.menu(
								"1 .. Pridani studenta\n"+
								"2 .. Odebrani studenta\n"+
								"3 .. Zpet", 3))
						{
						case 1:
							((Ucitel) ucitel).studenti.add(temp);
							System.out.println("Ucitel "+ucitel.getPrijmeni()+" uci studenta "+student.getPrijmeni()+".");
							break;
						case 2:
							if (((Ucitel) ucitel).studenti.contains(temp))
								((Ucitel) ucitel).studenti.remove(temp);
							System.out.println("Ucitel "+ucitel.getPrijmeni()+" jiz neuci studenta "+student.getPrijmeni()+".");
							break;
						}
					}
					else
						System.out.println("Ucitel s takovym ID nebyl nalezen.");
				}
				else
					System.out.println("Student s takovym ID nebyl nalezen.");	
				break;
			}
			case 8:
				skola.tiskUcitelu();
				break;
			case 9:
			{
				System.out.println("Zadejte ID ucitele");
				int idU = Functionalities.scanInt();
				Osoba ucitel = skola.getOsoba(idU);
				if (ucitel instanceof Ucitel)
				{
					Ucitel tempU = (Ucitel)ucitel;
					skola.tiskStudentuUcitele(tempU);
				}
				else
					System.out.println("Neexistuje ucitel s takovym ID.");
				break;
			}	
			case 10:
				skola.tiskVsichniAbecedne();
				break;
			case 11:
				System.out.format("Celkove vydaje skoly cini %.2f Kc.\n",skola.vydaje());
				break;
			case 12:
			{
				DConnection conn = new DConnection();
				conn.connect(userName, password);
				if (conn.ulozVse(skola))
					System.out.println("Uspech");
				conn.disconnect();
				break;
			}
			case 13:
			{
				DConnection conn = new DConnection();
				conn.connect(userName, password);
				if (conn.nactiVse(skola))
					System.out.println("Uspech");
				conn.disconnect();
				break;
			}
			case 14:
			{
				DConnection conn = new DConnection();
				conn.connect(userName, password);
				System.out.println("Zadej ID osoby k vymazani");
				int id = Functionalities.scanInt();
				conn.vymazOsobuSQL(skola, id);
				conn.disconnect();
				break;
			}
			case 15:
			{
				DConnection conn = new DConnection();
				conn.connect(userName, password);
				System.out.println("Zadej ID osoby k nacteni");
				int id = Functionalities.scanInt();
				conn.nactiOsobuSQL(skola, id);
				conn.disconnect();
				break;
			}
			case 16:
				leave = true;
				System.out.println("Vytvoreno Markem Szymutkem za pouziti referencnich materialu ucitele Ing. Jiriho Prinosila, Ph.D.\n");
				break;
			}

		}
		sc.close();
	}
	
	
	static class Functionalities
	{
		static Scanner sc = new Scanner(System.in);
		
		static int menu(String options, int exitOption)
		{
			int opt = 0;
			while (true)
			{
				System.out.println("Zvolte moznost");
				System.out.println(options);
				opt = scanInt();
				if (opt <= exitOption && opt > 0)
					return opt;
				else 
				{
					System.out.println("Zkuste to znovu");
				}
			}
		}
		
		static String scanWord()
		{
			String res = sc.next();
			if (res == "")
				res = sc.next();
			return res;
		}
		
		static LocalDate scanLocalDate()
		{
			boolean success = false;
			LocalDate vysledek = null;
			while (!success)
			{
				System.out.println("Zadejte datum narozeni ve formatu yyyy-MM-dd, doplnte nulami prazdna mista");
				String date = Functionalities.scanWord();
				try
				{
					vysledek = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					success = true;
				}
				catch(DateTimeParseException e)
				{
					System.out.println("To se nepovedlo, zkuste to znovu");
				}
			}
			return vysledek;
		}
		
		static int scanInt()
		{
			while (true)
			{
				if (sc.hasNextInt())
					return sc.nextInt();
				else
				{
					System.out.println("Prosim, zadejte cele cislo");
					sc.next();
				}
			}
		}
		
		static double scanDouble()
		{
			while (true)
			{
				if (sc.hasNextDouble())
					return sc.nextDouble();
				else
				{
					System.out.println("Prosim, zadejte destinne cislo");
					sc.next();
				}
			}
		}
	}
}
