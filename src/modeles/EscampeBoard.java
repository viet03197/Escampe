package modeles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EscampeBoard implements Partie1 {
	/************* Constantes *************/
	public final static int TAILLE = 6;
	public final static int[][] LISERE = {	{1, 2, 2, 3, 1, 2},
											{3, 1, 3, 1, 3, 2},
											{2, 3, 1, 2, 1, 3},
											{2, 1, 3, 2, 3, 1},
											{1, 3, 1, 3, 1, 2},
											{3, 2, 2, 1, 3, 2}};
	public final static int NOIR = 0;
	public final static int BLANC = 1;
	public final static String coupParDefaut = "C1/A1/C2/D2/E2/F2";
	public final static String coupParDefautBas = "C6/A6/B5/C5/E5/F5";
	
	/************* Attributs *************/
	private char plateau[][];
	
	/** Position format : 
	 * 	pos[m][n] : m = joueur, n = 0 -> Licorne et n = 1-5 -> Paladin
	 * 	pos1 = A-F, pos2 = 1-6
	**/
	private int position1[][];
	private int position2[][];
	
	private boolean setNoir;
	private boolean setBlanc;
	
	private int dernierLisere;
	
	/************* Constructeurs *************/
	public EscampeBoard(String fileName) {
		this.plateau = new char[6][6];
		this.position1 = new int[2][6];
		this.position2 = new int[2][6];
		
		this.setNoir = false;
		this.setBlanc = false;
		this.dernierLisere = 0;
		this.setFromFile(fileName);
	}
	
	/* Constructeur par défaut
	 */
	public EscampeBoard() {
		this.plateau = new char[6][6];
		this.position1 = new int[2][6];
		this.position2 = new int[2][6];
		
		this.setNoir = false;
		this.setBlanc = false;
		this.dernierLisere = 0;
		
		for (int i = 0; i < this.plateau.length; i++) {
			for (int j = 0; j < this.plateau[0].length; j++) {
				plateau[i][j] = '-';
			}
		}
	}
	
	/************* Methodes *************/
	@Override
	public void setFromFile(String fileName) {
		// TODO Auto-generated method stub
		String tmp;
		int nbB = 1;
		int nbN = 1;
		FileReader f = null;
		
		try {
			f = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(f);
		
		try {
			tmp = br.readLine();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i=0; i<6; i++) {
			try {
				tmp = br.readLine().substring(3, 9);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			for (int j=0; j<6; j++) {
				plateau[i][j] = tmp.charAt(j);
				if (tmp.charAt(j) == 'b') {
					position1[BLANC][nbB] = i;
					position2[BLANC][nbB] = j;
					nbB++;
				}
				
				if (tmp.charAt(j) == 'n') {
					position1[NOIR][nbN] = i;
					position2[NOIR][nbN] = j;
					nbN++;
				}
				
				if (tmp.charAt(j) == 'B') {
					position1[BLANC][0] = i;
					position2[BLANC][0] = j;
				}
				
				if (tmp.charAt(j) == 'N') {
					position1[NOIR][0] = i;
					position2[NOIR][0] = j;
				}
			}
		}
		
		try {
			br.close();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (nbN == 5) {
			this.setNoir = true;
			if (nbB == 5) {
				this.setBlanc = true;
			}
		}
	}
	@Override
	public void saveToFile(String fileName) {
		// TODO Auto-generated method stub
		String bordure = "%  ABCDEF";
		FileWriter fout = null;
		
		try {
			fout = new FileWriter(fileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		BufferedWriter bw = new BufferedWriter(fout);
		
		try {
			bw.write(bordure);
			bw.newLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		for (int i=1; i<TAILLE+1; i++) {
			String tmp="0";
			tmp += Integer.toString(i) + " ";
			for (int j=0; j<TAILLE; j++) {
				tmp+= this.plateau[i-1][j];
			}
			tmp += " 0" + Integer.toString(i);
			
			try {
				bw.write(tmp);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			bw.write(bordure);
			bw.close();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean isValidMove(String move, String player) {
		if (move.length() > 5) {
			if (move.length() != 17) {
				//System.out.print(move + " ");
				//System.out.println("Un move de taille invalide !");
				return false;
			}
			
			if (player == "noir" && this.setNoir) {
				System.out.println("Les pieces de le joueur noir sont ete deja placees !");
				return false;
			}
			if (player == "blanc" && this.setBlanc) {
				System.out.println("Les pieces de le joueur blanc sont ete deja placees !");
				return false;
			}
			String[] pos = move.split("/");
			char[][] tmp = this.plateau.clone();
			boolean top = false;
			boolean bottom = false;
			for (int i=0; i<TAILLE; i++) {
				int x = this.charToInt(pos[i].charAt(1));
				int y = this.charToInt(pos[i].charAt(0));
				
				if (x == 2 || x == 3) {
					System.out.println("Seule les deux premieres lignes de chaque cote sont acceptees !");
					return false;
				}
				
				if (x <= 1) top = true;
				else bottom = true;
				
				if (top && bottom) {
					System.out.println("Les pieces doivent etre dans le meme cote !");
					return false;
				}
				
				if (tmp[x][y] == '-') {
					tmp[x][y] = player.charAt(0);
				}
				else {
					//System.out.println("Case occupe");
					return false;
				}
			}
		}
		else {
			if (move.length() != 5) {
				//System.out.println("Un move de taille invalide !");
				return false;
			}
			
			int x1 = this.charToInt(move.charAt(1));
			int y1 = this.charToInt(move.charAt(0));
			int x2 = this.charToInt(move.charAt(4));
			int y2 = this.charToInt(move.charAt(3));
			//System.out.println(y1 + " " + x1 + " " + y2 + " " + x2);
			if (x1<0 || y1<0 || x2<0 || y2<0 || x1>5 || y1>5 || x2>5 || y2>5) {
				//System.out.println("Indice invalide !");
				return false;
			}
			
			if (this.plateau[x1][y1] == 'b' && player == "noir") {
				//System.out.println("Ce n'est pas votre tour !");
				return false;
			}
			if (this.plateau[x1][y1] == 'B' && player == "noir") {
				//System.out.println("Ce n'est pas votre tour !");
				return false;
			}
			if (this.plateau[x1][y1] == 'n' && player == "blanc") {
				//System.out.println("Ce n'est pas votre tour !");
				return false;
			}
			if (this.plateau[x1][y1] == 'N' && player == "blanc") {
				//System.out.println("Ce n'est pas votre tour !");
				return false;
			}
			
			if (this.plateau[x2][y2] != '-' && this.plateau[x2][y2] != this.licorneAdversaire(player)) {
				return false;
			}
			
			int val = Math.abs(x2-x1) + Math.abs(y2-y1);
			switch (LISERE[x1][y1]) {
				case 1:
					if (val != 1) {
						//System.out.println("Case non-atteignable !");
						return false;
					}
					else return true;
				case 2:
					if (val != 2) {
						//System.out.println("val :" + val);
						//System.out.println("Case non-atteignable !");
						return false;
					}
					else {
						/** Perpendiculaire **/
						/* Horizontalement */
						if (x1 == x2) {
							if (y1 > y2) {
								if (this.plateau[x1][y1-1] != '-') {
									//System.out.println("Obstacle !");
									return false;
								}
								else return true;
							}
							else {
								if (this.plateau[x1][y1+1] != '-') {
									//System.out.println("Obstacle !");
									return false;
								}
								else return true;
							}
						}
						/* Verticalement */
						if (y1 == y2) {
							if (x1 > x2) {
								if (this.plateau[x1-1][y1] != '-') {
									//System.out.println("Obstacle !");
									return false;
								}
								else return true;
							}
							else {
								if (this.plateau[x1+1][y1] != '-') {
									//System.out.println("Obstacle !");
									return false;
								}
								else return true;
							}
						}
						/** Diagonale **/
						if (x1 > x2 && y1 > y2) {
							if (this.plateau[x1][y1-1] != '-' && this.plateau[x1-1][y1] != '-') {
								//System.out.println("Obstacle !");
								return false;
							}
							else return true;
						}
						
						if (x1 < x2 && y1 > y2) {
							if (this.plateau[x1][y1-1] != '-' && this.plateau[x1+1][y1] != '-') {
								//System.out.println("Obstacle !");
								return false;
							}
							else return true;
						}

						if (x1 < x2 && y1 < y2) {
							if (this.plateau[x1][y1+1] != '-' && this.plateau[x1+1][y1] != '-') {
								//System.out.println("Obstacle !");
								return false;
							}
							else return true;
						}
						
						if (x1 > x2 && y1 < y2) {
							if (this.plateau[x1][y1+1] != '-' && this.plateau[x1-1][y1] != '-') {
								//System.out.println("Obstacle !");
								return false;
							}
							else return true;
						}
					}
					break;
				case 3:
					if (val == 2) return false;
					else {
						if (val == 3) {
							if (x1 == x2) {
								if (y1 > y2) {
									return this.valide12(this.indiceToMove(x1, y1, x2, y1-2), player, 2);
								}
								else return this.valide12(this.indiceToMove(x1, y1, x2, y1+2), player, 2);
							}
							else
							if (y1 == y2) {
								if (x1 > x2) {
									return this.valide12(this.indiceToMove(x1, y1, x1-2, y2), player, 2);
								}
								else return this.valide12(this.indiceToMove(x1, y1, x1+2, y2), player, 2);
							}
							else 
							if (x2 == x1-1) {
								if (y1 > y2) {
									return (this.valide12(this.indiceToMove(x1, y1, x1-1, y1-1), player, 2) ||
											this.valide12(this.indiceToMove(x1, y1, x1, y1-2), player, 2)     );
								}
								else return (this.valide12(this.indiceToMove(x1, y1, x1-1, y1+1), player, 2) ||
											 this.valide12(this.indiceToMove(x1, y1, x1, y1+2), player, 2)	   );
							}
							else
							if (x2 == x1-2) {
								if (y1 > y2) {
									return (this.valide12(this.indiceToMove(x1, y1, x1-1, y1-1), player, 2) ||
											this.valide12(this.indiceToMove(x1, y1, x1-2, y1), player, 2)     );
								}
								else return (this.valide12(this.indiceToMove(x1, y1, x1-1, y1+1), player, 2) ||
											 this.valide12(this.indiceToMove(x1, y1, x1-2, y1), player, 2)	   );
							}
							else
							if (x2 == x1+1) {
								if (y1 > y2) {
									return (this.valide12(this.indiceToMove(x1, y1, x1+1, y1-1), player, 2) ||
											this.valide12(this.indiceToMove(x1, y1, x1, y1-2), player, 2)     );
								}
								else return (this.valide12(this.indiceToMove(x1, y1, x1+1, y1+1), player, 2) ||
											 this.valide12(this.indiceToMove(x1, y1, x1, y1+2), player, 2)	   );
							}
							else
							if (x2 == x1+2) {
								if (y1 > y2) {
									return (this.valide12(this.indiceToMove(x1, y1, x1+1, y1-1), player, 2) ||
											this.valide12(this.indiceToMove(x1, y1, x1+2, y1), player, 2)     );
								}
								else return (this.valide12(this.indiceToMove(x1, y1, x1+1, y1+1), player, 2) ||
											 this.valide12(this.indiceToMove(x1, y1, x1+2, y1), player, 2)	   );
							}
						}
						else {
							if (x1 == x2) {
								if (y1 > y2) {
									return ((this.valide12(this.indiceToMove(x1, y1, x1-1, y1-1), player, 2) &&
											 this.valide12(this.indiceToMove(x1, y1, x1-1, y1), player, 1)	   ) 
											||
											(this.valide12(this.indiceToMove(x1, y1, x1+1, y1-1), player, 2) &&
											 this.valide12(this.indiceToMove(x1, y1, x1+1, y1), player, 1)	   )
										   );
								}
								else return ((this.valide12(this.indiceToMove(x1, y1, x1-1, y1+1), player, 2) &&
										 	  this.valide12(this.indiceToMove(x1, y1, x1-1, y1), player, 1)	    ) 
											 ||
											 (this.valide12(this.indiceToMove(x1, y1, x1+1, y1+1), player, 2) &&
											  this.valide12(this.indiceToMove(x1, y1, x1+1, y1), player, 1)	    )
											);
							}
							else
								if (y1 == y2) {
									if (x1 > x2) {
										return ((this.valide12(this.indiceToMove(x1, y1, x1-1, y1-1), player, 2) &&
												 this.valide12(this.indiceToMove(x1, y1, x1, y1-1), player, 1)	   ) 
												||
												(this.valide12(this.indiceToMove(x1, y1, x1-1, y1+1), player, 2) &&
												 this.valide12(this.indiceToMove(x1, y1, x1, y1+1), player, 1)	   )
											   );
									}
									else return ((this.valide12(this.indiceToMove(x1, y1, x1+1, y1-1), player, 2) &&
											 	  this.valide12(this.indiceToMove(x1, y1, x1, y1-1), player, 1)	    ) 
												 ||
												 (this.valide12(this.indiceToMove(x1, y1, x1+1, y1+1), player, 2) &&
												  this.valide12(this.indiceToMove(x1, y1, x1, y1+1), player, 1)	    )
												);
								}
						}
					}
					break;
			}
		}
		return false;
	}
	@Override
	public String[] possiblesMoves(String player) {
		// TODO Auto-generated method stub
		int p = this.getPlayer(player);
		ArrayList<String> res = new ArrayList<String>();
		for (int i=0; i<this.position1[p].length; i++) {
			int x = this.position1[p][i];
			int y = this.position2[p][i];
			
			if (LISERE[x][y] == dernierLisere) {
				switch (this.dernierLisere) {
				case 1:
					if (this.isValidMove(this.indiceToMove(x, y, x-1, y), player)) {
						res.add(this.indiceToMove(x, y, x-1, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+1, y), player)) {
						res.add(this.indiceToMove(x, y, x+1, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y-1), player)) {
						res.add(this.indiceToMove(x, y, x, y-1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y+1), player)) {
						res.add(this.indiceToMove(x, y, x, y+1));
					}
					break;
				case 2:
					if (this.isValidMove(this.indiceToMove(x, y, x-2, y), player)) {
						res.add(this.indiceToMove(x, y, x-2, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+2, y), player)) {
						res.add(this.indiceToMove(x, y, x+2, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y-2), player)) {
						res.add(this.indiceToMove(x, y, x, y-2));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y+2), player)) {
						res.add(this.indiceToMove(x, y, x, y+2));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x-1, y-1), player)) {
						res.add(this.indiceToMove(x, y, x-1, y-1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x-1, y+1), player)) {
						res.add(this.indiceToMove(x, y, x-1, y+1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+1, y-1), player)) {
						res.add(this.indiceToMove(x, y, x+1, y-1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+1, y+1), player)) {
						res.add(this.indiceToMove(x, y, x+1, y+1));
					}
					break;
				case 3:
					if (this.isValidMove(this.indiceToMove(x, y, x-1, y), player)) {
						res.add(this.indiceToMove(x, y, x-1, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+1, y), player)) {
						res.add(this.indiceToMove(x, y, x+1, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y-1), player)) {
						res.add(this.indiceToMove(x, y, x, y-1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y+1), player)) {
						res.add(this.indiceToMove(x, y, x, y+1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x-3, y), player)) {
						res.add(this.indiceToMove(x, y, x-3, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+3, y), player)) {
						res.add(this.indiceToMove(x, y, x+3, y));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y-3), player)) {
						res.add(this.indiceToMove(x, y, x, y-3));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x, y+3), player)) {
						res.add(this.indiceToMove(x, y, x, y+3));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x-2, y-1), player)) {
						res.add(this.indiceToMove(x, y, x-2, y-1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x-2, y+1), player)) {
						res.add(this.indiceToMove(x, y, x-2, y+1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x-1, y-2), player)) {
						res.add(this.indiceToMove(x, y, x-1, y-2));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x-1, y+2), player)) {
						res.add(this.indiceToMove(x, y, x-1, y+2));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+1, y-2), player)) {
						res.add(this.indiceToMove(x, y, x+1, y-2));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+1, y+2), player)) {
						res.add(this.indiceToMove(x, y, x+1, y+2));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+2, y-1), player)) {
						res.add(this.indiceToMove(x, y, x+2, y-1));
					}
					if (this.isValidMove(this.indiceToMove(x, y, x+2, y+1), player)) {
						res.add(this.indiceToMove(x, y, x+2, y+1));
					}
				}
			}
			else
				if (this.dernierLisere == 0) {
					// Lisere 1
					if (LISERE[x][y] == 1) {
						if (this.isValidMove(this.indiceToMove(x, y, x-1, y), player)) {
							res.add(this.indiceToMove(x, y, x-1, y));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+1, y), player)) {
							res.add(this.indiceToMove(x, y, x+1, y));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x, y-1), player)) {
							res.add(this.indiceToMove(x, y, x, y-1));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x, y+1), player)) {
							res.add(this.indiceToMove(x, y, x, y+1));
						}
					}
					// Lisere 2
					else if (LISERE[x][y] == 2) {
						if (this.isValidMove(this.indiceToMove(x, y, x-2, y), player)) {
							res.add(this.indiceToMove(x, y, x-2, y));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+2, y), player)) {
							res.add(this.indiceToMove(x, y, x+2, y));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x, y-2), player)) {
							res.add(this.indiceToMove(x, y, x, y-2));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x, y+2), player)) {
							res.add(this.indiceToMove(x, y, x, y+2));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x-1, y-1), player)) {
							res.add(this.indiceToMove(x, y, x-1, y-1));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x-1, y+1), player)) {
							res.add(this.indiceToMove(x, y, x-1, y+1));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+1, y-1), player)) {
							res.add(this.indiceToMove(x, y, x+1, y-1));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+1, y+1), player)) {
							res.add(this.indiceToMove(x, y, x+1, y+1));
						}
					}
					else {
						// Lisere 3
						if (this.isValidMove(this.indiceToMove(x, y, x-3, y), player)) {
							res.add(this.indiceToMove(x, y, x-3, y));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+3, y), player)) {
							res.add(this.indiceToMove(x, y, x+3, y));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x, y-3), player)) {
							res.add(this.indiceToMove(x, y, x, y-3));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x, y+3), player)) {
							res.add(this.indiceToMove(x, y, x, y+3));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x-2, y-1), player)) {
							res.add(this.indiceToMove(x, y, x-2, y-1));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x-2, y+1), player)) {
							res.add(this.indiceToMove(x, y, x-2, y+1));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x-1, y-2), player)) {
							res.add(this.indiceToMove(x, y, x-1, y-2));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x-1, y+2), player)) {
							res.add(this.indiceToMove(x, y, x-1, y+2));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+1, y-2), player)) {
							res.add(this.indiceToMove(x, y, x+1, y-2));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+1, y+2), player)) {
							res.add(this.indiceToMove(x, y, x+1, y+2));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+2, y-1), player)) {
							res.add(this.indiceToMove(x, y, x+2, y-1));
						}
						if (this.isValidMove(this.indiceToMove(x, y, x+2, y+1), player)) {
							res.add(this.indiceToMove(x, y, x+2, y+1));
						}
					}
				}
		}
		String[] res2 = new String[res.size()];
		res.toArray(res2);
		return res2;
	}
	@Override
	public void play(String move, String player) {
		// TODO Auto-generated method stub
		//System.out.println(this.isValidMove(move, player));
		//if (this.isValidMove(move, player)) {
			//System.out.println("Licorne");
			if (move.length() == 5) {
				int x1 = this.charToInt(move.charAt(1));
				int y1 = this.charToInt(move.charAt(0));
				int x2 = this.charToInt(move.charAt(4));
				int y2 = this.charToInt(move.charAt(3));
			
				int p = this.getPlayer(player);
				for (int i=0; i<this.position1[p].length; i++) {
					if (this.position1[p][i] == x1 && this.position2[p][i] == y1) {
						this.position1[p][i] = x2;
						this.position2[p][i] = y2;
					
						if (this.plateau[x2][y2] == this.licorneAdversaire(player)) {
							this.position1[p^1][0] = -1;
						}
						this.plateau[x2][y2] = this.plateau[x1][y1];
						this.plateau[x1][y1] = '-';
						this.dernierLisere = LISERE[x2][y2];
					}
				}
			}
			else if (move.length() == 1) {
				
			}
			else {
				//System.out.println("Licorne");
				String[] pos = move.split("/");
				for (int i=0; i<TAILLE; i++) {
					int x = this.charToInt(pos[i].charAt(1));
					int y = this.charToInt(pos[i].charAt(0));
					
					if (i == 0) {
						this.plateau[x][y] = player.toUpperCase().charAt(0);
						this.position1[this.getPlayer(player)][0] = x;
						this.position2[this.getPlayer(player)][0] = y;
					}
					else {
						this.plateau[x][y] = player.charAt(0);
						this.position1[this.getPlayer(player)][i] = x;
						this.position2[this.getPlayer(player)][i] = y;
					}
				}
				if (player == "noir") this.setNoir = true;
				else this.setBlanc = true;
				//System.out.println(this.setBlanc + " " + this.setNoir);
			}
		//}
	}
	@Override
	public boolean gameOver() {
		// TODO Auto-generated method stub
		if (this.position1[0][0] == -1 || this.position1[1][0] == -1) return true;
		else return false;
	}
	
	/************* Autres methodes *************/
	public String indiceToMove(int x1, int y1, int x2, int y2) {
		String res ="";
		char p = (char) (y1+65);
		char s = (char) (y2+65);
		res += p;
		res += Integer.toString(x1+1);
		res += "-";
		res += s;
		res += Integer.toString(x2+1);
		//System.out.println(res);
		return res;
	}
	
	public char licorneAdversaire(String player) {
		if (player == "noir") return 'B';
		else return 'N';
	}
	
	public int charToInt(char c) {
		int res = (int) c;
		if (res >= 65) {
			return res-65;
		}
		else {
			return res-'0'-1;
		}
	}
	
	public int normalise(int x) {
		return x-1;
	}
	
	public int getPlayer(String x) {
		if (x == "noir") {
			return 0;
		}
		else return 1;
	}
	
	public char[][] empty(int t) {
		char[][] res = new char[t][t];
		for (int i=0; i<t; i++) {
			for (int j=0; j<t; j++) {
				res[i][j] = '-';
			}
		}
		return res;
	}
	
	public boolean valide12(String move, String player, int lisere) {
		int x1 = this.charToInt(move.charAt(1));
		int y1 = this.charToInt(move.charAt(0));
		int x2 = this.charToInt(move.charAt(4));
		int y2 = this.charToInt(move.charAt(3));
		
		if (x1<0 || y1<0 || x2<0 || y2<0 || x1>5 || y1>5 || x2>5 || y2>5) {
			//System.out.println("Indice invalide !");
			return false;
		}
		
		if (this.plateau[x2][y2] != '-') {
			return false;
		}
		
		if (lisere == 2) {
			/** Perpendiculaire **/
			/* Horizontalement */
			if (x1 == x2) {
				if (y1 > y2) {
					if (this.plateau[x1][y1-1] != '-') {
						//System.out.println("Obstacle !");
						return false;
					}
					else return true;
				}
				else {
					if (this.plateau[x1][y1+1] != '-') {
						//System.out.println("Obstacle !");
						return false;
					}
					else return true;
				}
			}
			/* Verticalement */
			else
			if (y1 == y2) {
				if (x1 > x2) {
					if (this.plateau[x1-1][y1] != '-') {
						//System.out.println("Obstacle !");
						return false;
					}
					else return true;
				}
				else {
					if (this.plateau[x1+1][y1] != '-') {
						//System.out.println("Obstacle !");
						return false;
					}
					else return true;
				}
			}
			/** Diagonale **/
			else
			if (x1 > x2 && y1 > y2) {
				if (this.plateau[x1][y1-1] != '-' && this.plateau[x1-1][y1] != '-') {
					//System.out.println("Obstacle !");
					return false;
				}
				else return true;
			}
			else
			if (x1 < x2 && y1 > y2) {
				if (this.plateau[x1][y1-1] != '-' && this.plateau[x1+1][y1] != '-') {
					//System.out.println("Obstacle !");
					return false;
				}
				else return true;
			}
			else
			if (x1 < x2 && y1 < y2) {
				if (this.plateau[x1][y1+1] != '-' && this.plateau[x1+1][y1] != '-') {
					//System.out.println("Obstacle !");
					return false;
				}
				else return true;
			}
			else
			if (x1 > x2 && y1 < y2) {
				if (this.plateau[x1][y1+1] != '-' && this.plateau[x1-1][y1] != '-') {
					//System.out.println("Obstacle !");
					return false;
				}
				else return true;
			}
		}
		else {
			return true;
		}
		return true;
	}
	
	public int[] countVariation(String player) {
		int[] variation = new int[4];
		for (int i = 0; i < EscampeBoard.TAILLE; i++) {
			int val = EscampeBoard.LISERE[this.position1[this.getPlayer(player)][i]]
										 [this.position2[this.getPlayer(player)][i]];
			if (val == 1) variation[1]++;
			else if (val == 2) variation[2]++;
			else if (val == 3) variation[3]++;
		}
		return variation;
	}
	
	public int variationEval(int[] vari) {
		/* Valeurs possibles 1:
		 * 2-2-2 => 0
		 * 1-1-4 => 3
		 * 1-4-1 => 6
		 * 4-1-1 => 7
		 * 1-2-3, 1-3-2 => 1
		 * 2-1-3, 3-1-2 => 2
		 * 2-3-1, 3-2-1 => 5
		 * 0-2-4, 0-4-2 => 10
		 * 2-0-4, 4-0-2 => 20
		 * 2-4-0, 4-2-0 => 50 
		 * Valeurs possibles apres 1:
		 * 
		 */
		int res = 0;
		if (vari[1] == vari[2] && vari[2] == vari[3]) return 0;
		if (vari[1] == 0) res += 10;
		else if (vari[1] == 1) res += 1;
		if (vari[2] == 0) res += 20;
		else if (vari[2] == 1) res += 2;
		if (vari[3] == 0) res += 50;
		else if (vari[3] == 1) res += 5;
		return res;
	}
	
	public boolean topIsFree() {
		boolean res = true;
		for (int i = 0; i<2; i++) {
			for (int j = 0; j<EscampeBoard.TAILLE; j++) {
				if (this.plateau[i][j] != '-') {
					return false;					
				}
			}
		}
		return res;
	}
	
	public int[][] getPos1() {
		return this.position1;
	}
	
	public int[][] getPos2() {
		return this.position2;
	}
	
	public char[][] getPlateau() {
		return this.plateau;
	}
	
	public boolean getSN() {
		return this.setNoir;
	}
	
	public boolean getSB() {
		return this.setBlanc;
	}
	
	public int getDernier() {
		return this.dernierLisere;
	}
	
	/* Utiliser pour Solo et les autres classes */
	public static int stringToIntSupplementaire(String player) {
		if (player == "noir") return 1;
		else return -1;
	}
	
	public static String intToStringSupplementaire(int player) {
		if (player == 1) return "noir";
		else return "blanc";
	}
	
	public boolean indiceSetJoueur(int player) {
		if (player == -1) {
			return this.setBlanc;
		}
		else return this.setNoir;
	}
	
	public static int iJoueurIndiceToMonIndice(int indice) {
		if (indice == -1) return EscampeBoard.BLANC;
		else return EscampeBoard.NOIR;
	}
	
	public static int monIndiceiToIJoueurIndice(int indice) {
		if (indice == EscampeBoard.BLANC) return -1;
		else return 1;
	}
	
	public EscampeBoard copy() {
		EscampeBoard b = new EscampeBoard();
		for (int i = 0; i< EscampeBoard.TAILLE; i++) {
			for (int j=0; j<EscampeBoard.TAILLE; j++) {
				b.plateau[i][j] = this.plateau[i][j];
			}
		}
		
		for (int i = 0; i< 2; i++) {
			for (int j=0; j<EscampeBoard.TAILLE; j++) {
				b.position1[i][j] = this.position1[i][j];
			}
		}
		
		for (int i = 0; i< 2; i++) {
			for (int j=0; j<EscampeBoard.TAILLE; j++) {
				b.position2[i][j] = this.position2[i][j];
			}
		}
		
		b.setBlanc = this.getSB();
		b.setNoir = this.getSN();
		b.dernierLisere = this.getDernier();
		return b;
	}
	
	public String toString() {
		String res = "  ABCDEF" + '\n';
		for (int i=0; i<TAILLE; i++) {
			res += Integer.toString(i+1) + " ";
			for (int j=0; j<TAILLE; j++) {
				res+=this.plateau[i][j];
			}
			res += '\n';
		}
		return res;
	}
	/************* Main *************/
	public static void main(String[] args) {
		String f1 = "test1.inp";
		String f2 = "test2.inp";
		EscampeBoard board = new EscampeBoard(f1);
		for (int i=0; i<TAILLE; i++) {
			for (int j=0; j<TAILLE; j++) {
				System.out.print(board.plateau[i][j]);
			}
			System.out.println();
		}
		
		for (int i=0; i<TAILLE; i++) {
			System.out.println(board.position1[1][i] + " " + board.position2[1][i]);
		}
		
		board.saveToFile(f2);
		System.out.println("====================================");
		System.out.println("1. " + board.isValidMove("B1-D1", "blanc"));
		System.out.println("====================================");
		System.out.println("2. " + board.isValidMove("B1-C1", "blanc"));
		System.out.println("====================================");
		System.out.println("3. " + board.isValidMove("A1-A2", "blanc"));
		System.out.println("====================================");
		System.out.println("4. " + board.isValidMove("E2-D4", "blanc"));
		System.out.println("====================================");
		System.out.println("5. " + board.isValidMove("E2-E5", "blanc"));
		System.out.println("====================================");
		System.out.println("6. " + board.isValidMove("E2-D2", "blanc"));
		System.out.println("====================================");
		System.out.println("7. " + board.isValidMove("E2-F2", "blanc"));
		System.out.println("====================================");
		System.out.println("8. " + board.isValidMove("F2-F4", "blanc"));
		System.out.println("====================================");
		System.out.println("9. " + board.isValidMove("F2-E1", "blanc"));
		System.out.println("====================================");
		System.out.println("10. " + board.isValidMove("E2-F2", "noir"));
		System.out.println("====================================");
		System.out.println("Un coup qui va prendre la licorne : ");
		System.out.println(board.isValidMove("B5-B2", "noir"));
		System.out.println("====================================");
		//board.dernierLisere = 2;
		String[] testPossible = board.possiblesMoves("blanc");
		System.out.println("====================================");
		for (int i=0; i<testPossible.length; i++) {
			System.out.print(testPossible[i] + " | ");
		}
		System.out.println();
		System.out.println("====================================");
		System.out.println(board.gameOver());
		board.play("B5-B2", "noir");
		for (int i=0; i<TAILLE; i++) {
			for (int j=0; j<TAILLE; j++) {
				System.out.print(board.plateau[i][j]);
			}
			System.out.println();
		}
		System.out.println(board.gameOver());
		
		EscampeBoard b2 = new EscampeBoard();
		b2.play(coupParDefaut, "noir");
		b2.play(coupParDefautBas, "blanc");
		System.out.println(b2.toString());
		b2.play("B5-B2", "blanc");
		System.out.println(b2.toString());
		b2.play("D2-D3", "noir");
		System.out.println(b2.toString());
		
		testPossible = b2.possiblesMoves("blanc");
		System.out.println("====================================");
		for (int i=0; i<testPossible.length; i++) {
			System.out.print(testPossible[i] + " | ");
		}
		System.out.println();
		System.out.println("====================================");
		b2.play("A3-C3", "blanc");
		System.out.println(b2.toString());
		
		testPossible = b2.possiblesMoves("noir");
		System.out.println("====================================");
		for (int i=0; i<testPossible.length; i++) {
			System.out.print(testPossible[i] + " | ");
		}
		System.out.println();
		System.out.println("====================================");
		b2.play("A2-A5", "noir");
		System.out.println(b2.toString());
	}
}
