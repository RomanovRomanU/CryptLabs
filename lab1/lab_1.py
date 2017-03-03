import re
from collections import OrderedDict
from math import log
def load_text(name):
	text = open(name,'r')
	return text.read()

def clear_text(string):
	#substituting bad symbols
	string = string.lower()
	string = re.sub(r"\.|\"|\(|\)|\?|\'|\d|\t|\<|\>|\*|\\|\n"," ",string)
	string = re.sub(r"[qwertyuiopasdfghjklzxcvbnm:!,-;=^]"," ",string)
	string = re.sub(r" +"," ",string)
	string = string.replace('ъ','ь')
	string = string.replace('ё','е')
	string = string.replace(' ','')
	return string

def number_of_letters(string):
	result = {}
	for i in range(len(string)):
		if string[i] in result:
			result[string[i]]+=1
		else:
			result.update({string[i]:1})
	sorted_result = OrderedDict(sorted(result.items()))
	# return list(sorted_result.items())
	return result

def number_of_bigrams(string):
	result = {}
	#Вот тут можем поменять шаг,что бы 
	#брать непересекающиеся биграммы
	for i in range(0,len(string)):
		if string[i:i+2] in result:
			result[string[i:i+2]]+=1
		else:
			result.update({string[i:i+2]:1})	
	sorted_result = OrderedDict(sorted(result.items()))
	# return list(sorted_result.items())
	return result

def H1(string):
	result = 0
	string_len = len(string)
	number_of_letters_result = number_of_letters(string)
	chars = number_of_letters_result.keys()
	for char in chars:
		frequency = number_of_letters_result[char]/string_len
		result += frequency*log(frequency,2)
	return (-result)

def H2(string):
	result = 0
	string_len = len(string)
	number_of_bigrams_result = number_of_bigrams(string)
	chars = number_of_bigrams_result.keys()
	for char in chars:
		frequency = number_of_bigrams_result[char]/sum(number_of_bigrams_result.values())
		result += frequency*log(frequency,2)
	return (-result)/2



if __name__=='__main__':
	text = load_text('Voyna_i_mir.txt')
	text = clear_text(text)
	# print('our alphabet: ',sorted(number_of_letters(text).keys()))
	# letters = number_of_letters(text)
	# for key in letters:
	# 	letters[key]=round(letters[key]/len(text),3)
	# print('letters frequency:',OrderedDict(sorted(letters.items())))
	# bigrams = number_of_bigrams(text)
	# for key in bigrams:
	# 	bigrams[key] = round(bigrams[key]/sum(bigrams.values()),3)
	# print('bigrams frequency:',OrderedDict(sorted(bigrams.items())))
	# result1 = number_of_letters(text)
	# result2 = number_of_bigrams(text)
	print('H1 is:',H1(text))
	print('H2 is:',H2(text))

