require 'set'
BIWORD_FACTOR = 5

def max_rank(ingredient_id)
  rank = 0
  name = $ingredients[ingredient_id].name
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
  url = URI("http://www.upcindex.com/"+upc)

  http = Net::HTTP.new(url.host, url.port)
  puts url

  request = Net::HTTP::Get.new("http://www.upcindex.com/"+upc)
  request["upgrade-insecure-requests"] = '1'
  request["x-devtools-emulate-network-conditions-client-id"] = '7ce6663a-8da2-4b36-9e68-22ad32cebe36'
  request["user-agent"] = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36'
  request["accept"] = 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8'
  request["accept-language"] = 'en-US,en;q=0.8'
  request["cache-control"] = 'no-cache'

  response = http.request(request)
  html_string = response.body.force_encoding('UTF-8')
  match = html_string.match(/<strong itemprop=\"name\">(.*)<\/strong>/)

  if match.nil?
    return "None"
  else
    match = match[0]
    match.gsub!(/<strong itemprop=\"name\">/, "")
    match.gsub!(/<\/strong>/, "")
    return match
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
  added_ingredients = Set.new()
  flattened_ingredients = ingredients.map {|i| $ingredients[i[:id]]}

  ingredients.each do |i|
    parent = $ingredients[i[:parent]]
    while parent do
      flattened_ingredients.push(parent) unless added_ingredients.include?(parent)
      added_ingredients.add(parent.id)
      parent = $ingredients[parent[:parent]]
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
