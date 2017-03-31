from lab_1 import bigram_frequency,load_text,clear_text,number_of_letters,H2
from operator import itemgetter

alphabet = ['а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о',
 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ы', 'ь', 'э', 'ю', 'я']
#Search of gcd by euklid algorithm
def euklid(a,b):
	#a always greater than b
	# if a<b:
	# 	a,b = b,a
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
def compation(a,b,n):
	d = euklid(a,n)
	if d == 1:
		x = (inverse(a,n) * b) % n
		return x
	elif b%d != 0:
		return 'Equation have no solution'
	else:
		#Equation have d number of solutions
		n_1 = n/d
		b_1 = b/d
		#Вот тут не уверен,что inverse по n_1,может и 
		#по n
		x_0 = (b_1 * inverse(a_1,n_1)) % n_1
		anwser = [x_0+y*n_1 for y in range(0,d)]
		return anwser

def bigram_to_number(bigram):
	a = bigram[0]
	a_number = alphabet.index(a)
	b = bigram[1]
	b_number = alphabet.index(b)
	# m*x_1 + x_2
	return len(alphabet)*a_number + b_number
#Проверить,что текст осмысленный
#Энтропия биграм H2 должна быть в параметрах
def nature_lang_check(string,H2):


if __name__ == '__main__':
	#У нас 31 буква,не забывай
	# text = load_text('Test.txt')
	text = load_text('Voyna_i_mir.txt')
	text = clear_text(text)
	frequency = bigram_frequency(text)
	frequency = sorted(frequency.items(),key=itemgetter(1))[::-1]
	frequency = frequency[0:5]

	ciphertext = load_text('12.txt')
	ciphertext_frequency = bigram_frequency(ciphertext)
	ciphertext_frequency = sorted(ciphertext_frequency.items(),key=itemgetter(1))[::-1]
	ciphertext_frequency = ciphertext_frequency[0:5]


	for bigram_1 in frequency:
		for bigram_2 in frequency:
			for cipher_bigram_1 in ciphertext_frequency:
				for cipher_bigram_2 in ciphertext_frequency:
					


	# frequency = list(OrderedDict(frequency.items()))
	print(frequency)
	print(ciphertext_frequency)

