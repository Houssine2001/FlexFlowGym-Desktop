package com.example.bty.Services;

import com.example.bty.Entities.Validation;

public interface IServiceValidation {
    public void ajouterValidation(Validation v); // ajouter code de validation
    public Validation findByCode(int code); // chercher code de validation
    public void deleteValidation(int code); // supprimer code de validation

    Validation findByIdUser(Integer id);
}