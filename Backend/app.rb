# -*- encoding : utf-8 -*-
require 'sinatra'
require "sinatra/activerecord"
require "sinatra/json"
require "sinatra/reloader" if development?

require "./model"
require "./ingredient"

# Grab sesion and user
before do
  request.env["HTTP_AUTHORIZATION"].try(:match, "Token (.*)") do |m|
    token = m[1]
    @session = Session.includes(:user).find_by_token(token)
    @user = @session.user if @session
  end
end

get '/' do
  return "Welcome to Pantri!"
end

post '/session' do
  return 400 unless params[:email]
  user = User.find_by_email(params[:email]) || User.create(params)
  session = user.sessions.create()

  return json token: session.token
end

delete '/session' do
  return 400 unless @session
  @session.destroy()
  return json status: "Success"
end

get '/pantry' do
  return 401 unless @user
  formatted_ingredients = @user.amounts.map do |i|
    {
      id: i.ingredient.id,
      name: i.ingredient.name.titleize(),
      quantity: i.quantity
    }
  end

  return json ingredients: formatted_ingredients
end

delete '/pantry/:id' do
  return 401 unless @user
  ingredient = Ingredient.find_by_id(params[:id])
  quantity = params[:quantity]
  return 404 unless ingredient

  new_quantity = 0
  if quantity && @user.ingredients.exists?(ingredient.id)
    amount = @user.amounts.where(ingredient_id: ingredient.id)[0]
    new_quantity = amount.quantity -= Integer(quantity)
    @user.ingredients.delete(ingredient)
  else
    amount = @user.amounts.where(ingredient_id: ingredient.id)[0]
    @user.ingredients.delete(ingredient)
  end

  if quantity && new_quantity > 0
    @user.amounts << Amount.new({ ingredient_id: ingredient.id, quantity: new_quantity })
  end

  @user.save

  return json status: "Success"
end

put '/pantry/:id' do
  return 401 unless @user
  ingredient = Ingredient.find_by_id(params[:id])
  quantity = params[:quantity] || [1, quantity].max
  return 404 unless ingredient

  if @user.ingredients.exists?(ingredient.id)
    puts "Found it"
    amount = @user.amounts.where(ingredient_id: ingredient.id)[0]
    quantity = amount.quantity += Integer(quantity)
    @user.ingredients.delete(ingredient)
  end

  @user.amounts << Amount.new({ ingredient_id: ingredient.id, quantity: quantity })

  @user.save

  return json status: "Success"
end

get '/recipe/' do
  return 401 unless @user

  recipes = lookup_recipes(@user.ingredients)
  formatted_recipes = recipes.map do |r|
    {
      id: r.id,
      name: r.name,
      thumbnail: r.thumbnail,
      image: r.image,
      cookTime: r.cook_time,
      ingredients: r.ingredient_text.split('\0'),
      steps: r.steps.split('\0')
    }
  end

  return json recipes: formatted_recipes.shuffle()
end

get '/ingredient/upc/:upc' do
  return 400 unless params[:upc]
  product_name = lookup_upc(params[:upc])
  return 404 unless product_name
  ingredient = classify_ingredient(product_name)
  return 404 unless ingredient

  return json ingredient: {id: ingredient.id, name: ingredient.name}
end

get '/ingredient/name/:name' do
  return 400 unless params[:name]

  ingredient = classify_ingredient(params[:name])
  return 404 unless ingredient

  return json ingredient: {id: ingredient.id, name: ingredient.name}
end
