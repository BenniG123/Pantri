# -*- encoding : utf-8 -*-
class ChangeUserIngredients < ActiveRecord::Migration
  def change
    rename_table :users_ingredients, :ingredients_users
  end
end
