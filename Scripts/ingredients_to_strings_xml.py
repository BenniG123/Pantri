
import html

with open('processed_ingredients.txt', 'r') as infile:
    with open('strings.xml', 'w') as outfile:
        outfile.write("<string-array name=\"master_ingredients\">\n")
        for line in infile:
            outfile.write("\t<item> {} </item>\n".format(html.escape(line.strip())))
        outfile.write("</string-array>")