package saberapp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Pau
 */
public class SaberAppServer_Alumne {

    public SaberAppServer_Alumne() {
    }

    public void startServerAlumne(DataInputStream entradaServer, DataOutputStream sortidaServer) throws IOException {

        String line = entradaServer.readUTF();
        System.out.println("line: " + line);

        //Enviem a on toca segons el que s'ens demana des del client
        switch (line) {
            case "1":
            case " login":
                System.out.println("S'ha seleccionat login");
                _login(entradaServer, sortidaServer);
                break;
            case "2":
            case "registreAlumne":
                System.out.println("S'ha seleccionat registreAlumne");
                _insert(entradaServer, sortidaServer);
                break;
            default:
                System.out.println("Les dades introdu�des no s�n correctes");
        }

    }

    /**
     * Logueja a un usuari a la aplicaci�
     *
     * @param entradaServer
     * @param sortidaServer
     */
    private void _login(DataInputStream entradaServer, DataOutputStream sortidaServer) throws IOException {

        try {

            UsuariDAO ud = new UsuariDAO();

            //Recull les dades del client
            String usuari = entradaServer.readUTF();
            String password = entradaServer.readUTF();

            // Mostra entrades del client per consola
            System.out.println("user: " + usuari);
            System.out.println("password: " + password);

            //Preparem la resposta
            boolean success = ud.login(usuari, password);
            System.out.println("login succesful? " + success);

            //Si el login �s correcte retornem 1
            if (success == true) {
                sortidaServer.writeInt(1);

                //Si �s incorrecte retorna 2
            } else {
                sortidaServer.writeInt(2);
            }

        } catch (Exception ex) {

            //Si s'ha produ�t algun error retorna 3
            sortidaServer.writeInt(3);
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }
    
    /**
     * Insereix a un professor a la aplicaci�
     *
     * @param entradaServer
     * @param sortidaServer
     */
    private void _insert(DataInputStream entradaServer, DataOutputStream sortidaServer) throws IOException {

        try {

            AlumneDAO ad = new AlumneDAO();

            //Recull les dades del client
            String nick = entradaServer.readUTF();

            String password = entradaServer.readUTF();

            String nom = entradaServer.readUTF();

            String cognoms = entradaServer.readUTF();

            String mail = entradaServer.readUTF();

            String image = entradaServer.readUTF();

            int id_institut = entradaServer.readInt();

            String materia = entradaServer.readUTF();

            // Mostra entrades del client per consola
            System.out.println("user: "
                    + nick + ", "
                    + password + ", "
                    + nom + ", "
                    + cognoms + ", "
                    + mail + ", "
                    + image + ", "
                    + id_institut + ", "
                    + materia);

            ad.insert(nick, password, nom, cognoms, mail, image, id_institut, materia);

            sortidaServer.writeUTF("Dades introdu�des");

        } catch (Exception ex) {

            //Si s'ha produ�t algun error retorna 2
            sortidaServer.writeUTF("Error entrant les dades");
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

}
