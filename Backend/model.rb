# -*- encoding : utf-8 -*-
class User < ActiveRecord::Base
  has_and_belongs_to_many :ingredients
  has_many :sessions
end

class Session < ActiveRecord::Base
  belongs_to :user
  before_create { self.token = SecureRandom.urlsafe_base64 }
end

class Ingredient < ActiveRecord::Base
  has_and_belongs_to_many :users
  has_and_belongs_to_many :recipes
  has_many :children, class_name: "Employee", foreign_key: "ingredient_id"
  belongs_to :parent, class_name: "Ingredient"
end

class Recipe < ActiveRecord::Base
  has_and_belongs_to_many :ingredients
end
