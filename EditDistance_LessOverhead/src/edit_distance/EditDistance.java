package edit_distance;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

public class EditDistance implements Callable<Integer> {

	private int threadNumber;
	private int[][] table;

	private String stringaRighe;
	private String stringaColonne;

	public EditDistance(int threadNumber, int[][] table, String stringaRighe, String stringaColonne) {
		this.threadNumber = threadNumber;
		this.table = table;
		this.stringaRighe = stringaRighe;
		this.stringaColonne = stringaColonne;

		if (threadNumber == 1) {
			inizializzaMatriceAvalNegativi();
			for (int i = 0; i < stringaRighe.length(); i++)
				table[i][0] = i; //// inizializza prima colonna
			for (int j = 0; j < stringaColonne.length(); j++)
				table[0][j] = j; //// inizializza prima riga
		}

	}

	public EditDistance() {
		// TODO Auto-generated constructor stub
	}

	public void inizializzaMatriceAvalNegativi() {

		for (int i = 0; i < stringaRighe.length(); i++) {
			for (int j = 0; j < stringaColonne.length(); j++) {

				table[i][j] = -1;

			}
		}

	}

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

		String stringaRighe = InteractionUser.readArticle("righe");
		String stringaColonne = InteractionUser.readArticle("Colonne");

		EditDistance ed = new EditDistance();
		ed.manageThreadsDiagonal(stringaRighe, stringaColonne);

		System.out.println("inizio colonne");
		ed.manageThreadsColumn(stringaRighe, stringaColonne);

	}

	public int manageThreadsColumn(String stringaRighe, String stringaColonne)
			throws InterruptedException, ExecutionException, IOException {

		int table[][] = new int[stringaRighe.length() + 1][stringaColonne.length() + 1];
		Future<Integer> future = null;

		ExecutorService exServFixed = Executors.newFixedThreadPool(4);
		long inizio = System.currentTimeMillis();

		for (int i = 1; i < 5; i++) {
			future = exServFixed.submit(new EditDistance(i, table, stringaRighe, stringaColonne));
		}

		while (!future.isDone())
			;
		System.out.println("L'ultimo Thread ha completato il suo lavoro: " + future.get());

		long fine = System.currentTimeMillis();
		System.err.println("Distance equeal to: " + table[stringaRighe.length() - 1][stringaColonne.length() - 1]);
		System.out.println("Tempo di esecuzione " + (fine - inizio));
		int dim = stringaRighe.length()* stringaColonne.length();
		 InteractionUser.append_Article(String.valueOf(fine-inizio) + " " + dim,
			   "Colonna Dim Velocità");
		return 0;
	}

	
	
	///Algoritmo usato per colonne presente in 
	public int manageThreadsDiagonal(String stringaRighe, String stringaColonne)
			throws InterruptedException, ExecutionException, IOException {

		Future<Integer> future = null;
		ExecutorService exServFixed = Executors.newFixedThreadPool(4);

		int tableDiagonal[][] = new int[stringaRighe.length() + 1][stringaColonne.length() + 1];

		int counterColumns = 1;

		long inizio = System.currentTimeMillis();

		while (counterColumns < 5) {
			future = exServFixed
					.submit(new editDistanceDiagonal(stringaRighe, stringaColonne, counterColumns, tableDiagonal, 0));
			counterColumns++;

		}

		while (!future.isDone())
			;
		future.get();


		int counterRighe = 1;

		exServFixed = Executors.newFixedThreadPool(4);

		while (counterRighe < 5) {
			future = exServFixed
					.submit(new editDistanceDiagonal(stringaRighe, stringaColonne, counterRighe, tableDiagonal, 1));

			counterRighe++;
		}

		while (!future.isDone())
			;
		System.out.println("L'ultimo Thread ha completato il suo lavoro: " + future.get());

		long fine = System.currentTimeMillis();

		int dim = stringaRighe.length() * stringaColonne.length();

		System.out.println("Tempo di esecuzione diagonale " + (fine - inizio));
		System.err.println(
				"Distance equeal to: " + tableDiagonal[stringaRighe.length() - 1][stringaColonne.length() - 1]);
		InteractionUser.append_Article(String.valueOf(fine - inizio) + " " + dim, "Diagonale Dim Velocità");

		return 0;

	}

	
	////Algoritmo usato per colonne
	public int distance(final String s1, final String s2, int table[][], int threadNumber) throws InterruptedException {

		while (threadNumber <= s1.length()) {

			for (int colonna = 1; colonna < table[threadNumber].length; colonna++) {

				while (table[threadNumber - 1][colonna] == -1 || table[threadNumber][colonna - 1] == -1
						|| table[threadNumber - 1][colonna - 1] == -1) {
					Thread.yield();
				}

				int del = table[threadNumber - 1][colonna] + 1;
				int ins = table[threadNumber][colonna - 1] + 1;
				int rep = table[threadNumber - 1][colonna - 1]
						+ (s1.charAt(threadNumber - 1) == s2.charAt(colonna - 1) ? 0 : 1);
				table[threadNumber][colonna] = Math.min(del, Math.min(ins, rep));

				
			

			}
			threadNumber = threadNumber + 4;

		}
	

		return table[s1.length()][s2.length()];
	}

	@Override

	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		return distance(stringaRighe, stringaColonne, table, threadNumber);
	}
}