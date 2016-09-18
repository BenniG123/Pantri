# -*- encoding : utf-8 -*-
require 'uri'
require 'net/http'

BIWORD_FACTOR = 5

def max_rank(ingredient_id)
  rank = 0
  name = $ingredients[ingredient_id][:name]
  name.split.each { |term| rank += $term_ranks[$term_id_table[term]]}
  split_into_biwords(name).each {|biword| rank += BIWORD_FACTOR * $biword_ranks[$biword_id_table[biword]]}

  return rank
end

def classify_ingredient(product_name)
  normalized_name = normalize_string(product_name)
  terms = normalized_name.split().map {|term| $term_id_table[term]}
  biwords = split_into_biwords(normalized_name).map {|biword| $biword_id_table[biword]}

  ingredient_ranks = {}
  terms.each do |term|
    next unless term
    ingredients = $term_postings[term]
    ingredients.each do |ingredient|
      ingredient_ranks[ingredient] || ingredient_ranks[ingredient] = 0
      ingredient_ranks[ingredient] += $term_ranks[term]
    end
  end

  biwords.each do |biword|
    next unless biword
    ingredients = $biword_postings[biword]
    ingredients.each do |ingredient|
      ingredient_ranks[ingredient] || ingredient_ranks[ingredient] = 0
      ingredient_ranks[ingredient] += BIWORD_FACTOR * $biword_ranks[biword]
    end
  end

  return nil if ingredient_ranks.empty?
  
  top_ingredient = ingredient_ranks.keys.reduce do |top, ingredient|
    normalized_top = ingredient_ranks[top] / max_rank(top)
    normalized_ingredient = ingredient_ranks[ingredient] / max_rank(ingredient)

    if normalized_top > normalized_ingredient
      top
    elsif normalized_top < normalized_ingredient
      ingredient
    elsif ingredient_ranks[top] > ingredient_ranks[ingredient]
      top
    else
      ingredient
    end
  end

  return Ingredient.find(top_ingredient)
end

def normalize_string(str)
  stop_patterns = [
    /[0-9]*/,
    /\(.*\)/,
    /\sto taste/,
    /\sas needed/,
    /\sfor\s.*$/,
    /[\s^][\w'*]*[®™]/,
    /cans?\s/,
    /cupss?\s/,
    /ounces?\s/,
    /packages?\s/,
    /quarts?\s/,
    /cups?\s/,
    /bunch\s/,
    /bunches\s/,
    /bottle\s/,
    /pounds?\s/,
    /fluid ounces?/,
    /gallons?\s/,
    /liters?\s/,
    /pints?\s/,
    /pieces?\s/,  
    /jars?\s/,
    /teaspoons?\s/,
    /packets?\s/,
    /containers?\s/,
    /tablespoons?\s/,
    /envelopes?\s/,
    /fat free\s/,
    /fillets?\s/,
    /slice[^d]\s/,
    /slices\s/,
    /strips\s/,
    /sprigs\s/,
    /^loaf\s/,
    /pinch\s/,
    /pinches\s/,
    /jiggers?\s/,
    /bags?\s/,
    /heads?\s/,
    /cloves?\s/,
    /coarsely\s/,
    /roughly\s/,
    /thickly\s/,
    /thinly\s/,
    /uncokked\s/,
    /freshly\s/,
    /finely\s/,
    /diced\s/,
    /shredded\s/,
    /prepared\s/,
    /minced\s/,
    /ground\s/,
    /chopped\s/,
    /peeled\s/,
    /grated\s/,
    /fresh\s/,
    /canned\s/,
    /frozen\s/,
    /dried\s/,
    /bottles?\s/,
    /sliced?\s/,
    /wedges?\s/,
    /small\s/,
    /medium\s/,
    /large\s/,
    /inch\s/,
    /^\s*of\s/,
    /^\s*for\s/,
    /,.*/,
    /[\/:%]/,
    / - .*$/,
    /^or\s/,
    /^and\s/
  ]

  processed = str.downcase;
  processed.gsub!(/\s+/, ' ')
  processed.strip!()

  stop_patterns.each do |pattern|
    processed.gsub!(pattern, '')
  end

  processed.gsub!('-', ' ')
  processed.gsub!(/\s+/, ' ')
  processed.strip!()
  
  return processed.singularize
end

def lookup_upc(upc)
  key = "78a651159e2371cc2376d3f5588ec555"
  url = URI("http://api.upcdatabase.org/json/#{key}/#{upc}")

  response = Net::HTTP.get(url)
  json = JSON.parse(response)
  if json['valid'] == 'true'
    return json['description']
  else
    return nil
  end
end


def lookup_recipes(ingredients)
  combined_ingredients = ingredients.to_a.concat($common_ingredients)
  flattened_ingredients = flatten_ingredients(combined_ingredients).map{|i| i[:id]}.sort()
  possible_recipes = $recipe_id_ingredients.keys.keep_if do |id|
    sorted_subset?(flattened_ingredients, $recipe_id_ingredients[id])
  end

  return Recipe.find(possible_recipes.shuffle().first(50))
end

def sorted_subset?(set, subset)
  free_ingredients = 1
  setIndex = 0;
  subsetIndex = 0;

  while subsetIndex < subset.size
    if setIndex == set.size
      return subset.size - subsetIndex < free_ingredients 
    end 

    if set[setIndex] == subset[subsetIndex]
      setIndex += 1
      subsetIndex += 1
    elsif set[setIndex] < subset[subsetIndex]
      setIndex += 1
    elsif free_ingredients == 0
      return false
    else
      free_ingredients -= 1
      subsetIndex += 1
    end
  end

  return true
end

def flatten_ingredients(ingredients)
  added_ingredients = Set.new()
  to_visit = ingredients.map {|i| $ingredients[i[:id]]}
  flattened_ingredients = Array.new(to_visit)

  visited_ingredients = Set.new()

  to_visit.each do |i|
    parent = $ingredients[i[:parent]]
    unless visited_ingredients.include?(parent[:id])
      flattened_ingredients.push(parent)
      visited_ingredients.add(parent[:id])
      to_visit.push(parent)
    end

    i[:children].each do |child|
      unless visited_ingredients.include?(child[:id])
        flattened_ingredients.push(child)
        visited_ingredients.add(child[:id])
        to_visit.push(child)
      end
    end
  end

  return flattened_ingredients 
end

def build_recipe_info
  $recipes = Recipe.includes(:ingredients).all.to_a()
  $recipe_id_ingredients = {}
  $recipes.each do |r|
    $recipe_id_ingredients[r] = r.ingredients.map {|i| i.id}.sort()
  end
end

def build_info
  $ingredients = {}
  $common_ingredients = []

  ingredient_records = Ingredient.includes(:parent).all()
  ingredient_records.each do |i| 
    $ingredients[i.id] = {id: i.id, name: i.name}
    $ingredients[i.id][:parent] = i.parent.id if i.parent
    $ingredients[i.parent.id][:children] ||  $ingredients[i.parent.id][:children] = []
    $ingredients[i.parent.id][:children].push($ingredients[i.id])
    if i.name.include?('water') || i.name == 'salt' || i.name == 'pepper' || i.name == 'black pepepr'
      $common_ingredients.push({id: i.id, name: i.name})
    end
  end

  build_term_vector($ingredients.values)
  build_biword_vector($ingredients.values)
  build_recipe_info()
end


def build_term_vector(ingredients)
  $term_id_table = {}
  $term_postings = []

  term_counts = []
  total_counted = 0
  ingredients.each do |ingredient|
    terms = ingredient[:name].split()
    terms.each do |term|
      $term_id_table[term] = term_counts.size unless $term_id_table.has_key?(term)
      id = $term_id_table[term]
      
      term_counts[id] || term_counts[id] = 0
      term_counts[id] += 1
      $term_postings[id] || $term_postings[id]= []
      $term_postings[id].push(ingredient[:id])
      total_counted += 1
    end
  end

  $term_ranks = term_counts.map { |count| Math.log(total_counted / count) }
end

def build_biword_vector(ingredients)
  $biword_id_table = {}
  $biword_postings = []

  biword_counts = []
  total_counted = 0
  biwords = []
  
  ingredients.each do |ingredient|
    split_into_biwords(ingredient[:name]).each do |biword|
      $biword_id_table[biword] = biword_counts.size unless $biword_id_table.has_key?(biword)
      id = $biword_id_table[biword]

      $biword_postings[id] || $biword_postings[id]= []
      $biword_postings[id].push(ingredient[:id])
      biword_counts[id] || biword_counts[id] = 0
      biword_counts[id] += 1
      total_counted += 1
    end
  end

  $biword_ranks = biword_counts.map { |count| Math.log(total_counted / count) }
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