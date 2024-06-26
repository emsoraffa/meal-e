package com.solberg.models;

import java.util.List;

public class User {
  // TODO: add more attributes like locale, gender given name etc
  private String name;
  private String email;
  private List<ShoppingList> shoppingLists;

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public User(String name, String email, List<ShoppingList> shoppingLists) {
    this.name = name;
    this.email = email;
    this.shoppingLists = shoppingLists;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void addShoppingList(ShoppingList shoppingList) {
    this.shoppingLists.add(shoppingList);
  }
}
