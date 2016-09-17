require 'json'

exit 1 unless ARGV[0]
json_file = File.open(ARGV[0])
recipes = JSON.parse(json_file.read())['recipes']
ingredients = recipes.map {|r| r['ingredients']}.flatten().sort

ingredients.uniq.each {|i| puts(i)}
json_file.close