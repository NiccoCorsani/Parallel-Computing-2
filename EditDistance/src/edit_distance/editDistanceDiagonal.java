package edit_distance;

import java.util.concurrent.Callable;

public class editDistanceDiagonal implements Callable<Integer> {

	private int ThreadNumber;

	private static int[][] table;

	private String stringaRighe;
	private String stringaColonne;
	private int costo = 1;

	private static Integer columnFinish = 0;

	private static int a = 0;

	public editDistanceDiagonal(String stringaRighe, String stringaColonne, int ThreadNumber, int[][] table) {

		this.ThreadNumber = ThreadNumber;
		this.table = table;
		this.stringaRighe = stringaRighe;
		this.stringaColonne = stringaColonne;

		//// inizializza solo la prima volta
		if (ThreadNumber == 1 && a == 0) {
			this.inizializzaMatriceAvalNegativi();
			for (int i = 0; i < stringaRighe.length(); i++)
				table[i][0] = i; //// inizializza prima colonna
			for (int j = 0; j < stringaColonne.length(); j++)
				table[0][j] = j; //// inizializza prima riga

			
			a = 1;

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

		int min = 0;

		if (columnFinish < stringaColonne.length() - 1) {

			/// qui il thread number rappresenta la colonna

			for (int i = 1; ThreadNumber >= 1; ThreadNumber--) {

				while (table[i - 1][ThreadNumber] == -1 || table[i][ThreadNumber - 1] == -1
						|| table[i - 1][ThreadNumber - 1] == -1) {
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
			
	
//// questa è la soluzione più veloce trovata confrontando con Thread.sleep e con wait notify
				}

				///// riguardare con foglio
				int costo = 1;

				if (i >= stringaRighe.length())
					break; ///// messo perchè giustamente il coso sotto da eccezione

				if (stringaRighe.charAt(i) == stringaColonne.charAt(ThreadNumber)) ////// Senza -1 per due motivi: 1.
																					////// che la prima lettera èsempre
																					////// " " quindi non va
																					////// analizzata,2.
					costo = 0;

				///// riguardare con foglio

				int del = table[i - 1][ThreadNumber] + 1; //// quello sopra
				int ins = table[i][ThreadNumber - 1] + 1; //// a sinistra
				int rep = table[i - 1][ThreadNumber - 1] + costo; //// Quello in diagonale sopra
				min = Math.min(del, Math.min(ins, rep)); /// inserimento

				table[i++][ThreadNumber] = min;

			}

		}

		/// qui il thread number rappresenta la riga

		else {

			for (int j = stringaColonne.length() - 1; ThreadNumber <= stringaRighe.length(); j--) {

				while (table[ThreadNumber - 1][j] == -1 || table[ThreadNumber][j - 1] == -1
						|| table[ThreadNumber - 1][j - 1] == -1) {
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					Thread.yield();
					//// questa è la soluzione più veloce trovata confrontando con Thread.sleep e
					//// con wait notify
				}

				int costo = 1;

				if (ThreadNumber >= stringaRighe.length())
					break; ///// messo perchè giustamente il coso sotto da eccezione
				///// era scritto male la fromula dentro il for in entrambi i for

				if (stringaRighe.charAt(ThreadNumber) == stringaColonne.charAt(j)) ////// Senza -1 per due motivi: 1.
																					////// che la prima lettera èsempre
																					////// " " quindi non va
																					////// analizzata,2.
					costo = 0;

				int del = table[ThreadNumber - 1][j] + 1; //// quello sopra
				int ins = table[ThreadNumber][j - 1] + 1; //// a sinistra
				int rep = table[ThreadNumber - 1][j - 1] + costo; //// Quello in diagonale sopra
				min = Math.min(del, Math.min(ins, rep)); /// inserimento

				table[ThreadNumber][j] = min;

				ThreadNumber++;

			}

		}

		synchronized (columnFinish) {
			columnFinish++;

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
