import os
#defines tic-tac-toe object
class TicTacToe():
	def __init__(self, position):
		self.position = position
		# 0 = nobody
		#'X'
		#'O'
		#'-' = tie
		self.won_by = 0
		self.board = [' ', ' ', ' ',
					  ' ', ' ', ' ',
					  ' ', ' ', ' ']

	def get(self, pos):
		return self.board[pos]

	def change(self, pos, to):
		if (to == True):
			self.board[pos] = 'X'
		else:
			self.board[pos] = 'O'
	
	def str(self, line):
		display = ""
		if (line == 0):
			display += str(self.board[0]) + " "
			display += str(self.board[1]) + " "
			display += str(self.board[2])
			return display
		elif (line == 1):
			display += str(self.board[3]) + " "
			display += str(self.board[4]) + " "
			display += str(self.board[5])
			return display
		elif (line == 2):
			display += str(self.board[6]) + " "
			display += str(self.board[7]) + " "
			display += str(self.board[8])
			return display

	def check_small_win(self, winner):
		if (winner):
			winner = 'X'
		else:
			winner = 'O'
		#first row
		if (self.board[0] == winner and self.board[1] == winner and self.board[2] == winner):
			self.won_by = winner
		#second row
		elif (self.board[3] == winner and self.board[4] == winner and self.board[5] == winner):
			self.won_by = winner
		#third row
		elif (self.board[6] == winner and self.board[7] == winner and self.board[8] == winner):
			self.won_by = winner
		#first column
		elif (self.board[0] == winner and self.board[3] == winner and self.board[6] == winner):
			self.won_by = winner
		#second column
		elif (self.board[1] == winner and self.board[4] == winner and self.board[7] == winner):
			self.won_by = winner
		#third column
		elif (self.board[2] == winner and self.board[5] == winner and self.board[8] == winner):
			self.won_by = winner
		#top left to botttom right
		elif (self.board[0] == winner and self.board[4] == winner and self.board[8] == winner):
			self.won_by = winner
		#top right to bottom left
		elif (self.board[2] == winner and self.board[4] == winner and self.board[6] == winner):
			self.won_by = winner
		else:
			global full
			full = False
			for i in range(9):
				if (self.board[i] != ' '):
					full = True
				else:
					full = False
					break
			if (full):
				self.won_by = '-'

def cls():
	os.system('cls' if os.name=='nt' else 'clear')

def display_board():
	cls()
	print(" -------\n| 1 2 3 |\n| 4 5 6 |\n| 7 8 9 |\n -------\n")
	display = " -----------------------\n"
	smaller_board = 0
	offset = 0
	for i in range(9):
		display += "| "
		for j in range(3):
			display += ult_board[j+offset].str(smaller_board) + " | "
		smaller_board += 1
		if (smaller_board >= 4):
			smaller_board = 0
		if ((i+1) % 3 == 0):
			smaller_board = 0
			offset+=3
			display += "\n -----------------------"
		display += "\n"
	print(display)

#big_spot = ult_board[int]
#in_turn = True (X) False (O)
def choose_small_spot(big_spot, in_turn):
	print("board", ult_board.index(big_spot) + 1, "\n")
	if (in_turn):
		print("X's turn\n")
	else:
		print("O's turn\n")
	try:
		spot = int(input("choose a spot on the small board (1-9): "))
		print("\n")
	except ValueError:
		print("unacceptable value, try again")
		print("\n")
		choose_small_spot(big_spot, in_turn)
	else:
		if (spot >=1 and spot <= 9 and big_spot.get(spot-1) == ' ' and big_spot.won_by == 0):
			big_spot.change(spot-1, in_turn)
			big_spot.check_small_win(in_turn)
			global win_con
			win_con = check_big_win(in_turn)
			if (win_con[0] and win_con[1] != '-'):
				display_board()
				print(win_con[1], "has won the game!")
				return
			elif (win_con[0] and win_con[1] == '-'):
				display_board()
				print("it's a tie!")
				return
			global turn
			turn = not turn
			global prev_small_spot
			prev_small_spot = spot
		else:
			print("unacceptable value, try again")
			print("\n")
			choose_small_spot(big_spot, in_turn)

#prev_small_spot = 0 if its wherever
#prev_small_spot actual range = (1-9)
#in_turn = True (X) False (O)
def choose_spot(prev_small_spot, in_turn):
	if (prev_small_spot == 0 or ult_board[prev_small_spot-1].won_by != 0):
		if (in_turn):
			print("X's turn\n")
		else:
			print("O's turn\n")
		try:
			spot = int(input("choose a spot on the big board (1-9): "))
			print("\n")
		except ValueError:
			print("unacceptable value, try again")
			print("\n")
			choose_spot(prev_small_spot, in_turn)
		else:
			if (spot >=1 and spot <= 9 and ult_board[spot-1].won_by == 0):
				choose_small_spot(ult_board[spot-1], in_turn)
			else:
				print("unacceptable value, try again")
				print("\n")
				choose_spot(prev_small_spot, in_turn)
	else:
		choose_small_spot(ult_board[prev_small_spot-1], in_turn)
			
#True = win detected
def check_big_win(winner):
	if (winner): winner = 'X'
	else: winner = 'O'
	#first row
	if (ult_board[0].won_by == winner and ult_board[1].won_by == winner and ult_board[2].won_by == winner):
		return [True, winner]
	#second row
	elif (ult_board[3].won_by == winner and ult_board[4].won_by == winner and ult_board[5].won_by == winner):
		return [True, winner]
	#third row
	elif (ult_board[6].won_by == winner and ult_board[7].won_by == winner and ult_board[8].won_by == winner):
		return [True, winner]
	#first column
	elif (ult_board[0].won_by == winner and ult_board[3].won_by == winner and ult_board[6].won_by == winner):
		return [True, winner]
	#second column
	elif (ult_board[1].won_by == winner and ult_board[4].won_by == winner and ult_board[7].won_by == winner):
		return [True, winner]
	#third column
	elif (ult_board[2].won_by == winner and ult_board[5].won_by == winner and ult_board[8].won_by == winner):
		return [True, winner]
	#top left to botttom right
	elif (ult_board[0].won_by == winner and ult_board[4].won_by == winner and ult_board[8].won_by == winner):
		return [True, winner]
	#top right to bottom left
	elif (ult_board[2].won_by == winner and ult_board[4].won_by == winner and ult_board[6].won_by == winner):
		return [True, winner]
	else:
		global out_full
		out_full = False
		for i in range(9):
			if (ult_board[i].won_by != 0):
				out_full = True
			else:
				out_full = False
				break
		if (out_full):
			return [True, '-']
		return [False, 0]

ult_board = []
turn = True
prev_small_spot = 0
for i in range(9):
	ult_board.append(TicTacToe(i))
win_con = check_big_win(True)

#print("Welcome to Ultimate TicTacToe\n")
while(not win_con[0]):
	display_board()
	choose_spot(prev_small_spot, turn)
