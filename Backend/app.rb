require 'sinatra'
require "sinatra/activerecord"
require "sinatra/json"

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

post '/session' do
  return 400 unless params[:email]
  user = User.find_by_email(params[:email]) || User.create(params)
  session = user.sessions.create()

  return json token: session.token
end

delete '/session' do
  return 400 unless @session
  session.destroy()
end

get '/pantry' do
  return 401 unless @user
  formatted_ingredients = @user.ingredients.map do |i|
    {
      id: i.id,
      name: i.name
    }

    return json ingredients: formatted_ingredients
  end
end

delete '/pantry/:id' do
  return 401 unless @user
  ingredient = @user.ingredients.find(params[:id]);
  return 404 unless ingredient
  ingredient.destroy

  return json status: "Success"
end

put '/pantry/:id' do
  return 401 unless @user
  ingredient = Ingredient.find(:id)
  return 404 unless ingredient
  @user.ingredients.push(ingredient)
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
      ingredients: r.ingredients.split('🏩'),
      steps: r.ingredients.split('🏩')
    }
  end

  return json recipes: formatted_recipes
end

get '/ingredient/upc' do
  return 400 unless params[:upc]
  product_name = lookup_upc(params[:upc])
  return 404 unless product_name
  ingredient = classify_ingredient(product_name)
  return 404 unless ingredient

  return json ingredient: {id: ingredient.id, name: ingredient.name}
end