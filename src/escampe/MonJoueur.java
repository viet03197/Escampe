package escampe;
//import java.util.Scanner;
import modeles.EscampeBoard;

public class MonJoueur implements IJoueur {
	private final static int BLANC = -1;
	private final static int NOIR = 1;
	private int maCouleur;
	private int couleurEnnemi;
	private String binoNom;
	private EscampeBoard board = new EscampeBoard();
	
	public void initJoueur(int couleur) {
		maCouleur = couleur;
		if (couleur == BLANC) binoNom = "BLANC";
		else binoNom = "NOIR";
		if (maCouleur == BLANC) couleurEnnemi = NOIR;
		else couleurEnnemi = BLANC;
	}
	
	public int getNumJoueur() {
		return maCouleur;
	}
	
	public String choixMouvement() {
		String coup;
		int val = 0;
		if (board.getPos1()[EscampeBoard.iJoueurIndiceToMonIndice(maCouleur)][0] == -1) {
			return "xxxxx";
		}
		if (board.indiceSetJoueur(maCouleur)) {
			String[] coups = board.possiblesMoves(EscampeBoard.intToStringSupplementaire(maCouleur));
			for (String x : coups) {
				System.out.print(x + " | ");
			}
			System.out.println();
			System.out.println("Cest le joueur " + this.binoName() + " qui choisit un coup : ");
			int[] h = this.getHeuristique(coups, maCouleur, 0);
			if (coups.length > 0) {
				coup = coups[this.maxArrParIndice(h)];
				val = this.maxArr(h); 
			}
			else coup = "E";
		}
		else {
			System.out.println("Je veux placer les pieces !");
			if (couleurEnnemi == NOIR) {
				if (board.topIsFree()) coup = EscampeBoard.coupParDefaut;
				else coup = EscampeBoard.coupParDefautBas;
			}
			else {
				coup = EscampeBoard.coupParDefaut;
			}
		}
		if (val != 0) System.out.println("J'ai choisi le coup suivant : " + coup + " avec " + val + " pts");
		System.out.println("Avant");
		System.out.println(board.toString());
		board.play(coup, EscampeBoard.intToStringSupplementaire(maCouleur));
		System.out.println("Apres mon coup :");
		System.out.println(board.toString());
		return coup;
	}
	
	public void declareLeVainqueur(int couleur) {
		if (couleur == maCouleur) {
			System.out.println("J'ai gagné !");
		}
	}
	
 	public void mouvementEnnemi(String coup) {
 		// À implementer
 		String ennemi = EscampeBoard.intToStringSupplementaire(couleurEnnemi);
 		this.board.play(coup, ennemi);
 		System.out.println(board.toString());
 		
 	}
	
	public String binoName() {
		return binoNom;
	}
	
	private int[] getHeuristique(String[] coups, int player, int prof) {
		int[] heuristique = new int[coups.length];
		int[] vaEnnemi = board.countVariation(EscampeBoard.intToStringSupplementaire(player*(-1)));
		int[] vaAmi = board.countVariation(EscampeBoard.intToStringSupplementaire(player));
		int[][] pos1 = board.getPos1();
		int[][] pos2 = board.getPos2();
		//char[][] plat = board.getPlateau();

		//int evalEnnemi = board.variationEval(vaEnnemi);
		for (int i=0; i< coups.length; i++) {
			EscampeBoard b2 = board.copy();
			int res = 0;
			int x1 = board.charToInt(coups[i].charAt(1));
			int y1 = board.charToInt(coups[i].charAt(0));
			int x2 = board.charToInt(coups[i].charAt(4));
			int y2 = board.charToInt(coups[i].charAt(3));
			
			int distanceAvecLicorne = Math.abs(x2-pos1[EscampeBoard.iJoueurIndiceToMonIndice(player*(-1))][0]) + 
									  Math.abs(y2-pos2[EscampeBoard.iJoueurIndiceToMonIndice(player*(-1))][0]);
			
			//System.out.println("Cal" + board.indiceToMove(x1, y1, x2, y2));
			
			int avant = EscampeBoard.LISERE[x1][y1];
			int apres = EscampeBoard.LISERE[x2][y2];
			
			b2.play(coups[i], EscampeBoard.intToStringSupplementaire(player));
			if (b2.gameOver()) {
				heuristique[i] = Integer.MAX_VALUE/2;
				break;
			}
			
			int adverse = player*(-1);
			if (prof == 0) {
				int tmpA[] = this.getHeuristique(b2.possiblesMoves(EscampeBoard.intToStringSupplementaire(adverse)), adverse, prof+1);
				//System.out.println(tmpA.toString());
				int tmp = this.maxArr(tmpA);
				if (tmp == Integer.MAX_VALUE/2 ) {		
					heuristique[i] = Integer.MIN_VALUE/2;
					continue;
				}
			}
			
			if (vaAmi[avant] > 2) res += 150;
			else if (vaAmi[avant] < 2) res -= 100;
			
			if (vaAmi[apres]+1 > 2) res -= 100;
			else if (vaAmi[apres]+1 < 2) res += 150;
			
			if (vaEnnemi[apres] == 0) res += 1000;
			else 
				if (vaEnnemi[apres] > 4) res -= 200;
				else
					if (vaEnnemi[apres] > 2) res -= 100;
			
			res -= distanceAvecLicorne*25;
			//System.out.println("res  " + res);
			heuristique[i] = res;
		}
		return heuristique;
	}
	
	private int maxArr(int[] arr) {
		if (arr.length == 0) return Integer.MIN_VALUE/2;
		int res = arr[0];
		for (int i = 0; i<arr.length; i++) {
			if (res < arr[i]) res = arr[i];
		}
		return res;
	}
		
	private int maxArrParIndice(int[] arr) {
		int res = 0;
		for (int i = 0; i<arr.length; i++) {
			if (arr[res] < arr[i]) res = i;
		}
		return res;
	}
	
}
