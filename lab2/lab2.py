from lab_1 import bigram_frequency,load_text,clear_text,number_of_letters,H2
from operator import itemgetter

alphabet = ['а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о',
 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ы', 'э', 'ю', 'я']
#Search of gcd by euklid algorithm
# Tested
def euklid(a,b):
	#a always greater than b
	# if a<b:
	#a,b = b,a
	r_0 = a
	r_1 = b
	u_0 = 1
	u_1 = 0
	v_0 = 0
	v_1 = 1
	while r_1 != 0:
		r_next = r_0%r_1
		q_1 = (r_0 - (r_0%r_1))/r_1
		r_0 = r_1
		r_1 = r_next
		#r_s = u_s *a + v_s *b 
		u_next = u_0 - q_1 * u_1
		u_0 = u_1
		u_1 = u_next

		v_next = v_0 - q_1 * v_1
		v_0 = v_1
		v_1 = v_next
	if r_0 == 1:
		return r_0,u_0,v_0
	else:
		return r_0,None,None
#For finding inverse element
# Tested
def inverse(a,n):
	if(a>n):
		a = a%n
	gcd,u,v = euklid(a,n)
	if u != None:
		return u
	else:
		return 'Inverse element is apsent'
# ax = b (mod n)
# Function for finding x
# Tested
def compation(a,b,n):
	d = euklid(a,n)[0]
	if d == 1:
		x = (inverse(a,n) * b) % n
		return x
	elif b%d != 0:
		return 'Equation have no solution'
	else:
		#Equation have d number of solutions
		a_1 = a/d
		n_1 = n/d
		b_1 = b/d
		#Вот тут не уверен,что inverse по n_1,может и 
		#по n
		x_0 = (b_1 * inverse(a_1,n_1)) % n_1
		anwser = [x_0+y*n_1 for y in range(0,d)]
		return anwser

def bigram_to_number(bigram):
	m = 31
	a = bigram[0]
	a_number = alphabet.index(a)
	b = bigram[1]
	b_number = alphabet.index(b)
	# m*x_1 + x_2
	return m*a_number + b_number
#Проверить,что текст осмысленный
#Энтропия биграм H2 должна быть в параметрах
def nature_lang_check(string,global_H2):
	test = "когдапожарньеисоседи"

	if string == None:
		return False
	if test in string:
		print('aы' in string)
	#Проверка на запрещённые биграммы
	# forbidden_bigrams = ['жы',"шы"]
	forbidden_bigrams = ["аы","аь","ьы","шы","жы"]
	for forbidden_bigram in forbidden_bigrams:
		if forbidden_bigram in string:
			return False
	# Проверка на соответствие энтропии
	# Если энтропия хотя бы одного символа в два раза отличаеться
	# от заданной энтропии,то проверку не проходит
	# 
	# local_H2 = H2(string)
	# if local_H2>global_H2*1.01 or local_H2<global_H2*0.99:
	# 	return False
	# 	
	# for key in local_H2:
	# 	if local_H2[key]>(global_H2[key]*2) or local_H2[key]<(global_H2[key]/2):
	# 		return False
	return True
#Найти биграмму x_1 x_2
def number_to_bigram(number):
	m = 31
	x_2 = number % m
	x_1 = (number - x_2)/m
	return alphabet[int(x_1)] + alphabet[int(x_2)]

#Расшифровка афинного шифра
#с заданныи ключом
# Проверял на слове "тест"(шифртекст "уфмг")
# И a = 13, b = 17
def decipher(ciphertext,a,b):
	a= int(a)
	b = int(b)
	m = 31
	text = ''
	a_inverse = inverse(a,m**2)
	if a_inverse == 'Inverse element is apsent':
		return None
	#x = a**-1 (y-b) mod m**2
	#Если не обрежется на целое кол-во биграм
	if len(ciphertext)%2!=0:
		#Добавляем лишнюю букву
		ciphertext+='н'
	for i in range(0,len(ciphertext),2):
		y = bigram_to_number(ciphertext[i:i+2])
		x = (a_inverse * (y-b)) % (m**2)
		text += number_to_bigram(x)
	return text

# Find b parametre in afinne cipher
def find_b(a,x,y,m):
	b = (y - a*x) % (m**2)
	return b



if __name__ == '__main__':
	# #У нас 31 буква,не забывай
	# # text = load_text('Test.txt')
	text = load_text('Test.txt')
	text = clear_text(text)
	global_H2 = H2(text)
	frequency = bigram_frequency(text)
	frequency = sorted(frequency.items(),key=itemgetter(1))[::-1]
	frequency = frequency[0:5]
	# We don`t need frequency of bigrams
	for i in range(len(frequency)):
		frequency[i] = frequency[i][0]
	ciphertext = load_text('12.txt')
	ciphertext = clear_text(ciphertext)
	ciphertext_frequency = bigram_frequency(ciphertext)
	ciphertext_frequency = sorted(ciphertext_frequency.items(),key=itemgetter(1))[::-1]
	ciphertext_frequency = ciphertext_frequency[0:5]
	# We don`t need frequency of bigrams
	for i in range(len(ciphertext_frequency)):
		ciphertext_frequency[i] = ciphertext_frequency[i][0]
	m = 31
	# оклй аз ог тдхво
	# когд ап ож арные
	x_1,x_2 = bigram_to_number("то"),bigram_to_number("на")
	y_1,y_2 = bigram_to_number("хк"),bigram_to_number("вх")
	y = (y_1 - y_2) % (m**2)
	x = (x_1 - x_2) % (m**2)
	a = compation(x,y,m**2)
	# print(a)
	# Когда зашифровую с x_1 y_1,то расшифровует только их
	# И так же с x_2,y_2
	b = find_b(a,x_1,y_1,m)
	# ок лй аз ог тд хв оэ шк тж сэ лл ыэ еж ях бч ех екв
	# ко гд ап ож ар нь еисоседиушбилеоауф ма ноин
	# ок лй аз ог тд хв оэ шк тж
	# print(b)
	# print(decipher(ciphertext,a,b))
	# print(decipher(ciphertext,a,b))

	# len of our natural language dictionary 
	iterations = 0
	for bigram_1 in frequency[::]:
		# break
		other_bigrams = frequency[::]
		#We don`t need to choose bigram1 again
		del other_bigrams[other_bigrams.index(bigram_1)]
		for bigram_2 in other_bigrams:
			for cipher_bigram_1 in ciphertext_frequency[::]:
				other_cipher_bigrams = ciphertext_frequency[::]
				del other_cipher_bigrams[other_cipher_bigrams.index(cipher_bigram_1)]
				for cipher_bigram_2 in other_cipher_bigrams:

					# Number value of bigram_1 and bigram_2
					x_1,x_2 = bigram_to_number(bigram_1),bigram_to_number(bigram_2)
					# Number value of bigram 
					y_1,y_2 = bigram_to_number(cipher_bigram_1),bigram_to_number(cipher_bigram_2)
					y = (y_1 - y_2) % (m**2)
					x = (x_1 - x_2) % (m**2)

					#Find the a in (a,b) couple for
					#Afiine cipher
					a = compation(x,y,m**2)

					if a =='Equation have no solution':
						continue
					# Find the b in (a,b) couple 
					# for Afinne cipher
					if type(a) == list:
						b = []
						for a_part in a:
							b.append(find_b(a_part,x_1,y_1,m))
					else:
						b = find_b(a,x_1,y_1,m)


					# Now decipher text with known keys a,b
					# For simplisity(don`t handle two variants: a is number or list)

					if type(a) != list:
						a = [a]
						b = [b]

					for index in range(len(a)):
						a_part = a[index]
						b_part = b[index]
						deciphered_text = decipher(ciphertext,a_part,b_part)
						if nature_lang_check(deciphered_text,global_H2):
							print(deciphered_text[0:40])

						# print(deciphered_text[0:15])

	# for bigram_1 in frequency[::]:
	# 	for bigram_2 in frequency[::]:
	# 		for cipher_bigram_1 in ciphertext_frequency[::]:
	# 			for cipher_bigram_2 in ciphertext_frequency[::]:
	# 				#Перегоняем наши биграммы в числовые значения

	# 				m = 31
	# 				if bigram_1 == bigram_2:
	# 					continue
	# 				if cipher_bigram_1 == cipher_bigram_2:
	# 					continue
	# 				number_bigram_1 = bigram_to_number(bigram_1)
	# 				number_bigram_2 = bigram_to_number(bigram_2)

	# 				number_cipher_bigram_1 = bigram_to_number(cipher_bigram_1)
	# 				number_cipher_bigram_2 = bigram_to_number(cipher_bigram_2)

	# 				y = number_cipher_bigram_1 - number_cipher_bigram_2
	# 				x = number_bigram_1 - number_bigram_2
	# 				# Теперь у нас уравнение вида ax = y(mod m**2)
	# 				a = compation(x,y,m**2)
					
	# 				#Теперь это может быть один или несколько ответов
	# 				if type(a) == list:
	# 					b = []
	# 					for a_1 in a:
	# 						b.append(number_cipher_bigram_1 - a_1*number_bigram_1)%(m**2)
	# 					for i in range(a):
	# 						a_1 = a[i]
	# 						b_1 = b[i]

	# 						deciphered_text = decipher(ciphertext,a_1,b_1)
	# 						if nature_lang_check(deciphered_text)==True:
	# 							print("\n",deciphered_text)
	# 				else:
	# 					b = (number_cipher_bigram_1 - a*number_bigram_1)%(m**2)
	# 					deciphered_text = decipher(ciphertext,a,b)
	# 					print(deciphered_text)
	# 					if nature_lang_check(deciphered_text,H2)==True:
	# 						print("\n",deciphered_text)
					



	# frequency = list(OrderedDict(frequency.items()))

