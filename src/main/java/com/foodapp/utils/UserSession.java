package com.foodapp.utils;

import com.foodapp.model.Plat;
import com.foodapp.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private static UserSession instance;

    private User currentUser;
    // Map<Plat, Quantité> : C'est ton panier
    private Map<Plat, Integer> cart;

    private UserSession() {
        cart = new HashMap<>();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
        this.cart.clear();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addToCart(Plat plat, int quantity) {
        if (cart.containsKey(plat)) {
            cart.put(plat, cart.get(plat) + quantity);
        } else {
            cart.put(plat, quantity);
        }
    }

    public Map<Plat, Integer> getCart() {
        return cart;
    }
}