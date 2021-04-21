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
		try //Na��t�n� p�ihla�ovac�ch �daj� k SQL ze souboru
		{
			FileReader DBC = new FileReader(new File("DBConfig.txt"));
			BufferedReader DBConfig = new BufferedReader(DBC);
			path = DBConfig.readLine();
			String[] pole = path.split(":");
			userName = DBConfig.readLine();
			if (pole.length == 2)
			{
				path = pole[1];
				System.out.println("Cesta k datab�zi na�tena �sp�n�.");
			}
			else
			{
				path = "myDB.db";
				System.out.println("Byla nastavena defaultn� cesta k datab�zi.");
			}
			pole = userName.split(":");
			if (pole.length == 2)
			{
				userName = pole[1];
				System.out.println("Jm�no na�teno �sp�n�.");
			}
			else
				System.out.println("Bylo nastaveno defaultn� jm�no");
			password = DBConfig.readLine();
			pole = password.split(":");
			if (pole.length == 2)
			{
				password = pole[1];
				System.out.println("Heslo na�teno �sp�n�.");
			}
			else
				System.out.println("Bylo nastaveno defaultn� heslo");
			DBConfig.close();
			DBC.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("�daje k p�ipojeni do SQL se nepoda�ilo na��st.");
		}
		boolean leave = false;
		while(!leave) //tier 0 menu //////////////////////////////////////////////////////////////////////////////////////////
		{
			switch(menu(
					"1 .. P�id�n� nov�ho �lov�ka\n"+
					"2 .. Informace z Java datab�ze\n"+
					"3 .. Manipulace s Java datab�z�\n"+
					"4 .. SQL\n"+
					"5 .. Ukon�it aplikaci\n", 5))
			{
			case 1: //tier 0 - p�id�n�
			{
				int option;
				if (skola.existujiUcitele())
				{
					option = menu(
							"1 .. P�id�n� studenta\n"+
							"2 .. P�id�n� u�itele\n"+
							"3 .. Zpet", 3);
				}
				else
					option = 2;
				switch(option)
				{ //tier 1 menu - p�id�n� - start ////////////////////////////////////////////////
					case 1: //student
					{
						HashSet<Ucitel> ucitele = new HashSet<>();
						int id = 0;
						do
						{
							System.out.println("Zadejte ID u�itele tohoto studenta, pokud dal��ho u�itele p�idat nechcete, zadejte 0.");
							id = scanInt();
							Osoba ucitel = skola.getOsoba(id);
							if (ucitel instanceof Ucitel)
								ucitele.add((Ucitel)ucitel);
							else if (id != 0)
								System.out.println("Neexistuj�c� ID u�itele. U�itel nemohl byt studentovi p�idan.");
						} while (id != 0);
						System.out.println("Zadejte jm�no studenta");
						String jmeno = scanWord();
						System.out.println("Zadejte p��jmen�");
						String prijmeni = scanWord();
						int studID = skola.addStudent(jmeno, prijmeni, scanLocalDate(), ucitele); //ma na starosti i spojeni s listem studentu ucitele
						System.out.println("Byl p�id�n student. ID: "+studID);
						break;
					}
					case 2: //u�itel
					{
						System.out.println("Zadejte jm�no u�itele");
						String jmeno = scanWord();
						System.out.println("Zadejte p��jmen�");
						String prijmeni = scanWord();
						int ucID = skola.addUcitel(jmeno, prijmeni, scanLocalDate());
						System.out.println("Byl p�id�n u�itel. ID: "+ucID);
						break;
					}
				} //tier 1 menu - p�id�n� - end	///////////////////////////////////////////////////	
				break;
			}
			case 2: //tier 0 - tisk
			{
				int option = menu(
						"1 .. Tisk cel� datab�ze podle ID\n"+
						"2 .. Tisk cel� datab�ze podle abecedn�ho �azen�\n"+
						"3 .. �daje o �lov�ku podle ID\n"+
						"4 .. U�itel� ��ka, jeho� ID zad�te\n"+
						"5 .. Tisk v�ech u�itel� podle po�tu ��k�\n"+
						"6 .. Tisk ��k� u�itele podle pr�m�ru\n"+
						"7 .. Zobrazit v�daje �koly\n"+
						"8 .. Zp�t\n", 8);
				switch (option)
				{ // tier 1 menu - tisk - start ////////////////////////////////////////////////////
					case 1: //podle ID
						skola.tiskDatabaze();
						break;
						
					case 2: //podle abecedy
						skola.tiskVsichniAbecedne();
						break;
						
					case 3: //info o osob�
					{
						System.out.println("Zadejte ID po�adovan� osoby");
						int id = scanInt();
						Osoba temp = skola.getOsoba(id);
						if (temp instanceof Student)
						{
							System.out.format("Student, ID: "+temp.getID()+", jm�no a p��jmen�: "+temp.getJmeno()+" "+temp.getPrijmeni()+
									", datum narozen�: "+temp.getDatumNar()+"\nStudijn� pr�m�r: %.2f, finan�n� odm�ny: %.2f\n", ((Student) temp).getPrumer(), temp.getOdmena());
						}
						else if(temp instanceof Ucitel)
						{
							System.out.format("U�itel, ID: "+temp.getID()+", jm�no a p��jmen�: "+temp.getJmeno()+" "+temp.getPrijmeni()+
									", datum narozen�: "+temp.getDatumNar()+"\nFinan�n� odm�ny : %.2f\n", ((Ucitel)temp).getCistaOdmena());
							System.out.println("��ci tohoto u�itele:");
							for (Student stud: ((Ucitel) temp).getStudenti())
								System.out.println(stud.getJmeno()+" "+stud.getPrijmeni()+", ID: "+stud.getID());
						}
						else
							System.out.println("Osobu se v datab�zi nepoda�ilo nal�zt.");
						break;
					}
						
					case 4: //u�itel� ��ka
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
					case 5: //u�itel� podle po�tu student�
						skola.tiskUcitelu();
						break;
					case 6: //studenti jednoho u�itele
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
							System.out.println("Neexistuje u�itel s takov�m ID.");
						break;
					}
					case 7: //v�daje
						System.out.format("Celkov� v�daje �koly pro u�itele �in� %.2f K�.\n",skola.vydajeZaUcitele());
						System.out.format("V�daje za stipendia student� ��n� %.2f K�,\n", skola.vydajeZaStudenty());
						break;
				} // tier 1 menu - tisk - end ///////////////////////////////////////////////
				
				break;
			}
			case 3: //tier 0 - manipulace s datab�z�
				{ 
					int option = menu(
							"1 .. Ozn�mkovat studenta\n"+
							"2 .. Propustit �lov�ka\n"+
							"3 .. P�id�n� / odebr�n� studenta u�iteli\n"+
							"4 .. Zp�t\n", 4);
					switch (option)
					{ // tier 1 menu - manipulace - start ///////////////////////////////////
						case 1: // ozn�mkovat studenta
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
									System.out.println("Zn�mka zvolena ze �patn�ho rozsahu!");
							}
							else
								System.out.println("Student s takov�m ID nebyl nalezen");
						}
							break;
						case 2: // propustit �lov�ka
						{
							System.out.println("Zadejte ID cloveka k odstraneni");
							int id = scanInt();
							Osoba temp = skola.getOsoba(id);
							if (temp != null)
							{
								System.out.print("Opravdu chcete odstranit tuto osobu z datab�ze?\n"
										+temp.getJmeno()+" "+temp.getPrijmeni()+", "+temp.getClass().getSimpleName()
										+"\nPotvr�te zad�n�m ��sla 1:\n");
								if ((scanInt()) == 1)
									if (skola.remove(id))
										System.out.println("Byl �sp�n� odstran�n z�znam");
							}
							else
								System.out.println("Osoba nenalezena.");
						}
							break;
						case 3: // p�esun student� mezi u�iteli
						{
							System.out.println("Zadejte ID studenta");
							int idS = scanInt();
							Osoba student = skola.getOsoba(idS);
							if (student instanceof Student)
							{
								System.out.println("Zadejte ID u�itele");
								int idU = scanInt();
								Osoba ucitel = skola.getOsoba(idU);
								if (ucitel instanceof Ucitel)
								{
									Student temp = (Student)student;
									switch(menu(
											"1 .. P�id�n� studenta\n"+
											"2 .. Odebr�n� studenta\n"+
											"3 .. Zp�t\n", 3))
									{ // tier 2 menu - manipulace se studenty - start //////
									case 1:
										((Ucitel) ucitel).studentDoSeznamu(temp);
										System.out.println("U�itel "+ucitel.getPrijmeni()+" u�� studenta "+student.getPrijmeni()+".");
										break;
									case 2:
										if (((Ucitel) ucitel).removeStudent(temp))
											System.out.println("Student "+temp.getPrijmeni()+" byl odebr�n u�iteli "+ucitel.getPrijmeni()+".");
										else
											System.out.println("Student nebyl nalezen v seznamu tohoto u�itele.");
										break;
									} // tier 2 menu - manipulace se studenty - end ////////
								}
								else
									System.out.println("U�itel s takov�m ID nebyl nalezen.");
							}
							else
								System.out.println("Student s takov�m ID nebyl nalezen.");	
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
							"1 .. Ulo�it v�e do SQL\n"+
							"2 .. Na��st v�e z SQL\n"+
							"3 .. Vymaz�n� osoby z SQL\n"+
							"4 .. Na�ten� osoby z SQL\n"+
							"5 .. Zp�t\n", 5);
					switch (option)
					{ // tier 1 menu - sql - start ////////////////////////////////////////////////
						case 1: // ulo�it v�e
						{
							if (conn.ulozVse(skola))
								System.out.println("�sp�ch");
							else
								System.out.println("Nepoda�ilo se ulo�it data");
						}
							break;
						case 2: // na��st v�e
						{
							if (conn.nactiVse(skola))
								System.out.println("�sp�ch");
							else
								System.out.println("Nepoda�ilo se na��st data");
						}
							break;
						case 3: // vymaz�n� 1 osoby
						{
							System.out.println("Zadejte ID osoby k vymaz�n�");
							int id = scanInt();
							if (conn.vymazOsobuSQL(skola, id))
								System.out.println("��dost byla SQL datab�z� zpracov�na");
							else
								System.out.println("Chyba p�i kontaktov�n� datab�ze");
						}
							break;
						case 4: // na�ten� 1 osoby
						{
							System.out.println("Zadej ID osoby k nacteni");
							int id = scanInt();
							if (conn.nactiOsobuSQL(skola, id))
								System.out.println("��dost byla SQL datab�z� zpracov�na");
							else
								System.out.println("Chyba p�i kontaktov�n� datab�ze");
							
						}
							break;		
					} // tier 1 menu - sql - end //////////////////////////////////////////////////
					conn.disconnect();
				}
				else
					System.out.println("Spojen� s datab�z� se nezda�ilo.");
			}
				break;

			case 5: // tier 0 - leave
			{
				leave = true;
				System.out.println("Vytvo�eno Markem Szymutkem za pou�it� referen�n�ch materi�l� u�itele Ing. Ji��ho P�inosila, Ph.D.\n");
				break;

			}
		}
	}
}	
	//###############################################POMOCN� FUNKCE################################################
	static Scanner sc = new Scanner(System.in);
	
	static int menu(String options, int exitOption)
	{
		int opt = 0;
		while (true)
		{
			System.out.println("Zvolte mo�nost");
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
			System.out.println("Zadejte datum narozen� ve form�tu yyyy-MM-dd, dopl�te nulami pr�zdn� m�sta");
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
				System.out.println("Pros�m, zadejte cel� ��slo");
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
				System.out.println("Prosim, zadejte destinn� ��slo");
				sc.next();
			}
		}
	}
}
