package com.example.bty.Services;

import com.example.bty.Entities.User;

import java.util.List;

public interface IServiceUser {
    public void register(User user);
    public boolean emailExists(String email);
    public int Authentification(String email,String password);
    public void ActiverOrDesactiver(int id);
    public void update(User user);
    public void delete(int id);
    public List<User> getAllMembers();
    public List<User> getAllCoaches();

    public User findByEmail(String email);
    public void desactiverAcc(int id);



    void updateImage(String image, int id);

    void updatePassword(String text, Integer id);

    User findByID(int idUser);
    public void setSecretKey(String secret, int id);
    public void EnableOrDisablemfa(int id);
}