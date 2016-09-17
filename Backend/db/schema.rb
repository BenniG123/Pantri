# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160917085614) do

  create_table "ingredients", force: :cascade do |t|
    t.string  "name"
    t.integer "parent_id"
  end

  add_index "ingredients", ["parent_id"], name: "index_ingredients_on_parent_id"

  create_table "ingredients_recipes", id: false, force: :cascade do |t|
    t.integer "ingredient_id"
    t.integer "recipe_id"
  end

  add_index "ingredients_recipes", ["ingredient_id"], name: "index_ingredients_recipes_on_ingredient_id"
  add_index "ingredients_recipes", ["recipe_id"], name: "index_ingredients_recipes_on_recipe_id"

  create_table "recipes", force: :cascade do |t|
    t.string "name"
    t.string "thumbnail"
    t.string "image"
    t.string "cook_time"
    t.string "steps"
    t.string "ingredients"
  end

  create_table "sessions", force: :cascade do |t|
    t.integer "user_id"
    t.string  "token"
  end

  add_index "sessions", ["token"], name: "index_sessions_on_token", unique: true
  add_index "sessions", ["user_id"], name: "index_sessions_on_user_id"

  create_table "users", force: :cascade do |t|
    t.string "email"
  end

  add_index "users", ["email"], name: "index_users_on_email", unique: true

  create_table "users_ingredients", id: false, force: :cascade do |t|
    t.integer "user_id"
    t.integer "ingredient_id"
  end

  add_index "users_ingredients", ["ingredient_id"], name: "index_users_ingredients_on_ingredient_id"
  add_index "users_ingredients", ["user_id"], name: "index_users_ingredients_on_user_id"

end
