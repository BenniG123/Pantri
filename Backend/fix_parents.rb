# -*- encoding : utf-8 -*-
require "sinatra/activerecord"
require "sinatra/json"

require "./model"

ingredients = JSON.parse(File.open(ARGV[0]).read())

ingredients.each_with_index do |ingredient, i|
  next unless ingredient['parent']

  entity = Ingredient.find(i) 
  entity.parent = Ingredient.find(ingredient['parent'])
  entity.save
end
