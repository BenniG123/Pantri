
import html

with open('processed_ingredients.txt', 'r') as infile:
    with open('strings.xml', 'w') as outfile:
        outfile.write("<string-array name=\"master_ingredients\">\n")
        for line in infile:
            htmlStuff = html.escape(line.strip())
            words = htmlStuff.split(' ')
            writeThis = ''
            for word in words:
                word.replace('&#x27', '\\&#x27')
                writeThis += word[0].upper() + word[1 : len(word)] + ' '
            writeThis = writeThis[0 : len(writeThis) - 1]
            outfile.write("\t<item>{}</item>\n".format(writeThis))
        outfile.write("</string-array>")