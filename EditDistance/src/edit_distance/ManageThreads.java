package edit_distance;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ManageThreads {

	int counterColumns = 1;
	String stringaDaAnalizzare = null;
	List<Future> futureList = new ArrayList<>();
	Future<Integer> future = null;
	private String stringaRighe = "";
	private String stringaColonne = "";
	private ExecutorService exServFixed; /// LA cpu è sempre occupata: 4 thread ok UpperoBound = 1
	private ScheduledExecutorService executorScheduled;

	public ManageThreads() {
		

		
		this.stringaRighe = stringaRighe;
		this.stringaColonne = stringaColonne;

	}

	public int manageThreadsFixedSiezeParallel(String stringaRighe, String stringaColonne)
			throws InterruptedException, ExecutionException, IOException {
		long inizio = System.currentTimeMillis();
		exServFixed = Executors.newFixedThreadPool(2);
		int table[][] = new int[stringaRighe.length() + 1][stringaColonne.length() + 1];
		counterColumns = 1;
		while (counterColumns <= stringaColonne.length()) {
			/* non bloccante */
			future = exServFixed.submit(new EditDistanceParallel(stringaRighe, stringaColonne, counterColumns, table));

			counterColumns++;
		}
		while (!future.isDone())
			;
		System.out.println("L'ultimo Thread ha completato il suo lavoro: " + future.get());

		long fine = System.currentTimeMillis();
		System.out.println("Tempo di esecuzione Parallela" + (fine - inizio));

		System.err.println("Distance equeal to: " + table[stringaRighe.length()][stringaColonne.length()]);
		int LastValTable = table[stringaRighe.length()][stringaColonne.length()];

		int dim = stringaRighe.length()* stringaColonne.length();
		
		 InteractionUser.append_Article(String.valueOf(fine - inizio) + " " + String.valueOf(dim),
				  "Colonna Dim Velocità fixedThreadPool");

		return table[stringaRighe.length()][stringaColonne.length()];
	}

	public int manageThreadsScheduledThreadPool(String stringaRighe, String stringaColonne)
			throws InterruptedException, IOException {

		executorScheduled = Executors.newScheduledThreadPool(2);

		int table[][] = new int[stringaRighe.length() + 1][stringaColonne.length() + 1];

		counterColumns = 1;
		
		long inizio = System.currentTimeMillis();

		while (counterColumns <= stringaColonne.length()) {

			/* non bloccante */

			future = executorScheduled.schedule(
					(new EditDistanceParallel(stringaRighe, stringaColonne, counterColumns, table)), 1,
					TimeUnit.MILLISECONDS);

			try {
				future.get();

			} catch (InterruptedException e) {

			} catch (ExecutionException e) {

			} //// assegnamento sopra è non bloccante
			counterColumns++;

		}

		try {
			exServFixed.shutdownNow();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		long fine = System.currentTimeMillis();
		
		int dim = stringaRighe.length()* stringaColonne.length();

		 InteractionUser.append_Article(String.valueOf(fine - inizio) + " " + String.valueOf(dim),
				  "Colonna Dim Velocità scheduledThreadPool");
		
		System.out.println("Tempo di esecuzione Scheduled" + (fine - inizio));

		System.err.println("Distance equeal to: " + table[stringaRighe.length()][stringaColonne.length()]);				

		return table[stringaRighe.length()][stringaColonne.length()];

	}

	public int distance(final String s1, final String s2) throws IOException {
		/* We fill the table once, i.e. linear runtime: O(i + 1 + j + 1) */

		int table[][] = new int[s1.length() + 1][s2.length() + 1];

		
		long inizio = System.currentTimeMillis();

		for (int i = 0; i < table.length; i++) {			
			for (int j = 0; j < table[i].length; j++) {
				/// prima riga e colonna
				if (i == 0) {
					table[i][j] = j;
				} else if (j == 0) {
					table[i][j] = i;
				}
				/// prima riga e colonna

				else {
					int del = table[i - 1][j] + 1;
					int ins = table[i][j - 1] + 1;
					int rep = table[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1);
					table[i][j] = Math.min(del, Math.min(ins, rep));
				}
			}
		}
		
		
		long fine = System.currentTimeMillis();

		System.out.println("Tempo di esecuzione Sequenziale" + (fine - inizio));

		System.err.println("Distance equeal to: " + table[stringaRighe.length()][stringaColonne.length()]);		
				
		int dim = s1.length()* s2.length();

		
		 InteractionUser.append_Article(String.valueOf(fine - inizio) + " " + String.valueOf(dim),
				  "Sequential Dim Velocità");
		 
		return table[s1.length()][s2.length()];
		
	}

	public int manageThreadsDiagonal(String stringaRighe, String stringaColonne)
			throws InterruptedException, ExecutionException, IOException {

		int tableDiagonal[][] = new int[stringaRighe.length() + 1][stringaColonne.length() + 1];

		int counterColumns = 1;

		exServFixed = Executors.newFixedThreadPool(4);

		long inizio = System.currentTimeMillis();

		while (counterColumns < stringaColonne.length()) {
			future = exServFixed
					.submit(new editDistanceDiagonal(stringaRighe, stringaColonne, counterColumns, tableDiagonal));
			counterColumns++;
		}

		while (!future.isDone())
			;
		future.get();

		int counterRighe = 1;

		while (counterRighe <= stringaRighe.length()) {
			future = exServFixed
					.submit(new editDistanceDiagonal(stringaRighe, stringaColonne, counterRighe, tableDiagonal));
			counterRighe++;
		}

		while (!future.isDone())
			;
		System.out.println("L'ultimo Thread ha completato il suo lavoro: " + future.get());

		long fine = System.currentTimeMillis();

		
		int dim = stringaRighe.length()* stringaColonne.length();

		
		System.out.println("Tempo di esecuzione diagonale " + (fine - inizio));
		System.err.println("Distance equeal to: " + tableDiagonal[stringaRighe.length()-1][stringaColonne.length()-1]);
		 InteractionUser.append_Article(String.valueOf(fine-inizio) + " " + dim,
			   "Diagonale Dim Velocità");

		return 0;

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

		ManageThreads manageThreads = new ManageThreads();

		String riga = InteractionUser.readArticle("righe");

		String colonna = InteractionUser.readArticle("Colonne");

		riga = " " + riga;

		colonna = " " + colonna;
		
		
		
		
		
		
		manageThreads.distance(riga, colonna);
		
		manageThreads.manageThreadsFixedSiezeParallel(riga, colonna);

		manageThreads.manageThreadsDiagonal(riga, colonna);

		manageThreads.manageThreadsScheduledThreadPool(riga, colonna);
		
		
		
	}

}
