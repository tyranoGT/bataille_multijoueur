import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/** Application permettant de jouer a la bataille navale contre un autre joueur avec un hote et un client
 * (a executer via la console d'Intellij en raison des codes couleurs)
 * @author PITOU Tom
 * @since 05/03/2023
 * @see <a href="https://stackoverflow.com/questions/41226555/how-do-i-run-the-same-application-twice-in-intellij">Pour executer deux fois la meme application dans IntelliJ</a>
 * @see <a href="https://github.com/tyranoGT/bataille_multijoueur.git">Ce projet est disponible sur Github</a>
 */
public class bataille_multijoueur {
    /** Variable representant l'autre joueur
     */
    public static Socket autre_joueur;

    /** Variable representant le flux de sortie de l'autre joueur
     */
    public static BufferedReader flux_entrant;

    /** Variable representant le flux d'entree de l'autre joueur
     */
    public static PrintWriter flux_sortant;

    /** Variable representant si ce joueur est l'hote de la partie
     */
    public static boolean hote=false;

    /** Variable representant le code couleur du rouge pour l'affichage console d'Intellij
     */
    public static int rouge=31;
    /** Variable representant le code couleur du vert pour l'affichage console d'Intellij
     */
    public static int vert=32;
    /** Variable representant le code couleur du violet pour l'affichage console d'Intellij
     */
    public static int violet=35;
    /** Variable representant le code couleur du jaune pour l'affichage console d'Intellij
     */
    public static int jaune=33;
    /** Variable representant le code couleur du bleu pour l'affichage console d'Intellij
     */
    public static int bleu=34;
    /** Variable representant le code couleur du texte souligne pour l'affichage console d'Intellij
     */
    public static int souligne=4;
    /** Variable representant le code couleur du texte surligne pour l'affichage console d'Intellij
     */
    public static int surligne=7;

    /** Classe representant un tuple bateau et int (pour representer une quantite de tel bateau).
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static class Paire_bateau_quantite
    {
        /** Bateau du tuple
         */
        private Bateau objet;

        /** int du tuple
         */
        private int quantite;

        /** Constructeur du tuple bateau/int
         * @param a le bateau assigne au tuple
         * @param b le int assigne au tuple
         */
        public Paire_bateau_quantite(Bateau a, int b) {
            this.objet = a;
            this.quantite = b;
        }

        /** Permet d'obtenir le bateau du tuple
         * @return le bateau : objet
         */
        public Bateau obtenir_objet() {
            return objet;
        }

        /** Permet d'obtenir le int du tuple
         * @return le int : quantity
         */
        public int obtenir_quantite() {
            return quantite;
        }
    }
    /** Classe abstraite representant un bateau.
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static abstract class Bateau
    {
        /** nom du bateau
         */
        protected String m_nom;
        /** taille du bateau
         */
        protected int m_taille;
        /** identifiant du bateau representant le bateau sur la grille de jeu
         */
        protected int m_indentifiant;

        /** Permet d'obtenir le nom du bateau
         * @return le nom du bateau : m_nom
         */
        public String obtenir_nom()
        {
            return m_nom;
        }

        /** Permet d'obtenir la taille du bateau
         * @return la taille du bateau : m_taille
         */
        public int obtenir_taille()
        {
            return m_taille;
        }

        /** Permet d'obtenir l'identifiant du bateau
         * @return l'identifiant du bateau : m_identifiant
         */
        public int obtenir_identifiant()
        {
            return m_indentifiant;
        }
    }

    /** Classe heritant de bateau representant le type de bateau : Porte avions.
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static class Porte_avions extends Bateau
    {
        /** Constructeur par defaut qui initialise le nom,la taille et l'identifiant du bateau.
         */
        public Porte_avions()
        {
            m_nom="porte-avions";
            m_taille=5;
            m_indentifiant=1;
        }

    }

    /** Classe heritant de bateau representant le type de bateau : Croiseur.
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static class Croiseur extends Bateau
    {
        /** Constructeur par defaut qui initialise le nom,la taille et l'identifiant du bateau.
         */
        public Croiseur()
        {
            m_nom="croiseur";
            m_taille=4;
            m_indentifiant=2;
        }
    }

    /** Classe heritant de bateau representant le type de bateau : Contre torpilleurs.
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static class Contre_torpilleurs extends Bateau
    {
        /** Constructeur par defaut qui initialise le nom,la taille et l'identifiant du bateau.
         */
        public Contre_torpilleurs()
        {
            m_nom="contre-torpilleurs";
            m_taille=3;
            m_indentifiant=3;
        }
    }

    /** Classe heritant de bateau representant le type de bateau : Sous marin.
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static class Sous_marin extends Bateau
    {
        /** Constructeur par defaut qui initialise le nom,la taille et l'identifiant du bateau.
         */
        public Sous_marin()
        {
            m_nom="sous-marin";
            m_taille=3;
            m_indentifiant=4;
        }
    }

    /** Classe heritant de bateau representant le type de bateau : Torpilleur.
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static class Torpilleur extends Bateau
    {
        /** Constructeur par defaut qui initialise le nom,la taille et l'identifiant du bateau.
         */
        public Torpilleur()
        {
            m_nom="torpilleur";
            m_taille=2;
            m_indentifiant=5;
        }
    }

    /** Variable representant le nombre de lignes de la grille de jeu
     */
    public static final int nb_lignes=10;

    /** Variable representant le nombre de colonnes de la grille de jeu
     */
    public static final int nb_colonnes=10;

    /** Variable representant la grille de jeu du joueur
     */
    public static int[][] grilleJeu=new int[nb_lignes][nb_colonnes];

    /** Variable representant les bateaux que possederont les deux joueurs
     */
    public static final Paire_bateau_quantite[] bateaux_de_chaque_joueur=
            {
                    new Paire_bateau_quantite(new Porte_avions(),1),
                    new Paire_bateau_quantite(new Croiseur(),1),
                    new Paire_bateau_quantite(new Contre_torpilleurs(),1),
                    new Paire_bateau_quantite(new Sous_marin(),1),
                    new Paire_bateau_quantite(new Torpilleur(),1)
            };

    /** Permet de connecter les deux joueurs (un hote et un client) puis lance la partie
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static void connexion() throws IOException {
        System.out.println("1=host 2=join");
        Scanner scanner = new Scanner(System.in);
        String rep = scanner.nextLine();
        int int_rep=Integer.parseInt(rep);
        if(int_rep==1)
        {
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(7777);
            }
            catch(Exception e) {
                cls();
                System.out.print("\u001b[01;" + rouge  +"m"+ "Le port doit etre deja utilise"+ "\u001b[00m ");
                attendre(2);
                cls();
                connexion();
                return;
            }
            hote=true;
            boolean client_has_join=false;
            System.out.println("en attente de connexion de l'autre joueur");
            while(!client_has_join) {
                autre_joueur = serverSocket.accept();
                while(!(autre_joueur.isClosed())){
                    client_has_join=true;
                    flux_entrant = new BufferedReader(new InputStreamReader(autre_joueur.getInputStream()));
                    flux_sortant = new PrintWriter(autre_joueur.getOutputStream(), true);
                    System.out.println("l'autre joueur est connecte");
                    engagement();
                    break;
                }
            }
        }
        else
        {
            try {
                autre_joueur = new Socket("localhost", 7777);
            }
            catch(Exception e) {
                cls();
                System.out.print("\u001b[01;" + rouge  +"m"+ "Le port ne doit pas etre ouvert"+ "\u001b[00m ");
                attendre(2);
                cls();
                connexion();
                return;
            }
            flux_entrant = new BufferedReader(new InputStreamReader(autre_joueur.getInputStream()));
            flux_sortant = new PrintWriter(autre_joueur.getOutputStream(), true);
            engagement();
        }
    }

    /** Initialise la grille du joueur en lui demandant ou il veut placer ses bateaux
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static void initGrilleJeu()
    {
        Scanner scanner;
        boolean fin=false;
        int nombre_de_bateaux_places=0;
        while(!fin)
        {
            int ligne=0;
            int colonne=0;
            int direction=1;
            int etape_actuelle=1;
            int taille_bateau=bateaux_de_chaque_joueur[nombre_de_bateaux_places].obtenir_objet().m_taille;
            while(etape_actuelle<=3)
            {
                cls();
                System.out.println(
                        "\u001b[01;" + souligne +"m"+
                                "Vous devez placez ce bateau:"+
                                "\u001b[00m "+
                                "\u001b[01;" + violet +"m"+
                                bateaux_de_chaque_joueur[nombre_de_bateaux_places].obtenir_objet().m_nom+
                                "\u001b[00m "+
                                "\nIl a comme taille "+
                                "\u001b[01;" + violet +"m"+
                                taille_bateau+
                                "\u001b[00m" +
                                " cases"+
                                "\u001b[01;" + souligne +"m"+
                                "\nVoici l'état de votre grille:"+
                                "\u001b[00m ");
                AfficherGrille();
                switch(etape_actuelle)
                {
                    case 1:
                        scanner = new Scanner(System.in);
                        System.out.println("\u001b[01;" + souligne  +"m"+ "Choissisez une colonne allant de "+(char)65+ " à "+(char)(nb_colonnes+64)+ "\u001b[00m :");
                        String colonne_rep = scanner.nextLine();
                        if(colonne_rep=="")
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Repondez"+ "\u001b[00m ");
                            attendre(2);
                            break;
                        }
                        char premier_char_rep=colonne_rep.toUpperCase().charAt(0);
                        if(premier_char_rep>=(char)65 && premier_char_rep<=(char)(nb_colonnes+64))
                        {
                            colonne=premier_char_rep-65;
                            etape_actuelle+=1;
                        }
                        else
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Votre choix est en dehors du terrain"+ "\u001b[00m ");
                            attendre(2);
                        }
                        break;
                    case 2:
                        scanner = new Scanner(System.in);
                        System.out.println("\u001b[01;" + souligne  +"m"+ "Choissisez une ligne allant de 1 à "+nb_colonnes+ "\u001b[00m :");
                        String ligne_rep = scanner.nextLine();
                        if(ligne_rep=="")
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Repondez"+ "\u001b[00m ");
                            attendre(2);
                            break;
                        }
                        if(!est_un_entier(ligne_rep))
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Mettez uniquement des chiffres"+ "\u001b[00m ");
                            attendre(2);
                            break;
                        }
                        int int_ligne_rep=Integer.parseInt(ligne_rep);
                        if(int_ligne_rep>=1 && int_ligne_rep<=nb_colonnes)
                        {
                            ligne=int_ligne_rep-1;
                            etape_actuelle+=1;
                        }
                        else
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Votre choix est en dehors du terrain"+ "\u001b[00m ");
                            attendre(2);
                        }
                        break;
                    case 3:
                        scanner = new Scanner(System.in);
                        System.out.println("horizontal (1) ou vertical (2)");
                        String direction_rep = scanner.nextLine();
                        if(direction_rep=="")
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Repondez"+ "\u001b[00m ");
                            attendre(2);
                            break;
                        }
                        if(!est_un_entier(direction_rep))
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Mettez uniquement des chiffres"+ "\u001b[00m ");
                            attendre(2);
                            break;
                        }
                        direction=Integer.parseInt(direction_rep);
                        if(direction==1 || direction==2)
                        {
                            etape_actuelle+=1;
                        }
                        else
                        {
                            cls();
                            System.out.print("\u001b[01;" + rouge  +"m"+ "Votre choix est en dehors des propositions"+ "\u001b[00m ");
                            attendre(2);
                        }
                        break;
                    default:
                        break;
                }
            }
            if(posOk (ligne, colonne, direction,taille_bateau))
            {
                switch(direction)
                {
                    case 1:
                        for(int i=0;i<taille_bateau;i++)
                        {
                            grilleJeu[ligne][colonne+i]=bateaux_de_chaque_joueur[nombre_de_bateaux_places].obtenir_objet().obtenir_identifiant();
                        }
                        break;
                    case 2:
                        for(int i=0;i<taille_bateau;i++)
                        {
                            grilleJeu[ligne+i][colonne]=bateaux_de_chaque_joueur[nombre_de_bateaux_places].obtenir_objet().obtenir_identifiant();
                        }
                        break;
                    default:
                        break;
                }
                nombre_de_bateaux_places+=1;
            }
            else
            {
                cls();
                System.out.print("\u001b[01;" + rouge  +"m"+ "Votre bateau ne peut pas etre place ici"+ "\u001b[00m ");
                attendre(2);
                continue;
            }
            if(nombre_de_bateaux_places==bateaux_de_chaque_joueur.length)
            {
                fin=true;
            }
        }
    }

    /** Permet de verifier si un bateau peut etre place a tel endroit de la grille
     * @author PITOU Tom
     * @since 05/03/2023
     * @param l la ligne de la position du bateau a place
     * @param c la colonne de la position du bateau a place
     * @param d si la valeur de ce parametre vaut : 1 le bateau est place horizontalement | 2 le bateau est place verticalement
     * @param t la taille du bateau a placer
     * @return un booleen pour signifier que la position est correct
     */
    public static boolean posOk (int l, int c, int d, int t)
    {
        switch(d)
        {
            case 1:
                if(c+t>=nb_colonnes)
                    return false;
                for(int i=0;i<t;i++)
                {
                    if(grilleJeu[l][c+i]!=0)
                    {
                        return false;
                    }
                }
                break;
            case 2:
                if(l+t>=nb_lignes)
                    return false;
                for(int i=0;i<t;i++)
                {
                    if(grilleJeu[l+i][c]!=0)
                    {
                        return false;
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }

    /** Permet d'afficher la grille de jeu dans la console
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static void AfficherGrille() {
        int couleur=0;
        System.out.print("    ");
        for(int j=0;j<nb_colonnes;j++)
        {
            System.out.print("\u001b[01;" + surligne  +"m"+ "|"+(char)(j+65)+"|"+ "\u001b[00m ");
        }
        System.out.println();
        for(int i=0;i<nb_lignes;i++)
        {
            System.out.print("\u001b[01;" + surligne  +"m"+ String.format("|%02d|", (i+1))+ "\u001b[00m ");
            for(int j=0;j<nb_colonnes;j++)
            {
                int val=grilleJeu[i][j];
                switch(val)
                {
                    case 0:
                        couleur=bleu;
                        break;
                    case 6:
                        couleur=rouge;
                        break;
                    case 7:
                        couleur=jaune;
                        break;
                    case 8:
                        System.out.print("\u001b[01;" + rouge  +"m"+ "X"+ "\u001b[00m   ");
                        continue;
                    default:
                        couleur=vert;
                }
                System.out.print("\u001b[01;" + couleur  +"m"+ val+ "\u001b[00m   ");
            }
            System.out.println();
        }
    }

    /** Permet d'afficher la grille de jeu dans la console de l'autre joueur sans montrer l'emplacement des bateaux ennemis
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static void AfficherGrilleTouche()
    {
        String ligne="    ";
        for(int j=0;j<nb_colonnes;j++)
        {
            ligne+="\u001b[01;" + surligne  +"m"+ "|"+(char)(j+65)+"|"+ "\u001b[00m ";
        }
        flux_sortant.println(ligne);
        for(int i=0;i<nb_lignes;i++)
        {
            ligne="";
            ligne+="\u001b[01;" + surligne  +"m"+ String.format("|%02d|", (i+1))+ "\u001b[00m ";
            for(int j=0;j<nb_colonnes;j++)
            {
                int val=grilleJeu[i][j];
                switch(val)
                {
                    case 6:
                        ligne+="\u001b[01;" + vert  +"m"+ val+ "\u001b[00m   ";
                        break;
                    case 7:
                        ligne+="\u001b[01;" + jaune  +"m"+ val+ "\u001b[00m   ";
                        break;
                    case 8:
                        ligne+="\u001b[01;" + rouge  +"m"+ "X"+ "\u001b[00m   ";
                        break;
                    default:
                        ligne+="\u001b[01;" + bleu  +"m"+ 0+ "\u001b[00m   ";
                }
            }
            flux_sortant.println(ligne);
        }
        flux_sortant.println("fin");
    }

    /** Permet de commencer et jouer une partie contre l'autre joueur
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static void engagement() throws IOException {
        initGrilleJeu();
        boolean autre_joueur_init_fini=false;
        cls();
        System.out.println("l'adversaire positionne encore ses bateaux...");
        while(!autre_joueur_init_fini)
        {
            flux_sortant.println("ok");
            String msg;
            if ((msg=flux_entrant.readLine()) != null){
                autre_joueur_init_fini=true;
            }
        }
        boolean mon_tour=false;
        if(hote)
        {
            mon_tour=(0 == randRange (0, 2));
            boolean msg_recu=false;
            while(!msg_recu)
            {
                flux_sortant.println(!mon_tour);
                String msg;
                if ((msg=flux_entrant.readLine()) != null){
                    msg_recu=true;
                }
            }
        }
        else
        {
            boolean msg_recu=false;
            while(!msg_recu) {
                String msg;
                if ((msg = flux_entrant.readLine()) != null) {
                    mon_tour = Boolean.parseBoolean(msg);
                    flux_sortant.println("ok");
                    msg_recu=true;
                }
            }
        }
        cls();
        if(!mon_tour)
        {
            System.out.println("_____________________________");
            System.out.println("il joue en premier");
            System.out.println("_____________________________");
        }
        else
        {
            System.out.println("_____________________________");
            System.out.println("Vous jouez en premier");
            System.out.println("_____________________________");
        }
        attendre(2);
        Scanner scanner;
        int ligne=0;
        int colonne=0;
        while(!(autre_joueur.isClosed()))
        {
            cls();
            if(mon_tour)
            {
                String msg;
                String[] grille_ennemi=new String[nb_lignes+1];
                int cpt=0;
                boolean msg_fin=false;
                while(!msg_fin)
                {
                    if((msg=flux_entrant.readLine())!=null)
                    {
                        if(msg.equals("fin"))
                        {
                            msg_fin=true;
                        }
                        else
                        {
                            grille_ennemi[cpt]=msg;
                            cpt+=1;
                        }
                    }
                }
                int etape_actuelle=1;
                while(etape_actuelle<=2)
                {
                    cls();
                    System.out.println("_____________________________");
                    System.out.println("Ou envoyez vous votre torpille?");
                    System.out.println("_____________________________");
                    System.out.println("Grille ennemi");
                    for(int i=0;i<nb_lignes+1;i++)
                    {
                        System.out.println(grille_ennemi[i]);
                    }
                    switch(etape_actuelle)
                    {
                        case 1:
                            scanner = new Scanner(System.in);
                            System.out.println("\u001b[01;" + souligne  +"m"+ "Choissisez une colonne allant de "+(char)65+ " à "+(char)(nb_colonnes+64)+ "\u001b[00m :");
                            String colonne_rep = scanner.nextLine();
                            if(colonne_rep=="")
                            {
                                cls();
                                System.out.print("\u001b[01;" + rouge  +"m"+ "Repondez"+ "\u001b[00m ");
                                attendre(2);
                                break;
                            }
                            char premier_char_rep=colonne_rep.toUpperCase().charAt(0);
                            if(premier_char_rep>='A' && premier_char_rep<='J')
                            {
                                colonne=premier_char_rep-65;
                                etape_actuelle+=1;
                            }
                            else
                            {
                                cls();
                                System.out.print("\u001b[01;" + rouge  +"m"+ "Votre choix est en dehors du terrain"+ "\u001b[00m ");
                                attendre(2);
                            }
                            break;
                        case 2:
                            scanner = new Scanner(System.in);
                            System.out.println("\u001b[01;" + souligne  +"m"+ "Choissisez une ligne allant de 1 à "+nb_colonnes+ "\u001b[00m :");
                            String ligne_rep = scanner.nextLine();
                            if(ligne_rep=="")
                            {
                                cls();
                                System.out.print("\u001b[01;" + rouge  +"m"+ "Repondez"+ "\u001b[00m ");
                                attendre(2);
                                break;
                            }
                            if(!est_un_entier(ligne_rep))
                            {
                                cls();
                                System.out.print("\u001b[01;" + rouge  +"m"+ "Mettez uniquement des chiffres"+ "\u001b[00m ");
                                attendre(2);
                                break;
                            }
                            int int_ligne_rep=Integer.parseInt(ligne_rep);
                            if(int_ligne_rep>=1 && int_ligne_rep<=10)
                            {
                                ligne=int_ligne_rep-1;
                                etape_actuelle+=1;
                            }
                            else
                            {
                                cls();
                                System.out.print("\u001b[01;" + rouge  +"m"+ "Votre choix est en dehors du terrain"+ "\u001b[00m ");
                                attendre(2);
                            }
                            break;
                        default:
                            break;
                    }
                }
                cls();
                System.out.println("_____________________________");
                System.out.println("Vous jouez "+(ligne+1)+" "+(char)(colonne+65));
                System.out.println("_____________________________");
                flux_sortant.println(ligne+" "+colonne);
                String result="";
                boolean msg_recu=false;
                while(!msg_recu) {
                    if ((msg = flux_entrant.readLine()) != null) {
                        result = msg;
                        flux_sortant.println("ok");
                        msg_recu=true;
                    }
                }
                cls();
                System.out.println("_____________________________");
                System.out.print("Vous jouez "+(ligne+1)+" "+(char)(colonne+65)+" c'est: ");
                System.out.println(result);
                System.out.println("_____________________________");
                attendre(4);
                msg_recu=false;
                while(!msg_recu) {
                    if ((msg = flux_entrant.readLine()) != null) {
                        if(Boolean.parseBoolean(msg))
                        {
                            System.out.println("_____________________________");
                            System.out.println("Vous avez gagne");
                            System.out.println("_____________________________");
                            autre_joueur.close();
                        }
                        flux_sortant.println("ok");
                        msg_recu=true;
                    }
                }
            }
            else
            {
                System.out.println("en attente de l'adeversaire");
                AfficherGrilleTouche();
                AfficherGrille();
                String msg;
                boolean msg_recu=false;
                while(!msg_recu)
                {
                    if ((msg=flux_entrant.readLine()) != null){
                        String[] pos_str = msg.split(" ");
                        ligne=Integer.parseInt(pos_str[0]);
                        colonne=Integer.parseInt(pos_str[1]);
                        msg_recu=true;
                    }
                }
                cls();
                System.out.println("_____________________________");
                System.out.println("il joue "+(ligne+1)+" "+(char)(colonne+65));
                System.out.println("_____________________________");
                System.out.println("Votre grille :");
                int cible=grilleJeu[ligne][colonne];
                grilleJeu[ligne][colonne]=8;
                AfficherGrille();
                attendre(2);
                grilleJeu[ligne][colonne]=cible;
                cls();
                System.out.println("_____________________________");
                System.out.print("il joue "+(ligne+1)+" "+(char)(colonne+65)+" c'est: ");
                String resultat=mouvement(ligne,colonne);
                System.out.println("_____________________________");
                System.out.println("Votre grille :");
                AfficherGrille();
                msg_recu=false;
                while(!msg_recu) {
                    flux_sortant.println(resultat);
                    if ((msg = flux_entrant.readLine()) != null) {
                        msg_recu=true;
                    }
                }
                attendre(4);
                boolean fin=vainqueur();
                if(fin)
                {
                    System.out.println("_____________________________");
                    System.out.println("Vous avez perdu");
                    System.out.println("_____________________________");
                }
                msg_recu=false;
                while(!msg_recu) {
                    flux_sortant.println(fin);
                    if ((msg = flux_entrant.readLine()) != null) {
                        msg_recu=true;
                    }
                }
            }
            mon_tour=!mon_tour;
        }
    }

    /** Permet de verifier si la partie est fini soit que tous les bateaux d'une grille ont ete coules
     * @author PITOU Tom
     * @since 05/03/2023
     * @return un booleen indiquant si la partie est fini
     */
    public static boolean vainqueur()
    {
        for(int i=0;i<nb_lignes;i++)
        {
            for(int j=0;j<nb_colonnes;j++)
            {
                int val=grilleJeu[i][j];
                if(val==0 || val==6 || val==7)
                {
                    continue;
                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }

    /** Permet de verifier si on a coule un bateau
     * @author PITOU Tom
     * @since 05/03/2023
     * @param bateau l'identifiant du bateau a verifier s'il est coule
     * @param l
     * @param c
     * @return un booleen indiquant si le bateau a ete coule
     */
    public static boolean coule(int bateau,int l,int c)
    {
        for(int i=0;i<nb_lignes;i++)
        {
            for(int j=0;j<nb_colonnes;j++)
            {
                if(grilleJeu[i][j]==bateau)
                {
                    return false;
                }
            }
        }
        return true;

        /*
        int taille_ship=0;
        for(int i=0;i<ships_of_each_player.length;i++)
        {
            if(ships_of_each_player[i].get_object().m_indentifiant==ship)
            {
                taille_ship=ships_of_each_player[i].get_object().m_taille;
            }
        }
        for(int i=-taille_ship+1;i<taille_ship-1;i++)
        {
            int line=l+i;
            if(line>=0 && line<=nb_lignes)
            {
                if(grille[line][c]==ship)
                {
                    return false;
                }
            }
        }
        for(int j=-taille_ship+1;j<taille_ship-1;j++)
        {
            int colonne=c+j;
            if(colonne>=0 && colonne<=nb_lignes)
            {
                if (grille[l][colonne] == ship)
                {
                    return false;
                }
            }
        }
        return true;*/
    }

    /** Permet d'agir en fonction de ce que touche le tir
     * @author PITOU Tom
     * @since 05/03/2023
     * @param l la ligne dans la position du tir
     * @param c la colonne dans la position du tir
     * @return un string contenant l'information a faire passer a l'autre joueur
     */
    public static String mouvement(int l, int c)
    {
        int cible=grilleJeu[l][c];
        if(cible==0 || cible==7)
        {
            grilleJeu[l][c]=7;
            System.out.println("\u001b[01;" + bleu  +"m"+ "à l'eau"+ "\u001b[00m");
            return "\u001b[01;" + bleu  +"m"+ "à l'eau"+ "\u001b[00m";
        }
        else
        {
            grilleJeu[l][c]=6;
            if(coule(cible,l,c))
            {
                System.out.println("\u001b[01;" + rouge  +"m"+ "coule"+ "\u001b[00m");
                return "\u001b[01;" + rouge  +"m"+ "coule"+ "\u001b[00m";
            }
            else
            {
                System.out.println("\u001b[01;" + jaune  +"m"+ "touche"+ "\u001b[00m");
                return "\u001b[01;" + jaune  +"m"+ "touche"+ "\u001b[00m";
            }
        }
    }

    /** Permet d'effacer la console d'Intellij (cette console ne pouvant etre efface utilisation de println pour sauter un nombre de lignes suffisantes)
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static void cls()
    {
        /*le code commente permet d'effacer la console quand notre application est execute sur le cmd de windows cependant les couleurs de la
        console d'Intellij etant plus interessantes*/
        /*try
        {
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }
        catch(Exception e)
        {

        }*/
        for(int i=0;i<50;i++)
        {
            System.out.println();
        }
    }

    /** Variable pour la generation aleatoire (seed)
     */
    public static Random rand = new Random ();

    /** Permet d'obtenir un nombre entier aleatoire entre deux bornes
     * @since 05/03/2023
     * @param a la borne minimale (inclu)
     * @param b la borne maximale (exclu)
     * @return un int le nombre resultant de la generation aleatoire
     */
    public static int randRange (int a, int b)
    {
        return rand.nextInt(b-a)+a;
    }
    /** Permet de faire un delai de x secondes
     * @author PITOU Tom
     * @since 05/03/2023
     * @param nb_secondes le nombre de secondes du delai
     */
    public static void attendre(int nb_secondes)
    {
        try
        {
            TimeUnit.SECONDS.sleep(nb_secondes);
        }
        catch (InterruptedException e) {
        }
    }

    /** Permet de verifier si un string est un int
     * @author PITOU Tom
     * @since 05/03/2023
     * @param str le string a verifier
     * @return un booleen indiquant si le string est un int ou non
     */
    public static boolean est_un_entier(String str)
    {
        for(int i=0;i<str.length();i++)
        {
            if(Character.digit(str.charAt(i),10)<0)
            {
                return false;
            }
        }
        return true;
    }

    /**Le main de l'application lance la connexion entre deux joueurs à l'execution du programme
     * @author PITOU Tom
     * @since 05/03/2023
     */
    public static void main(String[] args) throws IOException {
        connexion();
    }
}
