package saberapp.client.alumne;

import java.io.DataInputStream;
import saberapp.connection.SaberAppConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Classe per posar els m�todes per fer el testing com si fos un client
 *
 * @author Pau
 */
public class SaberAppClientAlumne extends SaberAppConnection {

    //Constructor. Instanciem una connexi� de tipus client
    public SaberAppClientAlumne() throws IOException {
        super("client");
    }

    /**
     * M�tode per iniciar el client
     */
    public void startClient() {
        try {
            entradaClient = new DataInputStream(cs.getInputStream());
            String line = entradaClient.readUTF();
            System.out.println("Server Message: " + line);

            //Flujo de datos hacia el servidor
            sortidaClient = new DataOutputStream(cs.getOutputStream());

            //Enviem un 1 perque el servidor s�piga que es connecta un alumne
            sortidaClient.writeInt(2);

            System.out.println("Quina operaci� vols fer?");
            System.out.println("========================");
            System.out.println("1- login");
            System.out.println("2- registreAlumne");
            System.out.println("0- Sortir");

            Scanner teclat = new Scanner(System.in);
            String in = teclat.nextLine();

            sortidaClient.writeUTF(in);

            switch (in) {
                case "1":
                case " login":
                    System.out.println("S'ha seleccionat login");
                    login();
                    break;
                case "2":
                case "registreAlumne":
                    System.out.println("S'ha seleccionat registreAlumne");
                    registreAlumne();
                    break;
                case "logout":
                    System.out.println("S'ha seleccionat logout");
                    logout();
                    break;
                default:
                    System.out.println("Les dades introdu�des no s�n correctes");
            }

            //cs.close();//Fin de la conexi�n
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void login() throws IOException {
        Scanner teclat = new Scanner(System.in);

        System.out.println("User");
        String user = teclat.nextLine();
        sortidaClient.writeUTF(user);

        System.out.println("Password");
        String password = teclat.nextLine();
        sortidaClient.writeUTF(password);

        entradaClient = new DataInputStream(cs.getInputStream());
        int line = entradaClient.readInt();
        System.out.println("Server Message: " + line);
        //1 existeix / 2 no existeix / 3 error

    }

    private void registreAlumne() throws IOException {

        Scanner teclat = new Scanner(System.in);

        //La id l'hauria de posar autom�ticament la BD
        System.out.println("Nick");
        String nick = teclat.nextLine();
        sortidaClient.writeUTF(nick);

        System.out.println("Password");
        String password = teclat.nextLine();
        sortidaClient.writeUTF(password);

        System.out.println("Nom");
        String nom = teclat.nextLine();
        sortidaClient.writeUTF(nom);

        System.out.println("Cognoms");
        String cognoms = teclat.nextLine();
        sortidaClient.writeUTF(cognoms);

        System.out.println("Mail");
        String mail = teclat.nextLine();
        sortidaClient.writeUTF(mail);

        System.out.println("Image");
        String image = teclat.nextLine();
        sortidaClient.writeUTF(image);

        System.out.println("id_institut");
        int id_institut = teclat.nextInt();
        teclat.nextLine();  // https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
        sortidaClient.writeInt(id_institut);

        System.out.println("Curs");
        String materia = teclat.nextLine();
        sortidaClient.writeUTF(materia);

        entradaClient = new DataInputStream(cs.getInputStream());
        String line = entradaClient.readUTF();
        System.out.println("Server Message: " + line);
        //1- tot correcte, 2-error en algun camp, 3-error
    }

    private void logout() {
        try {
            cs.close();
        } catch (IOException ex) {
            System.out.println("Error al close()");
            ex.printStackTrace();
        }
    }

}
