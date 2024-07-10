package com.example.bty.Services;
import com.example.bty.Entities.Commmande;
import com.example.bty.Entities.Produit;
import com.example.bty.Entities.User;
import com.example.bty.Entities.paiment;
import com.example.bty.Utils.ConnexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceProduit {
    private Connection connexion;
    private PreparedStatement pst;
    private Statement ste ;
    public ServiceProduit() {
        connexion = ConnexionDB.getInstance().getConnexion();
    }
    private static String userName;
    private static int userId;

  public boolean ajouterProduit(Produit produit) {
      String query = "INSERT INTO produit (id, nom, Description, Prix, Type, Quantite, quantite_vendues, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

      try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "");
           PreparedStatement statement = connection.prepareStatement(query)) {
          statement.setInt(1, produit.getIdProduit());
          statement.setString(2, produit.getNom());
          statement.setString(3, produit.getDescription());
          statement.setDouble(4, produit.getPrix());
          statement.setString(5, produit.getType());
          statement.setInt(6, produit.getQuantite());
          statement.setInt(7, produit.getQuantiteVendues());
          statement.setBytes(8, produit.getImage());

          statement.executeUpdate();
          return true;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }


    public List<Produit> consulterProduits() {
        List<Produit> produits = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
            String query = "SELECT * FROM produit";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Produit produit = new Produit();

                    produit.setIdProduit(resultSet.getInt("id"));
                    produit.setNom(resultSet.getString("nom"));
                    produit.setDescription(resultSet.getString("Description"));
                    produit.setPrix(resultSet.getDouble("Prix"));
                    produit.setType(resultSet.getString("Type"));
                    produit.setQuantite(resultSet.getInt("Quantite"));
                    produit.setQuantiteVendues(resultSet.getInt("quantite_vendues"));

                    produits.add(produit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion ou d'exécution de la requête
        }
        return produits;
    }


    public List<Commmande> consulterCommandes() {
        List<Commmande> commandes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
            String query = "SELECT * FROM commande"; // Assurez-vous que le nom de la table est correct

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Commmande commande = new Commmande();

                    commande.setIdCommande(resultSet.getInt("id"));
                    commande.setDateCommande(resultSet.getTimestamp("date_commande"));
                    commande.setIdProduit(resultSet.getInt("id_produit"));
                    commande.setNom(resultSet.getString("nom"));
                    commande.setMontant(resultSet.getDouble("montant"));

                    commandes.add(commande);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }


    public List<paiment> consulterPaiment() {
        List<paiment> paiments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
            String query = "SELECT * FROM  paiement"; // Assurez-vous que le nom de la table est correct

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    paiment commande = new paiment();

                    commande.setId(resultSet.getInt("id"));
                    commande.setNom(resultSet.getString("name"));
                    commande.setEmail(resultSet.getString("email"));
                    commande.setCarte(resultSet.getString("card_info"));
                    commande.setMm(resultSet.getInt("mm"));
                    commande.setYy(resultSet.getInt("yy"));
                    commande.setCvc(resultSet.getInt("cvc"));
                    commande.setTotal(resultSet.getInt("total"));


                    paiments.add(commande);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return paiments;
    }



    public List<Commmande> consulterCommandesParDate(LocalDate selectedDate) {
        List<Commmande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande WHERE DATE(date_commande) = ?";

        try (PreparedStatement preparedStatement = connexion.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(selectedDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Commmande commande = new Commmande();
                    commande.setIdCommande(resultSet.getInt("id"));
                    commande.setDateCommande(resultSet.getTimestamp("date_commande"));
                    commande.setIdProduit(resultSet.getInt("id_produit"));
                    commande.setNom(resultSet.getString("nom"));
                    commande.setMontant(resultSet.getDouble("montant"));
                    commandes.add(commande);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }


    public void modifierPrixProduit(int idProduit, double nouveauPrix) {
        // try (Connection connection = ConnexionDB.obtenirConnexion()) {
        String query = "UPDATE produit SET Prix = ? WHERE id = ?";
        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setDouble(1, nouveauPrix);
            statement.setInt(2, idProduit);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean modifierProduit(Produit produit) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
            String query = "UPDATE produit SET nom=?, description=?, prix=?, type=?, quantite=?, quantite_vendues=? WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, produit.getNom());
                statement.setString(2, produit.getDescription());
                statement.setDouble(3, produit.getPrix());
                statement.setString(4, produit.getType());
                statement.setInt(5, produit.getQuantite());
                statement.setInt(6, produit.getQuantiteVendues());
                statement.setInt(7, produit.getIdProduit());

                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /*public boolean supprimerProduit(int idProduit) {
        //try (Connection connection = ConnexionDB.obtenirConnexion()) {
        String query = "DELETE FROM produit WHERE idProduit = ?";
        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setInt(1, idProduit);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/

    public boolean supprimerProduit(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
            String query = "DELETE FROM produit WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Produit> rechercherProduitsParType(String type) {
        List<Produit> produits = new ArrayList<>();
        //try (Connection connection = ConnexionDB.obtenirConnexion()) {
        String query = "SELECT * FROM produit WHERE type = ?";
        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Produit produit = new Produit();
                    produit.setIdProduit(resultSet.getInt("id"));
                    produit.setNom(resultSet.getString("nom"));
                    produit.setDescription(resultSet.getString("description"));
                    produit.setPrix(resultSet.getDouble("prix"));
                    produit.setType(resultSet.getString("type"));
                    produit.setQuantite(resultSet.getInt("quantite"));
                    produit.setQuantiteVendues(resultSet.getInt("quantite_vendues"));
                    produits.add(produit);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }


    public List<Produit> filtrerProduitsParPlageDePrix(int prixMin, int prixMax) {
        List<Produit> produits = new ArrayList<>();
        //try (Connection connection = ConnexionDB.obtenirConnexion()) {
        String query = "SELECT * FROM produit WHERE prix BETWEEN ? AND ?";
        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setDouble(1, prixMin);
            statement.setDouble(2, prixMax);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Produit produit = new Produit();
                    produit.setIdProduit(resultSet.getInt("id"));
                    produit.setNom(resultSet.getString("nom"));
                    produit.setDescription(resultSet.getString("description"));
                    produit.setPrix(resultSet.getInt("prix"));
                    produit.setType(resultSet.getString("type"));
                    produit.setQuantite(resultSet.getInt("quantite"));
                    produit.setQuantiteVendues(resultSet.getInt("quantite_vendues"));

                    produits.add(produit);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }



    public void mettreAJourQuantiteVendueEtTotale(Produit produit, int quantiteAchete) {
        String query = "UPDATE produit SET quantite_vendues = quantite_vendues + ?, quantite = quantite - ? WHERE id = ?";

        try (
                PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setInt(1, quantiteAchete);
            statement.setInt(2, quantiteAchete);
            statement.setInt(3, produit.getIdProduit());

            int lignesModifiees = statement.executeUpdate();

            if (lignesModifiees > 0) {
                System.out.println("Mise à jour réussie.");
            } else {
                System.out.println("Aucune mise à jour effectuée. Vérifiez l'ID du produit.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Produit mapperProduit(ResultSet resultSet) throws SQLException {
        Produit produit = new Produit();
        produit.setIdProduit(resultSet.getInt("id"));
        produit.setNom(resultSet.getString("nom"));
        produit.setDescription(resultSet.getString("description"));
        produit.setPrix(resultSet.getDouble("prix"));
        produit.setType(resultSet.getString("type"));
        produit.setQuantite(resultSet.getInt("quantite"));
        produit.setQuantiteVendues(resultSet.getInt("quantite_vendues"));



        return produit;
    }

    public Produit obtenirMeilleurVendeur() {
        Produit meilleurVendeur = null;
        String query = "SELECT * FROM produit ORDER BY quantite_vendues DESC LIMIT 1";

        try (
                PreparedStatement statement = connexion.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                meilleurVendeur = mapperProduit(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meilleurVendeur;
    }

    public double calculerChiffreAffairesTotal() {
        double chiffreAffairesTotal = 0.0;
        String query = "SELECT SUM(quantite_vendues * prix) AS chiffreAffaires FROM produit";

        try (
                PreparedStatement statement = connexion.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                chiffreAffairesTotal = resultSet.getDouble("chiffreAffairees");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chiffreAffairesTotal;
    }




    public void afficherColonnesProduit() {
        String query = "SELECT id,nom, description, prix, type, quantite FROM produit";

        try (Statement statement = connexion.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Affichage des noms de colonnes
            System.out.println("La liste des produits disponibles :");
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Affichage des données
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    public void ajouterCommande(Timestamp dateCommande, User user, Map<Produit, Integer> produitsDansPanier) {
        String query = "INSERT INTO Commande (date_commande, id_user, nom_client, id_produit, nom,montant) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setTimestamp(1, dateCommande);

            statement.setInt(2, user.getId());
            statement.setString(3, user.getName());


            // Parcourir chaque produit dans le panier
            for (Map.Entry<Produit, Integer> entry : produitsDansPanier.entrySet()) {
                Produit produit = entry.getKey();
                int quantiteAchete = entry.getValue();

                // Ajouter des valeurs pour id_produit et nom_produit
                statement.setInt(4, produit.getIdProduit());
                statement.setString(5, produit.getNom());
                statement.setDouble(6, produit.getPrix() * quantiteAchete);

                statement.executeUpdate(); // Exécuter la requête pour chaque produit
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static double calculerChiffreAffaires() {
        double chiffreAffaires = 0.0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
            String query = "SELECT SUM(Montant) FROM commande";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    chiffreAffaires = resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chiffreAffaires;
    }



    public static String obtenirProduitPlusAchete() {
        String produitPlusAchete = null;

        // Utilisez try-with-resources pour vous assurer que la connexion est fermée correctement
        try (Connection connection = ConnexionDB.getInstance().getConnexion()) {
            // Votre requête SQL
            String query = "SELECT nom FROM produit ORDER BY Prix * Quantite DESC LIMIT 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    produitPlusAchete = resultSet.getString("nom");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produitPlusAchete;
    }


   /* public static void setUserDetails(String name, int id) {
        userName = name;
        userId = id;
    }

    public static void printUserDetails() {
        System.out.println("User Name: " + userName);
        System.out.println("User ID: " + userId);
    }*/



    public static boolean insertPayment(String name, String email, String cardInfo, int mm, int yy, int cvc, double total) {
        try {
            String lastTwelveDigits = cardInfo.substring(Math.max(0, cardInfo.length() - 12));

            // Créer une chaîne masquée avec des astérisques pour les 12 chiffres
            String maskedLastTwelveDigits = "*".repeat(Math.min(12, lastTwelveDigits.length()));

            // Concaténer les 4 premiers chiffres avec les 12 chiffres masqués
            String maskedCardInfo = cardInfo.substring(0, Math.max(0, cardInfo.length() - 12)) + maskedLastTwelveDigits;
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "");
            String query = "INSERT INTO paiement (name, email, card_info, mm, yy, cvc, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, maskedCardInfo);
                preparedStatement.setInt(4, mm);
                preparedStatement.setInt(5, yy);
                preparedStatement.setInt(6, cvc);
                preparedStatement.setDouble(7, total);

                preparedStatement.executeUpdate();

            }
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


}
