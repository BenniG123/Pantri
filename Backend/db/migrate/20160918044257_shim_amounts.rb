class ShimAmounts < ActiveRecord::Migration
  def change
      create_table :amounts, id: false do |t|
        t.belongs_to :ingredient, index: true
        t.belongs_to :user, index: true
        t.integer :quantity
      end
  end
end
