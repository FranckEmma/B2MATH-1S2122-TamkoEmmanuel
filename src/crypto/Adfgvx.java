package crypto;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;

public class Adfgvx {
	private String substitutionKey;
	private String transpositionKey;
	private String regexLetter = "^([A-Z]){26}$";
	private String regexNumber = "^([0-9]){10}$";
	private String regex = "^([A-Z]|[0-9]){36}$";
	private final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	private final Pattern patternNumber = Pattern.compile("^([0-9]){1}$", Pattern.MULTILINE);
	private final Pattern patternNumbers = Pattern.compile("^([0-9]){10}$", Pattern.MULTILINE);
	private final Pattern patternLetter = Pattern.compile("^([A-Z]){26}$", Pattern.MULTILINE);
	private int[][] mGrille = new int[7][7];
	private int mLength;

	/**
	 * Constructor
	 */
	public Adfgvx(String substitutionKey, String transpositionKey) {
		this.substitutionKey = substitutionKey;
		this.transpositionKey = transpositionKey;
		initGrille();
		createGrid();
	}

	private void initGrille() {
		// mGrille[0][0]= 'A';
		mGrille[0][1] = 'A';
		mGrille[0][2] = 'D';
		mGrille[0][3] = 'F';
		mGrille[0][4] = 'G';
		mGrille[0][5] = 'V';
		mGrille[0][6] = 'X';
		mGrille[1][0] = 'A';
		mGrille[2][0] = 'D';
		mGrille[3][0] = 'F';
		mGrille[4][0] = 'G';
		mGrille[5][0] = 'V';
		mGrille[6][0] = 'X';

	}

	public boolean verifySubstitutionKey() {
		// verifier si la subKey est en majuscule
		// si la cle de substitution n'est pas valide qu'est que je fais?
//		final Matcher matcher = pattern.matcher(this.substitutionKey);
//		if (!matcher.matches())
//			return false;
//		// diviser la cle en deux : lettre et chiffre
//		String letter = "";
//		String number = "";
//		for (int i = 0; i < this.substitutionKey.length(); i++) {
//			final Matcher matcherNumber = patternNumber.matcher(this.substitutionKey.charAt(i) + "");
//			if (matcherNumber.matches()) {
//				number += this.substitutionKey.charAt(i);
//			} else {
//				letter += this.substitutionKey.charAt(i);
//			}
//		}
//		final Matcher matcherNumbers = patternNumber.matcher(number);
//		final Matcher matcherLetter = patternLetter.matcher(letter);
//		if (matcherNumbers.matches() && matcherLetter.matches())
//			return true;
		SortedSet<Character> characters = new TreeSet<>(
				Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

		String letter = "";
		String number = "";
		for (int i = 0; i < this.substitutionKey.length(); i++) {
			char mCharacter2 = Character.toUpperCase(this.substitutionKey.charAt(i));// verifier si la subKey est en
																						// majuscule
			char c = checkCharacter(mCharacter2);
			if (characters.remove(c)) {// diviser la cle en deux : lettre et chiffre
				final Matcher matcherNumber = patternNumber.matcher(c + "");
				if (matcherNumber.matches()) {
					number += c;
				} else {
					letter += c;
				}
			}

		}
		final Matcher matcherNumbers = patternNumbers.matcher(number);
		final Matcher matcherLetter = patternLetter.matcher(letter);
		if (matcherNumbers.matches() && matcherLetter.matches())
			return true;
		return false;

	}

	private void createGrid() {
		int k = 0;
		if (verifySubstitutionKey()) {
			for (int i = 1; i < mGrille.length; i++) {
				for (int j = 1; j < mGrille.length; j++) {
					mGrille[i][j] = this.substitutionKey.charAt(k);
					k++;
				}
			}
		}

	}

	private char checkCharacter(char mCharacter2) {
		switch (mCharacter2) {
		case 'À':
		case 'Â':
		case 'Ä':
			return 'A';

		case 'Ç':
			return 'C';
		case 'É':
		case 'È':
		case 'Ê':
		case 'Ë':
			return 'E';
		case 'Î':
		case 'Ï':
			return 'I';
		case 'Ô':
		case 'Ö':
			return 'O';
		case 'Ù':
		case 'Û':
		case 'Ü':
			return 'U';
		case 'Ÿ':
			return 'Y';
		default:
			return mCharacter2;
		}

	}

	/*
	 * PUBLIC METHODS
	 */

	/**
	 * Encrypts a text into an ADFGVX cryptogram. The cryptogram is formatted with
	 * an hyphen '-' after each group of 5 characters. Ex: VXVGG-GFDXD-XFDDD
	 * 
	 * @param textToEncrypt A text to encrypt
	 * @return the ADFGVX cryptogram
	 */
	public String encrypt(String textToEncrypt) {
		String msmFilter = filterMessage(textToEncrypt);

		String msmCrypte = doCryptogramme(msmFilter);
		String result = "";
		int k, cpt = 0;
		int[][] grilleTansp = createGrilleTransp(msmCrypte);
		int[] keySort = grilleTansp[0];

		List<Character> tempList = new ArrayList<>();
		for (int i = 0; i < keySort.length; i++) {
			tempList.add((char) keySort[i]);
		}
		Arrays.sort(keySort);
//		int[] indice = new int[keySort.length];
//		for (int i = 0; i < grilleTansp[0].length; i++) {
//				Character temps =(char) keySort[i];
//				indice[i]=tempList.indexOf(temps);
//						
//				}
//	
		for (int i = 0; i < grilleTansp[0].length; i++) {
			Character temps = (char) keySort[i];
			var pos = tempList.indexOf(temps);
			for (int j = 1; j < grilleTansp.length; j++) {
				result += (char) grilleTansp[j][pos];
				cpt++;
				if (cpt == 5) {
					result += "-";
					cpt = 0;
				}
			}
		}
		return result;
	}

	private int[][] createGrilleTransp(String msmCrypte) {
		String chaine = this.transpositionKey + msmCrypte;
		var t = chaine.length();
		int[][] grilles = new int[chaine.length() / this.transpositionKey.length() + 1][this.transpositionKey.length()];
		int k = 0;
		for (int i = 0; i < grilles.length; i++) {
			for (int j = 0; j < grilles[i].length && k < t; j++) {
				grilles[i][j] = chaine.charAt(k);
				k++;
			}
		}
		mLength= msmCrypte.length();
		if(msmCrypte.length()%this.transpositionKey.length()!=0) {
			
			Random random = new Random();
			for (int i = 0; i < grilles[grilles.length - 1].length; i++) {
				if (grilles[grilles.length - 1][i] == 0) {
					grilles[grilles.length - 1][i] = "ADFGVX".charAt(random.nextInt(5));
				}
			}
		}
		return grilles;
	}

	private String doCryptogramme(String result) {
		int tab[];
		String msmCrypte = "";
		for (int i = 0; i < result.length(); i++) {
			tab = findPosition(result.charAt(i));
			msmCrypte += (char) mGrille[tab[0]][0] + "" + (char) mGrille[0][tab[1]];
		}
		return msmCrypte;
	}

	private int[] findPosition(int c) {
		for (int i = 1; i < mGrille.length; i++) {
			for (int j = 1; j < mGrille.length; j++) {
				if (mGrille[i][j] == c) {
					return new int[] { i, j };
				}
			}
		}
		return new int[] {};
	}

	private String filterMessage(String textToEncrypt) {
		String result = "";
		for (int i = 0; i < textToEncrypt.length(); i++) {
			var currentChar = Character.toUpperCase(textToEncrypt.charAt(i));
			final Matcher matcherNumber = patternNumber.matcher(currentChar + "");
			if (Character.isAlphabetic(currentChar) || matcherNumber.matches()) {
				result += checkCharacter(currentChar);
			}
		}
		return result;

	}

	/**
	 * Decrypts an ADFGVX cryptogram.
	 * 
	 * @param textToDecrypt An ADFGVX cryptogram
	 * @return the decrypted text
	 */
	public String decrypt(String textToDecrypt) {
		String chaine=""; 
		String messageBuilder = removeTirer(textToDecrypt);
		int [][] grilleAlpha= createGrille(messageBuilder);
		int [][]grilleNonAl= new int [messageBuilder.length()/this.transpositionKey.length()][this.transpositionKey.length()];

		char[] keySort = new char[this.transpositionKey.length()];
		char[] keyChar = new char[this.transpositionKey.length()];

		List<Character> tempList = new ArrayList<>();
		for (int i = 0; i < keySort.length; i++) {
			keyChar[i]=this.transpositionKey.charAt(i);
			keySort[i]=this.transpositionKey.charAt(i);
		}
		Arrays.sort(keySort);
		for (int i = 0; i < keyChar.length; i++) {
			tempList.add((char) keySort[i]);
		}
		for (int i = 0; i < grilleAlpha[0].length; i++) {
			Character temps = (char) keyChar[i];
			var pos = tempList.indexOf(temps);
			for (int j = 0; j < grilleAlpha.length; j++) {
			
				grilleNonAl[j][i]= grilleAlpha[j][pos];
			
			}
		}
		for (int i = 0; i < grilleNonAl.length; i++) {
			for (int j = 0; j < grilleNonAl[i].length; j++) {
				chaine += (char) grilleNonAl[i][j];
			}
		}
		int j=0;
		String message="";
		int pos = chaine.length()-mLength;
		for (int i = 0; i < chaine.length()-pos; i++) {
			j = i;
			i++;
			message+=doMessage( chaine.charAt(j), chaine.charAt(i));
			
		}
		
		return message;
	}
	private char doMessage(char charAt, char charAt2) {
		List<Character> characters = new ArrayList(Arrays.asList(' ','A','D','F','G','V','X'));
			
		
		return (char) mGrille[characters.indexOf(charAt)][characters.indexOf(charAt2)];
	}

	private int [][] createGrille(String messageBuilder) {
		var t = messageBuilder.length();
		var t1 =this.transpositionKey.length();
		int [][] grilleTrans= new int [messageBuilder.length()/this.transpositionKey.length()][this.transpositionKey.length()];
		int k=0;
		for (int i = 0; i < grilleTrans[0].length; i++) {
			for (int j = 0; j < grilleTrans.length; j++) {
				var c = (char)messageBuilder.charAt(j);
				grilleTrans[j][i]=messageBuilder.charAt(k);
				k++;
			}
		}
		return grilleTrans;
		
	}

	private String removeTirer(final String message) {
		String messageBuilder ="";
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) != '-') {
				messageBuilder+=message.charAt(i);
			}
		}
		return messageBuilder;
	}
	/*
	 * MAIN - TESTS
	 */
	/**
	 * The main method illustrates the use of the ADFGVX class.
	 * 
	 * @param args None
	 */
	public static void main(String[] args) {
		System.out.println("ADFGVX - Exemple de l'Ã©noncÃ©");
		System.out.println();

		String substitutionKey = "BJLZ4PW7AUVI0H3Y5MK8FEXQGDO16T9NSR2C";
		String transpositionKey = "BRUTES";
		String message = "DEMANDE RENFORTS D'URGENCE";
		System.out.println("Message : " + message);
		Adfgvx cypher = new Adfgvx(substitutionKey, transpositionKey);
		String encrypted = cypher.encrypt(message);
		System.out.println("Message chiffrÃ© : " + encrypted);
		String decrypted = cypher.decrypt(encrypted);
		System.out.println("Message dÃ©chiffrÃ© : " + decrypted);

		System.out.println();
		System.out.println("-------------------------------------");
		System.out.println();

		int repeatCount = 20000000;

		System.out.println(">>> PERFORMANCE - ENCRYPT x " + repeatCount);
		long time = System.currentTimeMillis();
		for (int i = 0; i < repeatCount; i++) {
			cypher.encrypt(message);
		}
		System.out.printf("Elapsed time = %.2f seconds\n", (System.currentTimeMillis() - time) / 1000.0);
		System.out.println();

		System.out.println(">>> PERFORMANCE - DECRYPT x " + repeatCount);
		time = System.currentTimeMillis();
		for (int i = 0; i < repeatCount; i++) {
			cypher.decrypt(encrypted);
		}
		System.out.printf("Elapsed time = %.2f seconds\n", (System.currentTimeMillis() - time) / 1000.0);
	}
}
