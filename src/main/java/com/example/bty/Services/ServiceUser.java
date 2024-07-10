package com.example.bty.Services;

import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import com.example.bty.Utils.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;


public class ServiceUser implements IServiceUser {


    private PreparedStatement pste;
    Connection cnx = ConnexionDB.getInstance().getConnexion();


    //** Register a new user
    @Override
    public void register(User u) {
        String req = "INSERT INTO `user` (`nom`,`email`, `password`,`telephone`,`roles`,`is_verified`,`mdp_exp`,`created_at`,`mfa_enabled`) VALUE (?,?,?,?,?,?,?,?,?)";
        try {
            pste = cnx.prepareStatement(req);
            pste.setString(1, u.getName());
            pste.setString(2, u.getEmail());
            String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(12));
            pste.setString(3, hashedPassword);
            pste.setString(4, u.getTelephone());
//            pste.setString(5, u.getRole().toString());
            //Gson gson = new Gson();
            //String roleJson = Arrays.toString(u.getRoles());
            JsonArray roleJsonArray = convertRolesToJsonArray(u.getRoles());
            pste.setString(5, roleJsonArray.toString());
            pste.setBoolean(6, u.isEtat());
            LocalDate date = LocalDate.now();
            LocalDate exp = date.plusDays(30);
            pste.setDate(7,java.sql.Date.valueOf(exp ));
            pste.setDate(8, java.sql.Date.valueOf(java.time.LocalDate.now()));
            System.out.print(Arrays.toString(u.getRoles()));
            pste.setBoolean(9, u.isMfaEnabled());

            pste.executeUpdate();
            System.out.println("utilisateur créée");
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static JsonArray convertRolesToJsonArray(Role[] roles) {
        JsonArray jsonArray = new JsonArray();
        for (Role role : roles) {
            jsonArray.add(new JsonPrimitive(role.toString()));
        }
        return jsonArray;
    }

    //** Check if an email already exists
    @Override
    public boolean emailExists(String email) {
        try {
            String query = "SELECT COUNT(*) FROM user WHERE email = ?";
            pste = cnx.prepareStatement(query);
            pste.setString(1, email);
            ResultSet resultSet = pste.executeQuery();
            if (resultSet.next()) {

                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'email : " + e.getMessage());
        }
        return false;
    }

    //** Authenticate a user
    @Override
    public int Authentification(String email, String password) {
        int status = 0;
        try {
            String req = "select * from user where email=? ";

            pste = cnx.prepareStatement(req);
            pste.setString(1, email);
            ResultSet rs = pste.executeQuery();
            //System.out.println(rs.getString("roles"));
            while (rs.next()) //l9a une ligne fi wosset lbase de donnee
            {
                System.out.println("Im here");
                //User u = this.findByEmail(email);
                if (rs.getString("is_verified").equals("0")) {

                    return 2;
                }

               else if (BCrypt.checkpw(password, rs.getString("password")))
               {
                    System.out.println("Im here2");
                    //if logged in successfully yemchy yasnaalou session w y7ottou fiha les informations mte3ou

                    //explain : f session bch y7ott le vrai role du user connecté khater 9bal ken y7ott role.ADMIN ou role.COACH meme si
                    // user connecté est un coach ou un membre
                    //Role[] userRoles = new Role[]{Role.valueOf(rs.getString("roles"))};
                    User u = new User(rs.getInt("id"), rs.getString("nom"), rs.getString("email"), rs.getString("password"), rs.getString("telephone"), null, rs.getString("image"));

                    //System.out.println("The connected is " + s.getLoggedInUser().getRole());
                    return 1;
                }
            } System.out.println("Invalid user credentials");
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return status;
    }

    //** Activer ou bdesactiver un membre
    @Override
    public void ActiverOrDesactiver(int id) {

        //verifier si l'utilisateur connecté est un admin
        Session s = Session.getInstance();
        User u = s.getLoggedInUser();
        if (!"ADMIN".equals(Arrays.toString(u.getRoles()))) {
            System.out.println("You are not allowed to perform this action");
            return;
        }


        String req = "UPDATE user SET etat =!etat  WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setInt(1, id);
            pste.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void desactiverAcc(int id) {


        String req = "UPDATE user SET is_verified =!is_verified  WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setInt(1, id);
            pste.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //** Update user information
    @Override
    public void update(User u) {
        String req = "UPDATE user SET nom = ?, email = ?, password = ?, telephone = ? WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setString(1, u.getName());
            pste.setString(2, u.getEmail());
            pste.setString(3, BCrypt.hashpw(u.getPassword(), BCrypt.gensalt()));
            pste.setString(4, u.getTelephone());
            pste.setInt(5, u.getId());
            int rowsUpdated = pste.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing user was updated successfully!");
            } else {
                System.out.println("No user found with this id!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //** Delete a user
    @Override
    public void delete(int id) {
        //verifier si l'utilisateur connecté est un admin
        Session s = Session.getInstance();
        User u = s.getLoggedInUser();
        System.out.println((Arrays.toString(new Role[]{Role.ADMIN})));
        System.out.println(Arrays.toString(u.getRoles()));
        if (!(Arrays.equals(u.getRoles(), new Role[]{Role.ADMIN}))) {
            System.out.println("You are not allowed to perform this action");
            return;
        }

        String req = "DELETE FROM user WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setInt(1, id);
            int rowsDeleted = pste.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A user was deleted successfully!");
            } else {
                System.out.println("No user found with this id!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<User> getAllMembers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {


            String req = "SELECT * FROM user WHERE JSON_CONTAINS(roles, '\"MEMBRE\"')";
            try {
                pste = cnx.prepareStatement(req);
                ResultSet rs = pste.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("nom"));
                    user.setEmail(rs.getString("email"));
                    //user.setPassword(rs.getString("password"));
                    user.setTelephone(rs.getString("telephone"));


                    Role []userRoles = new Role[]{Role.MEMBRE};
                    user.setRoles(userRoles);
                    user.setEtat(rs.getBoolean("is_verified"));
                    users.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;

    }

    @Override
    public List<User> getAllCoaches() {
        List<User> coaches = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {

            String req = "SELECT * FROM user WHERE JSON_CONTAINS(roles, '\"COACH\"')";
            try {
                pste = cnx.prepareStatement(req);
                ResultSet rs = pste.executeQuery();
                while (rs.next()) {
                    User coach = new User();
                    coach.setId(rs.getInt("id"));
                    coach.setName(rs.getString("nom"));
                    coach.setEmail(rs.getString("email"));
                    // Removed the line that fetches the password
                    coach.setTelephone(rs.getString("telephone"));

                    Role []userRoles = new Role[]{Role.COACH};
                    coach.setRoles(userRoles);
                    coach.setEtat(rs.getBoolean("is_verified"));
                    coaches.add(coach);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return coaches;
    }


    // Find a user by email
    @Override
    public User findByEmail(String email) {
        User U = new User();
        String req = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement pste = cnx.prepareStatement(req)) {
            pste.setString(1, email);

            ResultSet rs = pste.executeQuery();
            while (rs.next())
            {
                U.setId(rs.getInt("id"));
                U.setName(rs.getString("nom"));
                U.setEmail(rs.getString("email"));
                String role = rs.getString("roles");
                JsonArray jsonArray = JsonParser.parseString(role).getAsJsonArray();
                 Role[] roles = convertJsonArrayToRolesArray(jsonArray);

                //Role []userRoles = new Role[]{Role.valueOf(rs.getString("roles"))};
                U.setRoles(roles);
                U.setPassword(rs.getString("password"));
                U.setImage(rs.getString("image"));
                U.setTelephone(rs.getString("telephone"));
                U.setEtat(rs.getBoolean("is_verified"));
                U.setMfaEnabled(rs.getBoolean("mfa_enabled"));
                U.setMfaSecret(rs.getString("mfa_secret"));
                //  U.setPassword(rs.getString("password"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println( "Service "+"User "+U);
        return U;
    }

    @Override
    public void updateImage(String image, int id) {
        String req = "UPDATE user SET image = ? WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setString(1, image);
            pste.setInt(2, id);
            pste.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updatePassword(String text, Integer id) {
        String req = "UPDATE user SET password = ? WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setString(1, BCrypt.hashpw(text, BCrypt.gensalt()));
            pste.setInt(2, id);
            pste.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public User findByID(int idUser) {
        User user = new User();
        String req = "SELECT * FROM user WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setInt(1, idUser);
            ResultSet rs = pste.executeQuery();
            while (rs.next()) {

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("nom"));
                user.setEmail(rs.getString("email"));
                String role = rs.getString("roles");
                JsonArray jsonArray = JsonParser.parseString(role).getAsJsonArray();
                Role[] roles = convertJsonArrayToRolesArray(jsonArray);

                //Role []userRoles = new Role[]{Role.valueOf(rs.getString("roles"))};
                user.setRoles(roles);
                user.setEtat(rs.getBoolean("is_verified"));
                user.setPassword(rs.getString("password"));
                user.setTelephone(rs.getString("telephone"));
                user.setImage(rs.getString("image"));
                user.setMfaEnabled(rs.getBoolean("mfaEnabled"));
                user.setMfaSecret(rs.getString("mfaSecret"));
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName());
        }

        return user;
    }


    //** Set the secret key for multi-factor authentication
    @Override
    public void setSecretKey(String secret, int id) {
        String req = "UPDATE user SET mfa_secret  = ? WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setString(1, secret);
            pste.setInt(2, id);
            pste.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //** Enable or disable multi-factor authentication
    @Override
    public void EnableOrDisablemfa(int id) {
        String req = "UPDATE user SET mfa_enabled = !mfa_enabled WHERE id = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setInt(1, id);
            pste.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static Role[] convertJsonArrayToRolesArray(JsonArray jsonArray) {
        Role[] rolesArray = new Role[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            String roleStr = jsonArray.get(i).getAsString();
            rolesArray[i] = Role.valueOf(roleStr);
        }
        return rolesArray;
    }
}

