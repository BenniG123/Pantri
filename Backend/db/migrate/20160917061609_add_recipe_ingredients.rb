# -*- encoding : utf-8 -*-
class AddRecipeIngredients < ActiveRecord::Migration
  def change
    add_column :recipes, :ingredients, :string
  end
end
