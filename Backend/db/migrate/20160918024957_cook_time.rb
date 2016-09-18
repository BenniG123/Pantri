class CookTime < ActiveRecord::Migration
  def change
    rename_column :recipes, :calories, :cook_time
    change_column :recipes, :cook_time, :string
  end
end
