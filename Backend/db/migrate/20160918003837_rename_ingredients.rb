class RenameIngredients < ActiveRecord::Migration
  def change
    rename_column :recipes, :ingredients, :ingredient_text
  end
end
