text = open('our_variant.txt','r')
text = text.read()
text = text[::-1]

new_text = open('our_variant_reversed.txt','w')
new_text.write(text)
new_text.close()