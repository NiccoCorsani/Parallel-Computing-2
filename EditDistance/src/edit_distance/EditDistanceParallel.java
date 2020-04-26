package edit_distance;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class EditDistanceParallel implements Callable<Integer> {

	private int ThreadNumber;

	private static int[][] table;

	private String stringaRighe;
	private String stringaColonne;
	private int costo = 1;

	public EditDistanceParallel(String stringaRighe, String stringaColonne, int ThreadNumber, int[][] table) {

		this.ThreadNumber = ThreadNumber;
		this.table = table;
		this.stringaRighe = stringaRighe;
		this.stringaColonne = stringaColonne;

		//// inizializza solo la prima volta
		if (ThreadNumber == 1) {
			this.inizializzaMatriceAvalNegativi();
			for (int i = 0; i < stringaRighe.length() + 1; i++)
				table[i][0] = i; //// inizializza prima colonna
			for (int j = 0; j < stringaColonne.length() + 1; j++)
				table[0][j] = j; //// inizializza prima riga
		}

	}

	public void inizializzaMatriceAvalNegativi() {

		for (int i = 0; i < stringaRighe.length() + 1; i++) {
			for (int j = 0; j < stringaColonne.length() + 1; j++) {

				table[i][j] = -1;

			}
		}
		

	}

	public int distanceCoulmn() throws InterruptedException {

		long inizioUnaColonna = System.currentTimeMillis();
		for (int i = 1; i < stringaRighe.length() + 1; i++) {
			costo = 1;

			while (table[i - 1][ThreadNumber] == -1 || table[i][ThreadNumber - 1] == -1
					|| table[i - 1][ThreadNumber - 1] == -1) {			
				Thread.yield();   //// questa è la soluzione più veloce trovata confrontando con Thread.sleep e con wait notify
			}

			if (stringaRighe.charAt(i - 1) == stringaColonne.charAt(ThreadNumber - 1))
				costo = 0;

			int del = table[i - 1][ThreadNumber] + 1; //// quello sopra
			int ins = table[i][ThreadNumber - 1] + 1; //// a sinistra
			int rep = table[i - 1][ThreadNumber - 1] + costo; //// Quello in diagonale sopra
			table[i][ThreadNumber] = Math.min(del, Math.min(ins, rep)); /// inserimento
			int add = table[i][ThreadNumber];

		}
		long fineUnaColonna = System.currentTimeMillis();

		return ThreadNumber;

	}

	@Override
	public Integer call() throws Exception {
		return this.distanceCoulmn();
	}

}
