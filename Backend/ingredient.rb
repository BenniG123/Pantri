BIWORD_FACTOR = 5

def classify_ingredient(product_name)
  normalized_name = normalize_string(product_name)
  terms = normalized_name.split().map {|term| @term_id_table[term]}
  biwords = split_into_biwords(normalized_name).map {|biword| @biword_id_table[biword]}

  ingredient_ranks = {}
  terms.each do |term|
    ingredients = @term_postings[term]
    ingredients.each do |ingredient|
      ingredient_ranks[ingredient] || ingredient_ranks[ingredient] = 0
      ingredient_ranks[ingredient] += @term_ranks[term]
    end
  end

  biwords.each do |biword|
    ingredients = @biword_postings[biword]
    ingredients.each do |ingredient|
      ingredient_ranks[ingredient] || ingredient_ranks[ingredient] = 0
      ingredient_ranks[ingredient] += BIWORD_FACTOR * @biword_ranks[biword]
    end
  end

  return nil if ingredient_ranks.empty?
  top_ingredient = ingredient_ranks.keys.reduce do |top, ingredient|
    if ingredient_ranks[ingredient] < ingredient_ranks[top]
      top
    else
      ingredient 
    end
  end

  return Ingredient.find(top_ingredient)
end

def normalize_string(str)
  str.downcase()
end

def lookup_upc(upc)

end

def lookup_recipes(ingredients)
  flattened_ingredients = flatten_ingredients(ingredients.to_a).map {|i| i.id}.sort()
  possible_recipes = @recipe_id_ingredients.keys.keep_if do |id|
    sorted_subset?(flattened_ingredients, @recipe_id_ingredients[id])
  end

  return Recipe.find_all(possible_recipes)
end

def sorted_subset?(set, subset)
  setIndex = 0;
  subsetIndex = 0;

  while subsetIndex < subset.size
    return false if setIndex == set.size
    if set[setIndex] == subset[subsetIndex]
      setIndex += 1
      subsetIndex += 1
    elsif set[setIndex] < subset[subsetIndex]
      setIndex += 1
    else
      return false;
    end
  end

  return true
end

def flatten_ingredients(ingredients)
  added_ingredients = new Set()
  flattened_ingredients = new Array(ingredients)

  ingredients.each do |i|
    parent = i.parent
    while parent do
      flattened_ingredients.push(parent) unless added_ingredients.include?(parent)
      added_ingredients.add(parent.id)
      parent = parent.parent
    end
  end

  return flattened_ingredients 
end

def build_recipe_info
  @recipes = Recipe.includes(:ingredients).all.to_a()
  @recipe_id_ingredients = {}
  @recipes.each do |r|
    @recipe_id_ingredients[r] = r.ingredients.map {|i| i.id}.sort()
  end
end

def build_info
  @ingredients = {}

  ingredient_records = Ingredient.all.to_a()
  ingredient_records.each { |i| @ingredients[i.id] = i }
  build_term_vector(@ingredients.values)
  build_biword_vector(@ingredients.values)
  build_recipe_info()
end


def build_term_vector(ingredients)
  @term_id_table = {}
  @term_postings = []

  term_counts = []
  total_counted = 0
  ingredients.each do |ingredient|
    terms = ingredient.name.split()
    terms.each do |term|
      @term_id_table[term] = term_counts.size unless @term_id_table.has_key?(term)
      id = @term_id_table[term]
      
      term_counts[id] || term_counts[id] = 0
      term_counts[id] += 1
      @term_postings[id] || @term_postings[id]= []
      @term_postings[id].push(ingredient.id)
      total_counted += 1
    end
  end

  @term_ranks = term_counts.map { |count| Math.log(total_counted / count) }
end

def build_biword_vector(ingredients)
  @biword_id_table = {}
  @biword_postings = []

  biword_counts = []
  total_counted = 0
  biwords = []
  
  ingredients.each do |ingredient|
    split_into_biwords(ingredient.name).each do |biword|
      @biword_id_table[biword] = biword_counts.size unless @biword_id_table.has_key?(biword)
      id = @biword_id_table[biword]

      @biword_postings[id] || @biword_postings[id]= []
      @biword_postings[id].push(ingredient.id)
      biword_counts[id] || biword_counts[id] = 0
      biword_counts[id] += 1
      total_counted += 1
    end
  end

  @biword_ranks = biword_counts.map { |count| Math.log(total_counted / count) }
end

def split_into_biwords(str)
  biwords = []
  terms = str.split()

  (0...terms.size - 1).each do |i|
    biwords.push("#{terms[i]} #{terms[i + 1]}")
  end

  return biwords
end

# Pull down the world!
puts "Building In Memory Ingredient Info..."
build_info()
puts "Done!"
