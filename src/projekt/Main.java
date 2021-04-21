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
		Databaze skola = new Databaze();
		String userName = "", password = "", path = "";
		try //Naèítání pøihlašovacích údajù k SQL ze souboru
		{
			FileReader DBC = new FileReader(new File("DBConfig.txt"));
			BufferedReader DBConfig = new BufferedReader(DBC);
			path = DBConfig.readLine();
			String[] pole = path.split(":");
			userName = DBConfig.readLine();
			if (pole.length == 2)
			{
				path = pole[1];
				System.out.println("Cesta k databázi naètena úspìšnì.");
			}
			else
			{
				path = "myDB.db";
				System.out.println("Byla nastavena defaultní cesta k databázi.");
			}
			pole = userName.split(":");
			if (pole.length == 2)
			{
				userName = pole[1];
				System.out.println("Jméno naèteno úspìšnì.");
			}
			else
				System.out.println("Bylo nastaveno defaultní jméno");
			password = DBConfig.readLine();
			pole = password.split(":");
			if (pole.length == 2)
			{
				password = pole[1];
				System.out.println("Heslo naèteno úspìšnì.");
			}
			else
				System.out.println("Bylo nastaveno defaultní heslo");
			DBConfig.close();
			DBC.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Údaje k pøipojeni do SQL se nepodaøilo naèíst.");
		}
		boolean leave = false;
		while(!leave) //tier 0 menu //////////////////////////////////////////////////////////////////////////////////////////
		{
			switch(menu(
					"1 .. Pøidání nového èlovìka\n"+
					"2 .. Informace z Java databáze\n"+
					"3 .. Manipulace s Java databází\n"+
					"4 .. SQL\n"+
					"5 .. Ukonèit aplikaci\n", 5))
			{
			case 1: //tier 0 - pøidání
			{
				int option;
				if (skola.existujiUcitele())
				{
					option = menu(
							"1 .. Pøidání studenta\n"+
							"2 .. Pøidání uèitele\n"+
							"3 .. Zpet", 3);
				}
				else
					option = 2;
				switch(option)
				{ //tier 1 menu - pøidání - start ////////////////////////////////////////////////
					case 1: //student
					{
						HashSet<Ucitel> ucitele = new HashSet<>();
						int id = 0;
						do
						{
							System.out.println("Zadejte ID uèitele tohoto studenta, pokud dalšího uèitele pøidat nechcete, zadejte 0.");
							id = scanInt();
							Osoba ucitel = skola.getOsoba(id);
							if (ucitel instanceof Ucitel)
								ucitele.add((Ucitel)ucitel);
							else if (id != 0)
								System.out.println("Neexistující ID uèitele. Uèitel nemohl byt studentovi pøidan.");
						} while (id != 0);
						System.out.println("Zadejte jméno studenta");
						String jmeno = scanWord();
						System.out.println("Zadejte pøíjmení");
						String prijmeni = scanWord();
						int studID = skola.addStudent(jmeno, prijmeni, scanLocalDate(), ucitele); //ma na starosti i spojeni s listem studentu ucitele
						System.out.println("Byl pøidán student. ID: "+studID);
						break;
					}
					case 2: //uèitel
					{
						System.out.println("Zadejte jméno uèitele");
						String jmeno = scanWord();
						System.out.println("Zadejte pøíjmení");
						String prijmeni = scanWord();
						int ucID = skola.addUcitel(jmeno, prijmeni, scanLocalDate());
						System.out.println("Byl pøidán uèitel. ID: "+ucID);
						break;
					}
				} //tier 1 menu - pøidání - end	///////////////////////////////////////////////////	
				break;
			}
			case 2: //tier 0 - tisk
			{
				int option = menu(
						"1 .. Tisk celé databáze podle ID\n"+
						"2 .. Tisk celé databáze podle abecedního øazení\n"+
						"3 .. Údaje o èlovìku podle ID\n"+
						"4 .. Uèitelé žáka, jehož ID zadáte\n"+
						"5 .. Tisk všech uèitelù podle poètu žákù\n"+
						"6 .. Tisk žákù uèitele podle prùmìru\n"+
						"7 .. Zobrazit výdaje školy\n"+
						"8 .. Zpìt\n", 8);
				switch (option)
				{ // tier 1 menu - tisk - start ////////////////////////////////////////////////////
					case 1: //podle ID
						skola.tiskDatabaze();
						break;
						
					case 2: //podle abecedy
						skola.tiskVsichniAbecedne();
						break;
						
					case 3: //info o osobì
					{
						System.out.println("Zadejte ID požadované osoby");
						int id = scanInt();
						Osoba temp = skola.getOsoba(id);
						if (temp instanceof Student)
						{
							System.out.format("Student, ID: "+temp.getID()+", jméno a pøíjmení: "+temp.getJmeno()+" "+temp.getPrijmeni()+
									", datum narození: "+temp.getDatumNar()+"\nStudijní prùmìr: %.2f, finanèní odmìny: %.2f\n", ((Student) temp).getPrumer(), temp.getOdmena());
						}
						else if(temp instanceof Ucitel)
						{
							System.out.format("Uèitel, ID: "+temp.getID()+", jméno a pøíjmení: "+temp.getJmeno()+" "+temp.getPrijmeni()+
									", datum narození: "+temp.getDatumNar()+"\nFinanèní odmìny : %.2f\n", ((Ucitel)temp).getCistaOdmena());
							System.out.println("Žáci tohoto uèitele:");
							for (Student stud: ((Ucitel) temp).getStudenti())
								System.out.println(stud.getJmeno()+" "+stud.getPrijmeni()+", ID: "+stud.getID());
						}
						else
							System.out.println("Osobu se v databázi nepodaøilo nalézt.");
						break;
					}
						
					case 4: //uèitelé žáka
					{
						System.out.println("Zadejte ID studenta");
						int id = scanInt();
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
					case 5: //uèitelé podle poètu studentù
						skola.tiskUcitelu();
						break;
					case 6: //studenti jednoho uèitele
					{
						System.out.println("Zadejte ID ucitele");
						int idU = scanInt();
						Osoba ucitel = skola.getOsoba(idU);
						if (ucitel instanceof Ucitel)
						{
							Ucitel tempU = (Ucitel)ucitel;
							skola.tiskStudentuUcitele(tempU);
						}
						else
							System.out.println("Neexistuje uèitel s takovým ID.");
						break;
					}
					case 7: //výdaje
						System.out.format("Celkové výdaje školy pro uèitele èiní %.2f Kè.\n",skola.vydajeZaUcitele());
						System.out.format("Výdaje za stipendia studentù èíní %.2f Kè,\n", skola.vydajeZaStudenty());
						break;
				} // tier 1 menu - tisk - end ///////////////////////////////////////////////
				
				break;
			}
			case 3: //tier 0 - manipulace s databází
				{ 
					int option = menu(
							"1 .. Oznámkovat studenta\n"+
							"2 .. Propustit èlovìka\n"+
							"3 .. Pøidání / odebrání studenta uèiteli\n"+
							"4 .. Zpìt\n", 4);
					switch (option)
					{ // tier 1 menu - manipulace - start ///////////////////////////////////
						case 1: // oznámkovat studenta
						{
							System.out.println("Zadejte ID studenta k oznamkovani");
							int id = scanInt();
							Osoba temp = skola.getOsoba(id);
							if (temp instanceof Student)
							{
								System.out.println("Zadejte znamku");
								double znamka = scanDouble();
								if (znamka >= 1 && znamka <= 5)
									((Student)temp).addZnamka(znamka);
								else
									System.out.println("Známka zvolena ze špatného rozsahu!");
							}
							else
								System.out.println("Student s takovým ID nebyl nalezen");
						}
							break;
						case 2: // propustit èlovìka
						{
							System.out.println("Zadejte ID cloveka k odstraneni");
							int id = scanInt();
							Osoba temp = skola.getOsoba(id);
							if (temp != null)
							{
								System.out.print("Opravdu chcete odstranit tuto osobu z databáze?\n"
										+temp.getJmeno()+" "+temp.getPrijmeni()+", "+temp.getClass().getSimpleName()
										+"\nPotvrïte zadáním èísla 1:\n");
								if ((scanInt()) == 1)
									if (skola.remove(id))
										System.out.println("Byl úspìšnì odstranìn záznam");
							}
							else
								System.out.println("Osoba nenalezena.");
						}
							break;
						case 3: // pøesun studentù mezi uèiteli
						{
							System.out.println("Zadejte ID studenta");
							int idS = scanInt();
							Osoba student = skola.getOsoba(idS);
							if (student instanceof Student)
							{
								System.out.println("Zadejte ID uèitele");
								int idU = scanInt();
								Osoba ucitel = skola.getOsoba(idU);
								if (ucitel instanceof Ucitel)
								{
									Student temp = (Student)student;
									switch(menu(
											"1 .. Pøidání studenta\n"+
											"2 .. Odebrání studenta\n"+
											"3 .. Zpìt\n", 3))
									{ // tier 2 menu - manipulace se studenty - start //////
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
									} // tier 2 menu - manipulace se studenty - end ////////
								}
								else
									System.out.println("Uèitel s takovým ID nebyl nalezen.");
							}
							else
								System.out.println("Student s takovým ID nebyl nalezen.");	
						}
							break;
					} // tier 1 menu - manipulace - end //////////////////////////////////////
					
				}
				break;
			case 4: //tier 0 - sql
			{
				DConnection conn = new DConnection();
				if(conn.connect(path, userName, password))
				{
					int option = menu(
							"1 .. Uložit vše do SQL\n"+
							"2 .. Naèíst vše z SQL\n"+
							"3 .. Vymazání osoby z SQL\n"+
							"4 .. Naètení osoby z SQL\n"+
							"5 .. Zpìt\n", 5);
					switch (option)
					{ // tier 1 menu - sql - start ////////////////////////////////////////////////
						case 1: // uložit vše
						{
							if (conn.ulozVse(skola))
								System.out.println("Úspìch");
							else
								System.out.println("Nepodaøilo se uložit data");
						}
							break;
						case 2: // naèíst vše
						{
							if (conn.nactiVse(skola))
								System.out.println("Úspìch");
							else
								System.out.println("Nepodaøilo se naèíst data");
						}
							break;
						case 3: // vymazání 1 osoby
						{
							System.out.println("Zadejte ID osoby k vymazání");
							int id = scanInt();
							if (conn.vymazOsobuSQL(skola, id))
								System.out.println("Žádost byla SQL databází zpracována");
							else
								System.out.println("Chyba pøi kontaktování databáze");
						}
							break;
						case 4: // naètení 1 osoby
						{
							System.out.println("Zadej ID osoby k nacteni");
							int id = scanInt();
							if (conn.nactiOsobuSQL(skola, id))
								System.out.println("Žádost byla SQL databází zpracována");
							else
								System.out.println("Chyba pøi kontaktování databáze");
							
						}
							break;		
					} // tier 1 menu - sql - end //////////////////////////////////////////////////
					conn.disconnect();
				}
				else
					System.out.println("Spojení s databází se nezdaøilo.");
			}
				break;

			case 5: // tier 0 - leave
			{
				leave = true;
				System.out.println("Vytvoøeno Markem Szymutkem za použití referenèních materiálù uèitele Ing. Jiøího Pøinosila, Ph.D.\n");
				break;

			}
		}
	}
}	
	//###############################################POMOCNÉ FUNKCE################################################
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
			String date = scanWord();
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
