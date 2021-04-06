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
		//Scanner sc = new Scanner(System.in);
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
					"1 .. Pøidání nového èlovìka\n"+
					"2 .. Informace z Java databáze\n"+
					"3 .. Manipulace s Java databází\n"+
					"4 .. SQL\n"+
					"5 .. Ukonèit aplikaci\n", 5))
			{
			case 1: //pridani
			{
				int option;
				if (skola.existujiUcitele())
				{
					option = Functionalities.menu(
							"1 .. Pøidání studenta\n"+
							"2 .. Pøidání uèitele\n"+
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
							System.out.println("Zadejte ID uèitele tohoto studenta");
							int id = Functionalities.scanInt();
							Osoba ucitel = skola.getOsoba(id);
							if (ucitel instanceof Ucitel)
							{
								ucitele.add((Ucitel)ucitel);
								System.out.println("Pokud chcete pøidat další uèitele, zadejte 1, jinak zadejte jiné celé èíslo");
								if (Functionalities.scanInt() != 1)
									viceUcitelu = false;
							}
							else
								System.out.println("Neexistující ID uèitele. Uèitel nemohl byt studentovi pøidan.");
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
			}
			case 2: //tisk
			{
				int option = Functionalities.menu(
						"1 .. Tisk celé databáze podle ID\n"+
						"2 .. Tisk celé databáze podle abecedního øazení\n"+
						"3 .. Údaje o èlovìku podle ID\n"+
						"4 .. Uèitele žáka, jehož ID zadáte\n"+
						"5 .. Tisk všech uèitelù podle poètu žákù\n"+
						"6 .. Tisk žákù uèitele podle prùmìru\n"+
						"7 .. Zobrazit výdaje školy\n"+
						"8 .. Zpìt\n", 8);
				switch (option)
				{
					case 1:
						skola.tiskDatabaze();
						break;
						
					case 2:
						skola.tiskVsichniAbecedne();
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
							for (Student stud: ((Ucitel) temp).getStudenti())
								System.out.println(stud.getJmeno()+" "+stud.getPrijmeni()+", ID: "+stud.getID());
						}
						else
							System.out.println("Osobu se v databazi nepodarilo nalezt.");
						break;
					}
						
					case 4:
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
					case 5:
						skola.tiskUcitelu();
						break;
					case 6:
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
					case 7:
						System.out.format("Celkove vydaje skoly cini %.2f Kc.\n",skola.vydaje());
						break;
				}
				
				break;
			}
			case 3: //manipulace s databází
				{
					int option = Functionalities.menu(
							"1 .. Oznámkovat studenta\n"+
							"2 .. Propustit èlovìka\n"+
							"3 .. Pøidání / odebrání studenta uèiteli\n"+
							"4 .. Zpìt\n", 4);
					switch (option)
					{
						case 1:
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
						}
							break;
						case 2:
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
						}
							break;
						case 3:
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
											"1 .. Pøidání studenta\n"+
											"2 .. Odebrání studenta\n"+
											"3 .. Zpìt\n", 3))
									{
									case 1:
										((Ucitel) ucitel).studentDoSeznamu(temp);
										System.out.println("Uèitel "+ucitel.getPrijmeni()+" uèí studenta "+student.getPrijmeni()+".");
										break;
									case 2:
										if (((Ucitel) ucitel).removeStudent(temp))
											System.out.println("Student "+temp.getPrijmeni()+" byl odebrán uèiteli "+ucitel.getPrijmeni()+".");
										else
											System.out.println("Student nebyl nalezen v seznamu tohoto uèitele.");
										break;
									}
								}
								else
									System.out.println("Uèitel s takovým ID nebyl nalezen.");
							}
							else
								System.out.println("Student s takovým ID nebyl nalezen.");	
						}
							break;
					}
					
				}
				break;
			case 4: //sql
			{
				DConnection conn = new DConnection();
				conn.connect(userName, password);
				int option = Functionalities.menu(
						"1 .. Uložit vše do SQL\n"+
						"2 .. Naèíst vše z SQL\n"+
						"3 .. Vymazání osoby z SQL\n"+
						"4 .. Naètení osoby z SQL\n"+
						"5 .. Zpìt\n", 5);
				switch (option)
				{
					case 1:
					{
						if (conn.ulozVse(skola))
							System.out.println("Úspìch");
						else
							System.out.println("Nepodaøilo se uložit data");
					}
						break;
					case 2:
					{
						if (conn.nactiVse(skola))
							System.out.println("Úspìch");
						else
							System.out.println("Nepodaøilo se naèíst data");
					}
						break;
					case 3:
					{
						System.out.println("Zadejte ID osoby k vymazání");
						int id = Functionalities.scanInt();
						if (conn.vymazOsobuSQL(skola, id))
							System.out.println("Žádost byla SQL databází zpracována");
						else
							System.out.println("Chyba pøi kontaktování databáze");
					}
						break;
					case 4:
					{
						System.out.println("Zadej ID osoby k nacteni");
						int id = Functionalities.scanInt();
						if (conn.nactiOsobuSQL(skola, id))
							System.out.println("Žádost byla SQL databází zpracována");
						else
							System.out.println("Chyba pøi kontaktování databáze");
						
					}
						break;		
				}
				conn.disconnect();
			}
				break;

			case 5:
			{
				leave = true;
				System.out.println("Vytvoøeno Markem Szymutkem za použití referenèních materiálù uèitele Ing. Jiøího Pøinosila, Ph.D.\n");
				break;

			}
		}
	}
}	
	
	static class Functionalities
	{
		static Scanner sc = new Scanner(System.in);
		
		static int menu(String options, int exitOption)
		{
			int opt = 0;
			while (true)
			{
				System.out.println("Zvolte možnost");
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
				System.out.println("Zadejte datum narození ve formátu yyyy-MM-dd, doplòte nulami prázdná místa");
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
					System.out.println("Prosím, zadejte celé èíslo");
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
					System.out.println("Prosim, zadejte destinné èíslo");
					sc.next();
				}
			}
		}
	}
}
