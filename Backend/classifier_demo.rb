require "sinatra/activerecord"
ActiveRecord::Base.logger = nil

require "./model"
require "./ingredient"

while true do
  puts()
  print "Enter a product: "
  product = gets()
  ingredient = classify_ingredient(product)
  tree = [$ingredients[ingredient.id]]
  
  mem_ingr = $ingredients[ingredient.id]
  parent = $ingredients[mem_ingr[:parent]]
  
  while (parent)
    tree.push(parent)
    parent_id = parent[:parent]
    break unless parent_id
    parent = $ingredients[parent_id]
  end

  tree.each_with_index do |ingredient, i|
    padding = "  " * i
    puts "#{padding} #{ingredient[:name]}"
  end
end