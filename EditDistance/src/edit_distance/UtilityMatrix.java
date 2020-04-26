package edit_distance;

public class UtilityMatrix {

	
	
	
	
	
	
	public static void printMatrix(int table[][],String stringaRighe, String stringaColonne) {

		for (int i = 0; i < stringaRighe.length(); i++) {
			for (int j = 0; j < stringaColonne.length(); j++) {

				System.out.print(table[i][j] + " ");

			}
			
			System.out.println();

		}
		
		
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
