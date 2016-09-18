require "sinatra/activerecord"
require "sinatra/json"

require "./model"

def normalize_ingredient(ingredient)
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

  processed = ingredient.downcase();
  processed.gsub!(/\s+/, ' ')
  processed.strip!()

  stop_patterns.each do |pattern|
    processed.gsub!(pattern, '')
  end

  processed.gsub!('-', ' ')
  processed.gsub!(/\s+/, ' ')
  processed.strip!()
  processed.singularize
end

ingredient_map = {}
Ingredient.all.each {|i| ingredient_map[i.name] = i}

recipes = JSON.parse(File.open(ARGV[0]).read())['recipes']
recipes.each do |recipe|
  entity = Recipe.new({
    name: recipe['name'],
    thumbnail: recipe['thumb_url'],
    image: recipe['image_url'],
    cook_time: recipe['cook_time'],
    ingredient_text: recipe['ingredients'].join('\0'),
    steps: recipe['steps'].join('\0')
  })

  next unless recipe['ingredients'].each do |ingredient|
    normalized = normalize_ingredient(ingredient)
    if (ingredient_map.has_key?(normalized))
      entity.ingredients.push(ingredient_map[normalized]);
    else
      false
    end
  end

  entity.save()
end
