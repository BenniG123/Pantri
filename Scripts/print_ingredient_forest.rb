require 'json'

BIWORD_FACTOR = 5

@ingredients = File.open(ARGV[0]).read.split(/[\r\n]/)
@split_ingredients = @ingredients.map {|i| i.split()}
#Ewww sorry
def split_into_biwords(str)
  biwords = []
  terms = str.split()

  (0...terms.size - 1).each do |i|
    biwords.push("#{terms[i]} #{terms[i + 1]}")
  end

  return biwords
end

def find_parent(ingredient_name)
  terms = ingredient_name.split().map {|term| @term_id_table[term]}
  biwords = split_into_biwords(ingredient_name).map {|biword| @biword_id_table[biword]}

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

  num_ingr_terms = ingredient_name.split().length
  ingredient_ranks.reject! {|k, v| @split_ingredients[k].length >= num_ingr_terms}

  return nil if ingredient_ranks.empty?
  top_ingredient = ingredient_ranks.keys.reduce do |top, ingredient|
    if ingredient_ranks[ingredient] < ingredient_ranks[top]
      top
    else
      ingredient 
    end
  end

  return top_ingredient
end

@term_id_table = {}
@term_postings = []

term_counts = []
total_counted = 0
@ingredients.each_with_index do |ingredient, i|
  terms = ingredient.split
  terms.each do |term|
    @term_id_table[term] = term_counts.size unless @term_id_table.has_key?(term)
    id = @term_id_table[term]

    term_counts[id] || term_counts[id] = 0
    term_counts[id] += 1
    @term_postings[id] || @term_postings[id]= []
    @term_postings[id].push(i)
    total_counted += 1
  end
end

@term_ranks = term_counts.map { |count| Math.log(total_counted / count) }

@biword_id_table = {}
@biword_postings = []

biword_counts = []
total_counted = 0
biwords = []

@ingredients.each_with_index do |ingredient, i|
  split_into_biwords(ingredient).each do |biword|
    @biword_id_table[biword] = biword_counts.size unless @biword_id_table.has_key?(biword)
    id = @biword_id_table[biword]

    @biword_postings[id] || @biword_postings[id]= []
    @biword_postings[id].push(i)
    biword_counts[id] || biword_counts[id] = 0
    biword_counts[id] += 1
    total_counted += 1
  end
end

@biword_ranks = biword_counts.map { |count| Math.log(total_counted / count) }

ingredient_forest = @ingredients.each_with_index.map {|ing, i| {id: i, name: ing, parent: find_parent(ing)} }
puts(JSON.generate(ingredient_forest))
