require "sinatra/activerecord"
require "sinatra/json"

require "./model"

ingredients = JSON.parse(File.open(ARGV[0]).read())

ingredient_entities = ingredients.map {|i| Ingredient.new(id: i['id'], name: i['name'])}
ingredient_entities.each do |entitiy|
  entitiy.parent = ingredient_entities[entitiy.id]
  entitiy.save
end