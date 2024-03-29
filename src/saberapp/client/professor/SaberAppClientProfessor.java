package saberapp.client.professor;

import java.io.DataInputStream;
import saberapp.connection.SaberAppConnection;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import saberapp.dtos.Institut;

/**
 * Classe per posar els m�todes per fer el testing com si fos un client
 *
 * @author Pau
 */
public class SaberAppClientProfessor extends SaberAppConnection {

    //Necesari per des serialitzar: 
    //https://stackoverflow.com/questions/29946475/writing-aborted-java-io-notserializableexception
    private static final long serialVersionUID = -1892561327013038124L;

    //Constructor. Instanciem una connexi� de tipus client
    public SaberAppClientProfessor() throws IOException {
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

            //Enviem un 1 perque el servidor s�piga que es connecta un professor
            sortidaClient.writeInt(1);

            System.out.println("Quina operaci� vols fer?");
            System.out.println("========================");
            System.out.println("1- login");
            System.out.println("2- registreProfessor");
            System.out.println("51- allInstituts");
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
                case "registreProfessor":
                    System.out.println("S'ha seleccionat registreProfessor");
                    registreProfessor();
                    break;
                case "51":
                case "allInstituts":
                    System.out.println("S'ha seleccionat allInstituts");
                    allInstituts();
                    break;
                case "0":
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

    public void registreProfessor() throws IOException {

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

        System.out.println("Materia");
        String materia = teclat.nextLine();
        sortidaClient.writeUTF(materia);

        entradaClient = new DataInputStream(cs.getInputStream());
        String line = entradaClient.readUTF();
        System.out.println("Server Message: " + line);
        //1- tot correcte, 2-error en algun camp, 3-error
    }

    private void allInstituts() throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream entrada = new ObjectInputStream(new FileInputStream("instituts.obj"));
        String str = (String) entrada.readObject();
        List<Institut> llista = (List<Institut>) entrada.readObject();

        System.out.println(str);

        for (Institut in : llista) {
            System.out.println(in.getNom());
        }
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
