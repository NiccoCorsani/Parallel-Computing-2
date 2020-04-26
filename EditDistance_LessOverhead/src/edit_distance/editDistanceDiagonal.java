package edit_distance;

import java.util.concurrent.Callable;

public class editDistanceDiagonal implements Callable<Integer> {

	private int ThreadNumber;

	private static int[][] table;

	private String stringaRighe;
	private String stringaColonne;
	private int costo = 1;
	private int iniziaRighe = 0;

	private static Integer columnFinish = 0;

	public editDistanceDiagonal(String stringaRighe, String stringaColonne, int ThreadNumber, int[][] table,
			int iniziaRighe) {

		this.ThreadNumber = ThreadNumber;
		this.table = table;
		this.stringaRighe = stringaRighe;
		this.stringaColonne = stringaColonne;
		this.iniziaRighe = iniziaRighe;
		//// inizializza solo la prima volta
		if (ThreadNumber == 1 && iniziaRighe != 1) {
			/// la "a" serve per distiguire quando il contatore riparte dalla riga 1
			this.inizializzaMatriceAvalNegativi();
			for (int i = 0; i < stringaRighe.length(); i++)
				table[i][0] = i; //// inizializza prima colonna
			for (int j = 0; j < stringaColonne.length(); j++)
				table[0][j] = j; //// inizializza prima riga

		}

	}

	public void inizializzaMatriceAvalNegativi() {

		for (int i = 0; i < stringaRighe.length(); i++) {
			for (int j = 0; j < stringaColonne.length(); j++) {

				table[i][j] = -1;

			}
		}

	}

	public int distanceDiagonal() throws InterruptedException {

		int counter = ThreadNumber;

		int min = 0;

		if (iniziaRighe != 1) {
			while (counter <= stringaColonne.length()) {

				/// qui il thread number rappresenta la colonna

				for (int i = 1; ThreadNumber >= 1; ThreadNumber--) {

					while (table[i - 1][ThreadNumber] == -1 || table[i][ThreadNumber - 1] == -1
							|| table[i - 1][ThreadNumber - 1] == -1) 
						Thread.yield();
					

					

					int costo = 1;

					if (i >= stringaRighe.length())
						break; 

					try {
						if (stringaRighe.charAt(i) == stringaColonne.charAt(ThreadNumber))
							costo = 0;
					} catch (Exception e) {
						continue;   
					}

					int del = table[i - 1][ThreadNumber] + 1; //// quello sopra
					int ins = table[i][ThreadNumber - 1] + 1; //// a sinistra
					int rep = table[i - 1][ThreadNumber - 1] + costo; //// Quello in diagonale sopra
					min = Math.min(del, Math.min(ins, rep)); /// inserimento

					table[i++][ThreadNumber] = min;

					
				
				}

				counter = counter + 4;
				ThreadNumber = counter;

			}
		}
		
		if (iniziaRighe == 1) {

			while (counter <= stringaRighe.length()) {

				for (int j = stringaColonne.length() - 1; ThreadNumber <= stringaRighe.length() - 1; j--) {

					try {
						while (table[ThreadNumber - 1][j] == -1 || table[ThreadNumber][j - 1] == -1
								|| table[ThreadNumber - 1][j - 1] == -1) {
							Thread.yield();
				

						}
					} catch (Exception e) {
						break;
					}
					int costo = 1;


					if (stringaRighe.charAt(ThreadNumber) == stringaColonne.charAt(j)) 
						costo = 0;

					int del = table[ThreadNumber - 1][j] + 1; 
					int ins = table[ThreadNumber][j - 1] + 1;
					int rep = table[ThreadNumber - 1][j - 1] + costo;
					min = Math.min(del, Math.min(ins, rep));  

					table[ThreadNumber][j] = min;

					ThreadNumber++;
					

				}

				counter = counter + 4;
				ThreadNumber = counter;

			}
		}
		return ThreadNumber;

	}

	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		return distanceDiagonal();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
