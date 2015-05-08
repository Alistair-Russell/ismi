
public class Borrar {

	public static void main(String[] args) {
		String s = new String("CODEX:distinguishing_features&CODEX&notes_on_ownership&CODEX:notes");
		String[] array = s.split("&");
		System.out.println(array.length);
	}

}
