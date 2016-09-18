require 'active_support/inflector'

ingredients = File.open(ARGV[0]).read.split(/[\r\n]/)

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

processed = ingredients.map do |i|
  i.downcase!()
  i.gsub!(/\s+/, ' ')
  i.strip!()


  stop_patterns.each do |pattern|
    i.gsub!(pattern, '')
  end

  i.gsub!('-', ' ')
  i.gsub!(/\s+/, ' ')
  i.strip!()
  i.singularize
end

processed.reject! {|i| i.nil?}
processed.uniq.sort.each {|i| puts i}