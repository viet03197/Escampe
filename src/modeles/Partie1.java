package modeles;

public interface Partie1 {
	/** initialise un fichier d'un plateau
	 * 	@param fileName : le nom du fichier a lire
	 */
	public void setFromFile(String fileName);
	
	/**	sauve la configuration courant dans un fichier
	 * 	@param fileName : le nom du fichier a sauvegarder
	 * 	Le format doit etre compatible avec celui utilise pour la lecture
	 */
	public void saveToFile(String fileName);
	
	/** indique si le coup <move> est valide pour le joueur <player> sur le plateau courant
	 * 	@param move : le coup a jouer,
	 * 					sous la forme "B1-D1" en general,
	 * 					sous la forme "C6/A6/B5/D5/E6/F5" pour le coup qui place les pieces
	 * 	@param player : le joueur qui joue, represente par "noir" ou "blanc"
	 * 	@return 
	 */
	public boolean isValidMove(String move, String player);
	
	/**	calcule les coups possibles pour le joueur <player> sur le plateau courant
	 * 	@param player : le joueur qui joue, represente par "noir" ou "blanc"
	 * 	@return : liste des coups possibles
	 */
	public String[] possiblesMoves(String player);
	
	/**	modifie le plateau en jouant le coup <move>
	 * 	@param move : le coup a jouer, sous la forme indiquee ci-dessus
	 * 	@param player : le joueur qui joue, represente par "noir" ou "blanc"
	 */
	public void play(String move, String player);
	
	/** vrai lorsque le plateau correspond a une fin de partie
	 */
	public boolean gameOver();
	
}
