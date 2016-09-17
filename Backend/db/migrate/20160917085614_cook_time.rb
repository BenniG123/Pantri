class CookTime < ActiveRecord::Migration
  def change
    change_column :recipes, :calories, :string
    rename_column :recipes, :calories, :cook_time
  end
end
