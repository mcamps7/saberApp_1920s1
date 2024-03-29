package saberapp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import saberapp.dtos.Institut;
import saberapp.dtos.Professor;

/**
 * Ha d'implementar Serializable per poder passar objectes per sockets
 * @author Pau
 */
public class SaberAppServer_Professor {

    public SaberAppServer_Professor() {
    }

    public void startServerProfessor(DataInputStream entradaServer, DataOutputStream sortidaServer) throws IOException {

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
            case "registreProfessor":
                System.out.println("S'ha seleccionat registreProfessor");
                _insert(entradaServer, sortidaServer);
                break;
            case "51":
            case "allInstituts":
                System.out.println("S'ha seleccionat allInstituts");
                _allInstituts();
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

            ProfessorDAO pd = new ProfessorDAO();

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

            pd.insert(nick, password, nom, cognoms, mail, image, id_institut, materia);

            sortidaServer.writeUTF("Dades introdu�des");

        } catch (Exception ex) {

            //Si s'ha produ�t algun error retorna 2
            sortidaServer.writeUTF("Error entrant les dades");
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

    /**
     * Retorna una llista amb tots els instituts registrats a la BD Aquesta
     * llista servir� per pintar els selects amb els instituts perque els
     * usuaris piguin seleccionar el seu d'una llista tancada
     *
     * @return
     */
    private List<Institut> _allInstituts() {

        try {

            ProfessorDAO pd = new ProfessorDAO();
            List<Institut> instituts = new ArrayList<Institut>();

            List<Institut> ins = pd.allInstituts();

            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("instituts.obj"));
            salida.writeObject("Enviada llista d'instituts en un arxiu");
            salida.writeObject(ins);
            return instituts;

        } catch (Exception ex) {
            System.out.println("Error recuperant els instituts (_allInstituts)");
            ex.printStackTrace();
            return null;
        }

    }

}
