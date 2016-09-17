class CreateSchema < ActiveRecord::Migration

  def change
    create_table :users do |t|
      t.string :email
    end

    create_table :sessions do |t|
      t.belongs_to :user, index: true
      t.string :token
    end

    create_table :ingredients do |t|
      t.string :name
      t.references :parent, index: true
    end

    create_table :users_ingredients, id: false do |t|
      t.belongs_to :user, index: true
      t.belongs_to :ingredient, index: true
    end

    create_table :recipes do |t|
      t.string :name
      t.string :thumbnail
      t.string :image
      t.integer :calories
      t.string :steps # Sorry
    end

    create_table :ingredients_recipes, id: false do |t|
      t.belongs_to :ingredient, index: true
      t.belongs_to :recipe, index: true
    end
  
    add_index :users, :email, unique: true
    add_index :sessions, :token, unique: true
  end
end
